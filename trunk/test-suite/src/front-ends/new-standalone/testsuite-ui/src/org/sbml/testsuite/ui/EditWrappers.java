//
// @file EditWrappers.java
// @brief Composite for editing a wrapper configuration
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

import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.Util;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * Composite for editing a wrapper configuration
 */
public class EditWrappers
    extends Composite
{
    private final Text   txtName;
    private final Text   txtWrapperOutputDir;
    private final Text   txtUnsupportedTags;
    private final Text   txtWrapper;
    private final Text   txtWrapperArgs;
    private final Button btnWrapperCanRun;


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
                                                 btnWrapperCanRun.getSelection());

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
        btnWrapperCanRun.setSelection(config.isSupportsAllVersions());
    }


    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public EditWrappers(Composite parent, int style)
    {
        super(parent, style);
        setLayout(new FormLayout());

        Label lblName = new Label(this, SWT.NONE);
        FormData fd_lblName = new FormData();
        fd_lblName.top = new FormAttachment(0, 10);
        lblName.setLayoutData(fd_lblName);
        lblName.setText("Name:");

        txtName = new Text(this, SWT.BORDER);
        fd_lblName.right = new FormAttachment(txtName, -6);
        FormData fd_txtName = new FormData();
        fd_txtName.left = new FormAttachment(0, 161);
        txtName.setLayoutData(fd_txtName);

        Label lblWrapperOutputDirectory = new Label(this, SWT.NONE);
        FormData fd_lblWrapperOutputDirectory = new FormData();
        fd_lblWrapperOutputDirectory.top = new FormAttachment(lblName, 12);
        fd_lblWrapperOutputDirectory.right = new FormAttachment(lblName, 0,
                                                                SWT.RIGHT);
        lblWrapperOutputDirectory.setLayoutData(fd_lblWrapperOutputDirectory);
        lblWrapperOutputDirectory.setText("Wrapper Output Directory: ");

        txtWrapperOutputDir = new Text(this, SWT.BORDER);
        FormData fd_txtWrapperOutputDir = new FormData();
        fd_txtWrapperOutputDir.top = new FormAttachment(txtName, 6);
        fd_txtWrapperOutputDir.left = new FormAttachment(
                                                         lblWrapperOutputDirectory,
                                                         6);
        txtWrapperOutputDir.setLayoutData(fd_txtWrapperOutputDir);

        Button cmdBrowseOutputDir = new Button(this, SWT.NONE);
        cmdBrowseOutputDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                browseForOutputDir();
            }
        });
        fd_txtWrapperOutputDir.right = new FormAttachment(cmdBrowseOutputDir,
                                                          -6);
        fd_txtName.right = new FormAttachment(cmdBrowseOutputDir, 0, SWT.RIGHT);
        fd_txtName.bottom = new FormAttachment(cmdBrowseOutputDir, -4);
        FormData fd_cmdBrowseOutputDir = new FormData();
        fd_cmdBrowseOutputDir.top = new FormAttachment(
                                                       lblWrapperOutputDirectory,
                                                       -5, SWT.TOP);
        fd_cmdBrowseOutputDir.right = new FormAttachment(100, -10);
        cmdBrowseOutputDir.setLayoutData(fd_cmdBrowseOutputDir);
        cmdBrowseOutputDir.setText("...");

        Label lblUnsupportedTags = new Label(this, SWT.NONE);
        FormData fd_lblUnsupportedTags = new FormData();
        fd_lblUnsupportedTags.top = new FormAttachment(
                                                       lblWrapperOutputDirectory,
                                                       12);
        fd_lblUnsupportedTags.right = new FormAttachment(lblName, 0, SWT.RIGHT);
        lblUnsupportedTags.setLayoutData(fd_lblUnsupportedTags);
        lblUnsupportedTags.setText("Unsupported Tags: ");

        txtUnsupportedTags = new Text(this, SWT.BORDER);
        FormData fd_txtUnsupportedTags = new FormData();
        fd_txtUnsupportedTags.right = new FormAttachment(txtWrapperOutputDir,
                                                         0, SWT.RIGHT);
        fd_txtUnsupportedTags.top = new FormAttachment(txtWrapperOutputDir, 6);
        fd_txtUnsupportedTags.left = new FormAttachment(txtName, 0, SWT.LEFT);
        txtUnsupportedTags.setLayoutData(fd_txtUnsupportedTags);

        Button cmdEditTags = new Button(this, SWT.NONE);
        cmdEditTags.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                editUnsupportedTags();
            }
        });
        cmdEditTags.setText("...");
        FormData fd_cmdEditTags = new FormData();
        fd_cmdEditTags.top = new FormAttachment(lblUnsupportedTags, -5, SWT.TOP);
        fd_cmdEditTags.left = new FormAttachment(cmdBrowseOutputDir, 0,
                                                 SWT.LEFT);
        cmdEditTags.setLayoutData(fd_cmdEditTags);

        Label lblWrapper = new Label(this, SWT.NONE);
        lblWrapper.setText("Wrapper: ");
        FormData fd_lblWrapper = new FormData();
        fd_lblWrapper.right = new FormAttachment(lblName, 0, SWT.RIGHT);
        lblWrapper.setLayoutData(fd_lblWrapper);

        txtWrapper = new Text(this, SWT.BORDER);
        fd_lblWrapper.top = new FormAttachment(txtWrapper, 3, SWT.TOP);
        FormData fd_txtWrapper = new FormData();
        fd_txtWrapper.right = new FormAttachment(txtWrapperOutputDir, 0,
                                                 SWT.RIGHT);
        fd_txtWrapper.top = new FormAttachment(txtUnsupportedTags, 6);
        fd_txtWrapper.left = new FormAttachment(txtName, 0, SWT.LEFT);
        txtWrapper.setLayoutData(fd_txtWrapper);

        Button cmdBrowseWrapper = new Button(this, SWT.NONE);
        cmdBrowseWrapper.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                browseForWrapper();
            }
        });
        cmdBrowseWrapper.setText("...");
        FormData fd_cmdBrowseWrapper = new FormData();
        fd_cmdBrowseWrapper.top = new FormAttachment(cmdEditTags, 2);
        fd_cmdBrowseWrapper.left = new FormAttachment(cmdBrowseOutputDir, 0,
                                                      SWT.LEFT);
        cmdBrowseWrapper.setLayoutData(fd_cmdBrowseWrapper);

        Label lblWrapperArguments = new Label(this, SWT.NONE);
        lblWrapperArguments.setText("Wrapper Arguments:");
        FormData fd_lblWrapperArguments = new FormData();
        fd_lblWrapperArguments.right = new FormAttachment(lblName, 0, SWT.RIGHT);
        lblWrapperArguments.setLayoutData(fd_lblWrapperArguments);

        txtWrapperArgs = new Text(this, SWT.BORDER);
        fd_lblWrapperArguments.top = new FormAttachment(txtWrapperArgs, 3,
                                                        SWT.TOP);
        FormData fd_txtWrapperArgs = new FormData();
        fd_txtWrapperArgs.right = new FormAttachment(txtName, 0, SWT.RIGHT);
        fd_txtWrapperArgs.top = new FormAttachment(txtWrapper, 6);
        fd_txtWrapperArgs.left = new FormAttachment(txtName, 0, SWT.LEFT);
        txtWrapperArgs.setLayoutData(fd_txtWrapperArgs);

        btnWrapperCanRun = new Button(this, SWT.CHECK);
        FormData fd_btnWrapperCanRun = new FormData();
        fd_btnWrapperCanRun.top = new FormAttachment(txtWrapperArgs, 6);
        fd_btnWrapperCanRun.right = new FormAttachment(txtName, -21, SWT.RIGHT);
        fd_btnWrapperCanRun.left = new FormAttachment(txtName, 0, SWT.LEFT);
        btnWrapperCanRun.setLayoutData(fd_btnWrapperCanRun);
        btnWrapperCanRun.setText("Wrapper can run any SBML Level / Version");

        CLabel lblNewLabel = new CLabel(this, SWT.NONE);
        FormData fd_lblNewLabel = new FormData();
        fd_lblNewLabel.bottom = new FormAttachment(100, -6);
        fd_lblNewLabel.right = new FormAttachment(txtName, 0, SWT.RIGHT);
        fd_lblNewLabel.top = new FormAttachment(0, 164);
        fd_lblNewLabel.left = new FormAttachment(0, 10);
        lblNewLabel.setLayoutData(fd_lblNewLabel);
        lblNewLabel.setText("You can use the following codes in the command line:"
            + "\n"
            + "\n\t%d \t= path to the directory containting all test cases"
            + "\n\t%n \t= current test case number (of the form NNNNN)"
            + "\n\t%o \t= directory where the CSV output file should be written"
            + "\n\t%l \t= the SBML Level to be used"
            + "\n\t%v \t= the SBML Version to be used"
            + "\n"
            + "\nThe current test case SBML file will be located in the directory %d/%n and be named '%n-sbml-l%lv%v.xml, "
            + "\nwhere %l is the Level and %v is the Version of the  SBML file (for example '00123-sbml-l2v3.xml'). The "
            + "\ntest settings file will be named '%n-settings.txt' in the same directory (for example '00123-settings.txt'). "
            + "\nThe application must be instructed to write out the results into a file named '%o/%n.csv' (for example '00123.csv')");

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
       DialogFilterTags dialog = new DialogFilterTags(getShell(),
                                                      SWT.None);
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
