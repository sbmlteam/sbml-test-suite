//
// @file   PlotColorGenerator.java
// @brief  Class to encapsulate the colors used in our plots
// @author Michael Hucka
// @@date  Created 2013-01-14 <mhucka@caltech.edu>
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

package org.sbml.testsuite.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;


/**
 * Simple class to encapsulate the list of colors we use in our plots.  It
 * returns a new color to use from an internal list of predetermined colors,
 * and cycles back to the beginning of the list if a given plot needs more
 * colors than are in the list.  Every plot that uses this should always call
 * reset() to restart the colors from the beginning of the list.
 *
 * I created the colors by combining 3 sources, then swapping colors
 * repeatedly until I found the color sequence and juxtapositions
 * attractive (subjectively) and distinguishable:
 *
 * 1) A color scheme I created using the excellent tool colorschemedesigner.com
 *    http://colorschemedesigner.com/#3K40zllqaTeSj
 *
 * 2) A blog post, the author of which I can't figure out, at the following
 *    page (this is also where I first learned of colorschemedesigner.com):
 *    http://guidolan.blogspot.com/2010/03/how-to-create-beautiful-gnuplot-graphs.html
 *
 * 3) The colors from the Highcharts JS library.
 */
public class PlotColorGenerator
{
    private final static int[][] colorValues = { 
        {  6,  19,  54}, 
        { 75, 105, 170},
        {153, 179, 234}, 
        {207, 218, 239}, 
        { 47, 108,  61},
        {166, 235, 181},
        {170,  83, 100}, 
        { 55,  88,  63}, 
        {117,  77,  73}, 
        {  2,  61,  15}, 
        { 80,  11,   3},
        { 52,  60,  78},
        {150, 242, 170}, 
        {249, 163, 155}, 
        {206, 242, 214}, 
        {249, 215, 212},
        {58,   81, 140},
        {180, 124, 174},
        {226, 206, 113},
        {114, 151, 230},
        {129, 132, 119},
        {138, 198,   0},
        {155, 191, 171},
        {222, 219,  48},
        { 25,  91, 139},
        {128, 105, 155},
        {219, 132,  61},
        {122, 160, 103},
        {101, 111, 140},
        {137, 165,  78},
        { 69, 114, 167},
        {170,  70,  67},
        {232, 105,  70},
        {90,  154, 112},
        {146, 168, 205},
        {104, 156, 226},
        { 61, 150, 174},
        {249, 183, 176},
        { 65,  54,  89},
        {169, 189, 230},
        {181, 202, 146},
        {249, 224, 176},
        { 53, 150,  76},
        {249, 122, 109},
        {164, 125, 124},
        {103, 235, 132},
        {249, 201, 109},
        {230,  43,  23},
        { 29,  69, 153},
        { 17, 173,  52},
        {230, 159,  23},
        {143,  70,  63},
        { 47,  63,  96},
        {143, 116,  63},
        {109,  13,   3},
        {  3,  26,  73},
        {  2,  82,  20},
        {109,  73,   3},
    };
    private static Color[] colors = new Color[colorValues.length];
    private static int index = -1;


    public static Color nextColor()
    {
        incrementIndex();
        if (colors[index] == null)
            colors[index] = UIUtils.createColor(nextR(), nextG(), nextB());
        return colors[index];
    }


    public static RGB nextRGB()
    {
        incrementIndex();
        return new RGB(nextR(), nextG(), nextB());
    }


    public static void reset()
    {
        index = -1;
    }


    private final static void incrementIndex()
    {
        index = ++index % colorValues.length;
    }


    private final static int nextR()
    {
        return colorValues[index][0];
    }


    private final static int nextG()
    {
        return colorValues[index][1];
    }


    private final static int nextB()
    {
        return colorValues[index][2];
    }

}
