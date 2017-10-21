//
// @file CompareResultSet.java
// @brief Class comparing two result sets
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Testsuite. Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2009-2015 jointly by the following organizations:
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

package org.sbml.testsuite.core.data;

/**
 * Class comparing two result sets
 */
public class CompareResultSet
{
    private ResultSet expected;
    private ResultSet delivered;
    private boolean   requireAllColumns;


    /**
     * Constructor comparing two result sets
     * 
     * @param expected
     *            resultset 1
     * @param delivered
     *            resultset 2
     */
    public CompareResultSet(ResultSet expected, ResultSet delivered)
    {
        this.expected = expected;
        this.delivered = delivered;
        this.requireAllColumns = true;
    }


    /**
     * Compares the result sets according to the given relative and absolute
     * errors
     * 
     * @param absoluteError
     *            absolute error
     * @param relativeError
     *            relative error
     * @return boolean indicating success (true) or failure (false)
     */
    public boolean compareUsingTestSuite(double absoluteError,
                                         double relativeError)
    {
        if (expected == null || delivered == null) return false;

        int _nNumRows = Math.min(expected.getNumRows(), delivered.getNumRows());

        for (String columnName : expected.getHeaders())
        {
            if (columnName.toLowerCase().equals("time"))
                continue;
            if (delivered.hasColumn(columnName))
            {
                double[] aCol = expected.getColumn(columnName);
                double[] bCol = delivered.getColumn(columnName);

                for (int i = 0; i < _nNumRows; i++)
                {
                    double expectedValue = aCol[i];
                    double givenValue = bCol[i];

                    if (Double.isInfinite(expectedValue)
                        && !Double.isInfinite(givenValue)) return false;
                    if (Double.isNaN(expectedValue)
                        && !Double.isNaN(givenValue)) return false;
                    if (Double.isInfinite(givenValue)
                        && !Double.isInfinite(expectedValue)) return false;
                    if (Double.isNaN(givenValue)
                        && !Double.isNaN(expectedValue)) return false;

                    if (Math.abs(expectedValue - givenValue)
                        > (absoluteError + relativeError * Math.abs(expectedValue)))
                    {
                        return false;
                    }
                }
            }
            else
            {
                if (requireAllColumns) return false;
            }
        }

        return true;
    }


    /**
     * @return the expected
     */
    public ResultSet getExpected()
    {
        return expected;
    }


    /**
     * @return the delivered
     */
    public ResultSet getDelivered()
    {
        return delivered;
    }


    /**
     * @return the requireAllColumns
     */
    public boolean isRequireAllColumns()
    {
        return requireAllColumns;
    }


    /**
     * @param requireAllColumns
     *            the requireAllColumns to set
     */
    public void setRequireAllColumns(boolean requireAllColumns)
    {
        this.requireAllColumns = requireAllColumns;
    }


    /**
     * @param expected
     *            the expected to set
     */
    public void setExpected(ResultSet expected)
    {
        this.expected = expected;
    }


    /**
     * @param delivered
     *            the delivered to set
     */
    public void setDelivered(ResultSet delivered)
    {
        this.delivered = delivered;
    }
}
