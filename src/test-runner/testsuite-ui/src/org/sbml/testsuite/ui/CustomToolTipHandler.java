//
// @file   CustomToolTipHandler.java
// @brief  Custom tool-tip handler, for SWT controls that don't have tool tips.
// @author Michael Hucka
// @date   2013-11-14 <mhucka@caltech.edu>
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
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

/*
 * Parts of this code are based on 
 * http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/HowtoimplementhoverhelpfeedbackusingtheMouseTrackListener.htm
 * and on http://stackoverflow.com/questions/1351245/setting-swt-tooltip-delays
 * The rest of the code is original.
 */
public class CustomToolTipHandler
{
    private Shell tipShell;
    private Widget tipWidget;         // Widget this tooltip is hovering over.
    private Point tipPosition;        // The position being hovered over.
    private StyledText tipText;            // The text displayed in the tool tip.
    private Color black;

    final static int DEFAULT_SHOW_DELAY = 100; // Time delay to show tooltip.
    final static int DEFAULT_HIDE_DELAY = 200; // Time delay to hide tooltip.


    public CustomToolTipHandler(Shell parent)
    {
        final Display display = parent.getDisplay();

        tipShell = new Shell(parent, SWT.ON_TOP | SWT.TOOL);
        tipShell.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));

        RowLayout layout = new RowLayout(SWT.VERTICAL);
        layout.marginWidth = 0;
        layout.marginBottom = 5;        // Looks unbalanced otherwise.
        layout.fill = true;
        layout.pack = true;
        layout.center = true;

        tipShell.setLayout(layout);

        RowData layoutData = new RowData();
        layoutData.width = 300;

        tipText = new StyledText(tipShell, SWT.WRAP);
        tipText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        tipText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        tipText.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -1));
        tipText.setEditable(false);
        tipText.setLayoutData(layoutData);

        tipShell.pack();
        tipShell.layout();

        black = display.getSystemColor(SWT.COLOR_BLACK);
    }


    public void activateHoverHelp(final Control control)
    {
        /*
         * Get out of the way if we attempt to activate the control
         * underneath the tooltip
         */
        control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e)
            {
                if (tipShell.isVisible())
                    tipShell.setVisible(false);
            }
        });


        control.addListener(SWT.MouseHover, new Listener() {
            public void handleEvent(final Event event)
            {
                if (event.widget == null) return;
                final Display display = event.widget.getDisplay();
                display.timerExec(DEFAULT_SHOW_DELAY, new Runnable() {
                    public void run()
                    {
                        mouseHover(control, event);
                    }
                });
            }
        });


        control.addListener(SWT.MouseExit, new Listener() {
            public void handleEvent(final Event event)
            {
                if (event.widget == null) return;
                final Display display = event.widget.getDisplay();
                display.timerExec(DEFAULT_HIDE_DELAY, new Runnable() {
                    public void run()
                    {
                        if (tipShell.isVisible())
                            tipShell.setVisible(false);
                        tipWidget = null;
                    }
                });
            }
        });
    }


    /**
     * Sets the location for a hovering shell.
     *
     * @param shell
     *            the object that is to hover
     * @param position
     *            the position of a widget to hover over
     * @return the top-left location for a hovering box
     */
    private void setHoverLocation(Shell shell, Point position)
    {
        Rectangle displayBounds = shell.getDisplay().getBounds();
        Rectangle shellBounds = shell.getBounds();
        shellBounds.x = Math.max(Math.min(position.x,
                                          displayBounds.width
                                          - shellBounds.width), 0);
        shellBounds.y = Math.max(Math.min(position.y + 16,
                                          displayBounds.height
                                          - shellBounds.height), 0);
        shell.setBounds(shellBounds);
    }


    /**
     * Show the tool tip window when the mouse hovers.
     */
    private void mouseHover(final Control control, Event event)
    {
        Point point = new Point(event.x, event.y);
        Widget widget = event.widget;

        if (widget instanceof ToolBar)
        {
            ToolBar w = (ToolBar) widget;
            widget = w.getItem(point);
        }

        if (widget instanceof Table)
        {
            Table w = (Table) widget;
            widget = w.getItem(point);
        }

        if (widget instanceof Tree)
        {
            Tree w = (Tree) widget;
            widget = w.getItem(point);
        }

        if (widget == null)
        {
            tipShell.setVisible(false);
            tipWidget = null;
            return;
        }

        if (widget == tipWidget)
            return;

        tipWidget = widget;
        tipPosition = control.toDisplay(point);

        String text = (String) widget.getData("TIP_TEXT");
        if (text != null && text.length() > 0) // Don't show if text = "".
        {
            String labelText = (String) widget.getData("TIP_TEXT_LABEL");
            if (labelText != null && labelText.length() > 0)
            {
                StyleRange styleRange = new StyleRange();
                styleRange.start = 0;
                styleRange.length = labelText.length();
                styleRange.fontStyle = SWT.BOLD | SWT.ITALIC;
                styleRange.foreground = black;
                tipText.setText(labelText + text);
                tipText.setStyleRange(styleRange);
            }
            else
            {
                tipText.setText(text);
            }

            tipShell.pack();
            setHoverLocation(tipShell, tipPosition);
            tipShell.setVisible(true);
        }
    }
}
