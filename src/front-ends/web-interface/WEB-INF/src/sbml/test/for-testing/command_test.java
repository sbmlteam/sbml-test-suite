//
//  command_test.java
//  
//
//  Created by Kimberly Begley 
//  Description: Simple command line test program to test class functions
//  Preparation: Compile with sbmlTestcase.class and TestResultDetails.class and run.
//  Usage: java command_test /directory/of/user/results /directory/of/control/results
//  Output: "testname, passed test" OR "testname, failed test at nn points"
//  Latest revision date: 10-Mar-2008
//  Java version: jdk1.5.0_14
//  
//
import sbml.test.sbmlTestcase;
import sbml.test.TestResultDetails;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.math.*;
import javax.swing.*;
import java.util.regex.*;

public class command_test {

	public static void main(String[] args) {
	
		String userdir = new String(args[0]);
		String testdir = new String(args[1]);
		String control_settingsfile = new String();
		String testdir_listing [];
		int header = 1; 
		// Get directory listings of the input directory strings 
		File f = new File(testdir);
		testdir_listing = f.list();
		

		sbmlTestcase t1 = new sbmlTestcase();
		// Get the listing of tests in the user directory
		Map  userfiles = new HashMap();
		try {
			userfiles = t1.getUsertestlist(userdir);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		String value = new String();
		String userfile = new String();
		Set set = userfiles.keySet();
		Iterator iter = set.iterator();
		
		
		// For each file in the user directory - compare the results
		while(iter.hasNext()) {
			value = (String)iter.next();
			System.out.println("file is " + value);
			userfile = (String)userfiles.get(value);
			// Get the control results and the case settings file
			// note: File.separator has been known to possibly cause errors on some OS's - so far no problems though
			String controlfile_results = testdir + File.separator + value + File.separator + value + "-results.csv";
			control_settingsfile = testdir + File.separator + value + File.separator + value + "-settings.txt";
			
			// Get variables from the settings file
			try {
				t1.readSettingsFile(control_settingsfile);
			}
			catch (FileNotFoundException e) {
				System.out.println("Settings file not found");
				break;
			}
			catch (IOException e){
				System.out.println("IOException reading settings file");
				break;
			}
			int steps = t1.getSteps();
			int vars  = t1.getVariables_count();
			
			BigDecimal absd = t1.getAbs();
			BigDecimal reld = t1.getRel();
			
			// Read the control results into a multidimensional array
			BigDecimal [][] results = new BigDecimal [steps + header][];
			try {
				results = t1.readResults(controlfile_results, header,steps,vars);
			}
			catch(FileNotFoundException e) {
				System.out.println("Filenotfound when reading control results");
				continue;
			}
			catch(IOException e) {
				System.out.println("IOException error while reading control results");
				continue;
			}
			catch(Exception e) {
				System.out.println("Control file has too many variables for test");
				continue;
			}
			// Read the user results into a multidimensional array
			BigDecimal [][] user_results = new BigDecimal [steps + header][];
			try {
				user_results = t1.readResults(userfile, header,steps,vars);
			}
			catch(FileNotFoundException e) {
				System.out.println(e.getMessage());
				continue;
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				continue;
			}
			catch(NumberFormatException e) {
				System.out.println("Corrupt data entry - skipping test");
				continue;
			}
			catch(Exception e) {
				//System.out.println("User file has inconsistent number of variables for test");
				System.out.println(e.getMessage());
				continue;
			}
			
			// Compare the results file for same number of rows, columns etc
			try{
				t1.validateResults(results,user_results);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
			BigDecimal [][] comp_results = new BigDecimal [steps+1][vars+1];
			// Get the differences between each column of results files
			try{			
				comp_results = t1.compareResults(results,user_results,steps,vars);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}

			int pass_fail = 0;
			
			// Compare the differences with the allowable difference
			pass_fail = t1.analyzeResults(results,comp_results,vars,absd,reld);
			
			// Print to screen pass/fail details
			if(pass_fail == 0) { 
				System.out.println("passed test " + value); 
			}
			else { 
				System.out.println("failed test at " +pass_fail + " points");
			}
		}
	}
}
