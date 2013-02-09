//
// @file EditWrapper.java
// @brief Composite for editing a wrapper configuration
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
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

import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
                                                 btnWrapperAnyLV.getSelection());

        return config;
    }


    /**
     * Initializes this composite from the given configuration
     * @param config the configuration
     */
    public void loadFrom(WrapperConfig config)
    {
        txtName.setText(config.getName());
        txtWrapperOutputDir.setText(config.getOutputPath());
        txtUnsupportedTags.setText(config.getUnsupportedTagsString());
        txtWrapper.setText(config.getProgram());
        txtWrapperArgs.setText(config.getArguments());
        btnWrapperAnyLV.setSelection(config.isSupportsAllVersions());
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

        Label lblName = new Label(this, SWT.RIGHT);
        lblName.setAlignment(SWT.RIGHT);
        FormData fd_lblName = new FormData();
        fd_lblName.right = new FormAttachment(0, 170);
        fd_lblName.top = new FormAttachment(0, 10);
        lblName.setFont(UIUtils.getDefaultLabelFont());
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

        Label lblWrapper = new Label(this, SWT.RIGHT);
        lblWrapper.setAlignment(SWT.RIGHT);
        FormData fd_lblWrapper = new FormData();
        fd_lblWrapper.right = new FormAttachment(0, 170);
        fd_lblWrapper.top = new FormAttachment(0, 38);
        lblWrapper.setFont(UIUtils.getDefaultLabelFont());
        lblWrapper.setLayoutData(fd_lblWrapper);
        lblWrapper.setText("Wrapper path:");
        lblWrapper.setToolTipText("Path to the wrapper script or program.");

        txtWrapper = new Text(this, SWT.BORDER);
        FormData fd_txtWrapper = new FormData();
        fd_txtWrapper.left = new FormAttachment(0, 172);
        fd_txtWrapper.right = new FormAttachment(100, -55);
        fd_txtWrapper.top = new FormAttachment(lblWrapper, 0, SWT.CENTER);
        txtWrapper.setLayoutData(fd_txtWrapper);
        txtWrapper.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button cmdBrowseWrapper = new Button(this, SWT.NONE);
        cmdBrowseWrapper.setAlignment(SWT.CENTER);
        FormData fd_cmdBrowseWrapper = new FormData();
        fd_cmdBrowseWrapper.top = new FormAttachment(txtWrapper, -4, SWT.TOP);
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
        cmdBrowseWrapper.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        Label lblUnsupportedTags = new Label(this, SWT.RIGHT);
        lblUnsupportedTags.setAlignment(SWT.RIGHT);
        FormData fd_lblUnsupportedTags = new FormData();
        fd_lblUnsupportedTags.right = new FormAttachment(0, 170);
        fd_lblUnsupportedTags.top = new FormAttachment(0, 66);
        lblUnsupportedTags.setFont(UIUtils.getDefaultLabelFont());
        lblUnsupportedTags.setLayoutData(fd_lblUnsupportedTags);
        lblUnsupportedTags.setText("Unsupported tags:");
        lblUnsupportedTags.setToolTipText(
            "List of SBML Test Suite tags for tests that should be excluded. "
            + "Use this if the application is known not to support certain "
            + "SBML features or types of tests.");

        txtUnsupportedTags = new Text(this, SWT.BORDER);
        FormData fd_txtUnsupportedTags = new FormData();
        fd_txtUnsupportedTags.left = new FormAttachment(0, 172);
        fd_txtUnsupportedTags.right = new FormAttachment(100, -55);        
        fd_txtUnsupportedTags.top = new FormAttachment(lblUnsupportedTags, 0, 
                                                       SWT.CENTER);
        txtUnsupportedTags.setLayoutData(fd_txtUnsupportedTags);
        txtUnsupportedTags.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button cmdEditTags = new Button(this, SWT.NONE);
        cmdEditTags.setAlignment(SWT.CENTER);
        FormData fd_cmdEditTags = new FormData();
        fd_cmdEditTags.top = new FormAttachment(lblUnsupportedTags, -6, 
                                                SWT.TOP);
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
        cmdEditTags.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Label lblWrapperOutputDir = new Label(this, SWT.RIGHT);
        lblWrapperOutputDir.setAlignment(SWT.RIGHT);
        FormData fd_lblWrapperOutputDir = new FormData();
        fd_lblWrapperOutputDir.right = new FormAttachment(0, 170);
        fd_lblWrapperOutputDir.top = new FormAttachment(0, 94);
        lblWrapperOutputDir.setFont(UIUtils.getDefaultLabelFont());
        lblWrapperOutputDir.setLayoutData(fd_lblWrapperOutputDir);
        lblWrapperOutputDir.setText("Output directory:");
        lblWrapperOutputDir.setToolTipText("Directory on your system where "
                                           + "files can be written.");

        txtWrapperOutputDir = new Text(this, SWT.BORDER);
        FormData fd_txtWrapperOutputDir = new FormData();
        fd_txtWrapperOutputDir.left = new FormAttachment(0, 172);
        fd_txtWrapperOutputDir.right = new FormAttachment(100, -55);
        fd_txtWrapperOutputDir.top = new FormAttachment(lblWrapperOutputDir,
                                                              0, SWT.CENTER);
        txtWrapperOutputDir.setLayoutData(fd_txtWrapperOutputDir);
        txtWrapperOutputDir.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button cmdBrowseOutputDir = new Button(this, SWT.NONE);
        cmdBrowseOutputDir.setAlignment(SWT.CENTER);
        FormData fd_cmdBrowseOutputDir = new FormData();
        fd_cmdBrowseOutputDir.top = new FormAttachment(lblWrapperOutputDir, 
                                                       -6, SWT.TOP);
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
            public void widgetSelected(SelectionEvent arg0) {
                browseForOutputDir();
            }
        });
        cmdBrowseOutputDir.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        Label lblWrapperArguments = new Label(this, SWT.RIGHT);
        lblWrapperArguments.setFont(UIUtils.getDefaultLabelFont());
        lblWrapperArguments.setAlignment(SWT.RIGHT);
        lblWrapperArguments.setText("Arguments to wrapper:");
        lblWrapperArguments.setToolTipText("Command line arguments that should "
                                           + "be passed to the wrapper. See "
                                           + "below for more information.");
        FormData fd_lblWrapperArguments = new FormData();
        fd_lblWrapperArguments.right = new FormAttachment(0, 170);
        fd_lblWrapperArguments.top = new FormAttachment(0, 122);
        lblWrapperArguments.setLayoutData(fd_lblWrapperArguments);

        txtWrapperArgs = new Text(this, SWT.BORDER);
        FormData fd_txtWrapperArgs = new FormData();
        fd_txtWrapperArgs.left = new FormAttachment(0, 172);
        fd_txtWrapperArgs.right = new FormAttachment(100, -55);
        fd_txtWrapperArgs.top = new FormAttachment(lblWrapperArguments, 0,
                                                   SWT.CENTER);
        txtWrapperArgs.setLayoutData(fd_txtWrapperArgs);
        txtWrapperArgs.addKeyListener(UIUtils.createCloseKeyListener(shell));
        
        btnWrapperAnyLV = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperAnyLV = new FormData();
        fd_btnWrapperAnyLV.left = new FormAttachment(0, 170);
        fd_btnWrapperAnyLV.right = new FormAttachment(100, -31);
        fd_btnWrapperAnyLV.top = new FormAttachment(txtWrapperArgs, 6);
        btnWrapperAnyLV.setLayoutData(fd_btnWrapperAnyLV);
        btnWrapperAnyLV.setText("Wrapper can handle any SBML Level/Version");
        btnWrapperAnyLV.addKeyListener(UIUtils.createCloseKeyListener(shell));

        btnWrapperThreadsOK = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperThreadsOK = new FormData();
        fd_btnWrapperThreadsOK.left = new FormAttachment(0, 170);
        fd_btnWrapperThreadsOK.right = new FormAttachment(100, -31);
        fd_btnWrapperThreadsOK.top = new FormAttachment(btnWrapperAnyLV, 2);
        btnWrapperThreadsOK.setLayoutData(fd_btnWrapperThreadsOK);
        btnWrapperThreadsOK.setText("Wrapper can be run in parallel threads");
        btnWrapperThreadsOK.addKeyListener(UIUtils.createCloseKeyListener(shell));
        btnWrapperThreadsOK.setSelection(true);

        Label lblUsageInfo1 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo1 = new FormData();
        fd_lblUsageInfo1.left = new FormAttachment(0, 10);
        fd_lblUsageInfo1.right = new FormAttachment(100, -10);
        fd_lblUsageInfo1.bottom = new FormAttachment(100, -10);
        fd_lblUsageInfo1.top = new FormAttachment(0, 195);
        lblUsageInfo1.setLayoutData(fd_lblUsageInfo1);
        Color gray = display.getSystemColor(SWT.COLOR_DARK_GRAY);
        lblUsageInfo1.setForeground(gray);
        FontData[] fontData = lblUsageInfo1.getFont().getFontData();
        for (int i = 0; i < fontData.length; ++i) {
            fontData[i].setHeight(UIUtils.scaledFontSize(11));
        }
        final Font newFont = new Font(display, fontData);
        lblUsageInfo1.setFont(newFont);
        lblUsageInfo1.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                newFont.dispose();
            }
        });
        lblUsageInfo1.setText("You can use the following substitution codes in "
                              + "the wrapper command line arguments:");

        Label lblUsageInfo2 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo2 = new FormData();
        fd_lblUsageInfo2.left = new FormAttachment(0, 10);
        fd_lblUsageInfo2.right = new FormAttachment(100, -10);
        fd_lblUsageInfo2.bottom = new FormAttachment(100, -10);
        fd_lblUsageInfo2.top = new FormAttachment(0, 218);
        lblUsageInfo2.setLayoutData(fd_lblUsageInfo2);
        lblUsageInfo2.setForeground(gray);
        lblUsageInfo2.setFont(newFont);
        lblUsageInfo2.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                newFont.dispose();
            }
        });
        lblUsageInfo2.setText(
            "\t%d \t= path to the directory containting all test cases"
            + "\n\t%n \t= current test case number (of the form NNNNN)"
            + "\n\t%o \t= directory where the CSV output file should be written"
            + "\n\t%l \t= the SBML Level to be used"
            + "\n\t%v \t= the SBML Version to be used");

        Label lblUsageInfo3 = new Label(this, SWT.WRAP);
        FormData fd_lblUsageInfo3 = new FormData();
        fd_lblUsageInfo3.left = new FormAttachment(0, 10);
        fd_lblUsageInfo3.right = new FormAttachment(100, -10);
        fd_lblUsageInfo3.bottom = new FormAttachment(100, -10);
        fd_lblUsageInfo3.top = new FormAttachment(0, 296);
        lblUsageInfo3.setLayoutData(fd_lblUsageInfo3);
        lblUsageInfo3.setForeground(gray);
        lblUsageInfo3.setFont(newFont);
        lblUsageInfo3.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                newFont.dispose();
            }
        });
        lblUsageInfo3.setText(
            "Each test case consists of an SBML file and a settings file. The "
            + "files are located in the directory named %d/%n. The SBML file for "
            + "the test model is named '%n-sbml-l%lv%v.xml'. (Example: "
            + "'00123-sbml-l2v3.xml'.) The settings file is named "
            + "'%n-settings.txt' in the same directory. The wrapper or  "
            + "application must be instructed to write out the results into a "
            + "file named '%o/%n.csv' so that the SBML Test Runner can find it.");
    }


    protected void browseForOutputDir()
    {
        DirectoryDialog dlg = new DirectoryDialog(getShell());
        dlg.setText("Browse For Output dir");
        dlg.setMessage("Please select the wrapper output directory.");
        dlg.setFilterPath(txtWrapperOutputDir.getText());
        String selectedDirectory = dlg.open();
        if (selectedDirectory != null)
            txtWrapperOutputDir.setText(selectedDirectory);
    }


    protected void editUnsupportedTags()
    {
       DialogFilterTags dialog = new DialogFilterTags(getShell(), SWT.None);
       dialog.setDescription("Please select component and test tags to include.");
       dialog.setComponentTags(TestSuiteSettings.loadDefault().getSuite().getComponentTags());
       dialog.setTestTags(TestSuiteSettings.loadDefault().getSuite().getTestTags());
       dialog.center(getShell().getBounds());
       dialog.setSelectedTags(Util.split(txtUnsupportedTags.getText()));
       final Vector<String> selection = dialog.open();
       if (selection != null)
       {
           txtUnsupportedTags.setText(Util.toString(selection));
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
