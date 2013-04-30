//
// @file ResultSet.java
// @brief Class holding CSV data
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

/**
 * Class holding CSV data
 */
public class ResultSet
{
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
     * Computes a result set with absolute differences between two results
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

        double[][] temp = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++)
        {
            temp[i][0] = a.getData()[i][0];
            for (int j = 1; j < numCols; j++)
            {
                temp[i][j] = Math.abs(a.getData()[i][j] - b.getData()[i][j]);
            }
        }

        Vector<String> heads = (a.getHeaders().size() > b.getHeaders().size() ? b.getHeaders() : a.getHeaders());
        return new ResultSet(heads, temp);
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
        if (file == null || ! file.exists()) return null;
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
            if (string.trim().toUpperCase() == "INF")
                return Double.POSITIVE_INFINITY;
            if (string.trim().toUpperCase() == "-INF")
                return Double.NEGATIVE_INFINITY;
            if (string.trim().toUpperCase() == "NAN") return Double.NaN;
            return Double.parseDouble(string);
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private Vector<String> headers;

    private double[][]     data;


    /**
     * Default constructor
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
     * parses the given CSV file
     * 
     * @param file
     *            the file
     */
    private void parseFile(File file)
    {
        if (file == null || ! file.exists()) return;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            if (line == null)
            {

                // if this happens we have an empty file present, this is not
                // good! The best we can do at this point is to close the reader
                // and return.
                // that will flag the file as invalid later on.
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
            e.printStackTrace();
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
}
