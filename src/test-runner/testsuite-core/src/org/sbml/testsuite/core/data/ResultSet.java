//
// @file ResultSet.java
// @brief Class holding CSV data
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

/**
 * Class holding CSV data
 */
public class ResultSet
{
    private Vector<String> headers;
    private File           dataFile = null;
    private double[][]     data;
    private boolean        hasInfinityOrNaN = false;
    private boolean        parseable = true;


    /**
     * Default constructor.
     */
    public ResultSet()
    {
        headers = new Vector<String>();
    }


    /**
     * Constructs a new result set from the given CSV file
     * 
     * @param file
     *            the csv file
     */
    public ResultSet(File file)
    {
        this();
        dataFile = file;
        parseFile(file);
    }


    /**
     * Constructs a new result set with given headers and data
     * 
     * @param headers
     *            the headers
     * @param data
     *            the data
     */
    public ResultSet(Vector<String> headers, double[][] data)
    {
        this.headers = headers;
        this.data = data;
    }


    /**
     * Adds the row of data to the rawdata
     * 
     * @param rawData
     *            rawData vector
     * @param dimensions
     *            dimensions
     * @param line
     *            the current line
     */
    private static void addRow(Vector<double[]> rawData, int dimensions,
                               String line)
    {
        if (line == null) return;
        if (line.trim().length() == 0) return;
        try
        {
            String[] oValues = line.split(",");
            double[] oRow = new double[dimensions];
            if (oValues.length == 0 || oValues.length != dimensions) return;
            for (int i = 0; i < dimensions; i++)
            {
                oRow[i] = saveConvert(oValues[i], 0.0);
            }
            rawData.add(oRow);
        }
        catch (Exception ex)
        {
            // nevermind
        }
    }


    /**
     * Return true if the headers of the two result sets match.
     */
    public static boolean sameColumnOrder(ResultSet a, ResultSet b)
    {
        int numCols = Math.min(a.getNumColumns(), b.getNumColumns());
        Vector<String> aHeaders = a.getHeaders();
        Vector<String> bHeaders = b.getHeaders();

        if (aHeaders == null || bHeaders == null)
            return false;

        for (int i = 0; i < numCols; i++)
        {
            if (! aHeaders.get(i).equalsIgnoreCase(bHeaders.get(i))
                && ! aHeaders.get(i).equalsIgnoreCase("[" + bHeaders.get(i) + "]")
                && ! bHeaders.get(i).equalsIgnoreCase("[" + aHeaders.get(i) + "]"))
                return false;
        }
        return true;
    }


    /**
     * Computes a result set with absolute differences between two results.
     * 
     * @param a
     *            result 1
     * @param b
     *            result 2
     * @return resultset of absolute differences
     */
    public static ResultSet diff(ResultSet a, ResultSet b)
    {
        if (a == null || b == null) return null;

        int numRows = Math.min(a.getNumRows(), b.getNumRows());
        int numCols = Math.min(a.getNumColumns(), b.getNumColumns());
        double[][] diffArray = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++)
        {
            diffArray[i][0] = a.getData()[i][0];
            for (int j = 1; j < numCols; j++)
            {
                diffArray[i][j] = Math.abs(a.getData()[i][j] - b.getData()[i][j]);
            }
        }

