//
// @file   PreferenceDialog.java
// @brief  PreferenceDialog for the sbml test suite
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

package org.sbml.testsuite.ui;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.WrapperConfig;


/**
 * PreferenceDialog for the sbml test suite
 */
public class PreferenceDialog
    extends Dialog
{
    protected Shell             shell;
    private TestSuiteSettings   result;
    private TestSuiteSettings   previousResult;
    private Text                txtCasesDir;
    private String              origCasesDir;
    private EditListOfWrappers  wrappersEditor;

    /** Tracks whether user has already indicated whether to save changes.
        The default is true because we check for changes and ask for
        confirmation *unless* the user clicked on Save or Cancel explicitly.
    */
    private boolean             needConfirmSave = true;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public PreferenceDialog(Shell parent)
    {
        super(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        setText("SBML Test Suite Preferences");
        createContents();
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shell = new Shell(getParent(), getStyle());
        shell.setImage(UIUtils.getImageResource("sbml_256.png"));
        shell.setMinimumSize(new Point(630, 395));
        shell.setSize(740, 495);
        shell.setTouchEnabled(true);
        shell.setText("Preferences");
        GridLayout gl_shell = new GridLayout(1, true);
        gl_shell.marginWidth = 10;
        gl_shell.horizontalSpacing = 0;
        shell.setLayout(gl_shell);

        Composite outerComp = new Composite(shell, SWT.NONE);
        outerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                               1, 1));
        GridLayout gl_outerComp = new GridLayout(5, false);
        gl_outerComp.marginWidth = 0;
        gl_outerComp.marginTop = 5;
        gl_outerComp.marginRight = 0;
        gl_outerComp.marginHeight = 0;
        gl_outerComp.horizontalSpacing = 0;
        outerComp.setLayout(gl_outerComp);

        Label lblTestCasesDir = new Label(outerComp, SWT.NONE);
        lblTestCasesDir.setText("Test Cases Directory:");
        lblTestCasesDir.setToolTipText("The folder/directory where the SBML "
                                       + "Test Suite case files are located "
                                       + "on your computer.");

        txtCasesDir = new Text(outerComp, SWT.BORDER);
        txtCasesDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, 
                                               false, 3, 1));
        txtCasesDir.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Composite compBrowse = new Composite(outerComp, SWT.NONE);
        GridData gd_compBrowse = new GridData(SWT.FILL, SWT.CENTER, false, false,
                                              1, 1);
        // This next number is a cheat.  The button is hinted to 58, but this
        // is 55 in order to get the right edges to align.  I've literally 
        // spent hours trying many variations -- using gridlayout, setting 
        // different fields, etc. -- to try to get the button and the text 
        // field together to go flush against the right edge, without
        // success.  This hack & using 'exclude' on gd_btnBrowseCasesDir
        // is the best I've been able to do. -- MH
        gd_compBrowse.widthHint = 55;
        gd_compBrowse.heightHint = 25;
        compBrowse.setLayoutData(gd_compBrowse);
        Button btnBrowseCasesDir = new Button(compBrowse, SWT.NONE);
        GridData gd_btnBrowseCasesDir = new GridData(SWT.CENTER, SWT.CENTER, 
                                                     false, false, 1, 1);
        gd_btnBrowseCasesDir.exclude = true;
        btnBrowseCasesDir.setLayoutData(gd_btnBrowseCasesDir);
        btnBrowseCasesDir.setBounds(2, 2, 58, 25);
        
        btnBrowseCasesDir.setText("Edit");
        btnBrowseCasesDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                browseForCasesDir();
            }
        });
        
        Label sep1 = new Label(outerComp, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_sep1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
        gd_sep1.verticalIndent = 5;
        gd_sep1.horizontalIndent = 0;
        sep1.setLayoutData(gd_sep1);
        
        wrappersEditor = new EditListOfWrappers(outerComp, SWT.NONE);
        GridData gd_wrappersEditor = new GridData(SWT.FILL, SWT.TOP, true,
                                                  true, 5, 1);
        gd_wrappersEditor.heightHint = 378;
        gd_wrappersEditor.widthHint = 300;
        gd_wrappersEditor.minimumWidth = 300;
        gd_wrappersEditor.horizontalIndent = 0;
        gd_wrappersEditor.verticalIndent = 0;
        wrappersEditor.setLayoutData(gd_wrappersEditor);

        Label sep2 = new Label(outerComp, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_sep2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
        gd_sep2.verticalIndent = 0;
        gd_sep2.horizontalIndent = 0;
        sep2.setLayoutData(gd_sep2);

        Composite compSaveCancelButtons = new Composite(outerComp, SWT.NONE);
        compSaveCancelButtons.setLayout(new GridLayout(1, false));
        compSaveCancelButtons.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false,
                                             false, 3, 1));

        Composite compButtons = new Composite(outerComp, SWT.NONE);
        compButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false,
                                               false, 2, 1));

        Button btnCancel = new Button(compButtons, SWT.NONE);
        btnCancel.setBounds(3, 3, 75, 25);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                needConfirmSave = false;
                result = null;
                shell.close();          // Will invoke close listener.
            }
        });
        btnCancel.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button btnSave = new Button(compButtons, SWT.NONE);
        btnSave.setBounds(85, 3, 75, 25);
        btnSave.setText("Save");
        btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result = getResult(true, shell, wrappersEditor);
                if (! wrapperVerified(result.getLastWrapper()))
                    return;
                needConfirmSave = false;
                shell.close();           // Will invoke close listener.
            }
        });
        btnSave.setFocus();
        btnSave.addKeyListener(UIUtils.createCloseKeyListener(shell));

        shell.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event event)
            {
                if (needConfirmSave && settingsHaveChanged())
                    result = getResult(confirmSave(), shell, wrappersEditor);
                shell.dispose();
            }
        });
        shell.addKeyListener(UIUtils.createCloseKeyListener(shell));
        shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public TestSuiteSettings open()
    {
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        previousResult = getTestSuiteSettings(false);
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    protected void browseForCasesDir()
    {
        DirectoryDialog dlg = new DirectoryDialog(shell);
        dlg.setText("Browse For cases dir");
        dlg.setMessage("Please indicate where the SBML Test Suite cases are "
                       + "located. (Usually the path ends with "
                       + "'cases/semantic'.)");
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
        if (shell == null) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shell.getSize();
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
        shell.setLocation(x, y);
    }


    /**
     * @return the contents of the dialog as new settings object
     */
    public TestSuiteSettings getTestSuiteSettings(boolean persistCurrentValues)
    {
        TestSuiteSettings settings;
        WrapperConfig currentWrapper = wrappersEditor.getSelectedWrapper();

        if (currentWrapper != null)
            settings = new TestSuiteSettings(txtCasesDir.getText(),
                                             wrappersEditor.getWrappers(),
                                             currentWrapper.getName());
        else
            settings = new TestSuiteSettings(txtCasesDir.getText(),
                                             wrappersEditor.getWrappers());

        if (persistCurrentValues)
            settings.saveAsDefault();
        return settings;
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
        origCasesDir = settings.getCasesDir();
        wrappersEditor.loadWrappers(settings.getWrappers(),
                                    settings.getLastWrapperName());
    }


    public TestSuiteSettings getResult(boolean save, Shell shell, 
                                       EditListOfWrappers wrappersEditor)
    {
        if (save)
        {
            wrappersEditor.commitPrevious();
            return getTestSuiteSettings(true);
        }
        return null;
    }


    public boolean confirmSave()
    {
        return AskUser.saveCancel(shell, "The configuration has been modified.\n"
                                  + "Save your changes?");
    }
    
    
    private boolean wrapperVerified(WrapperConfig wrapper)
    {
        if (wrapper == null) 
            return false;

        String program = wrapper.getProgram();
        if (program == null || program.isEmpty())
            return AskUser.informWithOverride(shell,
                                              "The wrapper program was left unspecified "
                                              + "\nin the wrapper configuration; consequently, "
                                              + "\nthe wrapper cannot be used.");
        else
        {
            File path = new File(program);
            if (! path.exists())
                return AskUser.informWithOverride(shell,
                                                  "The path to the wrapper specified in "
                                                  + "\nthe wrapper configuration does not appear "
                                                  + "\nto exist; consequently, the wrapper "
                                                  + "\ncannot be used.");
            else if (! path.isFile())
                return AskUser.informWithOverride(shell,
                                                  "The path given for the wrapper program "
                                                  + "\ndoes not appear to be a valid file; "
                                                  + "\nconsequently, the wrapper cannot be used.");
            else if (! path.canRead())
                return AskUser.informWithOverride(shell,
                                                  "The file for the program specified in "
                                                  + "\nthe wrapper configuration does not appear "
                                                  + "\nto be readable; consequently, "
                                                  + "\nthe wrapper cannot be used.");
            else if (! path.canExecute())
                return AskUser.informWithOverride(shell,
                                                  "The program specified in the "
                                                  + "\nwrapper configuration does not appear "
                                                  + "\nto be executable; consequently, "
                                                  + "\nthe wrapper cannot be run.");
        }

        String outputPath = wrapper.getOutputPath();
        if (outputPath == null || outputPath.isEmpty())
            return AskUser.informWithOverride(shell,
                                              "The output directory was left unspecified "
                                              + "\nin the wrapper configuration; consequently, "
                                              + "\nthe wrapper cannot be used.");
        else
        {
            File path = new File(outputPath);
            if (! path.exists())
                return AskUser.informWithOverride(shell,
                                                  "The output directory specified in the "
                                                  + "\nwrapper configuration does not appear "
                                                  + "\nto exist; consequently, the wrapper "
                                                  + "\ncannot be used.");
            else if (! path.isDirectory())
                return AskUser.informWithOverride(shell,
                                                  "The output directory specified in the "
                                                  + "\nwrapper configuration does not appear "
                                                  + "\nto be a valid directory; consequently, "
                                                  + "\nthe wrapper cannot be used.");
            else if (! path.canRead())
                return AskUser.informWithOverride(shell,
                                                  "The output directory specified in the "
                                                  + "\nwrapper configuration does not appear "
                                                  + "\nto be readable; consequently, "
                                                  + "\nthe wrapper cannot be used.");
            else if (! path.canWrite())
                return AskUser.informWithOverride(shell,
                                                  "The output directory specified in the "
                                                  + "\nwrapper configuration does not appear "
                                                  + "\nto be writable; consequently, "
                                                  + "\nthe wrapper cannot be used.");
        }

        String args = wrapper.getArguments();
        if (args == null || args.isEmpty())
            return AskUser.informWithOverride(shell,
                                              "The arguments to the wrapper program have "
                                              + "\nbeen left undefined, which makes it impossible "
                                              + "\nfor the SBML Test Runner to invoke the wrapper "
                                              + "\nwith different test cases.");

        return true;
    }


    public boolean settingsHaveChanged()
    {
        if (origCasesDir == null)
            return false;

        return wrappersEditor.changesPending()
            || ! (origCasesDir.equals(txtCasesDir.getText()));
    }
}
