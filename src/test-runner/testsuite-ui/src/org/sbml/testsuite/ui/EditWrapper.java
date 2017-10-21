//
// @file EditWrapper.java
// @brief Composite for editing a wrapper configuration
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
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

import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.Util;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * Composite for editing a wrapper configuration
 */
public class EditWrapper
    extends Composite
{
    private final Text   txtName;
    private final Text   txtWrapperOutputDir;
    private final Text   txtUnsupportedTags;
    private final Text   txtWrapper;
    private final Text   txtWrapperArgs;
    private final Button btnWrapperAnyLV;
    private final Button btnWrapperThreadsOK;
    private final Button btnWrapperViewOnly;

    // Fields that we may need to enable/disable.

    private final Label  lblName;
    private final Label  lblWrapper;
    private final Button cmdBrowseWrapper;
    private final Label  lblWrapperOutputDir;
    private final Button cmdBrowseOutputDir;
    private final Label  lblWrapperArguments;
    private final Label  lblUnsupportedTags;
    private final Button cmdEditTags;

    private final Color normalTextColor
        = UIUtils.getDefaultLabelForegroundColor();
    private final Color inactiveTextColor
        = UIUtils.getInactiveTextColor();

    private String initialName;


    /**
     * @return the current state as wrapper configuration
     */
    public WrapperConfig toConfig()
    {
        WrapperConfig config = new WrapperConfig(
                                                 txtName.getText(),
                                                 txtWrapper.getText(),
                                                 txtWrapperOutputDir.getText(),
                                                 txtWrapperArgs.getText(),
                                                 txtUnsupportedTags.getText(),
                                                 btnWrapperAnyLV.getSelection(),
                                                 btnWrapperThreadsOK.getSelection(),
                                                 btnWrapperViewOnly.getSelection()
                                                 );

        return config;
    }


    /**
     * Initializes this composite from the given configuration.  It sets
     * the internal "initial name" field at the same time, so calling 
     * setInitialName() is not required if this method is called.
     *
     * @param config the configuration
     */
    public void loadFrom(WrapperConfig config)
    {
        if (config == null) return;
        initialName = config.getName();
        txtName.setText(config.getName());
        txtWrapperOutputDir.setText(config.getOutputPath());
        txtUnsupportedTags.setText(config.getUnsupportedTagsString());
        txtWrapper.setText(config.getProgram());
        txtWrapperArgs.setText(config.getArguments());
        btnWrapperAnyLV.setSelection(config.isSupportsAllVersions());
        btnWrapperThreadsOK.setSelection(config.isConcurrencyAllowed());
        btnWrapperViewOnly.setSelection(config.isViewOnly());
        updateWidgetStates();
    }


    /**
     * Explicitly sets the "initial name" field for the wrapper in this form.
     * This name is not changed once it's set by either this method or the
     * loadFrom() method, which allows callers to track what this wrapper
     * was called before the user may have changed the name.
     *
     * @param name the initial name
     */
    public void setInitialName(String name)
    {
        initialName = name;
    }


    public String getInitialName()
    {
        return initialName;
    }


    /**
     * Sets the focus on the wrapper name field.
     */
    public void setFocusOnNameField()
    {
        txtName.setFocus();
    }


    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public EditWrapper(Composite parent, int style)
    {
        super(parent, style);
        final Shell shell = parent.getShell();
        final Display display = Display.getCurrent();

        FormLayout layout = new FormLayout();
        setLayout(layout);

        int margin = 5;
        int offset = 20 - UIUtils.scaledFontSize(20);
        int nudge = offset/2;

        Font labelFont = UIUtils.getDefaultLabelFont();

        lblName = new Label(this, SWT.RIGHT);
        lblName.setAlignment(SWT.RIGHT);
        FormData fd_lblName = new FormData();
        fd_lblName.right = new FormAttachment(0, 170);
        fd_lblName.top = new FormAttachment(0, 10);
        lblName.setFont(labelFont);
        lblName.setLayoutData(fd_lblName);
        lblName.setText("Name:");
        lblName.setToolTipText("A name for this wrapper configuration.");

        txtName = new Text(this, SWT.BORDER);
        FormData fd_txtName = new FormData();
        fd_txtName.left = new FormAttachment(0, 172);
        fd_txtName.right = new FormAttachment(100, -55);
        fd_txtName.top = new FormAttachment(lblName, 0, SWT.CENTER);
        txtName.setLayoutData(fd_txtName);
        txtName.addKeyListener(UIUtils.createCloseKeyListener(shell));

        btnWrapperViewOnly = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperViewOnly = new FormData();
        fd_btnWrapperViewOnly.top = new FormAttachment(txtName, margin);
        fd_btnWrapperViewOnly.left = new FormAttachment(0, 170 + offset);
        fd_btnWrapperViewOnly.right = new FormAttachment(100, -31);
        btnWrapperViewOnly.setLayoutData(fd_btnWrapperViewOnly);
        btnWrapperViewOnly.setText("Pseudo-wrapper to view test cases only");
        btnWrapperViewOnly.addKeyListener(UIUtils.createCloseKeyListener(shell));
        btnWrapperViewOnly.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                updateWidgetStates();
            }
        });

        int checkboxSpace = (UIUtils.isLinux() ? -2 : 2);

        btnWrapperAnyLV = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperAnyLV = new FormData();
        fd_btnWrapperAnyLV.top = new FormAttachment(btnWrapperViewOnly, checkboxSpace);
        fd_btnWrapperAnyLV.left = new FormAttachment(0, 170 + offset);
        fd_btnWrapperAnyLV.right = new FormAttachment(100, -31);
        btnWrapperAnyLV.setLayoutData(fd_btnWrapperAnyLV);
        btnWrapperAnyLV.setText("Wrapper can handle any SBML Level/Version");
        btnWrapperAnyLV.addKeyListener(UIUtils.createCloseKeyListener(shell));

        btnWrapperThreadsOK = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperThreadsOK = new FormData();
        fd_btnWrapperThreadsOK.top = new FormAttachment(btnWrapperAnyLV, checkboxSpace);
        fd_btnWrapperThreadsOK.left = new FormAttachment(0, 170 + offset);
        fd_btnWrapperThreadsOK.right = new FormAttachment(100, -31);
        btnWrapperThreadsOK.setLayoutData(fd_btnWrapperThreadsOK);
        btnWrapperThreadsOK.setText("Wrapper can be run in parallel");
        btnWrapperThreadsOK.addKeyListener(UIUtils.createCloseKeyListener(shell));

        String wrapperToolTip = "Path to the wrapper script or program.";

        lblWrapper = new Label(this, SWT.RIGHT);
        lblWrapper.setAlignment(SWT.RIGHT);
        FormData fd_lblWrapper = new FormData();
        fd_lblWrapper.top = new FormAttachment(btnWrapperThreadsOK, margin + nudge);
        fd_lblWrapper.right = new FormAttachment(0, 170);
        lblWrapper.setFont(labelFont);
        lblWrapper.setLayoutData(fd_lblWrapper);
        lblWrapper.setText("Wrapper path:");
        lblWrapper.setToolTipText(wrapperToolTip);

        txtWrapper = new Text(this, SWT.BORDER);
        FormData fd_txtWrapper = new FormData();
        fd_txtWrapper.top = new FormAttachment(btnWrapperThreadsOK, margin);
        fd_txtWrapper.left = new FormAttachment(0, 172);
        fd_txtWrapper.right = new FormAttachment(100, -55);
        txtWrapper.setLayoutData(fd_txtWrapper);
        txtWrapper.addKeyListener(UIUtils.createCloseKeyListener(shell));

        cmdBrowseWrapper = new Button(this, SWT.NONE);
        cmdBrowseWrapper.setAlignment(SWT.CENTER);
        FormData fd_cmdBrowseWrapper = new FormData();
        fd_cmdBrowseWrapper.top = new FormAttachment(btnWrapperThreadsOK, nudge + 1);
        if (UIUtils.isMacOSX())
            fd_cmdBrowseWrapper.right = new FormAttachment(100, 5);
        else
        {
            fd_cmdBrowseWrapper.right = new FormAttachment(100, 0);
            fd_cmdBrowseWrapper.left = new FormAttachment(100, -50);
        }
        cmdBrowseWrapper.setLayoutData(fd_cmdBrowseWrapper);
        cmdBrowseWrapper.setText("Edit");
        cmdBrowseWrapper.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                browseForWrapper();
            }
        });
        cmdBrowseWrapper.setToolTipText(wrapperToolTip);
        cmdBrowseWrapper.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        String outputDirToolTip = "Directory on your system where files can be written.";

        lblWrapperOutputDir = new Label(this, SWT.RIGHT);
        lblWrapperOutputDir.setAlignment(SWT.RIGHT);
        FormData fd_lblWrapperOutputDir = new FormData();
        fd_lblWrapperOutputDir.top = new FormAttachment(txtWrapper, margin + nudge);
        fd_lblWrapperOutputDir.right = new FormAttachment(0, 170);
        lblWrapperOutputDir.setFont(labelFont);
        lblWrapperOutputDir.setLayoutData(fd_lblWrapperOutputDir);
        lblWrapperOutputDir.setText("Output directory:");
        lblWrapperOutputDir.setToolTipText(outputDirToolTip);

        txtWrapperOutputDir = new Text(this, SWT.BORDER);
        FormData fd_txtWrapperOutputDir = new FormData();
        fd_txtWrapperOutputDir.top = new FormAttachment(txtWrapper, margin);
        fd_txtWrapperOutputDir.left = new FormAttachment(0, 172);
        fd_txtWrapperOutputDir.right = new FormAttachment(100, -55);
        txtWrapperOutputDir.setLayoutData(fd_txtWrapperOutputDir);
        txtWrapperOutputDir.addKeyListener(UIUtils.createCloseKeyListener(shell));

        cmdBrowseOutputDir = new Button(this, SWT.NONE);
        cmdBrowseOutputDir.setAlignment(SWT.CENTER);
        FormData fd_cmdBrowseOutputDir = new FormData();
        fd_cmdBrowseOutputDir.top = new FormAttachment(txtWrapper, nudge + 1);
        if (UIUtils.isMacOSX())
            fd_cmdBrowseOutputDir.right = new FormAttachment(100, 5);
        else
        {
            fd_cmdBrowseOutputDir.right = new FormAttachment(100, 0);
            fd_cmdBrowseOutputDir.left = new FormAttachment(100, -50);
        }
        cmdBrowseOutputDir.setLayoutData(fd_cmdBrowseOutputDir);
        cmdBrowseOutputDir.setText("Edit");
        cmdBrowseOutputDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                browseForOutputDir();
            }
        });
        cmdBrowseOutputDir.setToolTipText(outputDirToolTip);
        cmdBrowseOutputDir.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        String unsupportedToolTip =
            "List of SBML Test Suite packages and/or tags for tests that "
            + "should be excluded. Use this if the application is known not "
            + "to support certain SBML features or types of tests.";

        lblUnsupportedTags = new Label(this, SWT.RIGHT);
        lblUnsupportedTags.setAlignment(SWT.RIGHT);
        FormData fd_lblUnsupportedTags = new FormData();
        fd_lblUnsupportedTags.top = new FormAttachment(txtWrapperOutputDir, margin + nudge);
        fd_lblUnsupportedTags.right = new FormAttachment(0, 170);
        lblUnsupportedTags.setFont(labelFont);
        lblUnsupportedTags.setLayoutData(fd_lblUnsupportedTags);
        lblUnsupportedTags.setText("Unsupported tags:");
        lblUnsupportedTags.setToolTipText(unsupportedToolTip);

        txtUnsupportedTags = new Text(this, SWT.BORDER);
        FormData fd_txtUnsupportedTags = new FormData();
        fd_txtUnsupportedTags.top = new FormAttachment(txtWrapperOutputDir, margin);
        fd_txtUnsupportedTags.left = new FormAttachment(0, 172);
        fd_txtUnsupportedTags.right = new FormAttachment(100, -55);        
        txtUnsupportedTags.setLayoutData(fd_txtUnsupportedTags);
        txtUnsupportedTags.addKeyListener(UIUtils.createCloseKeyListener(shell));

        cmdEditTags = new Button(this, SWT.NONE);
        cmdEditTags.setAlignment(SWT.CENTER);
        FormData fd_cmdEditTags = new FormData();
        fd_cmdEditTags.top = new FormAttachment(txtWrapperOutputDir, nudge + 1);
        if (UIUtils.isMacOSX())
            fd_cmdEditTags.right = new FormAttachment(100, 5);
        else
        {
            fd_cmdEditTags.right = new FormAttachment(100, 0);
            fd_cmdEditTags.left = new FormAttachment(100, -50);
        }
        cmdEditTags.setLayoutData(fd_cmdEditTags);
        cmdEditTags.setText("Edit");
        cmdEditTags.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                editUnsupportedTags();
            }
        });
        cmdEditTags.setToolTipText(unsupportedToolTip);
        cmdEditTags.addKeyListener(UIUtils.createCloseKeyListener(shell));

        lblWrapperArguments = new Label(this, SWT.RIGHT);
        lblWrapperArguments.setFont(labelFont);
        lblWrapperArguments.setAlignment(SWT.RIGHT);
        lblWrapperArguments.setText("Arguments to wrapper:");
        lblWrapperArguments.setToolTipText("Command line arguments that should "
                                           + "be passed to the wrapper. See "
                                           + "below for more information.");
        FormData fd_lblWrapperArguments = new FormData();
        fd_lblWrapperArguments.top = new FormAttachment(txtUnsupportedTags, margin + nudge);
        fd_lblWrapperArguments.right = new FormAttachment(0, 170);
        lblWrapperArguments.setLayoutData(fd_lblWrapperArguments);

        txtWrapperArgs = new Text(this, SWT.BORDER);
        FormData fd_txtWrapperArgs = new FormData();
        fd_txtWrapperArgs.top = new FormAttachment(txtUnsupportedTags, margin);
        fd_txtWrapperArgs.left = new FormAttachment(0, 172);
        fd_txtWrapperArgs.right = new FormAttachment(100, -55);
        txtWrapperArgs.setLayoutData(fd_txtWrapperArgs);
        txtWrapperArgs.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        Label lblUsageInfo1 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo1 = new FormData();
        fd_lblUsageInfo1.top = new FormAttachment(txtWrapperArgs, 2*margin);
        fd_lblUsageInfo1.left = new FormAttachment(0, 10);
        fd_lblUsageInfo1.right = new FormAttachment(100, -10);
        lblUsageInfo1.setLayoutData(fd_lblUsageInfo1);
        Color gray = display.getSystemColor(SWT.COLOR_DARK_GRAY);
        lblUsageInfo1.setForeground(gray);
        final Font newFont = UIUtils.createResizedFont(lblUsageInfo1.getFont(), -1);
        lblUsageInfo1.setFont(newFont);
        lblUsageInfo1.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e)
                {
                    newFont.dispose();
                }
        });
        lblUsageInfo1.setText("You can use the following substitution codes in "
                              + "the wrapper command line arguments:");

        Label lblUsageInfo2 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo2 = new FormData();
        fd_lblUsageInfo2.top = new FormAttachment(lblUsageInfo1, margin);
        fd_lblUsageInfo2.left = new FormAttachment(0, 10);
        fd_lblUsageInfo2.right = new FormAttachment(100, -10);
        lblUsageInfo2.setLayoutData(fd_lblUsageInfo2);
        lblUsageInfo2.setForeground(gray);
        lblUsageInfo2.setFont(newFont);
        lblUsageInfo2.setText(
                "\t%d\t= path to the directory containting all test cases"
            + "\n\t%n\t= current test case number (of the form NNNNN)"
            + "\n\t%o\t= directory where the CSV output file should be written"
            + "\n\t%l\t= the SBML Level to be used"
            + "\n\t%v\t= the SBML Version to be used");

        Label lblUsageInfo3 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo3 = new FormData();
        fd_lblUsageInfo3.top = new FormAttachment(lblUsageInfo2, margin);
        fd_lblUsageInfo3.left = new FormAttachment(0, 10);
        fd_lblUsageInfo3.right = new FormAttachment(100, -10);
        lblUsageInfo3.setLayoutData(fd_lblUsageInfo3);
        lblUsageInfo3.setForeground(gray);
        lblUsageInfo3.setFont(newFont);
        lblUsageInfo3.setText(
            "Each test case consists of an SBML file and a settings file. The "
            + "files are located in the directory named %d/%n. The SBML file for "
            + "the test model is named '%n-sbml-l%lv%v.xml'. (Example: "
            + "'00123-sbml-l2v3.xml'.) The settings file is named "
            + "'%n-settings.txt' in the same directory. The wrapper or  "
            + "application must be instructed to write out the results into a "
            + "file named '%o/%n.csv' so that the SBML Test Runner can find it.");

        updateWidgetStates();
    }


    private boolean wrapperIsViewOnly()
    {
        return btnWrapperViewOnly.getSelection();
    }


    private boolean wrapperIsNoWrapper()
    {
        return ("-- no wrapper --".equals(txtName.getText()));
    }


    private void updateWidgetStates()
    {
        // The no-wrapper wrapper also possesses the property of being
        // view-only, so test for no-wrapper first and view-only second.

        if (wrapperIsNoWrapper())
        {
            // Name field.

            lblName.setEnabled(false);
            lblName.setForeground(inactiveTextColor);
            txtName.setEnabled(false);
            txtName.setForeground(inactiveTextColor);

            // Is the wrapper a view-only pseudo-wrapper?

            btnWrapperViewOnly.setSelection(true);
            btnWrapperViewOnly.setEnabled(false);
            btnWrapperViewOnly.setForeground(inactiveTextColor);

            // Wrapper can handle any L/V.

            btnWrapperAnyLV.setSelection(true);
            btnWrapperAnyLV.setEnabled(false);
            btnWrapperAnyLV.setForeground(inactiveTextColor);

            // Wrapper can be run in parallel.

            btnWrapperThreadsOK.setSelection(true);
            btnWrapperThreadsOK.setEnabled(false);
            btnWrapperThreadsOK.setForeground(inactiveTextColor);

            // Wrapper path field.

            lblWrapper.setEnabled(false);
            lblWrapper.setForeground(inactiveTextColor);
            txtWrapper.setEnabled(false);
            cmdBrowseWrapper.setEnabled(false);

            // Output directory.

            lblWrapperOutputDir.setEnabled(false);
            lblWrapperOutputDir.setForeground(inactiveTextColor);
            txtWrapperOutputDir.setEnabled(false);
            cmdBrowseOutputDir.setEnabled(false);

            // Unsupported tags.

            lblUnsupportedTags.setEnabled(false);
            lblUnsupportedTags.setForeground(inactiveTextColor);
            txtUnsupportedTags.setEnabled(false);
            cmdEditTags.setEnabled(false);

            // Wrapper arguments.

            lblWrapperArguments.setEnabled(false);
            lblWrapperArguments.setForeground(inactiveTextColor);
            txtWrapperArgs.setEnabled(false);
        }
        else if (wrapperIsViewOnly())
        {
            // Name field.

            lblName.setEnabled(true);
            lblName.setForeground(normalTextColor);
            txtName.setEnabled(true);
            txtName.setForeground(normalTextColor);

            // Is the wrapper a view-only pseudo-wrapper?

            btnWrapperViewOnly.setSelection(true);
            btnWrapperViewOnly.setEnabled(true);
            btnWrapperViewOnly.setForeground(normalTextColor);

            // Wrapper can handle any L/V.

            btnWrapperAnyLV.setSelection(true);
            btnWrapperAnyLV.setEnabled(false);
            btnWrapperAnyLV.setForeground(inactiveTextColor);

            // Wrapper can be run in parallel.

            btnWrapperThreadsOK.setSelection(true);
            btnWrapperThreadsOK.setEnabled(false);
            btnWrapperThreadsOK.setForeground(inactiveTextColor);

            // Wrapper path field.

            lblWrapper.setEnabled(false);
            lblWrapper.setForeground(inactiveTextColor);
            txtWrapper.setEnabled(false);
            txtWrapper.setText("");
            cmdBrowseWrapper.setEnabled(false);

            // Output directory.

            lblWrapperOutputDir.setEnabled(true);
            lblWrapperOutputDir.setForeground(normalTextColor);
            txtWrapperOutputDir.setEnabled(true);
            cmdBrowseOutputDir.setEnabled(true);

            // Unsupported tags.

            lblUnsupportedTags.setEnabled(true);
            lblUnsupportedTags.setForeground(normalTextColor);
            txtUnsupportedTags.setEnabled(true);
            cmdEditTags.setEnabled(true);

            // Wrapper arguments.

            lblWrapperArguments.setEnabled(false);
            lblWrapperArguments.setForeground(inactiveTextColor);
            txtWrapperArgs.setEnabled(false);
            txtWrapperArgs.setText("");
        }
        else
        {
            // Name field.

            lblName.setEnabled(true);
            lblName.setForeground(normalTextColor);
            txtName.setEnabled(true);
            txtName.setForeground(normalTextColor);

            // Is the wrapper a view-only pseudo-wrapper?

            btnWrapperViewOnly.setEnabled(true);
            btnWrapperViewOnly.setForeground(normalTextColor);

            // Wrapper can handle any L/V.

            btnWrapperAnyLV.setEnabled(true);
            btnWrapperAnyLV.setForeground(normalTextColor);

            // Wrapper can be run in parallel.

            btnWrapperThreadsOK.setEnabled(true);
            btnWrapperThreadsOK.setForeground(normalTextColor);

            // Wrapper path field.

            lblWrapper.setEnabled(true);
            lblWrapper.setForeground(normalTextColor);
            txtWrapper.setEnabled(true);
            cmdBrowseWrapper.setEnabled(true);

            // Output directory.

            lblWrapperOutputDir.setEnabled(true);
            lblWrapperOutputDir.setForeground(normalTextColor);
            txtWrapperOutputDir.setEnabled(true);
            cmdBrowseOutputDir.setEnabled(true);

            // Unsupported tags.

            lblUnsupportedTags.setEnabled(true);
            lblUnsupportedTags.setForeground(normalTextColor);
            txtUnsupportedTags.setEnabled(true);
            cmdEditTags.setEnabled(true);

            // Wrapper arguments.

            lblWrapperArguments.setEnabled(true);
            lblWrapperArguments.setForeground(normalTextColor);
            txtWrapperArgs.setEnabled(true);
        }
    }


    protected void browseForOutputDir()
    {
        DirectoryDialog dlg = new DirectoryDialog(getShell());
        String selectedDir  = txtWrapperOutputDir.getText();
        boolean retry       = false;

        dlg.setText("Select the wrapper output directory");
        do
        {
            retry = false;
            dlg.setFilterPath(selectedDir);
            selectedDir = dlg.open();
            if (!wrapperIsViewOnly() && selectedDir != null
                && MarkerFile.exists(selectedDir))
                // && !confirmOutputDir(selectedDir))
                retry = true;
        }
        while (retry);

        if (selectedDir != null)
            txtWrapperOutputDir.setText(selectedDir);

        // If the user agreed to use the output directory, we need to remove
        // the marker file now, or else they will get asked again by
        // the sanity checks performed by PreferenceDialog.

        if (MarkerFile.exists(selectedDir))
            MarkerFile.remove(selectedDir);
    }


    private boolean confirmOutputDir(String dir)
    {
        String prog = MarkerFile.getContents(dir);
        String intro;
        String question = "Choose OK to associate"
            + "\nthose results with the current wrapper, or"
            + "\nchoose Cancel to go back to the Preferences"
            + "\npanel so that you can change the directory.";

        // If we can read the marker file, use the content to make a more
        // informative dialog.  If we can't read it, but it exists, we still
        // know this directory must have been used for a run, so we still ask.

        if (prog != null && prog.length() > 0)
        {
            // Skip the question if the wrapper program hasn't changed.

            if (txtWrapper.getText().equals(prog))
                return true;
            else
                intro = "The directory you have chosen appears to"
                    + "\ncontain the results of a previous run of"
                    + "\nthe SBML Test Runner, but for a different"
                    + "\nwrapper program,"
                    + "\n\n   " + prog
                    + "\nThe SBML Test Runner cannot know whether"
                    + "\nthe results in the directory can be reused"
                    + "\nfor the current wrapper. ";
        }
        else
        {
            // We found results, but for some reason the marker file is
            // empty, which is a situation that shouldn't arise because we
            // wrote the file in the first place.  Still, need to check.

            intro = "The directory you have chosen appears to"
                + "\ncontain the results of a previous run of"
                + "\nthe SBML Test Runner. The SBML Test Runner"
                + "\ncannot know whether the results in the"
                + "\ndirectory can be reused for the current"
                + "\nwrapper. ";
        }

        /// FTB: i find the dialogs not helpful. ifa path is specified by a user
        //       an additional confirmation seems unnecessary.I don't see this 
        //       dialog helping anyone
        return Tell.saveCancel(getShell(), intro + question);
    }


    protected void editUnsupportedTags()
    {
       TagsDialog dialog = new TagsDialog(getShell(), SWT.None);
       dialog.setTitle("Select unsupported tags");
       dialog.setComponentTags(TestSuiteSettings.loadDefault().getSuite().getComponentTags());
       dialog.setTestTags(TestSuiteSettings.loadDefault().getSuite().getTestTags());
       dialog.center(getShell().getBounds());
       dialog.setSelectedTags(Util.split(txtUnsupportedTags.getText()));

       if (dialog.open())
       {
           Vector<String> selectedTags = dialog.getSelectedTags();
           txtUnsupportedTags.setText(Util.toString(selectedTags));
       }
    }


    protected void browseForWrapper()
    {
        FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
        dlg.setText("Browse for wrapper");
        dlg.setFileName(txtWrapper.getText());
        String fileName = dlg.open();
        if (fileName != null)
            txtWrapper.setText(fileName);
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }
}
