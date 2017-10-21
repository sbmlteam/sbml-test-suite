//
// @file   ResultColor.java
// @brief  Class to encapsulate the colors we use for result indicators.
// @author Michael Hucka
// @date   Created 2013-01-13 <mhucka@caltech.edu>
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.*;


public enum ResultColor
{
    green  ( "green",  90,  200,  50, ResultType.Match),
    red    ( "red",    240,  30,  20, ResultType.NoMatch),
    yellow ( "yellow", 252, 175,  55, ResultType.CannotSolve),
    blue   ( "blue",   100, 130, 200, ResultType.Unsupported),
    gray   ( "gray",   195, 195, 195, ResultType.Unknown),
    white  ( "white",  255, 255, 255, ResultType.Unavailable),
    black  ( "black",  30,   30,  30, ResultType.Error);
    
    private final String name;
    private final int r;
    private final int g;
    private final int b;
    private final ResultType resultType;
    private final Image image;
    private final Color color;
    
    private final static int DEFAULT_IMAGE_SIZE = 12; 

    ResultColor(String name, int red, int green, int blue, ResultType type)
    {
        this.name = name;
        this.r = red;
        this.g = green;
        this.b = blue;
        this.resultType = type;
        this.color = UIUtils.createColor(r, g, b);
        this.image = createImage(DEFAULT_IMAGE_SIZE);
    }

    public String     getName()       { return name; }
    public int        getRed()        { return r; }
    public int        getGreen()      { return g; }
    public int        getBlue()       { return b; }
    public RGB        getRGB()        { return new RGB(r, g, b); }
    public ResultType getResultType() { return resultType; } 
    public Image      getImage()      { return image; }
    public Color      getColor()      { return color; }

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

    public static Color getColorForResultType(ResultType type)
    {
        for (ResultColor c : ResultColor.values())
            if (c.resultType == type)
                return c.getColor();
        return null;
    }

    public static ResultType getResultTypeForColor(Color color)
    {
        for (ResultColor c : ResultColor.values())
            if (c.color == color)
                return c.getResultType();
        return null;
    }

    private Image createImage(int imageSize)
    {
        final Color white = SWTResourceManager.getColor(SWT.COLOR_WHITE);

        final Display display = Display.findDisplay(Thread.currentThread());
        Image image = new Image(display, imageSize, imageSize);
        GC gc = new GC(image);
        gc.setAntialias(SWT.ON);      // It's the default on Macs, but not Win.
        gc.setBackground(getColor());
        gc.fillRoundRectangle(0, 0, imageSize, imageSize, 5, 5);

        // Due to antialiasing, the corners are not pure white.  This screws
        // up our ability to set transparency.  The following deliberately
        // paints the corner pixels white so that we can then set them to
        // transparent.  (This of course assumes that the background where
        // this is being used is in fact white.)

        gc.setBackground(white);
        gc.fillRectangle(0, 0, 1, 1);
        gc.fillRectangle(0, imageSize - 1, 1, 1);
        gc.fillRectangle(imageSize - 1, 0, 1, 1);
        gc.fillRectangle(imageSize - 1, imageSize - 1, 1, 1);
        gc.dispose();

        ImageData data = image.getImageData();
        data.transparentPixel = data.palette.getPixel(white.getRGB());

        final Image finalImage = new Image(display, data);
        image.dispose();
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                finalImage.dispose();
            }
        });
        return finalImage;
    }
}
