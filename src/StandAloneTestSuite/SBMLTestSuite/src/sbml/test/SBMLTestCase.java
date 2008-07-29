// @file    SBMLTestCase.java
// @brief   SBMLTestCase class for SBML Standalone application
// @author  Kimberly Begley
// 

//
//<!---------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// 
// Copyright 2008      California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available at
// http://sbml.org/Software/SBML_Test_Suite/license.html
//------------------------------------------------------------------------- -->
// Main test functions for SBML Test suite.
//


package sbml.test;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import javax.swing.*;
import java.util.regex.*;
import java.applet.*;

/**
 *This class contains most of the logic for the testing included in the SBML Test Suite.
 * @author Kimberly Begley
 * @version 2.0
 */
public class SBMLTestCase {
    
    public String testname;
	public int start;
	public double duration;
	public int steps;
	public int variables_count;
	public BigDecimal abs;
	public BigDecimal rel;
	
	/**
         * Gets the start time interval
         * @return The start time interval
         */
	public int getStart() {
		return start;
	}
        /**
         * Gets the test name
         * @return The test name
         */
	public String getTestname() {
		return testname;
	}
        /**
         * Gets the duration of the test
         * @return The duration of the test
         */
	public double getDuration() {
		return duration;
	}
        /**
         * Gets the number of steps
         * @return The number of steps
         */
	public int getSteps() {
		return steps;
	}
        /**
         * Gets the number of variables
         * @return The number of varaibles in the test
         */
	public int getVariables_count() {
		return variables_count;
	}
        /**
         * Gets the Absolute allowable difference
         * @return the absolute allowable difference
         */
	public BigDecimal getAbs() {
		return abs;
	}
        /**
         * Gets the relative allowable difference
         * @return the relative allowable difference
         */
	public BigDecimal getRel() {
		return rel;
	}
        /**
         * Gets the test case directory
         * @return A string containing the path to the test case directory - Used in web application only
         * @throws java.io.IOException if the config file cannot be opened
         * @throws java.io.FileNotFoundException if the config file cannot be found
         */
	public String getSbmlTestdir() throws IOException, FileNotFoundException {
		String sbmltestdir = new String();
		File sbmlconfigfile = new File("/usr/share/apache-tomcat-5.5.26/webapps/test_suite/WEB-INF/sbml_config_file.txt");
//		File sbmlconfigfile = new File("sbml_config_file.txt");

		String[][] config = new String[1][1];
//		try{
			BufferedReader bufRdr  = new BufferedReader(new FileReader(sbmlconfigfile));
			String line = null;
		
			while((line = bufRdr.readLine()) != null)
			{	
				line.trim();
				sbmltestdir = line;	
			}
			bufRdr.close();
//		}
		
	return sbmltestdir;
	}
        /**
         * Gets the appropriate settings file for a given test case.
         * @param value the test case name
         * @param testdir the test case directory
         * @return returns a path to the settings file
         */
	public String getSettingsFile(String value, String testdir) {
		String settingsfile = new String();
		settingsfile = testdir + File.separator + value + File.separator + value + "-settings.txt";

		return settingsfile;
	}
        /**
         * Gets the control results for a given test case.
         * @param value the test case name
         * @param testdir the test case directory
         * @return returns a path to the test case control results file
         */
	public String getControlResults(String value, String testdir) {
		String resultfile = new String();
		resultfile = testdir + File.separator + value + File.separator + value + "-results.csv";

		return resultfile;
	}
	/**
         * Gets the plot file path for a given test case
         * @param value the test case name
         * @param testdir the test case directory
         * @return returns a path to the test case plot file
         */
	public String getPlotFile(String value, String testdir) {
		String plotfile = new String();
		plotfile = testdir + File.separator + value + File.separator + value + "-plot.jpg";

		return plotfile;
	}
        /**
         * Gets the html file path for a given test case
         * @param value the test case name
         * @param testdir the test case directory
         * @return returns a path to the html file of test case details 
         */
	public String getHtmlFile(String value, String testdir) {
		String htmlfile = new String();
		htmlfile = testdir + File.separator + value + File.separator + value + "-model.html";

		return htmlfile;
	}

