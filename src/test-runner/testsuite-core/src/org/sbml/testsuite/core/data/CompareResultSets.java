//
// @file CompareResultSets.java
// @brief Class comparing two result sets
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

package org.sbml.testsuite.core.data;

import java.util.Vector;
import org.sbml.testsuite.core.TestCaseSettings;


/**
 * Class comparing two result sets
 */
public class CompareResultSets
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
    public CompareResultSets(ResultSet expected, ResultSet delivered)
    {
        this.expected = expected;
        this.delivered = delivered;
        this.requireAllColumns = true;
    }


    /**
     * Compares the result sets according to the relative and absolute error
     * settings found in the given TestCaseSettings.
     *
     * @param settings the test case settings object
     *
     * @return A Comparison object, which provides (1) a boolean to indicate
     * whether the ResultSet arrays match within the tolerances and (2) an
     * array of doubles representing the numerical differences.
     */
    public Comparison compare(TestCaseSettings settings)
    {
        if (settings == null) return new Comparison(false, null);
        return compare(settings.getAbsoluteError(), settings.getRelativeError(), -1);
    }


    /**
     * Compares a single row of the result sets according to the relative and
     * absolute error settings found in the given TestCaseSettings.
     *
     * @param settings the test case settings object
     * @param row a single row index, or -1 to compare all rows
     *
     * @return A Comparison object, which provides (1) a boolean to indicate
     * whether the ResultSet arrays match within the tolerances and (2) an
     * array of doubles representing the numerical differences.
     */
    public Comparison compareRow(TestCaseSettings settings, int row)
    {
        if (settings == null) return new Comparison(false, null);
        return compare(settings.getAbsoluteError(), settings.getRelativeError(), row);
    }


    /**
     * Compares the result sets according to the given relative and absolute
     * error settings.
     *
     * @param absError absolute error
     * @param relError relative error
     * @param row a single row index, or -1 to compare all rows
     *
     * @return A Comparison object, which provides (1) a boolean to indicate
     * whether the ResultSet arrays match within the tolerances and (2) an
     * array of doubles representing the numerical differences.
     */
    public Comparison compare(double absError, double relativeErr)
    {
        return compare(absError, relativeErr, -1);
    }


    /**
     * Compares a single row of the result sets according to the given
     * relative and absolute error settings.
     *
     * @param absError absolute error
     * @param relError relative error
     * @param row a single row index, or -1 to compare all rows
     *
     * @return A Comparison object, which provides (1) a boolean to indicate
     * whether the ResultSet arrays match within the tolerances and (2) an
     * array of doubles representing the numerical differences.
     */
    public Comparison compareRow(double absError, double relativeErr, int row)
    {
        return compare(absError, relativeErr, row);
    }


    /**
     * Internal workhorse function for comparing results.
     *
     * @param absError absolute error
     * @param relError relative error
     * @param row a single row index, or -1 to compare all rows
     *
     * @return Comparison object providing a boolean to indicate successful
     * comparison and an array of doubles to return the differences
     */
    private Comparison compare(double absError, double relError, int row)
    {
        // Prepare the return value for cases of problems.
        Comparison failure = new Comparison(false, null);

        if (expected == null || delivered == null) return failure;
        if (row > expected.getNumRows() || row > delivered.getNumRows())
            return failure;
        if (delivered.getNumRows() < expected.getNumRows())
            return failure;

        // I no longer remember why we would limit the rows like this.
        // int numRows = Math.min(expected.getNumRows(), delivered.getNumRows());
        int numRows = expected.getNumRows();
        int numCols = Math.min(expected.getNumColumns(), delivered.getNumColumns());

        // Edge case, where delivered results array lacks a column for time
        // and numCols ends up one less than it needs to be. Compensate.
        if (delivered.getIndex("time") < 0
            && expected.getNumColumns() > delivered.getNumColumns())
            numCols += 1;

        // Are we comparing only a single row?
        if (row >= 0)
            numRows = 1;

        double[][] diffArray = new double[numRows][numCols];
        boolean match = true;

        // Iterate over every column and compare the values.
        for (String columnName : expected.getHeaders())
        {
            int colIndex = expected.getIndex(columnName);
            double[] eCol = expected.getColumn(colIndex);
            if (columnName.equalsIgnoreCase("time"))
            {
                if (row >= 0)
                    diffArray[0][colIndex] = eCol[row];
                else
                {
                    for (int i = 0; i < numRows; i++)
                        diffArray[i][colIndex] = eCol[i];
                }
            }
            else if (delivered.hasColumn(columnName))
            {
                double[] dCol = delivered.getColumn(columnName);
                if (row >= 0)
                {
                    double expectedValue = eCol[row];
                    double givenValue = dCol[row];

                    diffArray[0][colIndex] = Math.abs(expectedValue - givenValue);
                    // The test against match is to skip needless computations
                    // if we already failed to match at a previous time.
                    if (match &&
                        (mismatchedBadValues(expectedValue, givenValue)
                         || (Math.abs(expectedValue - givenValue)
                             > (absError + relError * Math.abs(expectedValue)))))
                        match = false;
                }
                else
                {
                    for (int i = 0; i < numRows; i++)
                    {
                        double expectedValue = eCol[i];
                        double givenValue = dCol[i];

                        diffArray[i][colIndex] = Math.abs(expectedValue - givenValue);
                        if (match &&
                            (mismatchedBadValues(expectedValue, givenValue)
                             || (Math.abs(expectedValue - givenValue)
                                 > (absError + relError * Math.abs(expectedValue)))))
                            match = false;
                    }
                }
            }
            else if (requireAllColumns)
                return failure;
        }

        // FIXME this assumes headers vector has "time" as 1st column.
        return new Comparison(match, new ResultSet(expected.getHeaders(), diffArray));
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


    private boolean mismatchedBadValues(double a, double b)
    {
        return (Double.isInfinite(a) && !Double.isInfinite(b))
            || (Double.isInfinite(b) && !Double.isInfinite(a))
            || (Double.isNaN(a) && !Double.isNaN(b))
            || (Double.isNaN(b) && !Double.isNaN(a));
    }

}
