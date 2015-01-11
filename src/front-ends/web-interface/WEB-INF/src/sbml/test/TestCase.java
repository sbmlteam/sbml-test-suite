//
// @file    TestCase.java
// @brief   Parses the test case files and gathers the reference data
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2010-2015 jointly by the following organizations: 
//     1. California Institute of Technology, Pasadena, CA, USA
//     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
//     3. University of Heidelberg, Heidelberg, Germany
//
// Copyright (C) 2008-2009 California Institute of Technology (USA).
//
// Copyright (C) 2004-2007 jointly by the following organizations:
//     1. California Institute of Technology (USA) and
//     2. University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available on the Web at
// http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import java.applet.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.regex.*;


public class TestCase
    implements Comparable<TestCase>
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public TestCase(File testSuiteCasesDir, String caseName)
        throws IOException, Exception
    {
        if (testSuiteCasesDir == null)
            throw new IOException("Null parameter 'testSuiteCasesDir'.");

        if (caseName == null)
            throw new IOException("Null parameter 'caseName'.");

        this.caseName          = caseName;
        this.caseNum           = Integer.parseInt(caseName);
        this.testSuiteCasesDir = testSuiteCasesDir;

        generateFileNames();
        parseMFile();
        parseSettingsFile();

        // Note we deliberately don't parse the data file.  Leave that
        // to when it's actually needed.
    }

    public TestCase(String testSuiteCasesDir, String caseName)
        throws IOException, Exception
    {
        this(new File(testSuiteCasesDir), caseName);
    }

    public String getCaseName()                { return caseName; }
    public int    getCaseNum()                 { return caseNum; }
    public String getSynopsis()                { return synopsis; }
    public String getTestType()                { return testType; }
    public String getCategory()                { return category; }

    public File   getMFile()                   { return mFile; }
    public String getMFileName()               { return mFileName; }

    public File   getPlotFile()                { return plotFile; }
    public String getPlotFileName()            { return plotFileName; }

    public File   getHTMLFile()                { return htmlFile; }
    public String getHTMLFileName()            { return htmlFileName; }

    public File   getSettingsFile()            { return settingsFile; }
    public String getSettingsFileName()        { return settingsFileName; }

    public File   getExpectedDataFile()        { return expectedDataFile; }
    public String getExpectedDataFileName()    { return expectedDataFileName; }

    public Vector<String> getLevels()          { return levels; }
    public Vector<String> getComponentTags()   { return ctags; }
    public Vector<String> getTestTags()        { return ttags; }

    public String getComponentTagsAsString()   { return stringify(ctags); }
    public String getTestTagsAsString()        { return stringify(ttags); }

    public boolean hasLevel(String l)          { return levels.contains(l); }
    public boolean hasComponentTag(String t)   { return ctags.contains(t); }
    public boolean hasTestTag(String t)        { return ttags.contains(t); }
    public boolean hasTestType(String t)       { return testType.equals(t); }

    public double         getTestStart()       { return testStart; }
    public double         getTestDuration()    { return testDuration; }
    public double         getTestRelativeTol() { return testRelativeTol; }
    public double         getTestAbsoluteTol() { return testAbsoluteTol; }
    public int            getTestNumRows()     { return testNumRows; }
    public int            getTestNumVars()     { return testVars.size(); }
    public Vector<String> getTestVars()        { return testVars; }
    public Vector<String> getTestAmountVars()  { return testAmountVars; }
    public Vector<String> getTestConcentrationVars() { return testConcentVars;}

    /**
     * Parse the expected results CSV file and return it as a 2-D array.
     */
    public double[][] getExpectedData()
        throws Exception
    {
        if (expectedData == null)
            expectedData = parseDataFile(expectedDataFile, getTestNumRows(),
                                         getTestNumVars(), false);
        return expectedData;
    }
    
    /**
     * Returns a negative integer, zero, or a positive integer depending
     * on whether the given other case number is less than, equal or greater
     * that this one.
     */
    public int compareTo(TestCase other)
    {
        return caseNum - other.getCaseNum();
    }

    // 
    // -------------------------- Protected methods ---------------------------
    // 

    /**
     * This is used by subclasses (like for user results) to parse
     * other results data files.
     *
     * The boolean extraDataOK indicates whether extra data entries in a row
     * are permissible.  In reading our reference data, this is not (it would
     * mean an error somewhere), but in user data, it is.  For the latter,
     * call this method with extraDataOK set to true.
     */
    protected double[][] parseDataFile(File dataFile, int numRows, int numVars,
                                       boolean extraDataOK)
        throws Exception
    {
        String fileName    = dataFile.getName();
        Scanner fileReader = new Scanner(dataFile);

        if (! fileReader.hasNext())
            throw new Exception("Data file " + fileName + " is empty.");

        // The first column gives the time step.  It's not counted in
        // numVars, hence the + 1 below.

        double[][] data       = new double[numRows][numVars + 1];
        Pattern ignorePattern = Pattern.compile("^#.*|^\\s*$");
        Pattern numberPattern = Pattern.compile("\\s*(\\d+|-INF|INF|NaN)",
                                                Pattern.CASE_INSENSITIVE);

        // Don't count element 0, the time point, as a variable.

        int expected = numVars + 1;

        // We ignore a header line, comments and blank lines; thus, the
        // line number in the file will not equal the row number in the
        // data array, but to report errors, we need to track the file
        // line (and we also count it from 1 instead of 0).

        int dataRow = 0;
        int fileRow = 1;
        do
        {
            String line = fileReader.nextLine();

            if (ignorePattern.matcher(line).matches()) // Blank line or header.
                fileRow++;              
            else if (numberPattern.matcher(line).lookingAt())
            {
                String[] items = line.split(",");
                int found = items.length;

                if (found < expected || (found > expected && ! extraDataOK))
                    throw new Exception("Too "
                                        + (found > expected ? "many" : "few")
                                        + " data points in row " + fileRow
                                        + " of " + fileName + ": expected "
                                        + expected + ", but read " + found + ".");

                for (int col = 0; col < expected; col++)
                {
                    String thisItem = items[col].trim();
                    if (thisItem.compareToIgnoreCase(NEG_INF_STRING) == 0)
                        data[dataRow][col] = Double.NEGATIVE_INFINITY;
                    else if (thisItem.compareToIgnoreCase(POS_INF_STRING) == 0)
                        data[dataRow][col] = Double.POSITIVE_INFINITY;
                    else if (thisItem.compareToIgnoreCase(NAN_STRING) == 0)
                        data[dataRow][col] = Double.NaN;
                    else
                        data[dataRow][col] = Double.parseDouble(thisItem);
                }

                dataRow++;
                fileRow++;
            }
            else if (fileRow == 1)      // Not a number, but it's the header.
                fileRow++;
            else
                throw new Exception("Unexpected content in file " + fileName
                                    + " at line " + fileRow + ": '"
                                    + line + "'.");

        } while (fileReader.hasNext() && dataRow < numRows);

        // Check that we read the expected number of data rows:

        if (dataRow < numRows)
            throw new Exception("Too few data rows in file " + fileName
                                + ": expected " + numRows
                                + ", but read only " + dataRow + ".");
        else if (fileReader.hasNext()
                 && (numberPattern.matcher(fileReader.nextLine()).lookingAt()))
            throw new Exception("Too many data rows in file " + fileName
                                + ": expected only " + numRows + ".");

        fileReader.close();
        return data;
    }

    // 
    // --------------------------- Private methods ----------------------------
    // 

    private void generateFileNames()
    {
        String basePart = testSuiteCasesDir + File.separator
            + caseName + File.separator;

        mFileName            = caseName + "-model.m";
        htmlFileName         = caseName + "-model.html";
        plotFileName         = caseName + "-plot.html";
        expectedDataFileName = caseName + "-results.csv";
        settingsFileName     = caseName + "-settings.txt";

        mFile                = new File(basePart + mFileName);
        htmlFile             = new File(basePart + htmlFileName);
        plotFile             = new File(basePart + plotFileName);
        expectedDataFile     = new File(basePart + expectedDataFileName);
        settingsFile         = new File(basePart + settingsFileName);
    }

    private void parseMFile()
        throws Exception
    {
        // Do some sanity checking.

        if (! mFile.exists())
            throw new Exception("Nonexistent .m file: " + mFile.getPath() + ".");

        if (! mFile.canRead())
            throw new Exception("Unreadable .m file: " + mFile.getPath() + ".");

        // Let's get ready to parse.

        int left = 6;                   // Total # of fields we're looking for.
        Scanner fileReader = new Scanner(mFile);

        // We use 2 scanner objects.
        // The outer one reads the file one line at a time.
        // The inner one reads the tokens in the line.

        while (fileReader.hasNext() && left > 0)
        {
            Scanner line = new Scanner(fileReader.nextLine());

            if (! line.hasNext()) continue; // Skip blank lines.

            String t = line.next();
            if      (t.equalsIgnoreCase("levels:"))        { levels   = readTokens(line); left--; }
            else if (t.equalsIgnoreCase("testType:"))      { testType = line.next();      left--; }
            else if (t.equalsIgnoreCase("category:"))      { category = line.next();      left--; }
            else if (t.equalsIgnoreCase("componentTags:")) { ctags    = readTokens(line); left--; }
            else if (t.equalsIgnoreCase("testTags:"))      { ttags    = readTokens(line); left--; }
            else if (t.equalsIgnoreCase("synopsis:"))
            {
                synopsis = line.nextLine();
                left--;

                // Synopsis can be more than one line long.  The following
                // checks if the subsequent token has a ':', which is a
                // hack for detecting a tag label.  If the line *doesn't*,
                // assume it's part of the synopsis text.

                while (! fileReader.hasNext("\\w+:"))
                    synopsis += fileReader.nextLine();
            }
        }

        if (left > 0)
            throw new Exception("Didn't find all fields expected in .m file "
                                + mFile.getPath() + ".");
    }

    private void parseSettingsFile()
        throws Exception
    {
        // Do some sanity checking.

        if (! settingsFile.exists())
            throw new Exception("Nonexistent settings file "
                                + settingsFile.getPath() + ".");

        if (! settingsFile.canRead())
            throw new Exception("Unreadable settings file "
                                + settingsFile.getPath() + ".");
        
        // Let's get ready to parse.

        int left = 8;                   // Total # of fields we're looking for.
        Scanner fileReader = new Scanner(settingsFile);

        // We use 2 scanner objects.
        // The outer one reads the file one line at a time.
        // The inner one reads the tokens in the line.

        while (fileReader.hasNext() && left > 0)
        {
            Scanner line = new Scanner(fileReader.nextLine());

            if (! line.hasNext()) continue; // Skip blank lines.

            // Note: the "number of steps" in our test cases, as given by
            // the "steps: " line of the NNNNN-settings.txt file, excludes
            // time 0, so the number of rows is +1.

            String t = line.next();
            if (t.equalsIgnoreCase("start:"))              testStart       = line.nextDouble();
            else if (t.equalsIgnoreCase("duration:"))      testDuration    = line.nextDouble();
            else if (t.equalsIgnoreCase("steps:"))         testNumRows     = line.nextInt() + 1;
            else if (t.equalsIgnoreCase("relative:"))      testRelativeTol = line.nextDouble();
            else if (t.equalsIgnoreCase("absolute:"))      testAbsoluteTol = line.nextDouble();
            else if (t.equalsIgnoreCase("variables:"))     testVars        = readTokens(line);
            else if (t.equalsIgnoreCase("amount:"))        testAmountVars  = readTokens(line);
            else if (t.equalsIgnoreCase("concentration:")) testConcentVars = readTokens(line);
            else
            {
                // There shouldn't be anything else in the file.
                throw new Exception("Unexpected text found in settings file "
                                    + settingsFile.getName() + ": '"
                                    + line.toString() + "'.");
            }

            left--;
        }

        if (left > 0)
            throw new Exception("Didn't find all fields expected in settings"
                                + " file " + settingsFile.getPath() + ".");
    }

    private Vector<String> readTokens(Scanner line)
    {
        Vector<String> tokens = new Vector<String>();

        // We split at spaces or commas optionally surrounded by spaces.
        line.useDelimiter("(\\s+|\\s*,)\\s*");

        while (line.hasNext())
            tokens.add(line.next());

        return tokens;
    }

    private String stringify(Vector<String> tags)
    {
        Iterator it = tags.iterator();
        String s    = (it.hasNext() ? (String) it.next() : null);

        while (it.hasNext())
            s += ", " + (String) it.next();

        return s;
    }

    // 
    // -------------------------- Private variables --------------------------- 
    // 

    private final int caseNum;            // The test number.
    private final String caseName;        // The test number as a string.
    private final File testSuiteCasesDir; // Location of the cases directory.

    private String category;
    private String synopsis;
    private String testType;
    private Vector<String> levels;
    private Vector<String> ctags;
    private Vector<String> ttags;
    private File mFile;
    private String mFileName;
    private File plotFile;
    private String plotFileName;
    private File htmlFile;
    private String htmlFileName;
    private File expectedDataFile;
    private String expectedDataFileName;
    private File settingsFile;
    private String settingsFileName;

    private double testStart;
    private double testDuration;
    private double testRelativeTol;
    private double testAbsoluteTol;
    private int testNumRows;
    private Vector<String> testVars;
    private Vector<String> testAmountVars;
    private Vector<String> testConcentVars;
    private double[][] expectedData;

    // 
    // -------------------------- Private constants ---------------------------
    // 

    private final static String NAN_STRING = "NaN";
    private final static String POS_INF_STRING = "INF";
    private final static String NEG_INF_STRING = "-INF";

} // end of class


