
// 
// @file    ZipServelt.java
// @brief   Servlet to zip the test cases for the user.
// @author  Kimberly Begley
// @date    Created Jul 30, 2008, 9:25:21 AM
//
//
//----------------------------------------------------------------------------
//This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
//more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright 2008      California Institute of Technology.
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
package sbml.test;
import java.io.*;
import java.net.*;

import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;


public class ZipServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // response.setContentType("text/html;charset=UTF-8");
        // PrintWriter out = response.getWriter();

        try {
            
            response.setContentType("application/zip");
	//response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=selectedSbmlTestCases.zip");
            
            HttpSession session = request.getSession(true);

            File path = (File) session.getValue("path");
            Vector<String> selectedCases = (Vector<String>) session.getValue("tcases");
            
            
            if (path == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Session does not contain data path information.");
            } 
            
            if (selectedCases == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Session does not contain test case information");
            } 
            
            if (selectedCases.size() < 1) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No test cases selected");
            }
            

            
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

    //        File path = new File(request.getParameter("path"));
            Iterator tc = selectedCases.iterator();
            
            while(tc.hasNext()) {
                String tcase = (String)tc.next();
                addFilesToZip(new File(path+File.separator+tcase),File.separator+tcase+File.separator , zipOutputStream);
            }
            
        //    addFilesToZip(path, File.separator, zipOutputStream);


            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        } finally {
        }
    }

    private void addFilesToZip(File path, String pathInZip, ZipOutputStream zipOutputStream) throws Exception {

        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    addFilesToZip(files[i], pathInZip + files[i].getName() + File.separator, zipOutputStream);
                } else {
                    try {
                        FileInputStream in = new FileInputStream(files[i]);


                        zipOutputStream.putNextEntry(new ZipEntry(pathInZip + files[i].getName()));

                        byte buf[] = new byte[2048];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            zipOutputStream.write(buf, 0, len);
                        }
                        zipOutputStream.closeEntry();
                        in.close();

                    } catch (IOException e) {
                        throw e;
                    }
                }
            }
        } else {
            throw new Exception("File not found");
        }




    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Servlet to zip the test cases for user";
    }
    // </editor-fold>
}
