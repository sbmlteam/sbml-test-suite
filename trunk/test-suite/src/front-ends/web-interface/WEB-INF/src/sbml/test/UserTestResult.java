//
// @file    UserTestResult.java
// @brief   Store the results of evaluating a single test case from the user
// @author  Michael Hucka
// @date    Created 2010-02-26 <mhucka@caltech.edu>
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
            throw new IOException("Null case");
        else
            testCase = theCase;
    }

    public void setNumDifferences(int numFail)
    {
        this.numDifferences = numFail;
    }

    public void setDifferences(BigDecimal[][] diffs)
    {
        this.differences = diffs;
    }

    public void setErrorMessage(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public boolean        hasResult()         { return testCase != null; }
    public UserTestCase   getUserTestCase()   { return testCase; }
    public int            getNumDifferences() { return numDifferences; }
    public BigDecimal[][] getDifferences()    { return differences; }
    public boolean        hasError()          { return errorMsg != null; }
    public String         getErrorMessage()   { return errorMsg; }

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
    private BigDecimal[][] differences;
    private String errorMsg;

} // end of class
