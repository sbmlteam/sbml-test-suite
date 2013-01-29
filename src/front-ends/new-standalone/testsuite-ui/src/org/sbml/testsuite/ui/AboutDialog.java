//
// @file   AboutDialog.java
// @brief  About dialog for the SBML Test Runner
// @author Michael Hucka
// @author Frank T. Bergmann
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * The "About" dialog for the SBML Test Suite Test Runner.
 */
public class AboutDialog
    extends Dialog
{
    protected Object result;
    protected Shell  shell;


    /**
     * Constructor to create the dialog.
     * 
     * @param parent
     * @param style
     */
    public AboutDialog(Shell parent, int style)
    {
        super(parent, style);
        setText("About the SBML Test Runner");
        createContents();
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shell == null) return;

        Point dialogSize = shell.getSize();
        shell.setLocation(shellBounds.x
            + (shellBounds.width - dialogSize.x) / 2, shellBounds.y
            + (shellBounds.height - dialogSize.y) / 2);
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        // This next value is 5 when dpi = 96, and 0 when dpi = 72.
        int offset = 20 - UIUtils.scaledFontSize(20);

        shell = new Shell(getParent(), SWT.DIALOG_TRIM);
        shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        shell.setSize(450, 380);
        shell.setText("About the SBML Test Runner");
        shell.setLayout(null);
        shell.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) { shell.dispose(); }
        });
        shell.setFocus();
        shell.addKeyListener(UIUtils.createCloseKeyListener(shell));
        shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));
        
        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setImage(UIUtils.getImageResource("icon_128x128.png"));
        lblNewLabel.setBounds(8 + offset, 10, 139, 138);
        
        Label lblNewLabel_1 = new Label(shell, SWT.NONE);
        lblNewLabel_1.setFont(UIUtils.getFont("Verdana", 20, SWT.BOLD));
        lblNewLabel_1.setBounds(158, 7 + offset, 270, 25);
        lblNewLabel_1.setText("The SBML Test Runner");
        
        Label lblVersion = new Label(shell, SWT.RIGHT);
        lblVersion.setAlignment(SWT.LEFT);
        lblVersion.setFont(UIUtils.getFont("Verdana", 11, SWT.ITALIC));
        lblVersion.setBounds(158, 38, 126, 14);
        lblVersion.setText("Version: " + Program.getVersion());
        
        Label lblTheSbmlTest = new Label(shell, SWT.WRAP);
        lblTheSbmlTest.setBounds(16, 154 - offset, 418, 67);
        lblTheSbmlTest.setText("The SBML Test Suite is a system for testing the degree and correctness of SBML support in a given SBML-compatible software program. The SBML Test Runner drives an application to perform each test in the Test Suite, and displays the results and conclusions.");
        lblTheSbmlTest.setFont(UIUtils.getFont("Verdana", 10, SWT.NORMAL));
        
        Label sep2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep2.setBounds(16, 225 - 2*offset, 417, 2);
        
        Label lblNewLabel_2 = new Label(shell, SWT.NONE);
        lblNewLabel_2.setFont(UIUtils.getFont("Verdana", 10, SWT.BOLD));
        lblNewLabel_2.setBounds(158, 67 - offset, 270, 28);
        if (UIUtils.isMacOSX())
            lblNewLabel_2.setText("Authors: Frank T. Bergmann and Michael Hucka.");
        else
            lblNewLabel_2.setText("Authors: Frank T. Bergmann and\nMichael Hucka.");
        
        Label lblPartOfThe = new Label(shell, SWT.WRAP);
        lblPartOfThe.setText("Part of the SBML Test Suite, written by Sarah Keating, Lucian Smith, Frank Bergmann, Kimberley Begley and Michael Hucka.");
        lblPartOfThe.setFont(UIUtils.getFont("Verdana", 10, SWT.NORMAL));
        lblPartOfThe.setBounds(158, 93 + offset, 270, 49);
        
        Label lblNewLabel_3 = new Label(shell, SWT.WRAP);
        lblNewLabel_3.setFont(UIUtils.getFont("Verdana", 10, SWT.ITALIC));
        lblNewLabel_3.setBounds(16, 235 - offset, 418, 30);
        lblNewLabel_3.setText("For more information about this and other SBML Team software, as well as about SBML itself, please visit the following website:");
        
        StyleRange styleRange = new StyleRange();
        styleRange.start = 0;
        styleRange.length = 15;
        styleRange.underline = true;
        final StyledText lblHttpsbmlorg = new StyledText(shell, SWT.CENTER);
        lblHttpsbmlorg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        lblHttpsbmlorg.setEditable(false);
        lblHttpsbmlorg.setToolTipText("Click to visit SBML.org.");
        lblHttpsbmlorg.setForeground(SWTResourceManager.getColor(65, 105, 225));
        lblHttpsbmlorg.setFont(UIUtils.getFont("Verdana", 11, SWT.BOLD));
        lblHttpsbmlorg.setBounds(173 - offset, 274, 103 + 3*offset, 20 + offset);
        lblHttpsbmlorg.setText("http://sbml.org");
        lblHttpsbmlorg.setStyleRange(styleRange);
        final Cursor handCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_HAND);
        lblHttpsbmlorg.addMouseListener(new MouseListener() {
                public void mouseDown(MouseEvent arg0)
                {
                    org.eclipse.swt.program.Program.launch("http://sbml.org");
                }
                public void mouseUp(MouseEvent arg0) { return; }
                public void mouseDoubleClick(MouseEvent arg0) { return; }
            });
        lblHttpsbmlorg.addListener(SWT.MouseEnter, new Listener() {
                public void handleEvent(Event e)
                {
                    lblHttpsbmlorg.setCursor(handCursor);
                }
            });
        lblHttpsbmlorg.addListener(SWT.MouseExit, new Listener() {
                public void handleEvent(Event e)
                {
                    lblHttpsbmlorg.setCursor(null);
                }
            });
        
        Label lblNewLabel_4 = new Label(shell, SWT.NONE);
        lblNewLabel_4.setImage(UIUtils.getImageResource("SBML.png"));
        lblNewLabel_4.setBounds(175, 300, 99, 40);
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public void open()
    {
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }
}
