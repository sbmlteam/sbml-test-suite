//
// @file TestSuiteSettings.java
// @brief TestSuiteSettings contains all information about the test suite that should be persisted between runs.
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
import java.util.Date;
import java.util.Vector;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Persister;


/**
 * TestSuiteSettings contains all information about the test suite that
 * should be persisted between runs.
 */
@Default
public class TestSuiteSettings
{
    private Vector<WrapperConfig> wrappers;
    private String                casesDir;

    @Element(required = false)
    private String                lastWrapper;

    @Element(required = false)
    private LevelVersion          lastLVcombo;

    @Transient
    private TestSuite suite;

    @Transient
    private static TestSuiteSettings defaultSettings;


    /**
     * Default constructor
     */
    public TestSuiteSettings()
    {
        super();
        wrappers = new Vector<WrapperConfig>();
        casesDir = "";
        lastWrapper = "";
        lastLVcombo = new LevelVersion(0, 0); // 0 indicates "highest".
    }


    /**
     * Constructs a new settings object with the given test suite directory
     * @param casesDir the directory containing test cases
     */
    public TestSuiteSettings(String casesDir)
    {
        this();
        this.casesDir = casesDir;
    }


    /**
     * Constructs a new settings object with test suite directory and wrapper configurations
     * @param casesDir the test suite directory
     * @param wrappers wrapper configurations
     */
    public TestSuiteSettings(String casesDir, Vector<WrapperConfig> wrappers)
    {
        this(casesDir);
        this.wrappers = new Vector<WrapperConfig>(wrappers);
    }


    /**
     * Constructs a new settings object with test suite directory, wrapper
     * configurations and last wrapper.
     *
     * @param casesDir the test suite directory
     * @param wrappers wrapper configurations
     */
    public TestSuiteSettings(String casesDir, Vector<WrapperConfig> wrappers,
                             String lastWrapper)
    {
        this(casesDir);
        this.wrappers = new Vector<WrapperConfig>(wrappers);
        this.lastWrapper = lastWrapper;
    }


    /**
     * Constructs a new settings object with test suite directory, wrapper
     * configurations and last wrapper.
     *
     * @param casesDir the test suite directory
     * @param wrappers wrapper configurations
     */
    public TestSuiteSettings(String casesDir, Vector<WrapperConfig> wrappers,
                             String lastWrapper, LevelVersion lv)
    {
        this(casesDir);
        this.wrappers = new Vector<WrapperConfig>(wrappers);
        this.lastWrapper = lastWrapper;
        this.lastLVcombo = lv;
    }


    /**
     * Read the settings from the given file
     * @param file the file to read from
     * @return the new settings file
     * @throws Exception possible io / deserialization exception
     */
    public static TestSuiteSettings fromFile(File file)
        throws Exception
    {
        Serializer serializer = new Persister();
        TestSuiteSettings suite = serializer.read(TestSuiteSettings.class, file);
        suite.setSuite(TestSuite.forDirectory(suite.getCasesFile()));
        return suite;
    }


    /**
     * By default the settings will be persisted in the user directory/.testsuite.xml
     * @see Util.getUserDir()
     * @return the default file
     */
    public static File getDefaultFile()
    {
        String userDir = Util.getUserDir();
        return new File(userDir + File.separator + ".testsuite.xml");
    }


