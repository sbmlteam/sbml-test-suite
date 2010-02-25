// 
// @file    UploadUnzipTest.java
// @brief   Uploads a zip file, unzips the contents & tests the uploaded data.
// @author  Kimberly Begley
// @author  Michael Hucka
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
//
// This is the main servlet file in the web application. It is called by
// the web form when a user uploads a zip file to the server. It performs
// the upload, unzips the file, writes the files to a temporary folder,
// analyzes the results and places the test-suite details in a vector of
// TestResultDetails and calls a jsp to display the results.  

package sbml.test;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.zip.*;
import sbml.test.sbmlTestcase;
import sbml.test.TestResultDetails;
import sbml.test.sbmlTestselection;
import java.math.*;


public class UploadUnzipTest extends HttpServlet
{
    // FIXME why isn't this sending back exceptions directly?

    // The page that handles forwarded error messages returned to the user:
    static final String ERROR_PAGE = "/web/error.jsp";

    // Errors are communicated back to error.jsp via the following codes:
    static final String ERR_BAD_UPLOAD    = "upload request failed";
    static final String ERR_BAD_FILE_NAME = "bad file names";
    static final String ERR_SERVER_ERROR  = "server error";

    /**
     * Interface function invoked by uploadresults.jsp.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // An upload from uploadresults.jsp is supposed to be a multipart
        // request.  If that's not what we got here, toss an error.
        
        if (! ServletFileUpload.isMultipartContent(request))
        {
            propagateError(ERR_SERVER_ERROR, "Servlet didn't get multipart content",
                           request, response);
            return;
        }

        // Set up objects to extract the user's upload.

        File uploadDir = createUploadDir();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(uploadDir);
        factory.setSizeThreshold(5 * 1024 * 1024); // In bytes.

        ServletFileUpload uploadServlet = new ServletFileUpload(factory);
        uploadServlet.setSizeMax(-1); // Set no limit on upload max size.
        
        // Unzip the uploaded archive.  This returns a list of the CSV file
        // names extracted as a result.

        Map userfiles = unzipUploadedFile(uploadDir, uploadServlet, request, response)

			
        try
        {
            sbmlTestcase t1 = new sbmlTestcase();

            // Get directory listings 
            String testdir = getServletContext().getRealPath("/test-cases");
            File dir = new File(testdir);
            String testdir_listing [] = dir.list();

            // get presence of a header line from the user when they upload maybe?
            int header = 1; 

            // this is the vector of results that will be input to the
            // page that makes the output to be displayed on screen
            Vector<TestResultDetails> output = new Vector<TestResultDetails>();

            // Use of hashmap here since the user filename could be
            // anything but must contain the testnumber.csv at the end
            // of it - so this returns a map with testname,
            // userfilename so we can iterate through the tests

            Map  userfiles = new HashMap();
            try
            {
                userfiles = t1.getUsertestlist(uploadDir.getPath());
            }
            catch (Exception e)
            {
                propagateError(ERR_BAD_FILE_NAME, e.getMessage(), request, response);
                return;
            }

            String value = new String();
            String userfile = new String();
            // turn hashmap into a treemap to order the list
            Map sorteduserfiles = new TreeMap(userfiles);
            Set set = sorteduserfiles.keySet();
            Iterator iter2 = set.iterator();
            int totalpoints=0;
			
            while (iter2.hasNext())
            {
                value = (String)iter2.next();
                userfile = (String)userfiles.get(value);
                System.out.println(userfile);
                String controlfile_results = t1.getControlResults(value, testdir);

                String control_settingsfile = t1.getSettingsFile(value, testdir);

                String plot_file = t1.getPlotFile(value, testdir);
                String html_file = t1.getHtmlFile(value, testdir);
				
                try
                {
                    t1.readSettingsFile(control_settingsfile);
                    totalpoints = t1.getVariables_count() * t1.getSteps();
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("Filenotfound when reading control results");
                    continue;
                }
                catch (IOException e)
                {
                    System.err.println("IOException error while reading control results");
                    continue;
                }
                catch (Exception e)
                {
                    System.err.println("Control file has inconsistent number of variables for test");
                    continue;
                }

                int steps = t1.getSteps();
                int vars  = t1.getVariables_count();
                BigDecimal absd = t1.getAbs();
                BigDecimal reld = t1.getRel();

                BigDecimal [][] results = new BigDecimal [steps + header][];
                try
                {
                    results = t1.readResults(controlfile_results, header, steps, vars);
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("Filenotfound when reading control results");
                    continue;
                }
                catch (IOException e)
                {
                    System.err.println("IOException error while reading control results");
                    continue;
                }
                catch (Exception e)
                {
                    System.err.println("Control file has inconsistent number of variables for test");
                    sbmlTestselection t3 = new sbmlTestselection();
                    String mfile = t3.getModelFile(value, testdir);
                    t3.readModelFile(mfile);
                    String desc = t3.getSynopsis();
                    Vector<String> cvector = t3.getComponenttags();
                    Vector<String> tvector = t3.getTesttags();
                    TestResultDetails t2 = new TestResultDetails(-1, value, desc,
                                                                 "Control file has inconsistent number of variables for test",
                                                                 plot_file, html_file, cvector, tvector, totalpoints);
                    output.addElement(t2);
                    continue;
                }

                BigDecimal [][] user_results = new BigDecimal [steps + header][];
                try
                {
                    user_results = t1.readResults(userfile, header, steps, vars);
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("Filenotfound when reading user results");
                    continue;
                }
                catch (IOException e)
                {
                    System.err.println("IOException error while reading user results");
                    continue;
                }
                catch (Exception e)
                {
                    System.err.println("User file has inconsistent number of variables for test");
                    sbmlTestselection t3 = new sbmlTestselection();
                    String mfile = t3.getModelFile(value, testdir);
                    t3.readModelFile(mfile);
                    String desc = t3.getSynopsis();
                    Vector<String> cvector = t3.getComponenttags();
                    Vector<String> tvector = t3.getTesttags();
                    TestResultDetails t2 = new TestResultDetails(-1, value, desc,
                                                                 "User file has inconsistent number of variables for test -- test skipped",
                                                                 plot_file, html_file, cvector, tvector, totalpoints);
                    output.addElement(t2);
                    continue;
                }
			
                try
                {
                    t1.validateResults(results, user_results);
                }
                catch (Exception e)
                {
                    System.err.println("User file has too many rows for test");
                    sbmlTestselection t3 = new sbmlTestselection();
                    String mfile = t3.getModelFile(value, testdir);
                    t3.readModelFile(mfile);
                    String desc = t3.getSynopsis();
                    Vector<String> cvector = t3.getComponenttags();
                    Vector<String> tvector = t3.getTesttags();
                    TestResultDetails t2 = new TestResultDetails(-1, value, desc, 
                                                                 "User file has too many time steps for test -- test skipped",
                                                                 plot_file, html_file, cvector, tvector, totalpoints);
                    output.addElement(t2);
                    continue;
                }

                BigDecimal [][] comp_results = new BigDecimal [steps+1][vars+1];
                try
                {
                    comp_results = t1.compareResults(results,user_results, steps, vars);
                }
                catch (Exception e)
                {
                    System.err.println("Files are different lengths - cannot compare them");
                    sbmlTestselection t3 = new sbmlTestselection();
                    String mfile = t3.getModelFile(value, testdir);
                    t3.readModelFile(mfile);
                    String desc = t3.getSynopsis();
                    Vector<String> cvector = t3.getComponenttags();
                    Vector<String> tvector = t3.getTesttags();
                    TestResultDetails t2 = new TestResultDetails(-1, value, desc,
                                                                 "User file has inconsistent number of entries -- test skipped",
                                                                 plot_file, html_file, cvector, tvector, totalpoints);
                    output.addElement(t2);
                    continue;
                }

                int pass_fail = t1.analyzeResults(results, comp_results, vars, absd, reld);

                sbmlTestselection t3 = new sbmlTestselection();
                String mfile = t3.getModelFile(value, testdir);
                t3.readModelFile(mfile);
                String desc = t3.getSynopsis();
                Vector<String> cvector = t3.getComponenttags();
                Vector<String> tvector = t3.getTesttags();
                TestResultDetails t2 = new TestResultDetails(pass_fail, value, desc, "",
                                                             plot_file, html_file, cvector, tvector, totalpoints);
                output.addElement(t2);
				
            } // end of while

            request.setAttribute("tests", output);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/web/showresults.jsp");
            dispatcher.forward(request, response);

        } // end of try
        catch (Exception e)
        {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      	

    
    } // end of doPost

    private File createUploadDir()
        throws ServletException
    {
        File uploadDir = new File(System.getProperty("java.io.tmpdir", "/tmp"),
                                  "testsuite" + File.separator
                                  + String.valueOf(System.currentTimeMillis()));

        if (! uploadDir.mkdirs())
            throw new ServletException("Cannot create " + uploadDir.getPath());

        if (! uploadDir.canRead())
            throw new ServletException("Cannot read " + uploadDir.getPath());

        return uploadDir;
    }

    private void propagateError(String code, String msg, HttpServletRequest req,
                                HttpServletResponse resp)
        throws ServletException, IOException
    {
        System.err.println("(" + code + ") " + msg);
        req.setAttribute("errorCode", code);
        req.setAttribute("errorMessage", msg);
        getServletContext().getRequestDispatcher(ERROR_PAGE).forward(req, resp);
    }

    private HashMap unzipUploadedFile(File uploadDir, ServletFileUpload uploadServlet,
                                      HttpServletRequest req, HttpServletResponse resp)
    {
        // parseRequest() returns a list of items, but our particular
        // request will only have one: the zip file uploaded by the user.
        // If we don't get that, something went wrong.

        List items;
        try
        {
            items = uploadServlet.parseReq(req);
        }
        catch (FileUploadException e)
        {
            propagateError(ERR_BAD_UPLOAD, e.getMessage(), req, resp);
            return;
        }

        if (items.isEmpty())
        {
            propagateError(ERR_BAD_UPLOAD, "Empty upload", req, resp);
            return;
        }
        else if (items.size() > 1)
        {
            propagateError(ERR_BAD_UPLOAD, "Got more than one file in upload",
                           req, resp);
            return;
        }

        // Unzip the file and write out the individual file contents to
        // disk.  This will create a bunch of files which are expected to
        // be the user's CSV results files.

        try
        {
            FileItem zipFile   = (FileItem) items.get(0);
            ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null)
            {
                int count;
                byte data[] = new byte[2048];

                FileOutputStream fos = new FileOutputStream(uploadDir.getPath()
                                                            + File.separator
                                                            + entry.getName());

                BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);

                while ((count = zis.read(data, 0, 2048)) != -1)
                {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
            }
            zis.close();
        }
        catch (FileNotFoundException e)
        {
            propagateError(ERR_SERVER_ERROR, e.getMessage(), req, resp);
            return;
        }
        catch (IOException e)
        {
            propagateError(ERR_SERVER_ERROR, e.getMessage(), req, resp);
            return;
        }
    }

} // end of class
