//
//  sbmlTestselection.java
//  
//
//  Created by Kimberly Begley 
//  This class contains a constructor with testname,category,synopsis,a vector of componenttags, a vector of testtags
//  testtype and a vector of levels
//  Contains methods to getTestname,getSynopsis,getTesttags,getTesttype,getComponenttags,getLevels, getSbmltestdir,
//  getModelfile and readModelfile
//  Methods need to be written and added to this class to incorporate the control logic to selecting test cases.
//  Still very much work in progress
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

public class sbmlTestselection  {
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
	

	public String getSbmlTestdir() {
		String sbmltestdir = new String();
		File sbmlconfigfile = new File("/usr/share/apache-tomcat-5.5.26/webapps/test_suite/WEB-INF/sbml_config_file.txt");
		String[][] config = new String[1][1];
		try{
			BufferedReader bufRdr  = new BufferedReader(new FileReader(sbmlconfigfile));
			String line = null;
			
			while((line = bufRdr.readLine()) != null)
			{	
				line.trim();
				sbmltestdir = line;	
			}
			bufRdr.close();
		}
		catch(IOException e) {
		// catch possible io errors from readLine()
			System.out.println("IOException error!");
			e.printStackTrace();
		}
	return sbmltestdir;
	}

	public String getModelFile(String value, String testdir) {
		String mapfile = new String();
		mapfile = testdir + File.separator + value + File.separator + value + "-model.m";

		return mapfile;
	}


	public void readModelFile(String filename) {
	/* Read settings file for test into a hash table rea
	   Input: is settings file name
	   Output: nothing - it sets the class variables from the settings file
	*/
	File modelfile = new File(filename);
	String line = null;
	String text = null;
	String nexttext = null;
	Map<String, String> m = new HashMap<String, String>();
	
	try {
		BufferedReader bRdr  = new BufferedReader(new FileReader(modelfile));
		// skip the first two lines
		line=bRdr.readLine();
		line=bRdr.readLine();
		int count = 0; // count the next 6 lines
		Pattern categoryp = Pattern.compile("category:$");
		Pattern synopsisp = Pattern.compile("synopsis:$");
		Pattern ctagsp = Pattern.compile("componentTags:$");
		Pattern ttagsp = Pattern.compile("testTags:$");
		Pattern typep = Pattern.compile("testtype:$");
		Pattern levelsp = Pattern.compile("levels:$");
		

		while((line = bRdr.readLine()) != null) {
			
			Matcher category_match = categoryp.matcher(line);
			Matcher synopsis_match = synopsisp.matcher(line);
			Matcher ctags_match = ctagsp.matcher(line);
			Matcher ttags_match = ttagsp.matcher(line);
			Matcher type_match = typep.matcher(line);
			Matcher levels_match = levelsp.matcher(line);

			if(category_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
					
				category=nexttext;
			}
			if(synopsis_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
					
				synopsis=nexttext;
				// read the next line - if it doesn't match ctags append it to previous value
				
				while((line = bRdr.readLine()) != null && !(ctags_match.find())) {
					// trim the line and append it to the previous one
					line = line.trim();
					synopsis = synopsis + line;
				}	
			}
			if(ctags_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
				Vector<String> ct = new Vector<String>();
				StringTokenizer cvar = new StringTokenizer(nexttext,",");
				
				while (cvar.hasMoreTokens()) { 
				//get next token and store it in the vector
					String com = cvar.nextToken().trim();
					ct.addElement(com);
				}
			componenttags = ct;
					
				
			}
			if(ttags_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
				Vector<String> tt = new Vector<String>();
				StringTokenizer tvar = new StringTokenizer(nexttext,",");
				
				while (tvar.hasMoreTokens()) { 
				//get next token and store it in the vector
					String test = tvar.nextToken().trim();
					tt.addElement(test);
				}
			testtags = tt;
			}
			if(type_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
					
			testtype=nexttext;
			}
			if(levels_match.find()) {
				StringTokenizer st = new StringTokenizer(line,":");
				while (st.hasMoreTokens()) { 
					text = st.nextToken();
					nexttext = st.nextToken();
					nexttext = nexttext.trim();
				}
				Vector<String> lev = new Vector<String>();
				StringTokenizer lvar = new StringTokenizer(line,",");
				
				while (lvar.hasMoreTokens()) { 
				//get next token and store it in the vector
					String levs = lvar.nextToken().trim();
					lev.addElement(levs);
				}
			levels = lev;
			}
				
			

				
		} // end of while
		bRdr.close();
		
	} // end of try
	catch(IOException e) {
		// catch possible io errors from readLine()
			System.out.println("IOException error!");
			e.printStackTrace();
	} // end of catch
	
   } // end of readmodelfile

}// end of class
