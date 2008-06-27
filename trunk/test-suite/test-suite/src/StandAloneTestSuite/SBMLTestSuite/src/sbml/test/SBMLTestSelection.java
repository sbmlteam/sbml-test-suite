// @file    SBMLTestSelection.java
// @brief   SBMLTestSelection class for SBML Standalone application
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
// Test selection methods for the SBML Test Suite.
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
 *
 * @author kimberlybegley
 */
public class SBMLTestSelection {
    
    public String testname;
	public String category;
	public String synopsis;
	public Vector<String> componenttags;
	public Vector<String> testtags;
	public String testtype;
	public Vector<String> levels;
	
	
	
	public String getTestname() {
		return testname;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public Vector<String> getComponenttags() {
		return componenttags;
	}
	public Vector<String> getTesttags() {
		return testtags;
	}
	public String getTesttype() {
		return testtype;
	}
	public Vector<String> getLevels() {
		return levels;
	}

	public Boolean containsComponent(String component) {
		
		if(componenttags.contains(component)) {
			return true;
		}
		else return false;
	}
	public Boolean containsTag(String tag) {
		
		if(testtags.contains(tag)) {
			return true;
		}
		else return false;
	}
	public Boolean containsLevel(String level) {
		
		if(levels.contains(level)) {
			return true;
		}
		else return false;
	}
	public Boolean containsTesttype(String type) {
		
		if(testtype.contentEquals(type)) {
			return true;
		}
		else return false;
	}
	


	public String getModelFile(String value, String testdir) {
		String mapfile = new String();
		mapfile = testdir + File.separator + value + File.separator + value + "-model.m";

		return mapfile;
	}


	public void readModelFile(String filename) {
               /* Read model file for test 
                Input: is settings file name
                Output: nothing - it sets the class variables from the settings file
                */
               File modelfile = new File(filename);
               String line = null;
               String text = null;
               String nexttext = null;
               boolean cat_flag = false;
               boolean syn_flag = false;
               boolean ttype_flag = false;
               boolean lev_flag = false;
               boolean ct_flag = false;
               boolean ttag_flag = false;

               try {
                       BufferedReader bRdr  = new BufferedReader(new FileReader(modelfile));
                       // skip the first two lines

                       int count = 0; 
                       String lastparm = "NONE";

                       boolean allread = false;

                       while( ((line = bRdr.readLine()) != null) && !allread) {
                               count++;

                               if(count <= 2) {
                                       continue;
                               }
                               else if(line.trim().length() == 0) {
                                       allread = true;
                               }
                               else if(line.toLowerCase().startsWith("category:")) {
                                       String[] splitline = line.split(":");
                                       category=splitline[1].trim();
                                       lastparm = "CATEGORY";
                                       cat_flag = true;
                               }
                               if(line.toLowerCase().startsWith("synopsis:")) {
                                       String[] splitline = line.split(":");
                                       synopsis=splitline[1].trim();
                                       lastparm = "SYNOPSIS";
                                       syn_flag = true;
                               }
                               else if (lastparm.equalsIgnoreCase("SYNOPSIS") && line.toLowerCase().startsWith(" ") ) {
                                       synopsis = synopsis + " " + line.trim();
                               }

                               else if(line.toLowerCase().startsWith("componenttags:")) {
                                       String[] splitline = line.split(":");
                                       String[] comp = splitline[1].split(",");
                                       componenttags = new Vector<String>();
                                       for(int i = 0; i < comp.length; i++)
                                               componenttags.add(comp[i].trim());
                                   
                                       lastparm = "COMPONENTTAGS";
                                       ct_flag = true;
                               }
                               else if(line.toLowerCase().startsWith("testtags:")) {
                                       String[] splitline = line.split(":");
                                       String[] tags = splitline[1].split(",");
                       		       testtags = new Vector<String>();
                                       for(int i = 0; i < tags.length; i++)
                                               testtags.add(tags[i].trim());
                                       
                                       lastparm = "TESTTAGS";
                                       ttag_flag = true;
                               }

                               else if(line.toLowerCase().startsWith("testtype:")) {
                                       String[] splitline = line.split(":");
                                       testtype=splitline[1].trim();
                                       lastparm = "TESTTYPE";
                                       ttype_flag = true;
                               }
                               else if(line.toLowerCase().startsWith("levels:")) {
                                       String[] splitline = line.split(":");
                                       String[] levarr = splitline[1].split(",");
                                       levels = new Vector<String>();
                                       for(int i = 0; i < levarr.length; i++)
                                               levels.add(levarr[i].trim());
                                       
                                       lastparm = "LEVELS";
                                       lev_flag = true;
                               }
                               else {
                                       // this should never happen, maybe put some error handling here
					// if it is critical or leave
                                       // empty to ignore other tags that are encountered
                               }
                       } // end of while
                       bRdr.close();

               } // end of try
               catch(IOException e) {
                       // catch possible io errors from readLine()
                       System.err.println("IOException error while reading model file");
                       e.printStackTrace();
               } // end of catch

               if( !(cat_flag && syn_flag && ttype_flag && lev_flag && ct_flag && ttag_flag)) {
                       // there is at least one parameter missing
               }

       } // end of readmodelfile


}