    /**
     * Load the default settings
     * @return default settings
     */
    public static TestSuiteSettings loadDefault()
    {
        if (defaultSettings != null) return defaultSettings;

        File defaultFile = getDefaultFile();
        if (!defaultFile.exists() || !defaultFile.isFile()
            || !defaultFile.canRead() || defaultFile.length() == 0)
        {
             return new TestSuiteSettings();
        }

        try
        {
            defaultSettings = fromFile(defaultFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            defaultSettings  = new TestSuiteSettings();
        }
        return defaultSettings;
    }


    /**
     * @return the current directory of test cases
     */
    public String getCasesDir()
    {
        return casesDir;
    }


    /**
     * @return a file for the stored cases directory
     */
    public File getCasesFile()
    {
        return new File(casesDir);
    }


    /**
     * @return the release date of the cases we have.
     */
    public Date getCasesReleaseDate()
    {
        if (suite != null)
            return suite.getCasesReleaseDate();
        else
            return null;
    }


    /**
     * @return output directory of the lastWrapper
     */
    public String getLastOutputDir()
    {
        WrapperConfig last = getLastWrapper();
        if (last == null) return null;
        return last.getOutputPath();
    }


    /**
     * @return the last used wrapper (if specified), otherwise the first wrapper configuration, and null if no wrapper is available
     */
    public WrapperConfig getLastWrapper()
    {
        WrapperConfig result = getWrapper(lastWrapper);
        if (result != null) return result;
        if (wrappers.size() > 0)
            return wrappers.get(0);
        return null;
    }


    /**
     * The name of the last (i.e. the last used) wrapper
     * @return the name of the last wrapper
     */
    public String getLastWrapperName()
    {
        if (lastWrapper == null || lastWrapper.length() == 0)
            if (getLastWrapper() != null)
                lastWrapper = getLastWrapper().getName();
        return lastWrapper;
    }


    /**
     * @return name of last wrapper, or if not specified the first name
     */
    public String getLastWrapperNameOrDefault()
    {
        String last = getLastWrapperName();
        if (getWrapper(last) == null && getLastWrapper() != null)
            return getLastWrapper().getName();
        return last;
    }


    /**
     * @return the last LevelVersion combination specified.
     */
    public LevelVersion getLastLevelVersion()
    {
        return lastLVcombo;
    }


    /**
     * @return the suite
     */
    public TestSuite getSuite()
    {
        return suite;
    }


    /**
     * Returns the wrapper with given name or null
     * @param name the name of the wrapper to return
     * @return the wrapper, if found, or null
     */
    public WrapperConfig getWrapper(String name)
    {
        if (lastWrapper == null || wrappers == null) return null;
        for (WrapperConfig config : wrappers)
        {
            if (config.getName().equals(name)) return config;
        }
        return null;
    }


    /**
     * @return all available wrappers
     */
    public Vector<WrapperConfig> getWrappers()
    {
        return wrappers;
    }


    /**
     * Save the current settings file as default
     */
    public void saveAsDefault()
    {
        try
        {
            writeToFile(getDefaultFile());
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Sets the cases directory
     * @param casesDir the new cases directory
     */
    public void setCasesDir(String casesDir)
    {
        this.casesDir = casesDir;
    }


    /**
     * Sets the cases directory
     * @param casesDir the new cases directory
     */
    public void setCasesDir(File casesDir)
    {
        this.casesDir = casesDir.getPath();
    }


    /**
     * Set the cases directory and save default settings
     * @param file the cases directory
     */
    public void setCasesFile(File file)
    {
        casesDir = file.getAbsolutePath();
        setSuite(TestSuite.forDirectory(file));
        saveAsDefault();
    }


    /**
     * Set the name of the last wrapper and save settings
     * @param lastWrapper the name of the last wrapper configuration
     */
    public void setLastWrapper(String lastWrapper)
    {
        this.lastWrapper = lastWrapper;
        saveAsDefault();
    }


    /**
     * set the name of the last wrapper and save settings
     * @param lastWrapper the last used wrapper configuration
     */
    public void setLastWrapper(WrapperConfig lastWrapper)
    {
        if (lastWrapper == null) return;

        setLastWrapper(lastWrapper.getName());
    }


    /**
     * Set the SBML level/version combination being used for testing.
     */
    public void setLastLevelVersion(LevelVersion lv)
    {
        lastLVcombo = lv;
    }


    /**
     * Set the SBML level/version combination being used for testing.
     */
    public void setLastLevelVersion(int level, int version)
    {
        lastLVcombo = new LevelVersion(level, version);
    }


    /**
     * @param suite
     *            the suite to set
     */
    public void setSuite(TestSuite suite)
    {
        this.suite = suite;
        for (WrapperConfig config : getWrappers())
            config.beginUpdate(suite, lastLVcombo);
    }


    /**
     * Set all wrappers and save as default
     * @param wrappers the wrappers to set
     */
    public void setWrappers(Vector<WrapperConfig> wrappers)
    {
        this.wrappers = wrappers;
        saveAsDefault();
    }

    /**
     * Writes this settings to file
     * @param file the file to write to
     * @throws Exception possible io / serialization exceptions
     */
    public void writeToFile(File file)
        throws Exception
    {
        Serializer serializer = new Persister();
        serializer.write(this, file);
    }

}
