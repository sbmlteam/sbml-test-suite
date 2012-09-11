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
// Copyright (C) 2009-2012 jointly by the following organizations:
// 1. California Institute of Technology, Pasadena, CA, USA
// 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
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
    private ResultSet result1;
    private ResultSet result2;
    private boolean   requireAllColumns;


    /**
     * Constructor comparing two result sets
     * 
     * @param result1
     *            resultset 1
     * @param result2
     *            resultset 2
     */
    public CompareResultSet(ResultSet result1, ResultSet result2)
    {
        this.result1 = result1;
        this.result2 = result2;
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
        ResultSet expected = result1;
        ResultSet another = result2;

        if (expected == null || another == null) return false;

        int _nNumRows = Math.min(expected.getNumRows(), another.getNumRows());

        for (String s : expected.getHeaders())
        {
            String sColumnName = s;

            if (sColumnName.toLowerCase().equals("time")) continue;

            if (expected.hasColumn(s) && another.hasColumn(s))
            {
                double[] aCol = expected.getColumn(s);
                double[] bCol = another.getColumn(s);

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

                    if (Math.abs(expectedValue - givenValue) > (absoluteError + relativeError
                        * Math.abs(expectedValue)))
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
     * @return the result1
     */
    public ResultSet getResult1()
    {
        return result1;
    }


    /**
     * @return the result2
     */
    public ResultSet getResult2()
    {
        return result2;
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
     * @param result1
     *            the result1 to set
     */
    public void setResult1(ResultSet result1)
    {
        this.result1 = result1;
    }


    /**
     * @param result2
     *            the result2 to set
     */
    public void setResult2(ResultSet result2)
    {
        this.result2 = result2;
    }
}
