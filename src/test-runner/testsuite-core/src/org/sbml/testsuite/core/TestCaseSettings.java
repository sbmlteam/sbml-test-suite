//
// @file TestCaseSettings.java
// @brief Class holding all settings for a specific test
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
import java.util.Vector;

/**
 * Class holding all settings for a specific test
 */
public class TestCaseSettings
{
    /**
     * Generates a new settings object for the given file
     * @param file the settings file
     * @return a new settings object
     */
    public static TestCaseSettings fromFile(File file)
    {
        return new TestCaseSettings(file);
    }

    /**
     * Generates a new settings object for the given filename
     * @param filename the settings filename
     * @return a new settings object
     */
    public static TestCaseSettings fromFile(String filename)
    {
        return new TestCaseSettings(new File(filename));
    }

    private double         absoluteError;

    private Vector<String> amount;
    private Vector<String> concentration;
    private double         endTime;

    private String         filename;

    private double         relativeError;

    private double         startTime;


    private int            steps;


    private Vector<String> variables;


    /**
     * Default constructor
     */
    public TestCaseSettings()
    {
        setVariables(new Vector<String>());
        setAmount(new Vector<String>());
        setConcentration(new Vector<String>());
    }


    /**
     * Constructor initializing a settings object from a file
     * @param file the settings file
     */
    public TestCaseSettings(File file)
    {
        this();
        initializeFromFile(file);
    }


    /**
     * @return the absoluteError
     */
    public double getAbsoluteError()
    {
        return absoluteError;
    }


    /**
     * @return the amount
     */
    public Vector<String> getAmount()
    {
        return amount;
    }


    /**
     * @return the concentration
     */
    public Vector<String> getConcentration()
    {
        return concentration;
    }


    /**
     * @return the endTime
     */
    public double getEndTime()
    {
        return endTime;
    }


    /**
     * @return the filename
     */
    public String getFilename()
    {
        return filename;
    }


    /**
     * @return the relativeError
     */
    public double getRelativeError()
    {
        return relativeError;
    }


    /**
     * @return the startTime
     */
    public double getStartTime()
    {
        return startTime;
    }


    /**
     * @return the steps
     */
    public int getSteps()
    {
        return steps;
    }


    /**
     * @return the variables
     */
    public Vector<String> getVariables()
    {
        return variables;
    }


    /**
     * Initializes this settings object from the given file
     * @param file the settings file
     */
    public void initializeFromFile(File file)
    {
        this.setFilename(file.getAbsolutePath());

        String contents = Util.readAllText(file);

        setStartTime(Util.parseDouble(Util.getSnippet(contents, "start:", "\n")));
        setEndTime(Util.parseDouble(Util.getSnippet(contents, "duration:", "\n")));
        setSteps(Util.parseInt(Util.getSnippet(contents, "steps:", "\n")));

        getVariables().addAll(Util.split(Util.getSnippet(contents,
                                                         "variables:", "\n")));

        getAmount().addAll(Util.split(Util.getSnippet(contents, "amount:", "\n")));

        getConcentration().addAll(Util.split(Util.getSnippet(contents,
                                                             "concentration:",
                                                             "\n")));

        setAbsoluteError(Util.parseDouble(Util.getSnippet(contents,
                                                          "absolute:", "\n")));
        setRelativeError(Util.parseDouble(Util.getSnippet(contents,
                                                          "relative:", "\n")));

    }


    /**
     * @param absoluteError
     *            the absoluteError to set
     */
    public void setAbsoluteError(double absoluteError)
    {
        this.absoluteError = absoluteError;
    }


    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(Vector<String> amount)
    {
        this.amount = amount;
    }


    /**
     * @param concentration
     *            the concentration to set
     */
    public void setConcentration(Vector<String> concentration)
    {
        this.concentration = concentration;
    }


    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(double endTime)
    {
        this.endTime = endTime;
    }


    /**
     * @param filename
     *            the filename to set
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }


    /**
     * @param relativeError
     *            the relativeError to set
     */
    public void setRelativeError(double relativeError)
    {
        this.relativeError = relativeError;
    }


    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(double startTime)
    {
        this.startTime = startTime;
    }


    /**
     * @param steps
     *            the steps to set
     */
    public void setSteps(int steps)
    {
        this.steps = steps;
    }


    /**
     * @param variables
     *            the variables to set
     */
    public void setVariables(Vector<String> variables)
    {
        this.variables = variables;
    }
}
