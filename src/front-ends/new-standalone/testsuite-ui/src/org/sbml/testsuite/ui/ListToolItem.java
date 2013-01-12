//
// @file   ListToolItem.java
// @brief  Home-grown custom toolbar item to replace SWT's ToolItem
// @author Michael Hucka
// @date   2013-01-10 <mhucka@caltech.edu>
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

import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class ListToolItem
    extends ToolItem
{
    static int MIN_WIDTH_NUM_CHARS = 30;

    private static Color gradientStartColor;
    private static Color gradientStopColor;
    private static Color borderColor;
    private static Color fontColor;

    private String  buttonText;
    private Display display;
    
    private static int RIGHT_PADDING_AMOUNT = 10;
    private boolean depressed = false;


    @Override protected void checkSubclass() { }    
    

    public ListToolItem(ToolBar toolbar, String initialText)
    {
        super(toolbar, SWT.DROP_DOWN);
        setText(initialText);           // Our setText(), not ToolItem's.
        toolbar.addListener(SWT.Paint, new Listener() {
                // This gets called repeatedly when the panel is being resized.
                public void handleEvent(Event e)
                {
                    repaintButton(e);
                }
            });
        toolbar.addListener(SWT.MouseDown, new Listener() {
                public void handleEvent(Event e)
                {
                    if (eventIsWithinBounds(e))
                    {
                        depressed = true;
                        notifyListeners(SWT.Selection, transmutedEvent(e));
                    }
                }
            });
        toolbar.addListener(SWT.MouseUp, new Listener() {
                public void handleEvent(Event e)
                {
                    if (eventIsWithinBounds(e))
                    {
                        notifyListeners(SWT.Selection, transmutedEvent(e));
                    }
                }
            });
        // Note: this next listener is attached to us, not the toolbar.
        addListener(SWT.MouseUp, new Listener() {
                public void handleEvent(Event e)
                {
                    if (! eventIsWithinBounds(e))
                    {
                        depressed = false;
                        repaintButton(e);
                    }
                }
            });
        toolbar.addListener(SWT.MouseDoubleClick, new Listener() {
                public void handleEvent(Event e)
                {
                    if (eventIsWithinBounds(e))
                        depressed = false;
                }
            });

        toolbar.addListener(SWT.MouseMove, new Listener() {
                public void handleEvent(Event e)
                {
                    if (! eventIsWithinBounds(e))
                        depressed = false;
                }
            });

        // Cache some values so we don't have to keep getting them when
        // listeners are invoked.

        display            = toolbar.getDisplay();
        gradientStartColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
        gradientStopColor  = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        borderColor        = display.getSystemColor(SWT.COLOR_DARK_GRAY);
        fontColor          = display.getSystemColor(SWT.COLOR_BLACK);
    }


    public ListToolItem(ToolBar toolbar, String initialText, int minCharWidth)
    {
        this(toolbar, initialText);
        MIN_WIDTH_NUM_CHARS = minCharWidth;
    }


    @Override
    public String getText()
    {
        return buttonText;
    }


    /*
     * We don't use the ToolItem setText() method to write the label because
     * we need to draw the widget shape and the text on top of that.
     * Instead, we store the true text in an internal field, and write spaces
     * in the string handed to ToolItem.setText() so that it draws the arrow
     * in the right place (which is to say, to the right of the text we do
     * paint on the widget).
     *
     * The padding used in the calculation was determined empirically; I
     * can't quite explain why it needs to be as much as it is, but that's
     * what works best on Mac OS X.
     *
     * The minimum width on the widget helps keep it from being
     * unattractively short when the text itself is short, and brings some
     * regularity to the size of the widget across changes to the text.
     */
    @Override
    public void setText(String string)
    {
        buttonText = string;

        int len = string.length();
        int numChars = (len < MIN_WIDTH_NUM_CHARS) ? MIN_WIDTH_NUM_CHARS : len;

        char[] charArray = new char[numChars + RIGHT_PADDING_AMOUNT];
        Arrays.fill(charArray, ' ');
        super.setText(new String(charArray));
        
        // If we changed the text, then it means the widget should be
        // reverted back to its normal appearance.
        depressed = false;
    }

    
    private final void repaintButton(Event e)
    {
        if (e == null || e.gc == null) 
            return;

        if (depressed)
        {
            e.gc.setForeground(gradientStopColor);
            e.gc.setBackground(gradientStartColor);
        }
        else
        {
            e.gc.setForeground(gradientStartColor);
            e.gc.setBackground(gradientStopColor);
        }
        Rectangle r = getBounds();
        e.gc.fillGradientRectangle(r.x - 10 , r.y + 3,
                                   r.width + 9, r.height - 13, true);
        e.gc.setForeground(borderColor);
        e.gc.drawRoundRectangle(r.x - 11 , r.y + 2,
                                r.width + 10, r.height - 12, 7, 7);
        e.gc.setForeground(fontColor);
        e.gc.drawText(buttonText, r.x - 6, r.y + 4, true);
        getParent().redraw();   // This gets rid of cheese left after moves.
    }

    
    private final boolean eventIsWithinBounds(Event e)
    {
        final Rectangle r = getBounds();
        return e.x >= r.x && e.x <= (r.x + r.width + 10)
            && e.y >= r.y && e.y <= (r.y + r.height);
    }


    /*
     * The stupid drop-down scheme for ToolItem only invokes the drop-down
     * menu if you click on the tiny triangle/arrow on the right-hand side.
     * I find that infuriating.  I wanted to make the ToolItem respond to
     * clicks anywhere in the widget, much like some Mac OS X toolbar widgets
     * in applications like Numbers.  The problem then becomes that a mouse
     * click outside of that triangle/arrow on the ToolItem doesn't invoke
     * the menu.  So, what the code below does is take an event from anywhere
     * in the widget and creates a fake SWT.ARROW event with fake x,y
     * coordinates.  The result can be passed to ToolItem's normal
     * mouseup/down listeners via a notifyListeners() call.
     */
    private final Event transmutedEvent(Event e)
    {
        // I couldn't get translated events to work by merely changing the
        // x,y coordinate of the event and handing that to the
        // notifyListeners() call.  I'm also not sure whether all the fields
        // need to be set; the list below is probably not the minimum set,
        // but I didn't want to waste time investigating what was.

        Event event     = new Event();
        event.detail    = SWT.ARROW;
        event.button    = e.button;
        event.count     = e.count;
        event.stateMask = e.stateMask;
        event.time      = e.time;
        event.data      = e.data;
        event.widget    = e.widget;
        Rectangle r     = getBounds();
        event.x         = r.x + r.width - 6;
        event.y         = r.y;
        return event;
    }

}
