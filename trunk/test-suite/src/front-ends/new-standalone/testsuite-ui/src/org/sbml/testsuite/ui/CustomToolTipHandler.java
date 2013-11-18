//
// @file   CustomToolTipHandler.java
// @brief  Custom tool-tip handler, for SWT controls that don't have tool tips.
// @author Michael Hucka
// @date   2013-11-14 <mhucka@caltech.edu>
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

import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;

public class CustomToolTipHandler
{
    private Shell parentShell;
    private Shell tipShell;
    private Widget tipWidget;         // Widget this tooltip is hovering over.
    private Point tipPosition;        // The position being hovered over.
    private Label tipText;            // The text displayed in the tool tip.


    public CustomToolTipHandler(Shell parent)
    {
        this.parentShell = parent;
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

        tipText = new Label(tipShell, SWT.WRAP);
        tipText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        tipText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        tipText.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, -1));
        tipText.setLayoutData(layoutData);

        tipShell.pack();
        tipShell.layout();
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

        /*
         * Trap hover events to pop-up the tooltip.
         */
        control.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent e)
            {
                if (tipShell.isVisible())
                    tipShell.setVisible(false);
                tipWidget = null;
            }


            public void mouseHover(MouseEvent event)
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
                tipText.setText(text != null ? text : "");
                tipShell.pack();
                setHoverLocation(tipShell, tipPosition);
                tipShell.setVisible(true);
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
}
