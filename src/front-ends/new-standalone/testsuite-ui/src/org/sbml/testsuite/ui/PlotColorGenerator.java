//
// @file   PlotColorGenerator.java
// @brief  Class to encapsulate the colors used in our plots
// @author Michael Hucka
// @@date  Created 2013-01-14 <mhucka@caltech.edu>
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

package org.sbml.testsuite.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * Simple class to encapsulate the list of colors we use in our plots.  It
 * returns a new color to use from an internal list of predetermined colors,
 * and cycles back to the beginning of the list if a given plot needs more
 * colors than are in the list.  Every plot that uses this should always call
 * reset() to restart the colors from the beginning of the list.
 */
public class PlotColorGenerator
{
    private final static int[][] colors = { {146, 168, 205},
                                            {170,  70,  67},
                                            {137, 165,  78},
                                            {128, 105, 155},
                                            { 69, 114, 167},
                                            {219, 132,  61},
                                            { 61, 150, 174},
                                            {164, 125, 124},
                                            {181, 202, 146},
                                            {249, 183, 176},
                                            {169, 189, 230},
                                            {166, 235, 181},
                                            {249, 224, 176},
                                            {249, 122, 109},
                                            {114, 151, 230},
                                            {103, 235, 132},
                                            {249, 201, 109},
                                            {230,  43,  23},
                                            { 29,  69, 153},
                                            { 17, 173,  52},
                                            {230, 159,  23},
                                            {143,  70,  63},
                                            { 47,  63,  96},
                                            { 47, 108,  61},
                                            {143, 116,  63},
                                            {109,  13,   3},
                                            {  3,  26,  73},
                                            {  2,  82,  20},
                                            {109,  73,   3} };
    private static int index = 0;


    public static Color nextColor()
    {
        incrementIndex();
        return new Color(Display.getCurrent(), nextR(), nextG(), nextB());
    }


    public static RGB nextRGB()
    {
        incrementIndex();
        return new RGB(nextR(), nextG(), nextB());
    }


    public static void reset()
    {
        index = 0;
    }


    private final static void incrementIndex()
    {
        index = ++index % colors.length;
    }


    private final static int nextR()
    {
        return colors[index][0];
    }


    private final static int nextG()
    {
        return colors[index][1];
    }


    private final static int nextB()
    {
        return colors[index][2];
    }

}
