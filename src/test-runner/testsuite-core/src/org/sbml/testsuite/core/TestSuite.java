//
// @file TestSuite.java
// @brief The Testsuite class, holding all testcases
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
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

package org.sbml.testsuite.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


/**
 * The Testsuite class holds all test cases
 */
public class TestSuite
{
    /** The name of the file that stores the release date of the test cases. */
    public static final String CASES_DATE_FILE_NAME = ".cases-archive-date";

    /** The release date of the test cases currently being used. */
    private Date             casesReleaseDate;

    /** The currently-configured directory of the test cases, which may
     *  not necessarily be the same as the internal cases directory. */
    private File             casesDirectory;

    /** All test cases, as a vector. */
    private Vector<TestCase> cases               = new Vector<TestCase>();

    /** All known test tags that appear in the test cases. */
    private Vector<String>   cachedTestTags      = new Vector<String>();

    /** All known component tags that appear in the test cases. */
    private Vector<String>   cachedComponentTags = new Vector<String>();


    /**
     * Constructor initializing the test suite from the given directory
     * @param directory the directory
     */
    public TestSuite(File directory)
    {
        initializeFromDirectory(directory);
    }


    /**
     * Creates a test suite for a directory
     * @param dir the directory
     * @return the new test suite
     */
    public static TestSuite forDirectory(File dir)
    {
        return new TestSuite(dir);
    }


    /**
     * Returns the test for the given index
     * @param index the index
     * @return the test with that index
     */
    public TestCase get(int index)
    {
        return cases.get(index);
    }


    /**
     * Returns the test case for the given id
     * @param id the id 
     * @return the test case for this id, or null
     */
    public TestCase get(String id)
    {
        for (TestCase test : cases)
            if (test.getId().equalsIgnoreCase(id)) return test;
        return null;
    }


    /**
     * @return the cases
     */
    public Vector<TestCase> getCases()
    {
        return cases;
    }


    /**
     * @return the casesDirectory
     */
    public File getCasesDirectory()
    {
        return casesDirectory;
    }


    /**
     * @return the release date of the cases.  null = unknown.
     */
    public Date getCasesReleaseDate()
    {
        return casesReleaseDate;
    }


    /**
     * @return a sorted vector of all unique component tags included in the
     * test cases of this suite
     */
    public Vector<String> getComponentTags()
    {
        if (cachedComponentTags != null && !cachedComponentTags.isEmpty())
            return cachedComponentTags;

        Vector<String> result = new Vector<String>();

        for (TestCase item : cases)
        {
            for (String tag : item.getComponentTags())
            {
                String trimmed = tag.trim();
                if (trimmed.length() > 0 && !result.contains(trimmed))
                    result.add(trimmed);
            }
        }

        Collections.sort(result);

        // Cache the value for next time.

        cachedComponentTags = result;
        return result;
    }


    /**
     * @return the number of test cases in this suite
     */
    public int getNumCases()
    {
        return cases.size();
    }


    /**
     * Returns all tests within a given range, so a start of 5 and an end of 10 would 
     * return the tests with id: 00005, 00006, 00007, 00008, 00009, 00010
     *  
     * @param start start value
     * @param end end value
     * @return a vector of tests within this range
     */
    public Vector<TestCase> getRange(int start, int end)
    {
        Vector<TestCase> cases = new Vector<TestCase>();
        for (int i = start; i <= end; i++)
        {
            String id = String.format("%5d", i);
            TestCase test = get(id);
            if (test != null) cases.add(test);
        }
        return cases;

    }


    /**
     * @return a vector of all testcases sorted by id
     */
    public Vector<TestCase> getSortedCases()
    {
        TestCase[] array = cases.toArray(new TestCase[0]);
        Arrays.sort(array, new Comparator<TestCase>() {

            public int compare(TestCase arg0, TestCase arg1)
            {
                return arg0.getId().compareTo(arg1.getId());
            }});
        return new Vector<TestCase>(Arrays.asList(array));
    }


    /**
     * @return a sorted vector of all unique test tags included in all the test
     * cases of this suite
     */
    public Vector<String> getTestTags()
    {
        if (cachedTestTags != null && !cachedTestTags.isEmpty())
            return cachedTestTags;

        Vector<String> result = new Vector<String>();

        for (TestCase item : cases)
        {
            for (String tag : item.getTestTags())
            {
                String trimmed = tag.trim();
                if (trimmed.length() > 0 && !result.contains(trimmed))
                    result.add(trimmed);
            }
        }

        Collections.sort(result);

        // Cache the value for next time.

        cachedTestTags = result;
        return result;
    }


    /**
     * Initializes this test suite from the given directory
     * @param directory the directory
     */
    public void initializeFromDirectory(File directory)
    {
        if (directory == null || !directory.exists()) return;
        casesDirectory = directory;

        cases.clear();
        cachedTestTags.clear();
        cachedComponentTags.clear();

        String[] files = directory.list(new FilenameFilter() {
            public boolean accept(File arg0, String arg1)
            {
                return arg1.length() == 5;
            }
        });
        Arrays.sort(files);
        for (String file : files)
        {
            TestCase newTestCase = new TestCase(new File(casesDirectory, file));
            String id = newTestCase.getId();
            if (Util.isNullOrEmpty(id)) continue;
            cases.add(newTestCase);
        }

        casesReleaseDate = Util.readArchiveDateFile(casesDirectory,
                                                    CASES_DATE_FILE_NAME);
    }


    /**
     * Replaces the test with the given index with the given test
     * @param index the index
     * @param test the test
     */
    public void set(int index, TestCase test)
    {
        cases.set(index, test);
    }


    /**
     * Replaces the test with the given id, with the given one
     * 
     * @param id the id of the test case
     * @param test the new test
     */
    public void set(String id, TestCase test)
    {
        TestCase oldTest = get(id);
        if (test != null)
        {
            cases.remove(oldTest);
        }

        cases.add(test);

    }


    /**
     * @param cases the cases to set
     */
    public void setCases(Vector<TestCase> cases)
    {
        this.cases = cases;
    }


    /**
     * @param casesDirectory the casesDirectory to set
     */
    public void setCasesDirectory(File casesDirectory)
    {
        this.casesDirectory = casesDirectory;
    }


    /**
     * Updates all test cases if they have changed
     * @return true if a test has changed, false otherwise
     */
    public boolean updateCases()
    {
        boolean updated = false;
        for (TestCase item : cases)
        {
            updated = updated | item.updateIfNewer();
        }

        if (updated)
        {
            // Need to update our caches.

            cachedTestTags.clear();
            cachedComponentTags.clear();
            getComponentTags();
            getTestTags();
        }

        return updated;
    }
}