/*
  2010-03-29 My first version of parseDataFile used this code.
  It took ~30ms to read a file.  The current code avoids the use of Scanner
  methods like line.nextBigDecimal() and takes only 1 ms to read a file!

        do
        {
            Scanner line = new Scanner(fileReader.nextLine());

            if (! line.hasNext() || line.hasNext(commentLine))
                continue;               // Skip blank lines and comment lines.

            line.useDelimiter(numberDelimiter);
            if (line.hasNextBigDecimal())
            {
                int currentCol = 0;
                while (line.hasNextBigDecimal() && currentCol <= numVars)
                    data[rowIndex][currentCol++] = line.nextBigDecimal();

                if (line.hasNextBigDecimal())
                    throw new Exception("Too many data elements in row "
                                        + (rowIndex + 1) + " of "
                                        + f.getPath() + ".");

                // FIXME check for not enough elements in row

                rowIndex++;
            }
            else
            {
                // The line doesn't start with a BigDecimal.  If it's the
                // first line, it might be a header, which we take to be a
                // first line that doesn't start with a number.  Ignore it.

                if (rowIndex == 0)
                    continue;
                else                    // Not a header, not a comment.
                    throw new Exception("Unexpected content in data file "
                                        + f.getPath() + ": " + line.toString());
            }
            line.close();
        } while (fileReader.hasNext() && rowIndex < numRows);
*/
