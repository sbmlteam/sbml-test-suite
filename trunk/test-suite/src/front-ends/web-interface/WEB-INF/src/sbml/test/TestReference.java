//
// @file    TestReference.java
// @brief   Parses the test case files and gathers the reference data
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
//
// $Id$
// $HeadURL$
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright 2008-2010 California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation.  A copy of the license agreement is provided
// in the file named "LICENSE.txt" included with this software distribution
// and also available at http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import java.io.*;
import java.util.*;
import java.applet.*;

public final class TestReference
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public TestReference(File testDir, String caseName)
        throws IOException, Exception
    {
        if (testDir == null)
            throw new IOException("Null parameter testDir");

        if (caseName == null)
            throw new IOException("Null parameter caseName");

        this.caseName = caseName;
        this.testDir = testDir;
        parseMFile();
    }

    public TestReference(String testDir, String caseName)
        throws IOException, Exception
    {
        this(new File(testDir), caseName);
    }

    public String getCaseName()        { return caseName; }
    public String getSynopsis()        { return synopsis; }
    public String getTestType()        { return testType; }
    public String getCategory()        { return category; }

    public File   getMFile()           { return mFile; }
    public String getMFileName()       { return mFileName; }

    public File   getPlotFile()        { return plotFile; }
    public String getPlotFileName()    { return plotFileName; }

    public File   getHTMLFile()        { return htmlFile; }
    public String getHTMLFileName()    { return htmlFileName; }

    public File   getResultsFile()     { return resultsFile; }
    public String getResultsFileName() { return resultsFileName; }

    public Vector<String> getLevels()        { return levels; }
    public Vector<String> getComponentTags() { return ctags; }
    public Vector<String> getTestTags()      { return ttags; }

    public String getComponentTagsAsString() { return stringify(ctags); }
    public String getTestTagsAsString()      { return stringify(ttags); }

    public boolean hasLevel(String level)    { return levels.contains(level); }
    public boolean hasComponentTag(String t) { return ctags.contains(t); }
    public boolean hasTestTag(String t)      { return ttags.contains(t); }
    public boolean hasTestType(String t)     { return testType.equals(t); }

    // 
    // --------------------------- Private methods ----------------------------
    // 

    private void parseMFile()
        throws Exception
    {
        String basePart = testDir + File.separator + caseName + File.separator;

        mFileName       = caseName + "-model.m";
        htmlFileName    = caseName + "-model.html";
        plotFileName    = caseName + "-plot.jpg";
        resultsFileName = caseName + "-results.csv";

        mFile           = new File(basePart + mFileName);
        htmlFile        = new File(basePart + htmlFileName);
        plotFile        = new File(basePart + plotFileName);
        resultsFile     = new File(basePart + resultsFileName);

        // Do some sanity checking.

        if (! mFile.exists())
            throw new Exception("Nonexistent .m file: " + mFile.getPath());

        if (! mFile.canRead())
            throw new Exception("Unreadable .m file: " + mFile.getPath());

        // Let's get ready to parse.

        int left = 6;                 // Total # of fields we're looking for.
        Scanner fileReader = new Scanner(mFile);

        // We use 2 scanner objects.
        // The outer one reads the file one line at a time.
        // The inner one reads the tokens in the line.

        while (fileReader.hasNext() && left > 0)
        {
            Scanner line = new Scanner(fileReader.nextLine());

            if (! line.hasNext()) continue; // Skip blank lines.

            String t = line.next();
            if      (t.equals("levels:"))        { levels   = readTags(line);  left--; }
            else if (t.equals("testType:"))      { testType = line.next();     left--; }
            else if (t.equals("category:"))      { category = line.next();     left--; }
            else if (t.equals("componentTags:")) { ctags    = readTags(line);  left--; }
            else if (t.equals("testTags:"))      { ttags    = readTags(line);  left--; }
            else if (t.equals("synopsis:"))
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
            throw new Exception("Failed to read all expected fields in .m file: "
                                + mFile.getPath());
    }

    private Vector<String> readTags(Scanner line)
    {
        Vector<String> tokens = new Vector<String>();

        line.useDelimiter("\\s*,\\s*"); // Split at ", "
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

    private final String caseName;      // The test number as a string.
    private final File testDir;         // Location of the cases directory.

    private String category;
    private String synopsis;
    private String testType;
    private Vector<String> levels;
    private Vector<String> ctags;
    private Vector<String> ttags;
    private File mFile;
    private File plotFile;
    private File htmlFile;
    private File resultsFile;
    private String mFileName;
    private String plotFileName;
    private String htmlFileName;
    private String resultsFileName;

}// end of class
