//
// @file   AboutDialog.java
// @brief  About dialog for the SBML Test Runner
// @author Michael Hucka
// @author Frank T. Bergmann
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * The "About" dialog for the SBML Test Suite Test Runner.
 */
public class AboutDialog
    extends Dialog
{
    // This next value is 5 when dpi = 96, and 0 when dpi = 72.
    final int offset = 20 - UIUtils.scaledFontSize(20);
    final Cursor handCursor = UIUtils.createCursor(SWT.CURSOR_HAND);
    Object result;
    Shell  shell;
    Date   casesDate;

    /**
     * Constructor to create the dialog.
     *
     * @param parent
     * @param style
     */
    public AboutDialog(Shell parent, int style)
    {
        super(parent, style);
    }


    /**
     * Set information that we display in the dialog.
     *
     * @param casesReleaseDate
     *            the release date of the test cases being used.
     */
    public void setInfo(Date casesReleaseDate)
    {
        casesDate = casesReleaseDate;
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
        shell = new Shell(getParent(), SWT.DIALOG_TRIM);
        shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        shell.setSize(450, 485);
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
        lblNewLabel_1.setFont(UIUtils.createResizedFont("Verdana", SWT.BOLD, 6));
        lblNewLabel_1.setBounds(158, 7 + offset, 270, 25);
        lblNewLabel_1.setText("The SBML Test Runner");

        Label lblVersion = new Label(shell, SWT.RIGHT);
        lblVersion.setAlignment(SWT.LEFT);

        if (UIUtils.isMacOSX())
            lblVersion.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, -1));
        else
            lblVersion.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, 0));
        lblVersion.setBounds(158, 38, 300, 14);
        lblVersion.setText("Version: " + Program.getVersion());

        if (casesDate != null)
        {
            Label lblCasesDate = new Label(shell, SWT.RIGHT);
            lblCasesDate.setAlignment(SWT.LEFT);
            if (UIUtils.isMacOSX())
                lblCasesDate.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, -1));
            else
                lblCasesDate.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, 0));
            lblCasesDate.setBounds(158, 53, 300, 16);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = formatter.format(casesDate);
            lblCasesDate.setText("Test cases release date: " + dateStr);
        }

        Label lblTheSbmlTest = new Label(shell, SWT.WRAP);
        lblTheSbmlTest.setBounds(16, 154 - offset + 2, 418, 67);
        lblTheSbmlTest.setText("The SBML Test Suite is a system for testing the degree and correctness of SBML support in a given SBML-compatible software program. The SBML Test Runner drives an application to perform each test in the Test Suite, and displays the results and conclusions.");
        if (UIUtils.isMacOSX())
            lblTheSbmlTest.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -2));
        else
            lblTheSbmlTest.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -1));

        Label sep2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep2.setBounds(16, 225 - 3*offset, 417, 2);
        sep2.moveAbove(null);

        Label lblNewLabel_2 = new Label(shell, SWT.NONE);
        if (UIUtils.isMacOSX())
            lblNewLabel_2.setFont(UIUtils.createResizedFont("SansSerif", SWT.BOLD, -2));
        else
            lblNewLabel_2.setFont(UIUtils.createResizedFont("SansSerif", SWT.BOLD, -1));
        lblNewLabel_2.setBounds(158, 78 - offset, 270, 20);
        lblNewLabel_2.setText("Authors: Frank T. Bergmann && Michael Hucka.");

        Label lblPartOfThe = new Label(shell, SWT.WRAP);
        lblPartOfThe.setText("Part of the SBML Test Suite by S.M. Keating, M. Hucka, L.P. Smith, F.T. Bergmann, B.E. Shapiro, T.W. Evans, C.S. Gillespie, D.J. Wilkinson, B.G. Olivier and A.M. Finney.");
        if (UIUtils.isMacOSX())
            lblPartOfThe.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -3));
        else
            lblPartOfThe.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -2));
        lblPartOfThe.setBounds(158, 92 + offset, 270, 49);

        Label lblNewLabel_3 = new Label(shell, SWT.WRAP);
        if (UIUtils.isMacOSX())
            lblNewLabel_3.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, -2));
        else
            lblNewLabel_3.setFont(UIUtils.createResizedFont("Verdana", SWT.ITALIC, -1));
        lblNewLabel_3.setBounds(16, 235 - 2*offset, 418, 30);
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
        if (UIUtils.isMacOSX())
            lblHttpsbmlorg.setFont(UIUtils.createResizedFont("Verdana", SWT.BOLD, -1));
        else
            lblHttpsbmlorg.setFont(UIUtils.createResizedFont("Verdana", SWT.BOLD, 0));
        lblHttpsbmlorg.setBounds(173 - offset, 274, 103 + 3*offset, 20 + offset);
        lblHttpsbmlorg.setText("http://sbml.org");
        lblHttpsbmlorg.setStyleRange(styleRange);

        lblHttpsbmlorg.addMouseListener(new MouseListener() {
                public void mouseDown(MouseEvent arg0)
                {
                    org.eclipse.swt.program.Program.launch("http://sbml.org/SBML_Projects/SBML_Test_Suite");
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
        lblNewLabel_4.setBounds(175, 300, 99, 44);

        Label sep3 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep3.setBounds(16, 358 - offset, 417, 2);
        sep3.moveAbove(null);

        Label lblAck = new Label(shell, SWT.LEFT);
        lblAck.setText("Acknowledgements:");
        lblAck.setBounds(16, 366, 140, 14 + offset);
        if (UIUtils.isMacOSX())
            lblAck.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -2));
        else
            lblAck.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -1));

        addAcknowledgement(400, "SWT GUI widgets: Opal Widgets",
                           "http://code.google.com/a/eclipselabs.org/p/opal/");
        addAcknowledgement(384, "Icons: Icons8",
                           "http://icons8.com");
        addAcknowledgement(416, "XML persistence layer: Simple XML Framework",
                           "http://simple.sourceforge.net");
        addAcknowledgement(432, "Plot widget: SWTChart",
                           "http://www.swtchart.org");

        final MouseListener closeListener = new MouseAdapter() {
                @Override
                public void mouseDown(MouseEvent e)
                {
                    shell.close();
                }
            };
        UIUtils.addMouseListenerRecursively(shell, closeListener);
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public void open()
    {
        createContents();
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


    /**
     * Add an entry to the list of acknowledgements at the bottom of the
     * About dialog. This needs to be given the absolute y coordinate for
     * where to put the line of text.
     */
    private void addAcknowledgement(int y, final String text, final String url)
    {
        final StyledText label = new StyledText(shell, SWT.LEFT);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        label.setEditable(false);
        label.setToolTipText("Click to visit " + url + ".");
        label.setForeground(SWTResourceManager.getColor(65, 105, 225));
        if (UIUtils.isMacOSX())
            label.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -2));
        else
            label.setFont(UIUtils.createResizedFont("Verdana", SWT.NORMAL, -1));
        label.setBounds(17, y, 300, 14 + offset);
        label.setText("\u2022 " + text + ".");
        label.addMouseListener(new MouseListener() {
                public void mouseDown(MouseEvent arg0)
                {
                    org.eclipse.swt.program.Program.launch(url);
                }
                public void mouseUp(MouseEvent arg0) { return; }
                public void mouseDoubleClick(MouseEvent arg0) { return; }
            });
        label.addListener(SWT.MouseEnter, new Listener() {
                public void handleEvent(Event e)
                {
                    label.setCursor(handCursor);
                }
            });
        label.addListener(SWT.MouseExit, new Listener() {
                public void handleEvent(Event e)
                {
                    label.setCursor(null);
                }
            });
    }
}