        Vector<String> heads = (a.getHeaders().size() > b.getHeaders().size()
                                ? b.getHeaders() : a.getHeaders());
        return new ResultSet(heads, diffArray);
    }


    /**
     * Computes a result set with absolute differences between the values
     * of a single row.
     * 
     * @param a
     *            result 1
     * @param b
     *            result 2
     * @return resultset of absolute differences
     */
    public static ResultSet diffRow(ResultSet a, ResultSet b, int row)
    {
        if (a == null || b == null) return null;
        if (row < 0 || row > a.getNumRows() || row > b.getNumRows()) return null;

        double[][] aData = a.getData();
        double[][] bData = b.getData();
        int numCols      = Math.min(a.getNumColumns(), b.getNumColumns());
        double[][] diffArray  = new double[1][numCols];

        for (int j = 0; j < numCols; j++)
            diffArray[row][j] = Math.abs(aData[row][j] - bData[row][j]);

        Vector<String> heads = b.getHeaders();
        if (a.getHeaders().size() <= b.getHeaders().size())
            heads = a.getHeaders();

        return new ResultSet(heads, diffArray);
    }


    /**
     * Creates a new result set for the given file
     * 
     * @param filename
     *            the CSV file
     * @return the result set
     */
    public static ResultSet fromFile(File file)
    {
        if (file == null || !file.exists()) // No such file.
            return null;
        return new ResultSet(file);
    }


    /**
     * Creates a new result set for the given filename
     * 
     * @param filename
     *            the filename to a CSV file
     * @return the result set
     */
    public static ResultSet fromFile(String filename)
    {
        return fromFile(new File(filename));
    }


    /**
     * Safely convert the given string to a double
     * 
     * @param string
     *            the string
     * @param defaultValue
     *            a default value
     * @return the strings double value or default
     */
    private static double saveConvert(String string, double defaultValue)
    {
        try
        {
            String text = string.trim().toUpperCase();

            if (text.equals("INF") || text.equals("1.#INF"))
                return Double.POSITIVE_INFINITY;
            if (text.equals("-INF") || text.equals("-1.#INF"))
                return Double.NEGATIVE_INFINITY;

            // The Wikipedia page for NaN describes many possible variations.
            // We allow them all, in case applications on different platforms
            // produce different versions.
            if (text.contains("NAN") || text.equals("-1.#IND"))
                return Double.NaN;

            return Double.parseDouble(string);
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }


    /**
     * Returns the column of data for the given index
     * 
     * @param index
     *            the index
     * @return the data column for that index
     */
    public double[] getColumn(int index)
    {
        int numColumns = getNumColumns();
        int numRows = getNumRows();
        if (index == -1 || index >= numColumns) return null;

        double[] col = new double[numRows];
        for (int i = 0; i < numRows; i++)
        {
            col[i] = data[i][index];
        }
        return col;

    }


    /**
     * Returns the column of data for the given header
     * 
     * @param header
     *            the header
     * @return the data column for that header
     */
    public double[] getColumn(String header)
    {
        return getColumn(getIndex(header));
    }


    /**
     * @return the file from whence this data came
     */
    public File getFile()
    {
        return dataFile;
    }


    /**
     * @return the data
     */
    public double[][] getData()
    {
        return data;
    }


    /**
     * @return the headers
     */
    public Vector<String> getHeaders()
    {
        return headers;
    }


    /**
     * Finds an index for the entry with given header
     * 
     * @param header
     *            the header to find
     * @return the index for that entry
     */
    public int getIndex(String header)
    {
        for (int i = 0; i < headers.size(); i++)
        {
            if (headers.get(i).equalsIgnoreCase(header)) return i;
        }
        for (int i = 0; i < headers.size(); i++)
        {
            if (headers.get(i).equalsIgnoreCase("[" + header + "]")) return i;
        }
        for (int i = 0; i < headers.size(); i++)
        {
            if (headers.get(i).contains(header)) return i;
        }
        return -1;
    }


    /**
     * @return number of columns
     */
    public int getNumColumns()
    {
        if (data == null || data.length == 0) return 0;
        return data[0].length;
    }


    /**
     * @return number of rows
     */
    public int getNumRows()
    {
        if (data == null) return 0;
        return data.length;
    }


    /**
     * @return the time column
     */
    public double[] getTimeColumn()
    {
        return getColumn("time");
    }


    /**
     * Returns a boolean indicating whether a column with given header exists
     * 
     * @param header
     *            the header
     * @return boolean indicating whether a column with given header exists
     */
    public boolean hasColumn(String header)
    {
        return getIndex(header) != -1;
    }


    /**
     * Parses the given CSV file.
     * Possible results:
     * - data remains null, parseable is true => file doesn't exist
     * - data non-null, parseable is false    => file couldn't be parsed
     * - data non-null, parseable is true     => all ok.
     * 
     * @param file
     *            the file
     */
    private void parseFile(File file)
    {
        if (file == null || !file.exists()) // No such file.
            return;
        else if (!file.canRead() || !file.isFile()) // Something's wrong.
        {
            parseable = false;
            return;
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            if (line == null)
            {
                // If this happens, we have an empty.  Not good!  Flag it,
                // close the reader and return. 
                parseable = false;
                reader.close();
                return;
            }
            String[] values = line.split(",");
            headers = new Vector<String>();
            for (int i = 0; i < values.length; i++)
            {
                String current = values[i].trim();
                if (current == null || current.length() == 0) continue;
                headers.add(current);
            }

            Vector<double[]> rows = new Vector<double[]>();

            while ((line = reader.readLine()) != null)
            {
                addRow(rows, headers.size(), line);
            }

            double[][] t = new double[1][0];
            data = rows.toArray(t);
            reader.close();

        }
        catch (Exception e)
        {
            parseable = false;
            e.printStackTrace();
            return;
        }

        // Check if any of the values are NaN or infinity, and mark this
        // result set appropriately.  We do this here so that callers don't
        // have to keep testing the values themselves.

        int numRows = data.length;
        int numColumns = data[0].length;
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numColumns; col++)
                if (Double.isNaN(data[row][col]) || Double.isInfinite(data[row][col]))
                {
                    hasInfinityOrNaN = true;
                    return;
                }
    }


    /**
     * Set the data
     * 
     * @param data
     *            the data to set
     */
    public void setData(double[][] data)
    {
        this.data = data;
    }


    /**
     * Set the headers
     * 
     * @param headers
     *            the headers
     */
    public void setHeaders(Vector<String> headers)
    {
        this.headers = headers;
    }


    /**
     * @return true if there lurks a NaN or infinity value in the data.
     */
    public boolean hasInfinityOrNaN()
    {
        return hasInfinityOrNaN;
    }

   
    public boolean parseable()
    {
        return parseable;
    }
}
