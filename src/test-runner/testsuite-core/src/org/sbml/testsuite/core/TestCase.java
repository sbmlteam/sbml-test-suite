//
// @file TestCase.java
// @brief The encapsulation of all elements making up a test case
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite. Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2009-2017 jointly by the following organizations:
// 1. California Institute of Technology, Pasadena, CA, USA
// 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
// 3. University of Heidelberg, Heidelberg, Germany
//
// Copyright (C) 2006-2008 by the California Institute of Technology,
// Pasadena, CA, USA
//
// Copyright (C) 2002-2005 jointly by the following organizations:
// 1. California Institute of Technology, Pasadena, CA, USA
// 2. Japan Science and Technology Agency, Japan
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation. A copy of the license agreement is provided
// in the file named "LICENSE.txt" included with this software distribution
// and also available online as http://sbml.org/software/libsbml/license.html
// ----------------------------------------------------------------------------
//

package org.sbml.testsuite.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Vector;
import org.sbml.testsuite.core.data.ResultSet;

/**
 * Implementation of a Testcase
 */
public class TestCase
{

    /**
     * Creates a test case for the given directory
     * 
     * @param dir
     *            the directory
     * @return a test case for the given directory
     */
    public static TestCase forDirectory(File dir)
    {
        TestCase test = new TestCase();
        test.initializeFromDirectory(dir);
        return test;
    }

    private File             caseDirectory;

    private Vector<String>   componentTags;

    private String           generatedBy;

    private String           id;

    private int              idNumber;

    private long             lastModified;

    private TestCaseSettings settings;

    private Vector<String>   supportedVersions;

    private String           synopsis;

    private Vector<String>   testTags;

    private String           testType;

    private Vector<String>   packagesPresent;


    /**
     * Default constructor
     */
    public TestCase()
    {
        componentTags = new Vector<String>();
        testTags = new Vector<String>();
        supportedVersions = new Vector<String>();
    }


    /**
     * Initializes a new Testcase from the given directory
     * 
     * @param directory
     *            the test case directory
     */
    public TestCase(File directory)

    {
        this();
        initializeFromDirectory(directory);
    }


    /**
     * @return the caseDirectory
     */
    public File getCaseDirectory()
    {
        return caseDirectory;
    }


    /**
     * @return the componentTags
     */
    public Vector<String> getComponentTags()
    {
        return componentTags;
    }


    /**
     * @return all component tags as (comma separated) string.
     */
    public String getComponentTagsString()
    {
        return Util.toString(componentTags);
    }


    /**
     * @return the packagespresent
     */
    public Vector<String> getPackagesPresent()
    {
        return packagesPresent;
    }


    /**
     * @return all packages present as (comma separated) string.
     */
    public String getPackagesPresentString()
    {
        return Util.toString(packagesPresent);
    }


    /**
     * @return HTML representing a document with description and plot
     */
    public String getDescriptionDocument()
    {
        if (!hasDescription()) return null;

        StringBuilder text = new StringBuilder();
        text.append("<html>" + "\n");
        text.append("<head>" + "\n");
        String title = String.format("Description for Test case {0}", id);
        text.append("<title>" + title + "</title>" + "\n");
        text.append("<style type=\"text/css\">" + "\n");
        // text.append(Resources.Site + "\n");
        text.append("</style>" + "\n");
        text.append("</head>" + "\n");
        text.append("<body>" + "\n");

        text.append("<div class=\"page\"><div class=\"header\"><div class=\"title\">"
            + "\n");
        text.append("</div></div><div class=\"main\">" + "\n");
        text.append("<h1>" + title + "</h1>" + "\n");
        text.append("<center><img src=\""
            + getDescriptionPlot().getAbsolutePath() + "\"/></center>" + "\n");
        text.append(Util.readAllText(getDescriptionHTML()) + "\n");
        text.append("</div></div>" + "\n");
        text.append("</body>" + "\n");
        text.append("</html>" + "\n");

        return text.toString();

    }


    /**
     * @return the description (*-model.m) file for this test
     */
    public File getDescriptionFile()
    {
        return new File(caseDirectory, String.format("%s-model.m", id));
    }


    /**
     * @return the file of the description HTML file
     */
    public File getDescriptionHTML()
    {
        return new File(caseDirectory, String.format("%s-model.html", id));

    }


    /**
     * @return the filename for the plot image
     */
    public File getDescriptionPlot()
    {
        return new File(caseDirectory, String.format("%s-plot.jpg", id));
    }


