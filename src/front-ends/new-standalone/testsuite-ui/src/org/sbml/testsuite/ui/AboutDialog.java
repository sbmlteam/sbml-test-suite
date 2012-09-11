//
// @file AboutDialog.java
// @brief About dialog for the SBML test suite
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
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

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * About dialog for the SBML test suite
 */
public class AboutDialog
    extends Dialog
{

    protected Object result;
    protected Shell  shlAboutSbmlTestsuite;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public AboutDialog(Shell parent, int style)
    {
        super(parent, style);
        setText("About SBML Testsuite");
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
        if (shlAboutSbmlTestsuite == null) return;

        Point dialogSize = shlAboutSbmlTestsuite.getSize();
        shlAboutSbmlTestsuite.setLocation(shellBounds.x
            + (shellBounds.width - dialogSize.x) / 2, shellBounds.y
            + (shellBounds.height - dialogSize.y) / 2);

    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shlAboutSbmlTestsuite = new Shell(getParent(), getStyle());
        shlAboutSbmlTestsuite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        shlAboutSbmlTestsuite.setSize(722, 564);
        shlAboutSbmlTestsuite.setText("About SBML Testsuite");
        shlAboutSbmlTestsuite.setLayout(new FormLayout());

        Button btnclose = new Button(shlAboutSbmlTestsuite, SWT.NONE);
        btnclose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                shlAboutSbmlTestsuite.close();
            }
        });
        FormData fd_btnclose = new FormData();
        fd_btnclose.bottom = new FormAttachment(100, -10);
        fd_btnclose.right = new FormAttachment(100, -10);
        fd_btnclose.width = 75;
        btnclose.setLayoutData(fd_btnclose);
        btnclose.setText("&Close");

        Label label = new Label(shlAboutSbmlTestsuite, SWT.NONE);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        FormData fd_label = new FormData();
        fd_label.bottom = new FormAttachment(100, 4);
        fd_label.right = new FormAttachment(100, 4);
        fd_label.top = new FormAttachment(0);
        fd_label.left = new FormAttachment(0);
        label.setLayoutData(fd_label);
        label.setImage(SWTResourceManager.getImage(AboutDialog.class,
                                                   "/data/TestSuiteLogo.png"));

    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open()
    {
        shlAboutSbmlTestsuite.open();
        shlAboutSbmlTestsuite.layout();
        Display display = getParent().getDisplay();
        while (!shlAboutSbmlTestsuite.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }
}
