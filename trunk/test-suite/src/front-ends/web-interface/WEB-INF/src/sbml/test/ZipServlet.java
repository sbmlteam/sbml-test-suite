//
// @file    ZipServlet.java
// @brief   Servlet to zip the test cases for the user.
// @author  Kimberly Begley
// @author  Michael Hucka
// @date    Created Jul 30, 2008, 9:25:21 AM
// @id      $Id$
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

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Iterator;
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
  private final String archiveFileBaseName = new String("SBML_test_cases");
  private final String omitFileRegexp =
    new String("\\d{5}-(model.m|results.csv|plot.(jpg|eps))");

  /** 
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods. 
   * @param request servlet request
   * @param response servlet response
   */
  protected void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      HttpSession  session = request.getSession(true);
      File path            = (File) session.getAttribute("path");
      Vector cases         = (Vector) session.getAttribute("cases");

      if (path == null)
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                           "Session does not contain data path information.");

      if (cases == null)
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "Session does not contain test case information");

      if (cases.size() < 1)
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "No test cases selected");
            
      response.setContentType("application/zip");
      response.setHeader("Content-disposition",
                         "attachment; filename=" + archiveFileBaseName + ".zip");

      ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

      Iterator tc = cases.iterator();
      while (tc.hasNext())
      {
        addFilesToZip((String) tc.next(), path, archiveFileBaseName, zos);
      }

      zos.flush();
      zos.close();
    }
    catch (Exception e)
    {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         e.toString());
    }
  }

  /**
   * Recursive function to add a case and all its contents to the zip
   * output stream.
   */
  private void addFilesToZip(String tcase, File root,
                             String prefix, ZipOutputStream zos)
    throws Exception
  {
    File dir = new File(root + File.separator + tcase);

    if (! dir.exists())
      throw new Exception("File '" + dir.toString() + "' not found");

    String pathInZip = prefix + File.separator + tcase + File.separator;

    Iterator entries = Arrays.asList(dir.listFiles()).iterator();
    while (entries.hasNext())
    {
      File   file = (File) entries.next();
      String name = file.getName();

      if (file.isDirectory())
      {
        addFilesToZip(pathInZip + name + File.separator, file, "", zos);
      }
      else if (! Pattern.matches(omitFileRegexp, name))
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
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /** 
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /** 
   * Returns a short description of the servlet.
   */
  public String getServletInfo()
  {
    return "Servlet to zip the test cases for user";
  }
}
