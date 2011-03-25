// 
// @file    UploadUnzipTest.java
// @brief   Uploads a zip file, unzips it, then tests & returns the data.
// @author  Michael Hucka
// @author  Kimberly Begley
// @date    Created Jul 30, 2008, 9:25:21 AM
//
// $Id$
// $HeadURL$
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright 2008-2010 California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation.  A copy of the license agreement is provided
// in the file named "LICENSE.txt" included with this software distribution
// and also available at http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import java.math.*;


/**
 * This is the main servlet clas in the web application.  It is called by
 * the web form when a user uploads a zip file to the server.  It performs
 * the upload, unzips the file, writes the files to a temporary folder,
 * analyzes the results and returns the results via session variables.
 */
public class UploadUnzipTest extends HttpServlet
{
    // 
    // --------------------------- Public methods ----------------------------
    // 

    /**
     * Interface function invoked by uploadresults.jsp.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // Store these because they're unique per session & we need them often.

        httpRequest  = request;
        httpResponse = response;

        OnlineSTS.logInvocation(request);

        // An upload from uploadresults.jsp is supposed to be a multipart
        // request.  If that's not what we got here, toss an error.
        
        if (! ServletFileUpload.isMultipartContent(request))
        {
            propagateError(SERVER_ERROR, "Didn't get multipart content.");
            return;
        }

        // Create the user's upload directory.

        uploadDir = createUploadDir();

        // Set up objects that will extract the user's upload.

        refCasesDir = getReferenceCasesDir();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(uploadDir);
        factory.setSizeThreshold(1 * 1024 * 1024); // In bytes.

        ServletFileUpload reqHandler = new ServletFileUpload(factory);

        // Result files for the current 927 test suite cases adds up to ~1
        // MB.  Adding some room for growth, if we get something > 5 MB,
        // something's wrong.

        reqHandler.setSizeMax(5 * 1024 * 1024);

        // Unzip the user's uploaded archive.  This returns an ordered list
        // of the CSV file names extracted as a result.

        TreeSet<UserTestCase> userCases = unzipUserArchive(reqHandler);

        // If we managed to extract data files from the user's upload, test
        // them, get the results, & dispatch them to the results JSP page.

        if (userCases != null)
            try
            {
                Vector<UserTestResult> results = performAnalysis(userCases);
                OnlineSTS.logInfo(request, "Returning results to user.");
                httpRequest.setAttribute("testResults", results);
                RequestDispatcher dispatcher
                    = httpRequest.getRequestDispatcher(RESULTS_PAGE);
                dispatcher.forward(httpRequest, httpResponse);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
    }

    // 
    // --------------------------- Private methods ----------------------------
    // 

    private Vector<UserTestResult> performAnalysis(TreeSet<UserTestCase> cases)
        throws ServletException, Exception
    {
        // The user's set of results may not include a result for every
        // test case.  In the results display (via showresults.jsp), we
        // loop over all possible expected results, because we display the
        // outcome to the user based on the complete set.  To make the
        // subsequent operations simpler, we construct a full-sized vector
        // here and return that at the end.  If the user didn't include a
        // particular case in the uploaded results, we leave the entry null
        // in the results vector.  IMPORTANT: the element at position 0 is
        // always empty, because there's no case numbered 00000.

        CasesTagsMap caseMap;

        try
        {
            caseMap = new CasesTagsMap(refCasesDir);
        }
        catch (Exception e)
        {
            propagateError(SERVER_ERROR, "Encountered error trying to"
                           + " read test suite data.", e);
            return null;
        }

        int highestNumber              = caseMap.getHighestCaseNumber();
        Vector<UserTestResult> results = new Vector<UserTestResult>();
        results.setSize(highestNumber + 1);  // +1 because we have no case 0.

        OnlineSTS.logInfo(httpRequest, "Starting analysis ...");

        caseLoop:
        for (UserTestCase theCase : cases)
        {
            String name = theCase.getCaseName();
            double[][] refData;
            double[][] userData;

            // If we can't read our reference data, something's really wrong.
            // Throw a hard failure.

            try
            {
                refData = theCase.getExpectedData();
            }
            catch (Exception e)
            {
                propagateError(SERVER_ERROR, "Unable to read reference data"
                               + " for case " + name + ".", e);
                return null;
            }

            UserTestResult thisResult = new UserTestResult(theCase);
            results.set(theCase.getCaseNum(), thisResult);

            // If we get an error reading the user's data file, we treat
            // it as their problem.  Log it and move on to the next case.

            try
            {
                userData = theCase.getUserData();
            }
            catch (Exception e)
            {
                OnlineSTS.logInfo(httpRequest, "Reporting problem: " + e.getMessage());
                thisResult.setErrorMessage(e.getMessage());
                continue;
            }

            // Start by getting some basic things.

            int numRows   = refData.length;
            int numCols   = refData[0].length;
            double absTol = theCase.getTestAbsoluteTol();
            double relTol = theCase.getTestRelativeTol();

            // First check that the user's data file has entries at the
            // same time steps as our reference data.  We do this using the
            // same epsilon as the absolute tolerance, so that (e.g.) 0 is
            // still considered equal to (e.g.) 0.000000000000001.

            for (int r = 0; r < numRows; r++)
                if (! tolerable(refData[r][0], userData[r][0], absTol, relTol))
                {
                    String msg = "Within the tolerances set for this case, the"
                        + " time step value on row " + r + " doesn't match the"
                        + " the expected value: expecting " + refData[r][0]
                        + " but read " + userData[r][0] + " instead.";
                    thisResult.setErrorMessage(msg);
                    OnlineSTS.logInfo(httpRequest, "Reporting problem: " + msg);
                    continue caseLoop;
                }

            // Now compare the results to what we expect.  In addition to
            // logging the total number of failures for each case, we also
            // construct a map of the data points that succeed or fail.
            // (That's the 'diffs' array.)  Note: although the user's
            // results and reference results are stored as Java Double's,
            // we use BigDecimal for the results of computations.  This
            // helps report more accurate results to the user.

            int failCount = 0;
            ResultDifference[][] diffs = new ResultDifference[numRows][numCols];

            for (int r = 0; r < numRows; r++)
                for (int c = 1; c < numCols; c++) // Skip col 1 b/c it's time.
                {
                    double refVal  = refData[r][c];
                    double userVal = userData[r][c];

                    if (! tolerable(refVal, userVal, absTol, relTol))
                    {
                        failCount++;
                        if (Double.isNaN(refVal) || Double.isNaN(userVal)
                            || Double.isInfinite(refVal) || Double.isInfinite(userVal))
                        {
                            diffs[r][c] = new ResultDifference();
                            diffs[r][c].setNumerical(false);
                        }
                        else
                        {
                            BigDecimal rv = new BigDecimal(refVal);
                            BigDecimal uv = new BigDecimal(userVal);
                            diffs[r][c] = new ResultDifference(rv.subtract(uv).abs());
                        }
                    }
                }

            thisResult.setNumDifferences(failCount);
            thisResult.setDifferences(diffs);

            if (failCount > 0)
                OnlineSTS.logInfo(httpRequest, "Found " + failCount
                                  + " difference" + (failCount > 1 ? "s" : "")
                                  + " in numerical results of case " + name);
        }

        OnlineSTS.logInfo(httpRequest, "... Finished analysis.");
        return results;
    }


    private File createUploadDir()
        throws ServletException
    {
        // On sbml, the root of this directory ends up being
        // /usr/share/tomcat5/temp/testsuite/

        File dir = new File(System.getProperty("java.io.tmpdir", "/tmp"),
                            "testsuite" + File.separator
                            + String.valueOf(System.currentTimeMillis()));

        if (! dir.mkdirs())
            throw new ServletException("Can't create directory "
                                       + dir.getPath() + ".");

        if (! dir.canRead())
            throw new ServletException("Can't read directory "
                                       + dir.getPath() + ".");

        return dir;
    }

    private TreeSet<UserTestCase> unzipUserArchive(ServletFileUpload reqHandler)
        throws ServletException, IOException
    {
        // parseRequest() returns a list of items, but our particular
        // httpRequest will only have one: the zip file uploaded by the user.
        // If we don't get that, something went wrong.

        List items;
        try
        {
            items = reqHandler.parseRequest(httpRequest);
        }
        catch (FileUploadException e)
        {
            propagateError(BAD_UPLOAD, e);
            return null;
        }

        // Some sanity checking.  The case of > 1 shouldn't happen because
        // we're in control of this part (via uploadresults.jsp), but let's
        // check it in case someone edits things in the future and
        // inadvertently breaks this part.

        if (items.isEmpty())
        {
            propagateError(BAD_UPLOAD, "No file uploaded.");
            return null;
        }
        else if (items.size() > 1)
        {
            propagateError(BAD_UPLOAD, "More than one file uploaded.");
            return null;
        }

        // Unzip the file and write out the individual file contents to
        // disk.  This will create a bunch of files that are expected to be
        // the user's CSV results files.  We create objects representing
        // each of these user results and put them in a list.  We ignore
        // any files that don't have the expected name pattern.

        FileItem zipFile               = (FileItem) items.get(0);
        TreeSet<UserTestCase> cases    = new TreeSet<UserTestCase>();
        ArrayList<String> badFileNames = new ArrayList<String>();
        try
        {
            ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
            ZipEntry entry;
            UserTestCase theCase;

            while ((entry = zis.getNextEntry()) != null)
            {
                String fileName   = entry.getName();
                String caseNumStr = caseNameFromFileName(fileName);
                if (caseNumStr == null)
                {
                    badFileNames.add(fileName);
                    continue;
                }

                File path = UserTestCase.pathToDataFile(uploadDir, caseNumStr);
                FileOutputStream fs = new FileOutputStream(path);
                BufferedOutputStream bos = new BufferedOutputStream(fs, 2048);

                int count;
                byte data[] = new byte[2048];

                while ((count = zis.read(data, 0, 2048)) != -1)
                    bos.write(data, 0, count);

                bos.flush();
                bos.close();

                theCase = new UserTestCase(refCasesDir, uploadDir, caseNumStr);
                cases.add(theCase);
            }
            zis.close();
        }
        catch (Exception e)
        {
            propagateError(SERVER_ERROR, e);
            return null;
        }

        if (cases.size() >= 1)
        {
            OnlineSTS.logInfo(httpRequest, "Got " + cases.size() + 
                              " case" + (cases.size() > 1 ? "s" : ""));
            return cases;
        }
        else
        {
            if (badFileNames.size() >= 1)
                propagateError(BAD_FILE_NAMES, badFileNames.get(0));
            else
                propagateError(EMPTY_ARCHIVE, zipFile.getName());

            return null;
        }
    }

    private String caseNameFromFileName(String fileName)
    {
        final Pattern caseNameRegexp = Pattern.compile("(\\d{5}).*\\.[Cc][Ss][Vv]$");

        Matcher matcher = caseNameRegexp.matcher(fileName);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    private File getReferenceCasesDir()
        throws ServletException, IOException
    {
        File dir = new File(getServletContext().getRealPath("/test-cases"));
        if (! dir.exists())
        {
            propagateError(SERVER_ERROR, "Nonexistent /test-cases directory.");
            return null;
        }
        if (! dir.canRead())
        {
            propagateError(SERVER_ERROR, "Unreadable /test-cases directory.");
            return null;
        }
        return dir;
    }

    private final boolean tolerable(double expectedVal, double actualVal,
                                    double absTol, double relTol)
    {
        if (Double.isInfinite(expectedVal))
            return (expectedVal == actualVal);
        else if (Double.isNaN(expectedVal))
            return Double.isNaN(actualVal);
        else if (Double.isNaN(actualVal) || Double.isInfinite(actualVal))
            return false;
        else
        {
            BigDecimal expected      = new BigDecimal(expectedVal);
            MathContext mc           = new MathContext(expected.precision());
            BigDecimal adjusted      = new BigDecimal(actualVal).round(mc);
            BigDecimal actualDiff    = expected.subtract(adjusted).abs();
            BigDecimal rtol          = new BigDecimal(relTol);
            BigDecimal atol          = new BigDecimal(absTol);
            BigDecimal allowableDiff = atol.add(rtol.multiply(expected.abs()));

            return (actualDiff.compareTo(allowableDiff) <= 0);
        }
    }

//          System.err.println("actual = " + actualDiff
//                             + ", adjusted = " + adjusted
//                             + ", allowable = " + allowableDiff);


    private void propagateError(String code, String msg)
        throws ServletException, IOException
    {
        propagateError(code, msg, null);
    }

    private void propagateError(String code, Exception e)
        throws ServletException, IOException
    {
        propagateError(code, e.toString(), e);
    }

    private void propagateError(String code, String msg, Exception e)
        throws ServletException, IOException
    {
        OnlineSTS.logError(httpRequest, "(" + code + ") " + msg);
        if (e != null) e.printStackTrace(System.err);
        httpRequest.setAttribute("errorCode", code);
        httpRequest.setAttribute("errorDetails", msg);
        httpRequest.setAttribute("errorSource", this.getClass().getName());
        getServletContext().getRequestDispatcher(ERROR_PAGE)
            .forward(httpRequest, httpResponse);
    }

    // 
    // -------------------------- Private variables ---------------------------
    // 

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;

    private File uploadDir;
    private File refCasesDir;

    // 
    // -------------------------- Private constants ---------------------------
    // 

    // The page that handles forwarded error messages returned to the user:
    private static final String ERROR_PAGE = "/web/error.jsp";

    // The page that handles displaying results to the user:
    private static final String RESULTS_PAGE = "/web/showresults.jsp";

    // Errors are communicated back to error.jsp via the following codes:
    private static final String BAD_UPLOAD      = "Upload request failed";
    private static final String BAD_FILE_NAMES  = "Bad file names";
    private static final String EMPTY_ARCHIVE   = "Empty archive";
    private static final String SERVER_ERROR    = "Server error";
    private static final String USER_DATA_ERROR = "User data error";

} // end of class
