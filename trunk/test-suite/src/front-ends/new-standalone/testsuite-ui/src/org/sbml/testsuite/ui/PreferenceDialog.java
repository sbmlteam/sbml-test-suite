//
// @file PreferenceDialog.java
// @brief PreferenceDialog for the sbml test suite
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * PreferenceDialog for the sbml test suite
 */
public class PreferenceDialog
    extends Dialog
{

    protected TestSuiteSettings result;
    protected Shell             shlPreferences;
    private Text                txtCasesDir;
    private EditListOfWrappers  compWrappers;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public PreferenceDialog(Shell parent, int style)
    {
        super(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
        setText("SWT Dialog");
        createContents();
    }


    protected void browseForCasesDir()
    {
        DirectoryDialog dlg = new DirectoryDialog(shlPreferences);
        dlg.setText("Browse For cases dir");
        dlg.setMessage("Please select the cases directory (usually cases/semantic).");
        dlg.setFilterPath(txtCasesDir.getText());
        String selectedDirectory = dlg.open();
        if (selectedDirectory != null) txtCasesDir.setText(selectedDirectory);

    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shlPreferences == null) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shlPreferences = new Shell(getParent(), getStyle());
        shlPreferences.setImage(SWTResourceManager.getImage(PreferenceDialog.class,
                                                            "/data/sbml_256.png"));
        shlPreferences.setMinimumSize(new Point(630, 480));
        shlPreferences.setTouchEnabled(true);
        shlPreferences.setSize(631, 478);
        shlPreferences.setText("Preferences");
        shlPreferences.setLayout(new GridLayout(1, true));

        Composite composite_1 = new Composite(shlPreferences, SWT.NONE);
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                               1, 1));
        composite_1.setLayout(new GridLayout(5, false));

        Label lblTestCasesDirectory = new Label(composite_1, SWT.NONE);
        lblTestCasesDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                                                         false, false, 1, 1));
        lblTestCasesDirectory.setText("Test Cases Directory:");

        txtCasesDir = new Text(composite_1, SWT.BORDER);
        txtCasesDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                               false, 3, 1));

        Composite compBrowse = new Composite(composite_1, SWT.NONE);
        compBrowse.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
                                              1, 1));
        compBrowse.setLayout(null);

        Button cmdBrowseCasesDir = new Button(compBrowse, SWT.NONE);
        cmdBrowseCasesDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                browseForCasesDir();
            }
        });
        cmdBrowseCasesDir.setBounds(3, 3, 21, 25);
        cmdBrowseCasesDir.setBounds(0, 0, 75, 25);
        cmdBrowseCasesDir.setText("...");

        compWrappers = new EditListOfWrappers(composite_1, SWT.NONE);
        compWrappers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                                5, 5));

        Composite composite = new Composite(composite_1, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false,
                                             false, 3, 1));

        Composite compButtons = new Composite(composite_1, SWT.NONE);
        compButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false,
                                               false, 2, 1));

        Button btnOk = new Button(compButtons, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                compWrappers.commitPrevious();
                result = getTestSuiteSettings();
                shlPreferences.close();
            }
        });
        btnOk.setBounds(3, 3, 75, 25);
        btnOk.setText("OK");

        Button btnCancel = new Button(compButtons, SWT.NONE);
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result = null;
                shlPreferences.close();
            }
        });
        btnCancel.setBounds(85, 3, 75, 25);
        btnCancel.setText("Cancel");

    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shlPreferences.getSize();
    }


    /**
     * @return the contents of the dialog as new settings object
     */
    public TestSuiteSettings getTestSuiteSettings()
    {
        return new TestSuiteSettings(txtCasesDir.getText(),
                                     compWrappers.getWrappers());
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public TestSuiteSettings open()
    {

        shlPreferences.open();
        shlPreferences.layout();
        Display display = getParent().getDisplay();
        while (!shlPreferences.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    /**
     * Sets the location of the dialog
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     */
    public void setLocation(int x, int y)
    {
        shlPreferences.setLocation(x, y);
    }


    /**
     * Initializes the dialog from the given settings
     * 
     * @param settings
     *            the settings to initialize from
     */
    public void setTestSuiteSettings(TestSuiteSettings settings)
    {
        txtCasesDir.setText(settings.getCasesDir());
        compWrappers.loadWrappers(settings.getWrappers(),
                                  settings.getLastWrapperName());

    }

}
