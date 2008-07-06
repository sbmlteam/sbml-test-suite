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

public class TestResultDetails {
	// the TestResultDetails class has 4 fields
	private int result;
	private int totalpoints;
	private String testname;
	private String description;
	private String warnings;
	private String plot;
	private String html;
	private Vector<String> ctags;
	private Vector<String> ttags;
	

	// the TestResultDetails class has one constructor
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



	// the TestResultDetails class has 4 methods
	public void setResult(int newValue) {
		result = newValue;
	}
	public void setTestname(String newValue) {
		testname = newValue;
	}
	public void setWarnings(String newValue) {
		warnings = newValue;
	}
	public void setDescription(String newValue) {
		description = newValue;
	}
	public void setPlot(String newValue) {
		plot = newValue;
	}
	public void setHtml(String newValue) {
		html = newValue;
	}
	public void setTotalpoints(int newValue) {
		totalpoints = newValue;
	}
	
	public int getResult() {
		return result;
	}
	public String getTestname() {
		return testname;
	}
	public String getWarnings() {
		return warnings;
	}
	public String getPlot() {
		return plot;
	}
	public String getHtml() {
		return html;
	}
	public String getDescription() {
		return description;
	}
	public Vector<String> getCtags() {
		return ctags;
	}
	public Vector<String> getTtags() {
		return ttags;
	}
	public int getTotalpoints() {
		return totalpoints;
	}
	public String getCtagsString() {
		String s = "";
		for(int i=0;i<ctags.size();i++) {	
			s= s + ctags.elementAt(i);
		}	
	  return s;		
	}
	public String getTtagsString() {
		String t = "";
		for(int i=0;i<ttags.size();i++) {	
			t= t + ttags.elementAt(i);
		}	
	  return t;		
	}
 //       public int mapElementAt(int index) {
//		return (elements.get(index)).booleanValue();
			
//	}
		
}
