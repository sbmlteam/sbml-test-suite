//
// @file   ResultColor.java
// @brief  Class to encapsulate the colors we use for result indicators.
// @author Michael Hucka
// @@date  Created 2013-01-13 <mhucka@caltech.edu>
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.sbml.testsuite.core.*;


public enum ResultColor
{
    green  ( "green",  90,  200,  50, ResultType.Match),
    red    ( "red",    255,  50,  40, ResultType.NoMatch),
    black  ( "black",  70,   70,  70, ResultType.Error),
    gray   ( "gray",   200, 200, 200, ResultType.Unknown),
    blue   ( "blue",   110, 140, 210, null),
    yellow ( "yellow", 252, 175,  55, ResultType.CannotSolve);

    private final String name;
    private final int r;
    private final int g;
    private final int b;
    private final ResultType resultType;
    private final Image image;
    
    private final static int DEFAULT_IMAGE_SIZE = 12; 

    ResultColor(String name, int red, int green, int blue, ResultType type)
    {
        this.name = name;
        this.r = red;
        this.g = green;
        this.b = blue;
        this.resultType = type;
        this.image = createImage(DEFAULT_IMAGE_SIZE);
    }

    public String     getName()       { return name; }
    public int        getRed()        { return r; }
    public int        getGreen()      { return g; }
    public int        getBlue()       { return b; }
    public RGB        getRGB()        { return new RGB(r, g, b); }
    public ResultType getResultType() { return resultType; } 
    public Image      getImage()      { return image; }

    public Image getImage(int imageSize)
    {
        if (imageSize == DEFAULT_IMAGE_SIZE)
            return image;
        else
            return createImage(imageSize);
    }

    public static Image getImageForResultType(ResultType type, int imageSize)
    {
        for (ResultColor c : ResultColor.values())
            if (c.resultType == type)
                return c.getImage(imageSize);
        return null;
    }
    
    public static Image getImageForResultType(ResultType type)
    {
        return getImageForResultType(type, DEFAULT_IMAGE_SIZE);
    }

    public Color getColor()
    {
        return new Color(Display.getCurrent(), r, g, b);
    }

    private Image createImage(int imageSize)
    {
        Image image = new Image(Display.getDefault(), imageSize, imageSize);
        GC gc = new GC(image);
        gc.setAntialias(SWT.ON);
        gc.setBackground(getColor());
        gc.setForeground(getColor());
        gc.fillRoundRectangle(0, 0, imageSize, imageSize, 5, 5);
        gc.dispose();
        return image;
    }
}
