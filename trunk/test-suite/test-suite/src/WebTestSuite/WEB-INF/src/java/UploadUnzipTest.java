//
//
// UploadUnzipTest.java
// Created by Kimberly Begley
// This is the main servlet file in the web application. It is called by the web form when a user uploads
// a zip file to the server. It performs the upload, unzips the file, writes the files to a temporary folder,
// analyzes the results and places the test-suite details in a vector of TestResultDetails and calls a jsp
// to display the results.
// Changing every day as well.
//



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
import sbml.test.sbmlTestxml;
import java.math.*;


public class UploadUnzipTest extends HttpServlet {
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		// check if the http request is a multipart request
		// with other words check that the http request can have uploaded files
		if (isMultipart) {
  			String base_directory = System.getProperty("java.io.tmpdir");
  			FileItemFactory factory = new DiskFileItemFactory();
  			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
  			servletFileUpload.setSizeMax(-1);

			try {

			

    			String directory = "testsuite";

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
					String fileName = URLDecoder.decode(item.getName(),"UTF-8");
        				
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
					
					try{

         					FileInputStream fis = new FileInputStream(file);
						
					
					
         				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
         				ZipEntry entry;
					
						while((entry = zis.getNextEntry()) != null) {
            						int count;
            						byte data[] = new byte[2048];

            						// write the files to the disk
            						FileOutputStream fos = new FileOutputStream(base_directory + File.separator+ directory + File.separator + entry.getName());
            						dest = new BufferedOutputStream(fos, 2048);

            						while ((count = zis.read(data, 0, 2048)) != -1) {
               							dest.write(data, 0, count);
            						}
            					dest.flush();
            					dest.close();
         					} // end of while
         				zis.close();
					
					}	
					catch (FileNotFoundException e) {
    						System.err.println("FileNotFoundException: " 
                        				+ e.getMessage());
    						
    
					} catch (IOException e) {
    						System.err.println("Caught IOException: " 
                        				+ e.getMessage());
					}
        				boolean success = file.delete();

    					if (!success)
      						throw new IllegalArgumentException("Delete: deletion failed");
  					
      					} //end of else
    			}//end of while

			// add test stuff here
			
			String userdir = new String(base_directory  + File.separator + directory);
			sbmlTestcase t1 = new sbmlTestcase();
			
			String testdir = t1.getSbmlTestdir();
			String testdir_listing [];

			// get presence of a header line from the user when they upload maybe?
			int header = 1; 
			// Get directory listings 
			File f = new File(testdir);
			testdir_listing = f.list();

			// this is the vector of results that will be input to the page that makes the output to be displayed on screen
			Vector<TestResultDetails> output = new Vector<TestResultDetails>();

			// use of hashmap here since the user filename could be anything but must contain the testnumber.csv at the end of it - so this returns a map with testname, userfilename so we can iterate through the tests
			Map  userfiles = new HashMap();
			
			userfiles = t1.getUsertestlist(userdir);

			String value = new String();
			String userfile = new String();
			Set set = userfiles.keySet();
			Iterator iter2 = set.iterator();
			

			while(iter2.hasNext()) {
				
				value = (String)iter2.next();
				userfile = (String)userfiles.get(value);
				String controlfile_results = t1.getControlResults(value,testdir);

				String control_settingsfile = t1.getSettingsFile(value,testdir);

				String plot_file = t1.getPlotFile(value,testdir);

				t1.readSettingsFile(control_settingsfile);
				int steps = t1.getSteps();
				int vars  = t1.getVariables_count();
				BigDecimal absd = t1.getAbs();
				BigDecimal reld = t1.getRel();

				BigDecimal [][] results = new BigDecimal [steps + header][];
				results = t1.readResults(controlfile_results, header,steps,vars);

				BigDecimal [][] user_results = new BigDecimal [steps + header][];
				user_results = t1.readResults(userfile, header,steps,vars);
			
				t1.validateResults(results,user_results);
				BigDecimal [][] comp_results = new BigDecimal [steps+1][vars+1];
				comp_results = t1.compareResults(results,user_results,steps,vars);
			
				int pass_fail = t1.analyzeResults(results,comp_results,vars,absd,reld);

				sbmlTestselection t3 = new sbmlTestselection();
				String mfile = t3.getModelFile(value,testdir);
				t3.readModelFile(mfile);
				String desc = t3.getSynopsis();
				TestResultDetails t2 = new TestResultDetails(pass_fail,value,desc,"",plot_file);
				output.addElement(t2);
				

			
		} // end of while
		//sbmlTestxml t3 = new sbmlTestxml();
		//t3.writetestxml(output);
		request.setAttribute("tests",output);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/showresults.jsp");
		
		dispatcher.forward(request,response);

		} // end of try
		catch (Exception e) {
    			e.printStackTrace();
    			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  		}
      

		
                
                
			
		} // end of if	

    
  } // end of doPost
} // end of class