        /**
         * Reads the results of a test case result file into a two dimensional bigdecimal array 
         * @param filename filename to read
         * @param header 1 or 0 indicates the presence of a header - at this time it is always set to 1 expecting all files to have a header
         * @param steps the number of steps as given in the settings file
         * @param variables_count the number of variables as giving in the settings file
         * @return a 2D BigDecimal array of results.
         * @throws java.io.IOException
         * @throws java.io.FileNotFoundException
         * @throws java.lang.NumberFormatException
         * @throws java.lang.Exception returns an exception if the file does not have the indicated number of variables as read from the settings fiel
         */
	public BigDecimal[][] readResults(String filename, int header, int steps, int variables_count) throws IOException, FileNotFoundException, NumberFormatException, Exception {
	/*  readResults - returns a csv file as a multidimensional array
		Input:a cvs file (filename) along with whether it has a header (header=1/0), step count (steps), number of variables
		(variables_count)  
		Output: multidimensional array.
	*/	
		BigDecimal [][] value = new BigDecimal [steps+1][(variables_count+1)];
 
		File testfile = new File(filename);
		/*try{ */
			BufferedReader bufRdr  = new BufferedReader(new FileReader(testfile));
			String line = null;
			int row = 0;
			int col = 0;
			// if header is present skip the header line
			if(header == 1) {
				line=bufRdr.readLine();
				
			}
			//read each line of test results file
			
			while((line = bufRdr.readLine()) != null && row < steps+2)
			{	
				String[] st = line.split(",");
				if(st.length != variables_count+1) {
					// file has too many variables - throw an exception
					throw new Exception ("File has inconsistent number of variables");
						
				}
				for(int i = 0; i < st.length; i++) {
					
                                               	value[row][col] = new BigDecimal(st[i].trim());
						col++;
					
																		
				}
				col = 0;
				row++;
			}
			bufRdr.close();
		
		return value;
	}
	/**
         * Makes sure the user result file has the same number of timesteps etc as defined in the settings file
         * @param resultdata control result multidimensional array
         * @param userresultdata user result multidimensional array
         * @throws java.lang.Exception throws an exception if there are an inconsistant number of rows between the two input files and if the time step values do not match up
         */
	public void validateResults(BigDecimal [][] resultdata, BigDecimal [][] userresultdata) throws Exception {
	/*  validateResults - makes sure that the users result file has the same timesteps etc as defined in the settings file
		Input: user result mulitidimensional array
		Output: none - returns an error if file not in correct format
	*/
	if(resultdata.length != userresultdata.length) {
		throw new Exception ("Inconsistent number of rows");
		
	}
	for ( int i = 0; i < resultdata.length; i++ )
	{
		if((resultdata[i][0]).compareTo(userresultdata[i][0]) != 0) {
			throw new Exception ("Time step values do not match up with test suite case");
			
		}
	}
	}
	/**
         * Subtracts the corresponding fields to give the difference between the values in the user and control result files
         * @param resultdata control result data multidimensional array
         * @param inputdata  user result data multidimensional array
         * @param steps the number of steps in the test
         * @param variables_count the number of variables in the test
         * @return returns a multidimensional array containing the differences of the compared files.
         * @throws java.lang.Exception if the files are of different lengths it returns an exception
         */
	public BigDecimal[][] compareResults(BigDecimal [][] resultdata, BigDecimal [][] inputdata, int steps, int variables_count) throws Exception{
	/*	compareResults  - subtracts each corresponding field to give the differences
		Input: the multidimensional array from the control result data file and a multidimensional
		array from the user data file   
		Output: a multidimensional array containing the differences of input files
	*/
	BigDecimal [][] result = new BigDecimal [steps+1][variables_count+1];
	int result_len = resultdata.length;
	int input_len = inputdata.length;
	// check that the files are the same length
	
	if(resultdata.length != inputdata.length) {
		throw new Exception ("Files are of different lengths - cannot compare them");
	}
	else {
	 for ( int i = 0; i < resultdata.length; i++ )
	{
		
		//check that the row is not null
		if(resultdata[i][0] != null) {
			
			//for each column
			for ( int j = 0; j < resultdata[j].length; j++) 
			{
				result[i][j] = resultdata[i][j].subtract(inputdata[i][j]);
			}
		}
	  } 
	  }
	  return result;
	} 
        /**
         * Looks at the allowable difference and how it compares to the differences of the user and control files
         * @param control control file results multidimensional array
         * @param differences differences mulitdimensional array (from the compareResults function) 
         * @param variables number of variables
         * @param absolute the absolute allowable difference from the settings file
         * @param relative the relative allowable difference from the settings file
         * @return and integer - the number of failed comparisons in the file - if equal to zero then the test passed.
         */
	public int analyzeResults(BigDecimal [][] control, BigDecimal [][] differences, int variables, BigDecimal absolute, BigDecimal relative) {
	/*	anaylzeResults - looks at the allowable difference and how it compares to the differences of the user and control files
		Input: differences array (from compareResults function), absolute difference, relative difference
		Output: integer - number of failed comparisons in file - if 0 then passed the test 
		*/
		
		int pass_fail = 0;
		int fcof = -1;
		int frof = -1;
		for(int i = 0; i < differences.length; i++) {
		
			// first column will always be time so we start at second column
			for( int j = 1; j < (differences[i].length); j++) {
				
				//System.out.println("differences at i length is " + differences[i].length);
			/*Check out which is greater absolute value or differences value if((((differences[i][j].abs()).max(absolute)).compareTo(absolute) == 0
			then the absolute difference between the user and control files is allowable and therefore it passes the test for that row/column value
			*/		
				//if the absolute difference is greater than the allowable absolute value
			/*	if((((differences[i][j].abs()).max(absolute)).compareTo(absolute) != 0)) {
					//System.out.println("Failed the absolute allowable difference- now testing the relative allowable difference at line " + i + "column " +j);
					
					// fails absolute difference test - now check if it passes relative test
					if(((((differences[i][j].abs())).max((relative).multiply(control[i][j]))).compareTo(relative)) != 0) {
						// fails both relative and absolute tests
						//System.out.println("Fails both relative and absolute tests here row " +i);
						//return a fail
						pass_fail++;
					}
				} */
				// if the absolute value of the differences is greater than the relative allowable difference multiplied
				// by the control value added to the absolute allowable difference - the test fails
				if((differences[i][j].abs()).compareTo(relative.multiply(control[i][j]).add(absolute)) == 1) {
					//fails test
					pass_fail++;
					if(frof == -1 && fcof == -1) {
						frof=i;
						fcof=j;
					}
				}
			}	
		}
		return pass_fail;
	}
	/**
         * Reads the values in the settings file 
         * @param filename path to the settings file
         * @throws java.io.IOException thrown if the settings file cannot be opened.
         * @throws java.io.FileNotFoundException thrown if the settings file cannot be found.
         */
	public void readSettingsFile(String filename) throws IOException, FileNotFoundException {
	/* Read settings file for test into a hash table rea
	   Input: is settings file name
	   Output: nothing - it sets the class variables from the settings file
	*/
	File settingsfile = new File(filename);
	String line = null;
	String text = null;
	String nexttext = null;
	Map<String, String> m = new HashMap<String, String>();
	
	try {
		BufferedReader bRdr  = new BufferedReader(new FileReader(settingsfile));
		while((line = bRdr.readLine()) != null) {
				//StringTokenizer st = new StringTokenizer(line,":");
				String[] st = line.split(":");
				
				for(int i = 0; (i+1)< st.length; i++)
                                        m.put(st[i].trim(),st[i+1].trim());

/*				while (st.hasMoreTokens()) { 
					//get text values and store in a hash table
					// change this if settings becomes more than 2 columns
					text = st.nextToken();
					text = text.trim();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
					//System.out.println("text,nexttext is " + text +" " + nexttext);
					m.put(text,nexttext);
				}
*/
		}
		bRdr.close();
		
		String stringstart = (String)m.get("start");
		start = Integer.parseInt(stringstart);
		String stringduration = (String)m.get("duration");
		duration = Double.parseDouble(stringduration);
		String variables = (String)m.get("variables");
		
		// read variables value and return a count of the number of variables 
		
		//int countv = 0;
		//StringTokenizer var = new StringTokenizer(variables,",");
		String[] var = variables.split(",");
		/*for(int j = 0; j<var.length; j++) {
					
		while (var.hasMoreTokens()) { 
			//get next token and store it in the array
			String num = var.nextToken();
			countv++;
		}
*/
		//variables_count = countv;
		variables_count=var.length;
		// get the steps variable from the settings file
		String st = (String)m.get("steps");
		steps = Integer.parseInt(st);
		// get the absolute allowable difference value
		String ad = (String)m.get("absolute");
		abs = new BigDecimal(ad);
		// get the relative allowable difference value
		String rd = (String)m.get("relative");
		rel = new BigDecimal(rd); 


	}
	catch(FileNotFoundException e) {
    			System.err.println("FileNotFoundException reading settings file ");
			e.printStackTrace();
		}
	catch(IOException e) {
		// catch possible io errors from readLine()
			System.err.println("IOException error reading settings file");
			e.printStackTrace();
		}
	
	}
        /**
         * Gets the user test file directory and checks the format of the files in the directory - only used in web version of test suite.
         * @param dirname path to the test case directory
         * @return a hashmap of the testname and user result file name 
         * @throws java.lang.Exception thrown if the user test file has an incorrect name format.
         */
public Map getUsertestlist(String dirname) throws Exception{
	/* Gets the user file directory and checks the format of the files in the directory
	   Input: is the directory path
	   Output: a hashmap with the testname and user result file name as associated columns
	*/
	
	Map<String, String> m = new HashMap<String, String>(1000);
	String userdir_listing [];
	String userfile = new String();
	String value = new String();
	File u = new File(dirname);
	userdir_listing = u.list();
	
	if(userdir_listing.length>0) {
		
	for(int i = 0; i < userdir_listing.length; i++) {
	
		// set user result filename
		userfile=dirname + File.separator + userdir_listing[i];
		// Match test case identifier
		Pattern p = Pattern.compile("\\d{5}+\\.csv$");
		Matcher matcher = p.matcher(userfile);
		if(matcher.find()) {
			value = matcher.group().substring(0,matcher.group().indexOf("."));	
			m.put(value,userfile);
		}
		else {
			System.out.println("User test files in incorrect file name format - please name files ending with nnnnn.csv - where nnnnn is the test number " + userfile);
			throw new Exception ("Incorrect file name format");
			
			
		}
	}
	}
	return m;
}
/**
 * Deletes a directory containing uploaded test cases.
 * @param path path to test case files
 * @return boolean value indicating success of deletion.
 */
public boolean deleteDirectory(File path) {
    if( path.exists() ) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
         if(files[i].isDirectory()) {
           deleteDirectory(files[i]);
         }
         else {
           files[i].delete();
         }
      }
    }
    return( path.delete() );
  }


}
