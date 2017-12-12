//
// @file    ZipServlet.java
// @brief   Servlet to zip the test cases for the user.
// @author  Michael Hucka
// @author  Kimberly Begley
// @date    Created Jul 30, 2008, 9:25:21 AM
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2010-2015 jointly by the following organizations: 
//     1. California Institute of Technology, Pasadena, CA, USA
//     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
//     3. University of Heidelberg, Heidelberg, Germany
//
// Copyright (C) 2008-2009 California Institute of Technology (USA).
//
// Copyright (C) 2004-2007 jointly by the following organizations:
//     1. California Institute of Technology (USA) and
//     2. University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available on the Web at
// http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.regex.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Called by the Online STS's process.jsp to create a zip archive of the
 * test cases selected by the user.  It omits certain files from the zip
 * archive, based on a regular expression defined below.
 * <p>
 * This class gets the selection parameters from the HTTP session data; it
 * also gets other data from the session, such as the location of the test
 * cases directory.
 */
public class ZipServlet extends HttpServlet
{
    // 
    // --------------------------- Public methods ----------------------------
    // 

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param req servlet request
     * @param resp servlet response
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        processRequest(req, resp);
    }


    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param req servlet request
     * @param resp servlet response
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        processRequest(req, resp);
    }


    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Servlet to create an archive of test cases for the user";
    }

    // 
    // --------------------------- Private methods ----------------------------
    // 

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. 
     * @param req servlet request
     * @param resp servlet response
     */ 
    private void processRequest(HttpServletRequest request,
                                HttpServletResponse resp)
        throws ServletException, IOException
    {
        try
        {
            HttpSession s = request.getSession(false);
            
            if (s == null)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                               "Null session variable.");

            File root              = (File) s.getAttribute("casesRootDir");
            String levelAndVersion = (String) s.getAttribute("levelAndVersion");

            if (levelAndVersion == null)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                               "Null levelAndVersion attribute in request.");

            if (! checkLevelAndVersion(levelAndVersion))
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                               "Unexpected or nonexistent Level and/or Version number.");

            if (root == null)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                               "Session does not contain data path information.");

            @SuppressWarnings("unchecked")
            Vector<String> cases = (Vector<String>) s.getAttribute("casesToReturn");

            if (cases == null)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Session has no test case information.");

            if (cases.size() < 1)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Empty cases list.");

            resp.setContentType("application/zip");
            resp.setHeader("Content-disposition",
                           "attachment; filename=" + ARCHIVE_NAME + ".zip");

            String includeFileRegex = buildFileIncludeRegex(levelAndVersion);
            OutputStream zipout  = resp.getOutputStream();
            
            if (zipout == null)
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Response output stream is null.");

            ZipOutputStream zos  = new ZipOutputStream(zipout);

            OnlineSTS.logInfo(request, "Zip'ing up " + cases.size() + " cases");

            for (String c : cases)
                addFilesToZip(c, root, ARCHIVE_NAME, includeFileRegex, zos);

            zos.flush();
            zos.close();
        }
        catch (java.io.IOException e)
        {
            OnlineSTS.logWarning(request, "Client aborted download.");
        }
        catch (Exception e)
        {
            OnlineSTS.logError(request, e.toString());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           e.toString());
        }
    }


    /**
     * Recursive function to add a case and all its contents to the zip
     * output stream.
     */
    private void addFilesToZip(String tcase, File root, String prefix,
                               String includeFileRegex, ZipOutputStream zos)
        throws Exception
    {
        File dir = new File(root + File.separator + tcase);

        if (! dir.exists())
            throw new Exception("File '" + dir.toString() + "' not found.");

        String pathInZip = prefix + File.separator + tcase + File.separator;

        Iterator entries = Arrays.asList(dir.listFiles()).iterator();
        while (entries.hasNext())
        {
            File   file = (File) entries.next();
            String name = file.getName();

            if (file.isDirectory())
            {
                addFilesToZip(pathInZip + name + File.separator, file, "",
                              includeFileRegex, zos);
            }
            else if (Pattern.matches(includeFileRegex, name))
            {
                zos.putNextEntry(new ZipEntry(pathInZip + name));

                FileInputStream in = new FileInputStream(file);
                byte buf[]         = new byte[4096];
                int len;

                while ((len = in.read(buf)) > 0)
                {
                    zos.write(buf, 0, len);
                }

                zos.closeEntry();
                in.close();
            }
        }
    }


    /**
     * Builds a regular expression of file names to include in the archive.
     */
    private String buildFileIncludeRegex(String levelAndVersion)
    {
        // 'rx' is the start of the regex, but note that it's not terminated.
        // We add some more pieces below, then close it off at the end.

        String rx = new String("(\\d{5}-plot.jpg|\\d{5}-plot.html|\\d{5}-settings.txt|\\d{5}-model.html|\\d{5}-model.m|\\d{5}-results.csv");
      
        if (levelAndVersion != null)      // Shouldn't happen, but let's be safe.
            for (int i = 0; i < KNOWN_LV_COMBOS.length; i++)
                if (KNOWN_LV_COMBOS[i][0].equals(levelAndVersion))
                    rx += "|\\d{5}-sbml-" + KNOWN_LV_COMBOS[i][1] + "(-sedml)?.xml";

        return rx + ")";
    }

    /**
     * Check that a given Level and Version string is valid.
     */
    private boolean checkLevelAndVersion(String levelAndVersion)
    {
        for (int i = 0; i < KNOWN_LV_COMBOS.length; i++)
            if (KNOWN_LV_COMBOS[i][0].equals(levelAndVersion))
                return true;
        return false;
    }


    // 
    // -------------------------- Private constants ---------------------------
    // 

    private final String ARCHIVE_NAME = new String("SBML_test_cases");
    private final String KNOWN_LV_COMBOS[][] = {
            {"1.2", "l1v2"},
            {"2.1", "l2v1"},
            {"2.2", "l2v2"},
            {"2.3", "l2v3"},
            {"2.4", "l2v4"},
            {"3.1", "l3v1"},
        };

}
