//
// @file   Comparison.java
// @brief  Class encapsulating the results of comparing ResultSets.
// @author Michael Hucka
// @date   Created 2017-10-20 <mhucka@caltech.edu>
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

package org.sbml.testsuite.core.data;

/**
 * Class encapsulating the result of comparing two result sets.
 */
public class Comparison
{
    private boolean match;
    private ResultSet resultSet;


    /**
     * Constructor for creating a Comparison using a @p match value and an
     * array of numerical results in @p resultSet.
     *
     * A value of true for @p match signifies a match or successful
     * comparison; false indicates a mismach or comparison failure.
     * The numerical differences computing in the comparison are stored
     * as ResultSet objects.
     *
     * @param match true for successful match, false for a failure
     * @param resultSet object representing the differences computed during
     * a comparison of result sets.
     */
    Comparison(boolean match, ResultSet resultSet)
    {
        this.match = match;
        this.resultSet = resultSet;
    }


    /**
     * Returns true if the result indicates matching ResultSet values.
     *
     * A value of true for @p match signifies a match or successful
     * comparison; false indicates a mismach or comparison failure.
     */
    public boolean isMatch()
    {
        return match;
    }


    /**
     * Returns the difference array stored in this Comparison.
     *
     * @retun ResultSet a minimal ResultSet object containing, as its array
     * of values, the result of the comparison (which is typically an array
     * of differences).
     */
    public ResultSet getResultSet()
    {
        return resultSet;
    }


    /**
     * Set the boolean value indicating a comparison match or failure.
     *
     * @param match true for successful match, false for a failure.
     */
    public void setMatch(boolean match)
    {
        this.match = match;
    }


    /**
     * Set the ResultSet object storing the differences computed during
     * a comparison operation.
     *
     * @param resultSet a minimal ResultSet object containing an array
     * of differences.
     */
    public void setResultSet(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }
}
