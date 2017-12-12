//
// @file MainModel.java
// @brief MainModel is the data model for the main window
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

package org.sbml.testsuite.ui.model;

import java.io.File;
import java.util.Vector;
import org.sbml.testsuite.core.TestSuite;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * MainModel is the data model for the main window
 */
public class MainModel
{
    private TestSuiteSettings settings;


    /**
     * Constructs a new model by loading the default settings
     */
    public MainModel()
    {
        settings = TestSuiteSettings.loadDefault();
    }


    /**
     * Constructs a new main model from the given directory
     * 
     * @param testSuiteDir
     *            the test suite directory
     */
    public MainModel(File testSuiteDir)
    {
        this();
        setTestSuiteDir(testSuiteDir);
    }


    /**
     * @return last output directory
     */
    public String getLastOutputDir()
    {
        return settings.getLastOutputDir();
    }


    /**
     * @return lastly used wrapper configuration
     */
    public WrapperConfig getLastWrapper()
    {
        return settings.getLastWrapper();

    }


    /**
     * @return current settings
     */
    public TestSuiteSettings getSettings()
    {
        return settings;
    }


    /**
     * @return the suite
     */
    public TestSuite getSuite()
    {
        return settings.getSuite();
    }


    /**
     * @return the testSuiteDir
     */
    public File getTestSuiteDir()
    {
        return settings.getCasesFile();
    }


    /**
     * @return the wrappers
     */
    public Vector<WrapperConfig> getWrappers()
    {
        return settings.getWrappers();
    }


    /**
     * Sets test suite settings
     * 
     * @param settings
     *            the settings
     */
    public void setSettings(TestSuiteSettings settings)
    {
        this.settings = settings;
        setTestSuiteDir(settings.getCasesFile());
    }


    /**
     * @param suite
     *            the suite to set
     */
    public void setSuite(TestSuite suite)
    {
        settings.setSuite(suite);
    }


    /**
     * @param testSuiteDir
     *            the testSuiteDir to set
     */
    public void setTestSuiteDir(File testSuiteDir)
    {
        settings.setCasesFile(testSuiteDir);
    }


    /**
     * @param wrappers
     *            the wrappers to set
     */
    public void setWrappers(Vector<WrapperConfig> wrappers)
    {
        settings.setWrappers(wrappers);
    }

}
