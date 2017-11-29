//
// @file   PreferenceDialog.java
// @brief  PreferenceDialog for the sbml test suite
// @author Frank T. Bergmann
// @author Michael Hucka
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

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.WrapperConfig;


/**
 * PreferenceDialog for the sbml test suite
 */
public class PreferenceDialog
    extends Dialog
{
    protected Shell            shell;
    private TestSuiteSettings  result;
    private TestSuiteSettings  previousResult;
    private EditListOfWrappers wrappersEditor;
    private Text               txtCasesDir;
    private String             origCasesDir;
    private Button             btnDeleteFiles;
    private Button             btnOverrideNumThreads;
    private Button             btnAutoCheckUpdates;
    private Button             btnAutoWatchFile;
    private Text               txtNumThreads;
    private int                previousNumThreads;
    /** Tracks whether user has already indicated whether to save changes.
        The default is true because we check for changes and ask for
        confirmation *unless* the user clicked on Save or Cancel explicitly.
    */
    private boolean            needConfirmSave = true;


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
        previousNumThreads = UIUtils.getIntPref("numThreads",
                                                TaskExecutor.defaultNumThreads(), this);
        createContents();
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        int height = (UIUtils.isLinux() ? 720 : 650);

        shell = new Shell(getParent(), getStyle());
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setMinimumSize(new Point(670, 550));
        shell.setSize(770, height);
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

        // This is a total hack, but I can't see how else to avoid overly-tight
        // space here except if we completely change the layout model.

        String lbl = "Test Cases Directory:";
        if (!UIUtils.isMacOSX()) lbl += " ";
        lblTestCasesDir.setText(lbl);

        String toolTip =
            "The folder/directory where the SBML Test Suite case files are "
            + "located on your computer.";

        lblTestCasesDir.setToolTipText(toolTip);

        txtCasesDir = new Text(outerComp, SWT.BORDER);
        txtCasesDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, 
                                               false, 3, 1));

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
        if (UIUtils.isMacOSX())
            btnBrowseCasesDir.setBounds(2, 2, 58, 25);
        else
            btnBrowseCasesDir.setBounds(5, 0, 50, 25);
        btnBrowseCasesDir.setText("Edit");
        btnBrowseCasesDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                browseForCasesDir();
            }
        });
        btnBrowseCasesDir.setToolTipText(toolTip);

        Label sep1 = new Label(outerComp, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_sep1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
        gd_sep1.verticalIndent = 5;
        gd_sep1.horizontalIndent = 0;
        sep1.setLayoutData(gd_sep1);

        wrappersEditor = new EditListOfWrappers(outerComp, SWT.NONE);
        GridData gd_wrappersEditor = new GridData(SWT.FILL, SWT.TOP, true,
                                                  true, 5, 1);
        gd_wrappersEditor.heightHint = 430;
        gd_wrappersEditor.widthHint = 300;
        gd_wrappersEditor.minimumWidth = 300;
        gd_wrappersEditor.horizontalIndent = 0;
        gd_wrappersEditor.verticalIndent = 0;
        wrappersEditor.setLayoutData(gd_wrappersEditor);

        Label sep2 = new Label(outerComp, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_sep2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
        gd_sep2.verticalIndent = 5;
        gd_sep2.horizontalIndent = 0;
        sep2.setLayoutData(gd_sep2);

        btnDeleteFiles = new Button(outerComp, SWT.CHECK);
        GridData gd_btnDeleteFiles = new GridData(SWT.LEFT, SWT.CENTER, false,
                                                  false, 5, 1);
        gd_btnDeleteFiles.verticalIndent = 5;
        btnDeleteFiles.setLayoutData(gd_btnDeleteFiles);
        btnDeleteFiles.setText("Delete existing output files before "
                               + "each invocation of a wrapper "
                               + "(normally preferrable)");
        btnDeleteFiles.setToolTipText(
            "When turned on, this makes the SBML Test Runner delete each "
            + "output result file prior to invoking the wrapper for a "
            + "given test case.");
        btnDeleteFiles.setSelection(UIUtils.getBooleanPref("deleteFiles", true, this));
        btnDeleteFiles.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                UIUtils.saveBooleanPref("deleteFiles",
                                        btnDeleteFiles.getSelection(), this);
            }
        });

        Composite compThreads = new Composite(outerComp, SWT.NONE);
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 5, 1);
        compThreads.setLayoutData(gd);
        GridLayout gl_compThreads = new GridLayout(2, false);
        gl_compThreads.marginWidth = 0;
        gl_compThreads.marginTop = 1;
        gl_compThreads.marginRight = 0;
        gl_compThreads.marginHeight = 0;
        gl_compThreads.horizontalSpacing = 0;
        compThreads.setLayout(gl_compThreads);

        btnOverrideNumThreads = new Button(compThreads, SWT.CHECK);
        btnOverrideNumThreads.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
                                                         true, true, 1, 1));
        btnOverrideNumThreads.setText("Explicitly set number of threads used "
                                      + "when running wrappers in parallel:");
        String overrideNumThreadsTip = 
            "When a wrapper is defined with the option to run in parallel set "
            + "to true, the SBML Test Runner chooses the number of parallel "
            + "threads equal to one less than the number of CPU cores on the "
            + "current computer. Use this option to override that default.";

        btnOverrideNumThreads.setToolTipText(overrideNumThreadsTip);
        btnOverrideNumThreads.setSelection(UIUtils.getBooleanPref("overrideNumThreads",
                                                                  false, this));

        txtNumThreads = new Text(compThreads, SWT.BORDER);
        GridData gd_tnt = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        gd_tnt.widthHint = 25;
        txtNumThreads.setLayoutData(gd_tnt);
        if (btnOverrideNumThreads.getSelection())
            txtNumThreads.setText(Integer.toString(previousNumThreads));
        else
            txtNumThreads.setText(Integer.toString(TaskExecutor.defaultNumThreads()));
        txtNumThreads.setEnabled(btnOverrideNumThreads.getSelection());
        txtNumThreads.setToolTipText(overrideNumThreadsTip);

        btnOverrideNumThreads.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                if (btnOverrideNumThreads.getSelection())
                    txtNumThreads.setEnabled(true);
                else
                    txtNumThreads.setEnabled(false);
            }
        });

        btnAutoWatchFile = new Button(outerComp, SWT.CHECK);
        GridData gd_btnAutoWatchFile = new GridData(SWT.LEFT, SWT.CENTER,
                                                          false, false, 5, 1);
        gd_btnAutoWatchFile.verticalIndent = 0;
        btnAutoWatchFile.setLayoutData(gd_btnAutoWatchFile);
        btnAutoWatchFile.setText("Watch displayed results file and automatically"
                                       + " reanalyze test case if the file changes");
        btnAutoWatchFile.setToolTipText(
            "When enabled, this option makes the Test Runner watch the output "
            + "file for the test case currently being displayed and automatically "
            + "reload the results if the file changes. Useful if you need to "
            + "experiment with the output for a test case and do not want to "
            + "keep re-running the case using the Test Runner.");
        btnAutoWatchFile.setSelection(UIUtils.getBooleanPref("autoWatchFile",
                                                                true, this));
        btnAutoWatchFile.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                UIUtils.saveBooleanPref("autoWatchFile",
                                        btnAutoWatchFile.getSelection(), this);
            }
        });

        btnAutoCheckUpdates = new Button(outerComp, SWT.CHECK);
        GridData gd_btnAutoCheckUpdates = new GridData(SWT.LEFT, SWT.CENTER,
                                                          false, false, 5, 1);
        gd_btnAutoCheckUpdates.verticalIndent = 0;
        btnAutoCheckUpdates.setLayoutData(gd_btnAutoCheckUpdates);
        btnAutoCheckUpdates.setText("Always check for new versions of "
                                       + "test cases when application starts");
        btnAutoCheckUpdates.setToolTipText(
            "Check for updated releases of the SBML Test Suite test cases "
            + "each time the SBML Test Runner is started.");
        btnAutoCheckUpdates.setSelection(UIUtils.getBooleanPref("autoCheckUpdates",
                                                                true, this));
        btnAutoCheckUpdates.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                UIUtils.saveBooleanPref("autoCheckUpdates",
                                        btnAutoCheckUpdates.getSelection(), this);
            }
        });

        Label sep3 = new Label(outerComp, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_sep3 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
        gd_sep3.verticalIndent = 5;
        gd_sep3.horizontalIndent = 0;
        sep3.setLayoutData(gd_sep3);

        Composite compSaveCancelButtons = new Composite(outerComp, SWT.NONE);
        compSaveCancelButtons.setLayout(new GridLayout(1, false));
        compSaveCancelButtons.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false,
                                             false, 3, 1));

        Composite compButtons = new Composite(outerComp, SWT.NONE);
        compButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false,
                                               false, 2, 1));

        Button btnSave = new Button(compButtons, SWT.NONE);
        btnSave.setBounds(3, 3, 75, 30);
        btnSave.setText("Save");
        btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                result = getResult(true, shell, wrappersEditor);
                if (!wrapperVerified(result.getLastWrapper(),
                                     previousResult.getLastWrapper()))
                    return;
                if (!validateSettings())
                    return;
                needConfirmSave = false;
                hide();
            }
        });
        btnSave.setFocus();

        Button btnCancel = new Button(compButtons, SWT.NONE);
        btnCancel.setBounds(85, 3, 75, 30);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                needConfirmSave = false;
                previousResult.saveAsDefault();
                result = null;
                hide();
            }
        });

        shell.setDefaultButton(btnSave);
        outerComp.setTabList(new Control[]{compButtons, txtCasesDir,
                                           wrappersEditor, compBrowse,
                                           btnDeleteFiles, compThreads});
        compThreads.setTabList(new Control[]{btnOverrideNumThreads,
                                             txtNumThreads});

        // The custom close listeners are because we don't dispose the widget
        // ourselves; we only hide it, because callers may need to retrieve
        // data.  We let callers explicitly dispose the widget when ready.

        Listener closeListener = new Listener() {
            public void handleEvent(Event event)
            {
                if (needConfirmSave && settingsHaveChanged())
                    result = getResult(confirmSave(), shell, wrappersEditor);
                hide();
                needConfirmSave = false;
                event.doit = false;
            }
        };

        shell.addListener(SWT.Close, closeListener);

        // Add keyboard bindings for cancelling out of this: command-. on
        // Macs and control-w elsewhere.  (This actually will make command-w
        // do the same thing on Macs, but that's okay.)

        final Listener closeKeyListener = new Listener() {
            @Override
            public void handleEvent (final Event event)
            {
                if (UIUtils.isModifier(event)
                    && ((UIUtils.isMacOSX() && event.keyCode == '.')
                        || event.keyCode == 'w'))
                {
                    shell.close();
                }
            }
        };
        final Display display = shell.getDisplay();
        display.addFilter(SWT.KeyDown, closeKeyListener);
        shell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent notUsed)
            {
                display.removeFilter(SWT.KeyDown, closeKeyListener);
            }
        });

        shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));

        shell.layout();
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public TestSuiteSettings open()
    {
        shell.open();
        Display display = getParent().getDisplay();
        previousResult = getTestSuiteSettings(false);
        needConfirmSave = true;
        while (!shell.isDisposed() && shell.isEnabled())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    public void dispose()
    {
        shell.close();
        shell.dispose();
    }


    public void hide()
    {
        shell.setVisible(false);
        shell.setEnabled(false);
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


    public String getCasesDir()
    {
        return txtCasesDir.getText();
    }


    public boolean confirmSave()
    {
        return Tell.saveCancel(shell, "The configuration has been modified.\n"
                               + "Save your changes?");
    }


    private boolean wrapperVerified(WrapperConfig newWrapper,
                                    WrapperConfig oldWrapper)
    {
        if (newWrapper == null) 
            return false;

        if ("-- no wrapper --".equals(newWrapper.getName()))
            return true;

        if (newWrapper.isViewOnly())
            return true;

        String program = newWrapper.getProgram();

        if (program == null || program.isEmpty())
            return Tell.informWithOverride(shell,
                                           "The wrapper program was left unspecified "
                                           + "\nin the wrapper configuration; consequently, "
                                           + "\nthe wrapper cannot be used.");
        else
        {
            File path = new File(program);
            if (! path.exists())
                return Tell.informWithOverride(shell,
                                               "The path to the wrapper specified in "
                                               + "\nthe wrapper configuration does not appear "
                                               + "\nto exist; consequently, the wrapper "
                                               + "\ncannot be used.");
            else if (! path.isFile())
                return Tell.informWithOverride(shell,
                                               "The path given for the wrapper program "
                                               + "\ndoes not appear to be a valid file; "
                                               + "\nconsequently, the wrapper cannot be used.");
            else if (! path.canRead())
                return Tell.informWithOverride(shell,
                                               "The file for the program specified in "
                                               + "\nthe wrapper configuration does not appear "
                                               + "\nto be readable; consequently, "
                                               + "\nthe wrapper cannot be used.");
            else if (! path.canExecute())
                return Tell.informWithOverride(shell,
                                               "The program specified in the "
                                               + "\nwrapper configuration does not appear "
                                               + "\nto be executable; consequently, "
                                               + "\nthe wrapper cannot be run.");
        }

        String outputPath = newWrapper.getOutputPath();
        if (outputPath == null || outputPath.isEmpty())
            return Tell.informWithOverride(shell,
                                           "The output directory was left unspecified "
                                           + "\nin the wrapper configuration; consequently, "
                                           + "\nthe wrapper cannot be used.");
        else
        {
            File path = new File(outputPath);
            if (! path.exists())
            {
                if (! path.mkdir())
                    return Tell.informWithOverride(shell,
                                                   "Unable to create the directory specified"
                                                   + "\nin the wrapper configuration; consequently,"
                                                   + "\nthe wrapper cannot be used.");
            }
            else if (! path.isDirectory())
                return Tell.informWithOverride(shell,
                                               "The output directory specified in the "
                                               + "\nwrapper configuration does not appear "
                                               + "\nto be a valid directory; consequently, "
                                               + "\nthe wrapper cannot be used.");
            else if (! path.canRead())
                return Tell.informWithOverride(shell,
                                               "The output directory specified in the "
                                               + "\nwrapper configuration does not appear "
                                               + "\nto be readable; consequently, "
                                               + "\nthe wrapper cannot be used.");
            else if (! path.canWrite())
                return Tell.informWithOverride(shell,
                                               "The output directory specified in the "
                                               + "\nwrapper configuration does not appear "
                                               + "\nto be writable; consequently, "
                                               + "\nthe wrapper cannot be used.");
            // FTB: i'd prefer the following check to go away, when i specify it as a user
            //      i encountered this issue many time running the wrapper, and it does not 
            //      help me. 
            // else if (MarkerFile.exists(path))
            // {
            //     // If we find a marker file in the output, it means the
            //     // output directory probably contains the results of a past
            //     // run of either this wrapper or a different wrapper.  We
            //     // need to warn the user if it looks like the results came
            //     // from a different wrapper program.

            //     String prog = MarkerFile.getContents(path);
            //     String question = "Choose OK to associate the"
            //         + "\nresults with the current wrapper, or choose"
            //         + "\nCancel to go back to the Preferences panel"
            //         + "\nso that you can change the directory.";
            //     String intro;

            //     if (prog == null || prog.length() == 0)
            //         intro = "The output directory you have chosen appears"
            //             + "\nto contain the results of a previous run of"
            //             + "\nthe SBML Test Runner. ";
            //     else
            //         intro = "The output directory you have chosen appears"
            //             + "\nto contain the results of a previous run of"
            //             + "\n\n   " + prog + "\n";

            //     // The contents of the marker file should never be null,
            //     // because we're the program that writes it in the first
            //     // place.  But, we have to program defensively.

            //     if (prog == null || prog.length() == 0)
            //     {
            //         // Bummer, it's empty.  Can't do much, but only bother the
            //         // user if they are changing the wrapper program.

            //         if (!oldWrapper.getProgram().equals(newWrapper.getProgram()))
            //             return Tell.saveCancel(shell, intro + question);
            //     }
            //     else if (!newWrapper.getProgram().equals(prog))
            //     {
            //         // If the results were generated by a different program,
            //         // confirm association of the results with *this* wrapper.

            //         String details = "Since the results in this directory appear"
            //             + "\nto have come from a different wrapper, the"
            //             + "\nSBML Test Runner needs your help to determine"
            //             + "\nwhether it makes sense to associate those"
            //             + "\nresults with the currently-selected wrapper,"
            //             + "\n\n    " + newWrapper.getProgram() + "\n";

            //         return Tell.saveCancel(shell, intro + details + question);
            //     }
            // }
        }

        String args = newWrapper.getArguments();
        if (args == null || args.isEmpty())
            return Tell.informWithOverride(shell,
                                           "The arguments to the wrapper program have "
                                           + "\nbeen left undefined, which makes it impossible "
                                           + "\nfor the SBML Test Runner to invoke the wrapper "
                                           + "\nwith different test cases. Please provide"
                                           + "\nsuitable arguments.");

        return true;
    }


    private boolean validateSettings()
    {
        if (!btnOverrideNumThreads.getSelection())
        {
            unsetNumThreads();
            return true;
        }

        // User checked the override-number-of-threads box.

        int numThreads = -1;
        try
        {
            numThreads = Math.abs(Integer.valueOf(txtNumThreads.getText()));
        }
        catch (NumberFormatException e)
        {
            // Probably a typo or something wrong. Ignore the value.
            unsetNumThreads();
            return true;
        }

        if (numThreads == 0)
        {
            // A value of 0 is unusable. Ignore it.
            unsetNumThreads();
            return true;
        }

        UIUtils.saveBooleanPref("overrideNumThreads", true, this);
        UIUtils.saveIntPref("numThreads", numThreads, this);
        return true;
    }


    private void unsetNumThreads()
    {
        btnOverrideNumThreads.setSelection(false);
        UIUtils.saveBooleanPref("overrideNumThreads", false, this);
        txtNumThreads.setText(Integer.toString(previousNumThreads));
        UIUtils.saveIntPref("numThreads", previousNumThreads, this);
    }


    public boolean settingsHaveChanged()
    {
        if (origCasesDir == null)
            return false;

        return wrappersEditor.changesPending()
            || ! (origCasesDir.equals(txtCasesDir.getText()));
    }


    public void setDeleteOutputFiles(boolean yesNo)
    {
        btnDeleteFiles.setSelection(yesNo);
        UIUtils.saveBooleanPref("deleteFiles", yesNo, this);
    }


    public boolean getDeleteOutputFiles()
    {
        return btnDeleteFiles.getSelection();
    }


    public boolean getOverrideNumThreads()
    {
        return btnOverrideNumThreads.getSelection();
    }


    public int getNumberOfThreads()
    {
        try
        {
            return Integer.valueOf(txtNumThreads.getText());
        }
        catch (NumberFormatException e)
        {
            return previousNumThreads;
        }
    }
}
