//
// @file    UserTestResult.java
// @brief   Store the results of evaluating a single test case from the user
// @author  Michael Hucka
// @date    Created 2010-02-26 <mhucka@caltech.edu>
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

import java.io.*;
import java.math.*;
import java.util.*;
import java.applet.*;

public class UserTestResult
    implements Comparable<UserTestResult>
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public UserTestResult(UserTestCase theCase)
        throws Exception
    {
        if (theCase == null)
            throw new IOException("Null case object.");
        else
            testCase = theCase;
    }

    public void setNumDifferences(int numFail)
    {
        this.numDifferences = numFail;
    }

    public void setDifferences(ResultDifference[][] diffs)
    {
        this.differences = diffs;
    }

    public void setErrorMessage(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public boolean        hasResult()            { return testCase != null; }
    public UserTestCase   getUserTestCase()      { return testCase; }
    public boolean        hasError()             { return errorMsg != null; }
    public String         getErrorMessage()      { return errorMsg; }
    public int            getNumDifferences()    { return numDifferences; }
    public ResultDifference[][] getDifferences() { return differences; }

    /**
     * Returns a negative integer, zero, or a positive integer depending
     * on whether the given other case number is less than, equal or greater
     * that this one.
     */
    public int compareTo(UserTestResult other)
    {
        return testCase.getCaseNum() - other.getUserTestCase().getCaseNum();
    }

    // 
    // -------------------------- Private variables --------------------------- 
    // 

    private UserTestCase testCase;
    private int numDifferences;
    private ResultDifference[][] differences;
    private String errorMsg;

} // end of class
