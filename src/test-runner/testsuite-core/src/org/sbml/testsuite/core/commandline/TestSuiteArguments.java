//
// @file TestSuiteArguments.java
// @brief TestSuiteArguments is a parser for command line arguments (unused)
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

package org.sbml.testsuite.core.commandline;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.sbml.testsuite.core.TestSuiteSettings;

/**
 * TestSuiteArguments is a parser for command line arguments (unused)
 */
public class TestSuiteArguments
{
    private boolean shouldRun;
    private boolean shouldListReleases;
    private boolean shouldDownload;
    private String  url;
    private Date    publishDate;
    private String  wrapperName;
    private String  testOrTestRange;


    /**
     * Constructor parsing arguments
     * 
     * @param args
     *            the arguments
     */
    public TestSuiteArguments(String args[])
    {

        parseArgs(args);

    }


    public void parseArgs(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            String current = args[i].toLowerCase();
            String first = (i + 1 < args.length ? args[i + 1] : null);
            String second = (i + 2 < args.length ? args[i + 2] : null);

            if ((current.equals("-r") || current.equals("--run"))
                && first != null && second != null)
            {
                shouldRun = true;
                wrapperName = first;
                testOrTestRange = second;
                i += 2;
            }
            else if (current.equals("-l") || current.equals("--list-releases"))
            {
                shouldListReleases = true;
                if (first != null)
                {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try
                    {
                        publishDate = df.parse(first);
                    }
                    catch (Exception e)
                    {
                        publishDate = null;
                    }
                }

            }
            else if ((current.equals("-d") || current.equals("--download-release"))
                && first != null)
            {
                shouldDownload = true;
                url = first;
            }

        }
    }


    /**
     * @return flag indicating whether valid arguments were provided
     */
    public boolean isValid()
    {
        return ((shouldDownload && url != null)
                || shouldListReleases
                || (shouldRun
                    && (wrapperName != null
                        && TestSuiteSettings.loadDefault().getWrapper(wrapperName) != null)));
    }


    /**
     * prints the usage information.
     * 
     * @param stream
     *            the stream to print to
     */
    public void printArguments(PrintStream stream)
    {
        printArguments(stream, null);
    }


    /**
     * prints the usage information.
     * 
     * @param stream
     *            the stream to print to
     * @param message
     *            an optional message
     */
    public void printArguments(PrintStream stream, String message)
    {
        stream.println("SBML TestSuite (core)");
        stream.println("=====================");
        stream.println();
        if (message != null && message.length() > 0)
        {
            stream.println(message);
            stream.println();
        }
        stream.println("Usage: ");
        stream.println();
        stream.println(" -r | --run <wrapperName> <test-range>");
        stream.println(" -l | --list-releases");
        stream.println(" -d | --download-release url");
        stream.println();

    }


    /**
     * If true the given wrapper and test range should be run.
     * 
     * @return the shouldRun
     */
    public boolean isShouldRun()
    {
        return shouldRun;
    }


    /**
     * Specifies whether the given wrapper should be run
     * 
     * @param shouldRun
     *            the shouldRun to set
     */
    public void setShouldRun(boolean shouldRun)
    {
        this.shouldRun = shouldRun;
    }


    /**
     * Returns the selected wrapper
     * 
     * @return the wrapperName
     */
    public String getWrapperName()
    {
        return wrapperName;
    }


    /**
     * Sets the wrapper for command line operations.
     * 
     * @param wrapperName
     *            the wrapperName to set
     */
    public void setWrapperName(String wrapperName)
    {
        this.wrapperName = wrapperName;
    }


    /**
     * Gets either a single test (by specifying a number), or a test range in
     * the format: start-end (with start and end included).
     * 
     * @return the testOrTestRange
     */
    public String getTestOrTestRange()
    {
        return testOrTestRange;
    }


    /**
     * Sets either a single test (by specifying a number), or a test range in
     * the format: start-end (with start and end included).
     * 
     * @param testOrTestRange
     *            the testOrTestRange to set
     */
    public void setTestOrTestRange(String testOrTestRange)
    {
        this.testOrTestRange = testOrTestRange;
    }


    /**
     * @return the shouldListReleases
     */
    public boolean isShouldListReleases()
    {
        return shouldListReleases;
    }


    /**
     * @param shouldListReleases
     *            the shouldListReleases to set
     */
    public void setShouldListReleases(boolean shouldListReleases)
    {
        this.shouldListReleases = shouldListReleases;
    }


    /**
     * @return the shouldDownload
     */
    public boolean isShouldDownload()
    {
        return shouldDownload;
    }


    /**
     * @param shouldDownload
     *            the shouldDownload to set
     */
    public void setShouldDownload(boolean shouldDownload)
    {
        this.shouldDownload = shouldDownload;
    }


    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * @return the publishDate
     */
    public Date getPublishDate()
    {
        return publishDate;
    }


    /**
     * @param publishDate
     *            the publishDate to set
     */
    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }
}
