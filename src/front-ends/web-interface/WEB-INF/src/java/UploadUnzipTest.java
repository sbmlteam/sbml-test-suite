// 
// @file    UploadUnzipTest.java
// @brief   Uploads a zip file, unzips the contents & tests the uploaded data.
// @author  Kimberly Begley
// @date    Created Jul 30, 2008, 9:25:21 AM
//
//----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
// more information about SBML, and the latest version of the SBML Test Suite.
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


public class UploadUnzipTest extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        // check if the http request is a multipart request
        // with other words check that the http request can have uploaded files
        if (isMultipart) {
            String base_directory = System.getProperty("java.io.tmpdir");
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            servletFileUpload.setSizeMax(-1);

            try {
                String systimestamp = String.valueOf(System.currentTimeMillis());
                String directory = "testsuite" + File.separator + systimestamp;

                // Parse the request
                List items = servletFileUpload.parseRequest(request);

                // Process the uploaded items
                Iterator iter = items.iterator();

                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
				
                    // the param tag directory is sent as a request parameter to the server
                    // check if the upload directory is available
                    if (item.isFormField()) {

                        String name = item.getFieldName();

                        if (name.equalsIgnoreCase("directory")) {
                            directory = item.getString();
                        }

                        // retrieve the files
                    } else {
					
                        // the fileNames are urlencoded
                        String fileName = URLDecoder.decode(item.getName(), "UTF-8");
        				
                        File file = new File(directory, fileName);
                        file = new File(base_directory, file.getPath());
					
                        // retrieve the parent file for creating the directories
                        File parentFile = file.getParentFile();

                        if (parentFile != null) {
                            parentFile.mkdirs();
                        }

                        // make sure its a zipped file
                        item.write(file);
					
                        // unzip the file
                        BufferedOutputStream dest = null;
                        // Create input stream to read zip entries
					
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                            ZipEntry entry;
					
                            while ((entry = zis.getNextEntry()) != null) {
                                int count;
                                byte data[] = new byte[2048];

                                // write the files to the disk
                                FileOutputStream fos = new FileOutputStream(base_directory
                                                                            + File.separator + directory
                                                                            + File.separator + entry.getName());
                                dest = new BufferedOutputStream(fos, 2048);

                                while ((count = zis.read(data, 0, 2048)) != -1) {
                                    dest.write(data, 0, count);
                                }
                                dest.flush();
                                dest.close();
                            } // end of while

                            zis.close();
                        } catch (FileNotFoundException e) {
                            System.err.println("FileNotFoundException: "
                                               + e.getMessage());
    						
    
                        } catch (IOException e) {
                            System.err.println("Caught IOException: " 
                                               + e.getMessage());
                        }

                        if (!file.delete()) {
                            throw new IllegalArgumentException("Delete: deletion failed");
                        }
  					
                    } //end of else
                }//end of while

                // add test stuff here
			
                String userdir = new String(base_directory  + File.separator + directory);
                File u = new File(userdir);
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
                try {
                    userfiles = t1.getUsertestlist(userdir);
                } catch (Exception e) {
                    System.err.println("Incorrect user file name format");
                    String error = "User test files in incorrect file name format " +
                        " -- please name files with the ending 'nnnnn.csv', " +
                        "where nnnnn is the test case number."; 
                    String nextJSP = "/web/error.jsp";
                    request.setAttribute("error", error);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
                    dispatcher.forward(request, response);
				
                }
                String value = new String();
                String userfile = new String();
                Set set = userfiles.keySet();
                Iterator iter2 = set.iterator();
                int totalpoints=0;
			

                while (iter2.hasNext()) {
				
                    value = (String)iter2.next();
                    userfile = (String)userfiles.get(value);
                    System.out.println(userfile);
                    String controlfile_results = t1.getControlResults(value, testdir);

                    String control_settingsfile = t1.getSettingsFile(value, testdir);

                    String plot_file = t1.getPlotFile(value, testdir);
                    String html_file = t1.getHtmlFile(value, testdir);
				
                    try {
                        t1.readSettingsFile(control_settingsfile);
                        totalpoints = t1.getVariables_count() * t1.getSteps();
                    } catch (FileNotFoundException e) {
                        System.err.println("Filenotfound when reading control results");
					
                        continue;
                    } catch (IOException e) {
                        System.err.println("IOException error while reading control results");
                        continue;
                    } catch (Exception e) {
                        System.err.println("Control file has inconsistent number of variables for test");
                        continue;
                    }

                    int steps = t1.getSteps();
                    int vars  = t1.getVariables_count();
                    BigDecimal absd = t1.getAbs();
                    BigDecimal reld = t1.getRel();

                    BigDecimal [][] results = new BigDecimal [steps + header][];
                    try {
                        results = t1.readResults(controlfile_results, header, steps, vars);
                    } catch (FileNotFoundException e) {
                        System.err.println("Filenotfound when reading control results");
                        continue;
                    } catch (IOException e) {
                        System.err.println("IOException error while reading control results");
                        continue;
                    } catch (Exception e) {
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
                    try {
                        user_results = t1.readResults(userfile, header, steps, vars);
                    } catch (FileNotFoundException e) {
                        System.err.println("Filenotfound when reading user results");
                        continue;
                    } catch (IOException e) {
                        System.err.println("IOException error while reading user results");
                        continue;
                    } catch (Exception e) {
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
			
                    try {
                        t1.validateResults(results, user_results);
                    } catch (Exception e) {
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
                    try {
                        comp_results = t1.compareResults(results,user_results, steps, vars);
                    } catch (Exception e) {
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

		// delete temporary user file directory
		t1.deleteDirectory(u);
		
		request.setAttribute("tests", output);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/showresults.jsp");
		dispatcher.forward(request, response);

            } // end of try
            catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
      	
        } // end of if	
    
    } // end of doPost

} // end of class
