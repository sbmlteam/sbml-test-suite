//
//  sbmlTestcase.java
//  
//
//  Created by Kimberly Begley 
//  Main class for all applications - contains a constructor with testname,start,duration,steps,number of variables
//  absolute allowable difference and relative allowable difference regarding a test.
//  Contains methods: getStart,getTestname,getDuration,getSteps,getVariables_count,getAbs,getRel,getSbmlTestdir,
//  getSettingsFile,getControlResults,getPlotFile,readResults,validateResults,compareResults,analyzeResults,
//  readSettingsFile,getUsertestlist
//  Still very much work in progress - gets updated daily - still needs alot of testing and error handling added to it
//  Also needs to be updated soon replacing BigDecimals with Doubles.
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

public class sbmlTestcase  {
	public String testname;
	public int start;
	public double duration;
	public int steps;
	public int variables_count;
	public BigDecimal abs;
	public BigDecimal rel;
	
	
	public int getStart() {
		return start;
	}
	public String getTestname() {
		return testname;
	}
	public double getDuration() {
		return duration;
	}
	public int getSteps() {
		return steps;
	}
	public int getVariables_count() {
		return variables_count;
	}
	public BigDecimal getAbs() {
		return abs;
	}
	public BigDecimal getRel() {
		return rel;
	}

	public String getSbmlTestdir() throws IOException, FileNotFoundException {
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
		catch (FileNotFoundException e) {
    			System.err.println("FileNotFoundException: ");
			e.printStackTrace();
		}
		catch(IOException e) {
		// catch possible io errors from readLine()
			System.err.println("IOException error reading application config file");
			e.printStackTrace();
		}
	return sbmltestdir;
	}

	public String getSettingsFile(String value, String testdir) {
		String settingsfile = new String();
		settingsfile = testdir + File.separator + value + File.separator + value + "-settings.txt";

		return settingsfile;
	}

	public String getControlResults(String value, String testdir) {
		String resultfile = new String();
		resultfile = testdir + File.separator + value + File.separator + value + "-results.csv";

		return resultfile;
	}
	
	public String getPlotFile(String value, String testdir) {
		String plotfile = new String();
		plotfile = testdir + File.separator + value + File.separator + value + "-plot.jpg";

		return plotfile;
	}

	public String getHtmlFile(String value, String testdir) {
		String htmlfile = new String();
		htmlfile = testdir + File.separator + value + File.separator + value + "-model.html";

		return htmlfile;
	}


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
	public int analyzeResults(BigDecimal [][] control, BigDecimal [][] differences, int variables, BigDecimal absolute, BigDecimal relative) {
	/*	anaylzeResults - looks at the allowable difference and how it compares to the differences of the user and control files
		Input: differences array (from compareResults function), absolute difference, relative difference
		Output: integer - number of failed comparisons in file - if 0 then passed the test 
		*/
		
		int pass_fail = 0;
		for(int i = 0; i < differences.length; i++) {
		
			// first column will always be time so we start at second column
			for( int j = 1; j < (differences[i].length); j++) {
				
				//System.out.println("differences at i length is " + differences[i].length);
			/*Check out which is greater absolute value or differences value if((((differences[i][j].abs()).max(absolute)).compareTo(absolute) == 0
			then the absolute difference between the user and control files is allowable and therefore it passes the test for that row/column value
			*/		
				//if the absolute difference is greater than the allowable absolute value
				if((((differences[i][j].abs()).max(absolute)).compareTo(absolute) != 0)) {
					//System.out.println("Failed the absolute allowable difference- now testing the relative allowable difference at line " + i + "column " +j);
					
					// fails absolute difference test - now check if it passes relative test
					if(((((differences[i][j].abs())).max((relative).multiply(control[i][j]))).compareTo(relative)) != 0) {
						// fails both relative and absolute tests
						//System.out.println("Fails both relative and absolute tests here row " +i);
						//return a fail
						pass_fail++;
					}
				}
			}	
		}
		return pass_fail;
	}
	
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
public Map getUsertestlist(String dirname) throws Exception{
	/* Gets the user file directory and checks the format of the files in the directory
	   Input: is the directory path
	   Output: a hashmap with the testname and user result file name as associated columns
	*/
	
	Map<String, String> m = new HashMap<String, String>();
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
			throw new Exception ("Incorrect file name format");
			//System.err.println("User test files in incorrect file name format - please name files ending with nnnnn.csv - where nnnnn is the test number");
			
		}
	}
	}
	return m;
}

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
