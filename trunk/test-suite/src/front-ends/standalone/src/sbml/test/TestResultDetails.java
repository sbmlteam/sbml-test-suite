// @file    TestResultDetails.java
// @brief   TestResultDetails class for SBML Standalone application
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
// Class and methods for the Test result details generated from testing.
//


package sbml.test;
import java.util.*;

/**
 * TestResultDetails is a class that contains the details of a tested test case
 * @author Kimberly Begley
 * @version 2.0
 */

public class TestResultDetails {
	// the TestResultDetails class has 9 fields
	private int result;
	private int totalpoints;
	private String testname;
	private String description;
	private String warnings;
	private String plot;
	private String html;
	private Vector<String> ctags;
	private Vector<String> ttags;
	
        /**
         * Constructor for TestResultDetails that contains a path to the html file
         * @param output indicates the number of failed tests, if 0 then the test passed if -1 then the test was skipped
         * @param name indicates the test name of the the test case
         * @param desc indicates the synopsis line from the test
         * @param warning indicates any warning messages - only set with skipped tests
         * @param plot_path indicates the path to the plot file
         * @param html_path indicates teh path to the html file
         * @param c vector of component tags associated with test
         * @param t vector of test tags associated with test
         * @param points is the number of variables multiplied by the number os steps
         */
	public TestResultDetails(int output, String name, String desc, String warning, String plot_path, String html_path, Vector<String> c, Vector<String> t, int points) {
		result = output;
		testname = name;
		description = desc;
		warnings = warning;
		plot = plot_path;
		html = html_path;
		ctags = c;
		ttags = t;
		totalpoints= points;
	}
        /**
         * Constructor for the TestResultDetails that does not contain a path to the html file - this one is no longer in use in the stand alone application or web application
         * @param output indicates the number of failed tests, if 0 then the test passed if -1 then the test was skipped
         * @param name indicates the test name of the the test case
         * @param desc indicates the synopsis line from the test
         * @param warning indicates any warning messages - only set with skipped tests
         * @param plot_path indicates the path to the plot file
         * @param c vector of component tags associated with test
         * @param t vector of test tags associated with test
         * @param points is the number of variables multiplied by the number os steps
         */
        public TestResultDetails(int output, String name, String desc, String warning, String plot_path, Vector<String> c, Vector<String> t, int points) {
		result = output;
		testname = name;
		description = desc;
		warnings = warning;
		plot = plot_path;
		ctags = c;
		ttags = t;
		totalpoints= points;
	}



	/**
         * setResult sets the result value of the TestResultDetails object
         * @param newValue integer value to set the result to
         */
	public void setResult(int newValue) {
		result = newValue;
	}
        /**
         * setTestname sets the test name of the TestResultDetails object
         * @param newValue the string value to set the testname to
         */
	public void setTestname(String newValue) {
		testname = newValue;
	}
        /**
         * setWarnings sets the warnings value of the TestResultDetails object
         * @param newValue the string value to set the warnings to
         */
	public void setWarnings(String newValue) {
		warnings = newValue;
	}
        /**
         * setDescription sets the description value of the TestResultDetails object
         * @param newValue the string value to set the description to
         */
	public void setDescription(String newValue) {
		description = newValue;
	}
        /**
         * setPlot sets the plot value of the TestResultDetails ojbect
         * @param newValue the string value to set the plot path to
         */
	public void setPlot(String newValue) {
		plot = newValue;
	}
        /**
         * setHtml sets the html path value of the TestResultDetails ojbect
         * @param newValue the string value of the html file path to set
         */
	public void setHtml(String newValue) {
		html = newValue;
	}
        /**
         * setTotalPoints sets the totalpoints value to the number of variables multiplied by the number of steps in the TestResultDetails object
         * @param newValue the value to set the total points to
         */
	public void setTotalpoints(int newValue) {
		totalpoints = newValue;
	}
	/**
         * getResult gets the result value of the TestResultDetails object
         * @return the integer result value
         */
	public int getResult() {
		return result;
	}
        /**
         * getTestname gets the test name value of the TestResultDetails ojbect
         * @return a string containing the test name 
         */
	public String getTestname() {
		return testname;
	}
        /**
         * getWarnings gets the warnings value of the TestResultDetails ojbect
         * @return a string containing the warnings 
         */
	public String getWarnings() {
		return warnings;
	}
        /**
         * getPlot gets the plot path of the TestResultDetails ojbect
         * @return returns a string containing the plot path 
         */
	public String getPlot() {
		return plot;
	}
        /**
         * getHtml gets the html file path of the TestResultDetails object
         * @return a string containing the path to the html file
         */
	public String getHtml() {
		return html;
	}
        /**
         * getDescription gets the description line of the TestResultDetails object
         * @return a string containing the description 
         */
	public String getDescription() {
		return description;
	}
        /**
         * getCtags gets the component tags associated with the TestResultDetails oject
         * @return a vector of strings containing the component tags
         */
	public Vector<String> getCtags() {
		return ctags;
	}
        /**
         * getTtags gets the test tags associated with the TestResultDetails object
         * @return a vector strings containing the test tags
         */
	public Vector<String> getTtags() {
		return ttags;
	}
        /**
         * getTotalpoints gets the total points associated with the TestResultDetails object
         * @return an integer value of the total points
         */
	public int getTotalpoints() {
		return totalpoints;
	}
        /**
         * getCtagsString gets the component tags in a string as opposed to a vector
         * @return a string representation of the component tags
         */
	public String getCtagsString() {
		String s = "";
		for(int i=0;i<ctags.size();i++) {	
			s= s + ctags.elementAt(i);
		}	
	  return s;		
	}
        /**
         * getTtagsString gets the test tags in a string as opposed to a vector 
         * @return a string representation of the test tags
         */
	public String getTtagsString() {
		String t = "";
		for(int i=0;i<ttags.size();i++) {	
			t= t + ttags.elementAt(i);
		}	
	  return t;		
	}
 
		
}