    /**
     * @return the result set of the expected result
     */
    public ResultSet getExpectedResult()
    {
        return ResultSet.fromFile(getExpectedResultFile());
    }


    /**
     * @return the file of the expected result for this test
     */
    public File getExpectedResultFile()
    {
        return new File(caseDirectory, String.format("%s-results.csv", id));
    }


    /**
     * @return the generatedBy
     */
    public String getGeneratedBy()
    {
        return generatedBy;
    }


    /**
     * @return the testType
     */
    public String getTestType()
    {
        return testType;
    }


    /**
     * @return the highest supported level
     */
    public int getHighestSupportedLevel()
    {
        int[] temp = Util.getLevelAndVersion(getHighestSupportedVersionString());
        return temp[0];
    }


    /**
     * @return the highest supported version
     */
    public int getHighestSupportedVersion()
    {
        int[] temp = Util.getLevelAndVersion(getHighestSupportedVersionString());
        return temp[1];
    }


    /**
     * @return the highest supported level and version
     */
    public LevelVersion getHighestSupportedLevelVersion()
    {
        return new LevelVersion(getHighestSupportedLevel(),
                                getHighestSupportedVersion());
    }


    /**
     * @return the highest supported version for this test, as a string.
     */
    public String getHighestSupportedVersionString()
    {
        return supportedVersions.get(supportedVersions.size() - 1).trim();
    }


    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }


    /**
     * @return the integer value of the id
     */
    public int getIndex()
    {
        return idNumber;
    }


    /**
     * @return the lastModified
     */
    public long getLastModified()
    {
        return lastModified;
    }


    /**
     * @return the SBML string for the highest supported version
     */
    public String getSBML()
    {
        return getSBML(getHighestSupportedVersionString());
    }


    /**
     * 
     * @param level
     *            the sbml level
     * @param version
     *            the sbml version
     * @return the SBML string for the given level and version
     */
    public String getSBML(int level, int version)
    {
        File file = getSBMLFile(level, version);

        return Util.readAllText(file);
    }


    /**
     * @param levelVersion
     *            the level / version to find
     * @return the sbml string for the given level / version string
     */
    public String getSBML(String levelVersion)
    {
        return Util.readAllText(getSBMLFile(levelVersion));
    }


    /**
     * @return the SBML file for the highest supported level / version.
     */
    public File getSBMLFile()
    {
        return getSBMLFile(getHighestSupportedVersionString());
    }


    /**
     * Return the SBML file for the given level / version combination .
     * 
     * @param level
     *            the sbml level
     * @param version
     *            the sbml version
     * @return the SBML file for the given level / version combination .
     */
    public File getSBMLFile(int level, int version)
    {
        if (! supportsLevelVersion(level, version)) return null;
        return new File(caseDirectory, String.format("%s-sbml-l%dv%d.xml", id,
                                                     level, version));
    }


    /**
     * Returns the SBML for the specified level and version. Supported
     * format is 'x.y' or 'lXVY' where x is the level and y is the version.
     * 
     * @param levelVersion
     *            String specifying the desired file
     * @return fileName for this version
     */
    public File getSBMLFile(String levelVersion)
    {
        int[] temp = Util.getLevelAndVersion(levelVersion);

        return getSBMLFile(temp[0], temp[1]);
    }


    /**
     * @return the file of the settings file for this test
     */
    public File getSettingFile()
    {
        return new File(caseDirectory, String.format("%s-settings.txt", id));
    }


    /**
     * @return the settings
     */
    public TestCaseSettings getSettings()
    {
        return settings;
    }


    /**
     * @param reread whether to re-read the settings file.
     * @return the settings.
     */
    public TestCaseSettings getSettings(boolean reread)
    {
        if (reread)
            initializeFromDirectory(caseDirectory);
        return settings;
    }


    /**
     * @return text for the status, containing only component and test tags
     */
    public String getStatusText()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Component Tags: " + Util.toString(componentTags));
        builder.append("\tTest Tags: " + Util.toString(testTags));
        return builder.toString();

    }


    /**
     * @return the supportedVersions
     */
    public Vector<String> getSupportedVersions()
    {
        return supportedVersions;
    }


    /**
     * @return the synopsis
     */
    public String getSynopsis()
    {
        return synopsis;
    }


    /**
     * @return the testTags
     */
    public Vector<String> getTestTags()
    {
        return testTags;
    }


    /**
     * @return all test tags as (comma separated) string.
     */
    public String getTestTagsString()
    {
        return Util.toString(testTags);
    }


    /**
     * @return text for a tooltip for this test, containing the synopsis,
     *         component and test tags
     */
    public String getToolTip()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("Test case: " + id + "\n");
        builder.append("===================" + "\n");
        builder.append("\n");
        builder.append(synopsis + "\n");
        builder.append("\n");
        builder.append("Component Tags: " + Util.toString(componentTags) + "\n");
        builder.append("\n");
        builder.append("Test Tags\t: " + Util.toString(testTags) + "\n");
        builder.append("\n");

        return builder.toString();

    }


    /**
     * @return true, if the directory was modified after this test was
     *         initialized
     */
    public boolean hasChanged()
    {
        return caseDirectory.lastModified() != lastModified;
    }


    /**
     * @return boolean indicating whether the description file exists for this
     *         test
     */
    public boolean hasDescription()
    {
        return getDescriptionHTML().exists() && getDescriptionPlot().exists();
    }


    /**
     * Initializes this tests from the given model description file
     * 
     * @param modelDesc
     *            the model description file (m-file)
     */
    public void initializeFromDesc(File modelDesc)
    {
        caseDirectory = modelDesc.getParentFile();

        id = caseDirectory.getName();
        idNumber = Integer.parseInt(id);

        String contents = Util.readAllText(modelDesc);

        synopsis = Util.getSnippet(contents, "synopsis:", "componentTags:");

        componentTags = new Vector<String>();
        testTags = new Vector<String>();
        supportedVersions = new Vector<String>();
        packagesPresent = new Vector<String>();

        componentTags.addAll(Util.split(Util.getSnippet(contents,
                                                        "componentTags:",
                                                        "testTags:"),
                                        new char[] {',', ' ', '\n'}));

        testTags.addAll(Util.split(Util.getSnippet(contents, "testTags:",
                                                   "testType:"), 
                                   new char[] { ',', ' ', '\n'}));

        supportedVersions.addAll(Util.split(Util.getSnippet(contents,
                                                            "levels:",
                                                            "generatedBy:"),
                                            new char[] {',', ' ', '\n'}));

        testType = Util.getSnippet(contents, "testType:", "levels:").trim();

        generatedBy = Util.getSnippet(contents, "generatedBy:", "\n").trim();

        packagesPresent.addAll(Util.split(Util.getSnippet(contents,
                                                          "packagesPresent:",
                                                          "\n"),
                                            new char[] {',', ' ', '\n'}));

        lastModified = caseDirectory.lastModified();

        settings = new TestCaseSettings(getSettingFile());
    }


    /**
     * Initializes this test from the given directory.
     * 
     * @param directory
     */
    public void initializeFromDirectory(File directory)
    {
        caseDirectory = directory;

        String[] files = directory.list(new FilenameFilter() {

            public boolean accept(File arg0, String arg1)
            {
                return arg1.endsWith("-model.m");
            }
        });

        if (files.length == 1)
        {
            initializeFromDesc(new File(directory, files[0]));
        }
    }


    /**
     * 
     * @param cases
     *            a vector of test cases
     * @return boolean indicating whether this test is part of the given vector
     */
    public boolean isIncludedIn(Vector<TestCase> cases)
    {
        return cases.contains(this);
    }


    /**
     * @return boolean indicating whether this test is valid. It is valid if the
     *         directory exists along with sbml, settings and result file.
     */
    public boolean isValid()
    {
        return caseDirectory.exists() && getSBMLFile().exists()
            && getSettingFile().exists() && getExpectedResultFile().exists();
    }


    /**
     * @param tags
     *            a number of tags
     * @return boolean indicating whether this test applies to at least one of
     *         the tags
     *         in the given vector
     */
    public boolean matches(Vector<String> tags)
    {
        return matches(tags, true);
    }


    /**
     * @param tags
     *            a number of tags
     * @return boolean indicating whether this test applies to at least one of
     *         the tags
     *         in the given vector
     */
    public boolean matches(TreeSet<String> tags)
    {
        return matches(new Vector<String>(tags), true);
    }


    /**
     * If a given tag is a package prefix (e.g., "fbc"), we treat
     * it as a wildcard.  In that case, if this test case has any tag with
     * that prefix, this returns a match (true).
     *
     * @param tags
     *            a number of tags
     * @param includeComponentTags
     *            if true not only test tags but also component tags will be
     *            compared
     * @return boolean indicating whether this test applies to at least one of
     *         the tags
     *         in the given vector
     */
    public boolean matches(Vector<String> tags, boolean includeComponentTags)
    {
        for (String tag : tags)
        {
            tag = tag.trim();
            if (testTags.contains(tag) || prefixMatch(testTags, tag))
                return true;
            if (includeComponentTags &&
                (componentTags.contains(tag) || prefixMatch(componentTags, tag)))
                return true;
        }
        return false;
    }


    private final boolean prefixMatch(final Vector<String> tags, final String prefix)
    {
        for (final String tag : tags)
        {
            final int colonIndex = tag.indexOf(':');
            if (colonIndex > -1)
            {
                final String thisPrefix = tag.substring(0, colonIndex);
                if (thisPrefix.equals(prefix))
                    return true;
            }
        }
        return false;
    }


    /**
     * Writes the description to a temp file and opens it in a web browser
     */
    public void openDescriptionInBrowser()
    {
        if (!hasDescription()) return;

        File tempFile;
        try
        {
            tempFile = File.createTempFile(System.getProperty("java.io.tmpdir"),
                                           ".html");
            Util.writeAllText(tempFile, getDescriptionDocument());
            Util.openFile(tempFile);
        }
        catch (IOException e)
        {}
    }


    /**
     * Open the expected result with the associated program
     */
    public void openExpectedResult()
    {
        Util.openFile(getExpectedResultFile());
    }


    /**
     * Opens the sbml file with the associated program
     */
    public void openSBML()
    {
        File file = getSBMLFile();
        Util.openFile(file);
    }


    /**
     * Open the SBML file for the given level / version with the associated
     * program
     * 
     * @param level
     *            the sbml level
     * @param level
     *            the sbml version
     */
    public void openSBML(int level, int version)
    {
        File file = getSBMLFile(level, version);

        Util.openFile(file);
    }


    /**
     * Open the SBML file for the given level / version with the associated
     * program
     * 
     * @param levelVersion
     *            the level / version string
     */
    public void openSBML(String levelVersion)
    {
        File file = getSBMLFile(levelVersion);

        Util.openFile(file);
    }


    /**
     * Opens the test case directory
     */
    public void openTestDirectory()
    {
        Util.openFile(caseDirectory);
    }


    /**
     * @param caseDirectory
     *            the caseDirectory to set
     */
    public void setCaseDirectory(File caseDirectory)
    {
        this.caseDirectory = caseDirectory;
    }


    /**
     * @param componentTags
     *            the componentTags to set
     */
    public void setComponentTags(Vector<String> componentTags)
    {
        this.componentTags = componentTags;
    }


    /**
     * @param generatedBy
     *            the generatedBy to set
     */
    public void setGeneratedBy(String generatedBy)
    {
        this.generatedBy = generatedBy;
    }


    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        if (id == null) return;
        this.id = id;
        this.idNumber = Integer.parseInt(id);
    }


    /**
     * @param lastModified
     *            the lastModified to set
     */
    public void setLastModified(long lastModified)
    {
        this.lastModified = lastModified;
    }


    /**
     * @param settings
     *            the settings to set
     */
    public void setSettings(TestCaseSettings settings)
    {
        this.settings = settings;
    }


    /**
     * @param supportedVersions
     *            the supportedVersions to set
     */
    public void setSupportedVersions(Vector<String> supportedVersions)
    {
        this.supportedVersions = supportedVersions;
    }


    /**
     * @param level
     *            the SBML Level
     * @param version
     *            the Version within the SBML Level
     */
    public boolean supportsLevelVersion(int level, int version)
    {
        if (level == 0) return true;    // 0 indicates "highest".
        return supportedVersions.contains(String.format("%d.%d",
                                                        level, version));
    }


    /**
     * @param lv
     *            the SBML Level and Version
     */
    public boolean supportsLevelVersion(LevelVersion lv)
    {
        if (lv == null) return true;

        int level   = lv.getLevel();
        int version = lv.getVersion();

        if (level == 0) return true;    // 0 indicates "highest".
        return supportedVersions.contains(String.format("%d.%d",
                                                        level, version));
    }


    /**
     * @param synopsis
     *            the synopsis to set
     */
    public void setSynopsis(String synopsis)
    {
        this.synopsis = synopsis;
    }


    /**
     * @param testTags
     *            the testTags to set
     */
    public void setTestTags(Vector<String> testTags)
    {
        this.testTags = testTags;
    }


    /**
     * Re-initializes this test from the directory in case it was changed.
     * 
     * @return true, if the test was reinitialized, false otherwise
     */
    public boolean updateIfNewer()
    {
        boolean changed = hasChanged();
        if (changed) initializeFromDirectory(caseDirectory);
        return changed;
    }

}
