//
// @file   AboutDialog.java
// @brief  About dialog for the SBML Test Runner
// @author Frank T. Bergmann
// @author Michael Hucka
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
//

package org.sbml.testsuite.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
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
    protected Object result;
    protected Shell  shlAboutTestRunner;


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
        if (shlAboutTestRunner == null) return;

        Point dialogSize = shlAboutTestRunner.getSize();
        shlAboutTestRunner.setLocation(shellBounds.x
            + (shellBounds.width - dialogSize.x) / 2, shellBounds.y
            + (shellBounds.height - dialogSize.y) / 2);
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shlAboutTestRunner = new Shell(getParent(), SWT.DIALOG_TRIM);
        shlAboutTestRunner.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        shlAboutTestRunner.setSize(288, 498);
        shlAboutTestRunner.setText("About SBML Testsuite");
        shlAboutTestRunner.setLayout(null);
        shlAboutTestRunner.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event) { shlAboutTestRunner.dispose(); }
        });
        shlAboutTestRunner.setFocus();
        UIUtils.addCloseKeyListener(shlAboutTestRunner, shlAboutTestRunner);
        UIUtils.addTraverseKeyListener(shlAboutTestRunner, shlAboutTestRunner);
        
        Label lblNewLabel = new Label(shlAboutTestRunner, SWT.NONE);
        lblNewLabel.setImage(SWTResourceManager.getImage(AboutDialog.class, "/data/sts-icon-shadowed-270x136.png"));
        lblNewLabel.setBounds(11, 10, 268, 134);
        
        Label lblNewLabel_1 = new Label(shlAboutTestRunner, SWT.NONE);
        lblNewLabel_1.setFont(SWTResourceManager.getFont("Verdana", 20, SWT.BOLD));
        lblNewLabel_1.setAlignment(SWT.CENTER);
        lblNewLabel_1.setBounds(11, 140, 266, 25);
        lblNewLabel_1.setText("The SBML Test Runner");
        
        Label sep1 = new Label(shlAboutTestRunner, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep1.setBounds(16, 194, 255, 2);
        
        Label lblVersion = new Label(shlAboutTestRunner, SWT.RIGHT);
        lblVersion.setAlignment(SWT.CENTER);
        lblVersion.setFont(SWTResourceManager.getFont("Verdana", 11, SWT.ITALIC));
        lblVersion.setBounds(102, 170, 84, 14);
        lblVersion.setText("Version");
        
        Label lblTheSbmlTest = new Label(shlAboutTestRunner, SWT.WRAP);
        lblTheSbmlTest.setBounds(14, 202, 260, 88);
        lblTheSbmlTest.setText("The SBML Test Suite is a conformance testing system that tests the degree and correctness of SBML support in a given software system. The SBML Test Runner drives an application to perform each test in the Test Suite, and displays the results and conclusions.");
        lblTheSbmlTest.setFont(SWTResourceManager.getFont("Verdana", 10, SWT.NORMAL));
        
        Label sep2 = new Label(shlAboutTestRunner, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep2.setBounds(16, 300, 255, 2);
        
        Label lblNewLabel_2 = new Label(shlAboutTestRunner, SWT.NONE);
        lblNewLabel_2.setFont(SWTResourceManager.getFont("Verdana", 10, SWT.NORMAL));
        lblNewLabel_2.setBounds(14, 308, 266, 14);
        lblNewLabel_2.setText("Written by Frank T. Bergmann and Michael Hucka.");
        
        Label lblNewLabel_3 = new Label(shlAboutTestRunner, SWT.WRAP);
        lblNewLabel_3.setFont(SWTResourceManager.getFont("Verdana", 10, SWT.ITALIC));
        lblNewLabel_3.setBounds(14, 340, 255, 49);
        lblNewLabel_3.setText("For more information about this and other SBML Team software, as well as about SBML itself, please visit the following website:");
        
        Label sep3 = new Label(shlAboutTestRunner, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
        sep3.setBounds(16, 331, 255, 2);
        
        Label lblHttpsbmlorg = new Label(shlAboutTestRunner, SWT.CENTER);
        lblHttpsbmlorg.setForeground(SWTResourceManager.getColor(65, 105, 225));
        lblHttpsbmlorg.setFont(SWTResourceManager.getFont("Verdana", 11, SWT.BOLD));
        lblHttpsbmlorg.setBounds(81, 390, 126, 20);
        lblHttpsbmlorg.setText("http://sbml.org");
        
        Label lblNewLabel_4 = new Label(shlAboutTestRunner, SWT.NONE);
        lblNewLabel_4.setImage(SWTResourceManager.getImage(AboutDialog.class, "/data/SBML.png"));
        lblNewLabel_4.setBounds(95, 420, 99, 40);
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public void open()
    {
        shlAboutTestRunner.open();
        shlAboutTestRunner.layout();
        Display display = getParent().getDisplay();
        while (!shlAboutTestRunner.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }
}
