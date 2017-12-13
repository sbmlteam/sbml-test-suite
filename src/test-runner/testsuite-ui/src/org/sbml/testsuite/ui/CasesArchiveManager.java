//
// @file   CasesArchiveManager.java
// @brief  Class implementing unpacking, downloading and updating of test cases
// @author Michael Hucka
// @date   2013-11-21 <mhucka@caltech.edu>
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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.sbml.testsuite.core.CancelCallback;
import org.sbml.testsuite.core.TestSuite;
import org.sbml.testsuite.core.UpdateCallback;
import org.sbml.testsuite.core.Util;


/**
 * Filter dialog for test / component tags
 */
public class CasesArchiveManager
{
    private final String INTERNAL_ARCHIVE = "sbml-semantic-test-cases.zip";
    private final String TEMP_ZIP_FILE_NAME = "testcasearchive.zip";

    private final Display display;
    private Shell parentShell;
    private CasesArchiveData latestArchiveData;
    private Date userDefaultCasesDate;
    private TaskExecutor executor = new TaskExecutor(1);
    private StatusDialog currentDialog;


    /**
     * Internal specialized class for creating dialogs that are hooked into
     * the processes later below.  This can show either a regular progress
     * bar or an indeterminate progress bar.  When it is created, the dialog
     * is not shown -- it is set to invisible.  To show it, callers should
     * call showAndWait().
     */
    class StatusDialog
        extends Dialog
    {
        private Shell             shell;
        private CustomProgressBar progressBar;
        private Label             message;
        private Label             icon;
        private Runnable          cancelAction;


        /**
         * @param msg The initial message to show in the dialog
         * @param infinite If true, show an indefinite progress bar
         * @param showCancel A callback for the cancel action
         */
        public StatusDialog(String msg, boolean infinite, boolean showCancel)
        {
            super(parentShell, SWT.None);
            createDialog(msg, infinite, showCancel);
        }


        /**
         * Creates the contents of the dialog.
         */
        private void createDialog(String msg, boolean infinite, boolean showCancel)
        {
            int margin = 12;
            int totalWidth = 400;
            int totalHeight = 140 + (!UIUtils.isMacOSX() ? margin/2 : 0);
            int buttonWidth = 80;

            shell = new Shell(getParent(), SWT.CLOSE | SWT.TITLE);
            shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
            shell.setMinimumSize(new Point(totalWidth, totalHeight));
            shell.setSize(totalWidth, totalHeight);
            shell.setText("Updating SBML Test Runner");
            FormLayout fl_shell = new FormLayout();
            fl_shell.marginHeight = 1;
            fl_shell.marginWidth = 1;
            shell.setLayout(fl_shell);

            icon = new Label(shell, SWT.NONE);
            icon.setImage(UIUtils.getImageResource("icon_64x64.png"));
            FormData fd_icon = new FormData();
            fd_icon.width = buttonWidth;
            fd_icon.top = new FormAttachment(0, margin);
            fd_icon.left = new FormAttachment(0, margin);
            icon.setLayoutData(fd_icon);

            message = new Label(shell, SWT.NONE);
            FormData fd_message = new FormData();
            fd_message.width = buttonWidth;
            fd_message.top = new FormAttachment(0, margin);
            fd_message.left = new FormAttachment(icon, margin);
            fd_message.right = new FormAttachment(100, -margin);
            message.setFont(UIUtils.createResizedFont("Verdana", SWT.BOLD, 0));
            message.setLayoutData(fd_message);
            message.setText(msg);

            int fudge = (UIUtils.isMacOSX() ? 3 : 0);

            final Composite compBar = new Composite(shell, SWT.NONE);
            FormData fd_compBar = new FormData();
            fd_compBar.top = new FormAttachment(message, margin);
            fd_compBar.left = new FormAttachment(icon, margin);
            fd_compBar.right = new FormAttachment(100, -margin - fudge);
            compBar.setLayoutData(fd_compBar);
            FillLayout fl = new FillLayout();
            fl.marginHeight = 1;
            fl.marginWidth = 1;
            compBar.setLayout(fl);

            if (infinite)
                progressBar = new CustomProgressBar(compBar, SWT.INDETERMINATE);
            else
                progressBar = new CustomProgressBar(compBar, SWT.HORIZONTAL);
            progressBar.resetSteps();

            Button cmdCancel = new Button(shell, SWT.NONE);
            cmdCancel.setText("Cancel");
            FormData fd_cmdCancel = new FormData();
            fd_cmdCancel.width = buttonWidth;
            fd_cmdCancel.top = new FormAttachment(compBar, margin);
            fd_cmdCancel.right = new FormAttachment(100, -margin);
            cmdCancel.setLayoutData(fd_cmdCancel);
            cmdCancel.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    if (cancelAction != null)
                        display.asyncExec(cancelAction);
                    cancelAction = null;
                }
            });
            cmdCancel.setFocus();
            cmdCancel.setVisible(showCancel);
            shell.setDefaultButton(cmdCancel);

            // Closing the dialog is like invoking cancel, so we hook up a
            // listener to do the action.

            shell.addListener(SWT.Close, new Listener() {
                public void handleEvent(Event event)
                {
                    if (cancelAction != null)
                        display.asyncExec(cancelAction);
                    cancelAction = null;
                }
            });

            shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));
            shell.addKeyListener(UIUtils.createCloseKeyListener(shell));

            shell.layout();
            shell.pack();
            shell.setVisible(false);
        }


        /**
         * Centers the dialog within the given rectangle.
         *
         * @param bounds the rectangle.
         */
        private void center(Rectangle parentBounds)
        {
            if (shell.isDisposed()) return;
            Point size = shell.getSize();
            shell.setLocation(parentBounds.x + (parentBounds.width - size.x) / 2,
                              parentBounds.y + (parentBounds.height - size.y) / 2);
        }


        /**
         * @param a fraction between 0 and 1.0
         */
        public void setProgress(final double value)
        {
            if (shell.isDisposed()) return;
            display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (progressBar.isDisposed()) return;
                    progressBar.updateProgress(value);
                }
            });
        }


        public void setCancelAction(Runnable action)
        {
            cancelAction = action;
        }


        /**
         * Resets the message in the dialog. This uses Display.asyncExec(...)
         * so that the caller doesn't have to worry aobut wrapping this
         * appropriately.
         */
        public void setMessage(final String msg)
        {
            if (shell.isDisposed()) return;
            display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (message.isDisposed()) return;
                    message.setText(msg);
                    if (shell.isDisposed()) return;
                    shell.layout();
                }
            });
        }


        /**
         * Shows the dialog and enter a read-dispatch loop until the dialog
         * is disposed.  This is meant to be invoked after the caller starts
         * a thread to do whatever work is needed.  The thread should call
         * the close() method on the dialog when the work is finished.
         */
        public void showAndWait()
        {
            if (shell.isDisposed() || parentShell.isDisposed())
                return;
            center(parentShell.getBounds());
            shell.open();
            while (shell != null && !shell.isDisposed())
            {
                if (!display.readAndDispatch())
                {
                    display.sleep();
                }
            }
        }


        /**
         * Closes the dialog.  The operation uses Display.syncExec(...) so that
         * callers don't need to worry about wrapping the call in the usual
         * scaffolding.  The use of syncExec(...) is deliberate so that this
         * method doesn't return until the close is actually done.
         */
        public void close()
        {
            if (shell.isDisposed()) return;

            // If this close came as part of a cancel action, the cancel
            // action would have been performed first.  However, if this
            // close() is called as part of the shell close listeners, then
            // we don't want to invoke the cancel action.  Otherwise, doing
            // so can cause the wrong dialog to be closed.
            cancelAction = null;

            // Note: syncExec, not asyncExec, so this method doesn't return
            // until the close is done.
            display.syncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (shell != null && !shell.isDisposed())
                        shell.close();
                }
            });
        }
    }


    /**
     * Constructor for the class.
     */
    public CasesArchiveManager(Shell parent)
    {
        parentShell = parent;
        display = parentShell.getDisplay();
    }


    /**
     * Returns the relative path to the cases datestamp file.
     */
    public String casesDateFileName()
    {
        return "cases" + File.separator + "semantic" + File.separator
            + TestSuite.CASES_DATE_FILE_NAME;
    }


    /**
     * Reads the date stamp file in the given test suite directory and
     * returns either a Date object corresponding to that date, or null if it
     * could not find the date file or something went wrong while trying to
     * read it.
     */
    public Date getCasesDate(File dir)
    {
        // FIXME when this gets called, dir is already cases/semantic, which
        // is confusing and not consistent with casesDateFileName().
        return Util.readArchiveDateFile(dir, TestSuite.CASES_DATE_FILE_NAME);
    }


    /**
     * Reads the date stamp file in the bundled archive of test cases and
     * returns either a Date object corresponding to that date, or null if it
     * could not find the date file or something went wrong.
     *
     * This method will bring up dialogs and tell the user if problems arise.
     */
    public Date getBundledCasesDate()
    {
        try
        {
            InputStream in = UIUtils.getFileResourceStream(INTERNAL_ARCHIVE);
            if (in == null)
            {
                Tell.error(parentShell, "The internal archive of the SBML test\n"
                           + "cases appears to be corrupt or missing. It is\n"
                           + "best not to proceed further. Please report this\n"
                           + "to the developers.", "Unable to find archive.");
                return null;
            }

            final String casesDateFileName = casesDateFileName();
            ZipInputStream zis = new ZipInputStream(in);
            ZipEntry entry = zis.getNextEntry();
            while (entry != null && !casesDateFileName.equals(entry.getName()))
                entry = zis.getNextEntry();
            if (entry != null)
                return Util.readArchiveDateFileStream(zis);
        }
        catch (Exception e)
        {
            Tell.error(parentShell, "The internal archive of the SBML test\n"
                       + "cases appears to be corrupt. It is best not to\n"
                       + "proceed further. Please report this to the\n"
                       + "developers.", UIUtils.stackTraceToString(e));
        }
        return null;
    }


    /**
     * Reads the date stamp file in the test case directory and returns either
     * a Date object corresponding to that date, or null if it could not find
     * the date file or something went wrong while trying to read it.
     *
     * This method will bring up dialogs and tell the user if problems arise.
     */
    public Date getUserDefaultCasesDate()
    {
        try
        {
            File dir = getDefaultCasesDir();
            if (dir == null)
                return null;
            else
                return getCasesDate(dir);
        }
        catch (Exception e)
        {
            // This means something is wrong with the default cases dir or
            // the date file.  We handle this by returning a very old date,
            // which will cause the calling code to treat it as a directory
            // in need of an update.

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                return formatter.parse("2001-01-01");
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }


    /**
     * Extract the archive of test cases that is bundled in the resources
     * directory of our application jar file.
     *
     * This method will bring up dialogs and tell the user if problems arise.
     */
    public void extractBundledCasesArchive()
    {
        File destDir = new File(Util.getUserDir());
        File destFile = new File(destDir, TEMP_ZIP_FILE_NAME);
        String errorMessage = "";
        String extraDetails = "";

        try
        {
            InputStream is = UIUtils.getFileResourceStream(INTERNAL_ARCHIVE);
            FileOutputStream fos = new FileOutputStream(destFile);

            if (is == null)
            {
                errorMessage = "The internal archive of the SBML test cases\n"
                    + "appears to be missing -- something is seriously\n"
                    + "wrong with this copy of the Test Runner. Please\n"
                    + "report this to the developers.";
            }
            else
            {
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                Util.copyInputStream(is, bos);
                unpackArchive(destFile);
                destFile.delete();
            }

            fos.close();
            return;
        }
        catch (FileNotFoundException e)
        {
            errorMessage = "The internal archive of the SBML test cases\n"
                + "appears to be missing -- something is seriously\n"
                + "wrong with this copy of the Test Runner.\n"
                + "Please report this to the developers.\n";
            extraDetails = UIUtils.stackTraceToString(e);
        }
        catch (IOException e)
        {
            errorMessage =  "Encountered unexpected error while reading\n"
                + "the internal copy of the SBML test case archive.\n"
                + "Please report this to the developers.\n";
            extraDetails = UIUtils.stackTraceToString(e);
        }

        Tell.error(parentShell, errorMessage, extraDetails);
    }


    /**
     * Write a short readme file into the test cases directory to help users
     * figure out what it's about.
     */
    public void addReadmeToCasesDir(File destDir)
    {
        File readmeFile = new File(destDir, "README.txt");
        String text = "This directory was created by the SBML Test Runner,"
            + " \npart of the SBML Test Suite.  For more information, please"
            + " \nvisit http://sbml.org/Software/SBML_Test_Suite.";

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(readmeFile));
            writer.append(text);
            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            // Nothing to do.
        }
    }


    /**
     * Returns the directory that contains the semantic test cases on the
     * user's computer.
     */
    public File getDefaultCasesDir()
    {
        return new File(Util.getInternalTestSuiteDir(), "/cases/semantic/");
    }


    /**
     * Runnable to be run as a subthread to unpack an archive file.  This is
     * invoked by unpackArchive(...).
     */
    class UnpackHandler
        implements Runnable
    {
        private File file;
        private AtomicBoolean cancelled;
        private AtomicBoolean success;

        UnpackHandler(File file, AtomicBoolean success, AtomicBoolean cancelled)
        {
            this.file = file;
            this.success = success;
            this.cancelled = cancelled;
        }

        @Override
        public void run()
        {
            // Note: this is the callback for the call to Util.unzipArchive(),
            // not the callback for the status dialog.
            CancelCallback cancelCallback = new CancelCallback() {
                public boolean cancellationRequested()
                {
                    return cancelled.get();
                }
            };

            if (!Util.unzipArchive(file, cancelCallback) && !cancelled.get())
            {
                // We failed to unzip the archive but we didn't get cancelled
                // by the user, which means something went wrong.
                success.set(false);
                currentDialog.close();
                display.syncExec(new Runnable() {
                    @Override
                    public void run()
                    {
                        Tell.error(parentShell,
                                   "Unable to extract cases from internal archive.",
                                   "Encountered an error attempting to unzip the archive\n"
                                   + "of test cases. It may be corrupted, or the destination\n"
                                   + "directory may not be writable or a file system error\n"
                                   + "or other problem may have occurred. The directory that\n"
                                   + "the Test Runner attempted to write in is the following:\n"
                                   + Util.getInternalTestSuiteDir() + "/cases");
                    }
                });
                return;
            }
            else if (cancelled.get())
            {
                display.syncExec(new Runnable() {
                    @Override
                    public void run()
                    {
                        Tell.error(parentShell,
                           "Interrupted -- test cases are incomplete!",
                           "As a result of the unpacking operation having been\n"
                           + "interrupted, the test cases are not fully installed\n"
                           + "or configured. The Test Runner is not in a fully\n"
                           + "operational state. You are advised to either use the\n"
                           + "'Restore test cases' menu item or else exit the\n"
                           + "program and recover manually.");
                    }
                });
                return;
            }

            // If we get here, we think we unzip'ed the archive.  Do a few
            // more sanity checks.
            File casesDir = getDefaultCasesDir();
            if (casesDir.isDirectory())
            {
                addReadmeToCasesDir(Util.getInternalTestSuiteDir());
                success.set(true);
                currentDialog.close();
            }
            else if (!casesDir.isDirectory() && casesDir.exists())
            {
                success.set(false);
                currentDialog.close();
                if (parentShell == null || parentShell.isDisposed()) return;
                display.syncExec(new Runnable() {
                    @Override
                    public void run()
                    {
                        Tell.error(parentShell,
                                   "The SBML test case archive is not in\n"
                                   + "the expected format. Aborting.",
                                   "Perhaps it has been moved or corrupted.");
                    }
                });
            }
        }
    }


    /**
     * Unpack the archive pointed to by @p file, which is assumed to be a
     * zip archive of SBML Test Suite test cases.
     *
     * @returns true if successfully unpacked the archive, false otherwise
     */
    public boolean unpackArchive(File file)
    {
        currentDialog = new StatusDialog("Unpacking test case archive...",
                                         true, true);

        final AtomicBoolean cancelled = new AtomicBoolean(false);
        final AtomicBoolean success = new AtomicBoolean(true);
        currentDialog.setCancelAction(new Runnable() {
            @Override
            public void run()
            {
                cancelled.set(true);
                if (currentDialog == null) return;
                currentDialog.setCancelAction(null);
                currentDialog.close();
            }
        });

        executor.init(false, 1);
        executor.execute(new UnpackHandler(file, success, cancelled));
        currentDialog.showAndWait();
        executor.waitForProcesses(display);
        currentDialog = null;
        if (cancelled.get())
            return false;
        else
        {
            return success.get();
        }
    }


    /**
     * Runnable used to check sf.net for the latest test case archive.  This
     * is called by checkForUpdates(...).
     */
    class ServerCheckHandler
        implements Runnable
    {
        @Override
        public void run()
        {
            // We use cached results *except* if we're running interactively;
            // in that case, we force a refresh, based on the premise that the
            // user wants an update check to be performed anew.

            if (latestArchiveData == null || currentDialog != null)
            {
                try
                {
                    latestArchiveData = ArchiveServer.getLatestArchiveData("semantic");
                }
                catch (Exception e)
                {
                    // If we're running interactively, tell the user we failed.
                    if (currentDialog != null)
                    {
                        currentDialog.close();
                        if (parentShell == null || parentShell.isDisposed())
                            return;
                        display.syncExec(new Runnable() {
                            @Override
                            public void run()
                            {
                                Tell.error(parentShell,
                                   "Problem getting archive data.",
                                   "Either the attempt to connect to the server\n"
                                   + "failed, or else the data read from the server\n"
                                   + "is corrupted in some way.  This can happen\n"
                                   + "if the network is unreachable or the site\n"
                                   + "changed such that our update command no\n"
                                   + "longer works as programmed.");
                            }
                        });
                    }
                    // Whether we tell the user or not, we give up.
                    return;
                }

                if (latestArchiveData == null)
                {
                    // If we're running interactively, tell the user we failed.
                    if (currentDialog != null)
                    {
                        currentDialog.close();
                        if (parentShell == null || parentShell.isDisposed())
                            return;
                        display.syncExec(new Runnable() {
                            @Override
                            public void run()
                            {
                                Tell.error(parentShell,
                                   "Failed to find updated test cases.",
                                   "Something is wrong with the update server\n"
                                   + "or this Test Runner. Please report\n"
                                   + "this to the developers.\n");
                            }
                        });
                    }
                    // Whether we tell the user or not, we give up.
                    return;
                }
            }
            if (currentDialog != null) currentDialog.close();
        }
    }


    /**
     * This blocks until the check is completed. Callers should invoke this
     * in a thread of some kind.
     */
    public boolean checkForUpdates(boolean quietly)
    {
        latestArchiveData = null;
        if (userDefaultCasesDate == null)
            userDefaultCasesDate = getUserDefaultCasesDate();
        if (userDefaultCasesDate == null)
            return false;

        executor.init(false, 1);
        if (quietly)
        {
            currentDialog = null;
            executor.execute(new ServerCheckHandler());
            executor.waitForProcesses(display);
        }
        else
        {
            currentDialog = new StatusDialog("Checking for updates...", true, true);

            final AtomicBoolean cancelled = new AtomicBoolean(false);
            currentDialog.setCancelAction(new Runnable() {
                @Override
                public void run()
                {
                    cancelled.set(true);
                    if (currentDialog == null) return;
                    currentDialog.setCancelAction(null);
                    currentDialog.close();
                }
            });

            executor.execute(new ServerCheckHandler());
            currentDialog.showAndWait();
            executor.waitForProcesses(display);
            currentDialog = null;

            if (cancelled.get())
                return false;
        }

        if (latestArchiveData != null)
        {
            Date latestDate = latestArchiveData.archiveDate();
            return latestDate.after(userDefaultCasesDate);
        }
        else
            return false;
    }


    /**
     * Runnable used to download an archive over the network.  Called by
     * updateFromNetwork(...).  This also assumes that updateFromNetwork()
     * sets up the initial dialog, including setting the type (either normal
     * or indeterminate).
     */
    class DownloadHandler
        implements Runnable
    {
        private URL sourceURL;
        private OutputStream outputStream;
        private int size;
        private AtomicBoolean cancelled;

        DownloadHandler(URL sourceURL, OutputStream outputStream, int size,
                        AtomicBoolean cancelled)
        {
            this.outputStream = outputStream;
            this.sourceURL = sourceURL;
            this.size = size;
            this.cancelled = cancelled;
        }

        @Override
        public void run()
        {
            final CancelCallback cancelCallback = new CancelCallback() {
                public boolean cancellationRequested()
                {
                    return cancelled.get();
                }
            };

            final int bufferSize = Util.getStreamReadBufferSize();
            final AtomicInteger progress = new AtomicInteger(0);
            boolean success = true;
            if (size > 0)
            {
                // Callback to update the progress bar.
                final UpdateCallback updateCallback = new UpdateCallback() {
                    public void update()
                    {
                        if (currentDialog == null) return;
                        final int current = progress.addAndGet(bufferSize);
                        currentDialog.setProgress(((double) current/(double) size));
                    }
                };
                success = Util.downloadUrlToStream(sourceURL, outputStream,
                                                   cancelCallback,
                                                   updateCallback);
            }
            else
            {
                // We didn't get the size of the archive, so there's no need
                // for an update callback.
                success = Util.downloadUrlToStream(sourceURL, outputStream,
                                                   cancelCallback, null);
            }
            if (currentDialog != null) currentDialog.close();
            if (!success && !cancelled.get())
            {
                // We failed for some reason.  While not strictly equivalent
                // to having been cancelled, we set the cancelled flag anyway
                // so that the caller knows not to proceed further.
                cancelled.set(true);
                display.asyncExec(new Runnable() {
                    @Override
                    public void run()
                    {
                        Tell.error(parentShell,
                           "Encountered problem during download operation.",
                           "A network error may have occurred (e.g., timeout)\n"
                           + "or some other local or server problem may have\n"
                           + "arisen. If the problem persists, please contact\n"
                           + "the developers.");
                    }
                });
            }
        }
    }


    /**
     * Method that handles downloading and unpacking an archive over the net.
     * This assumes that the archive URL (= updatedArchiveURL) and a cache of
     * the archive data (= latestArchiveData) have already been set by the
     * time this method is called.
     */
    public void updateFromNetwork()
    {
        if (latestArchiveData == null) return;

        File destDir = new File(Util.getUserDir());
        File destFile = new File(destDir, TEMP_ZIP_FILE_NAME);
        FileOutputStream fos = null;
        URL url = null;

        try
        {
            url = new URL(latestArchiveData.archiveDownloadURL());
            fos = new FileOutputStream(destFile);
        }
        catch (MalformedURLException e)
        {
            Tell.error(parentShell,
                       "Unable to read the test case archive from\n"
                       + "its network location.",
                       "Internal error: malformed URL. Please inform\n"
                       + "the developers.");
            return;
        }
        catch (FileNotFoundException e)
        {
            Tell.error(parentShell,
                       "Unable to read the test case archive from\n"
                       + "its network location.",
                       "The network may be unreachable, or the server\n"
                       + "may have changed, or some other unexpected\n"
                       + "error may have occurred. If the problem\n"
                       + "persists, please contact the developers.");
            return;
        }

        int size = latestArchiveData.archiveSize();
        boolean infiniteBar = (size == -1);

        currentDialog = new StatusDialog("Downloading updated tests...",
                                         infiniteBar, true);
        final AtomicBoolean cancelled = new AtomicBoolean(false);
        currentDialog.setCancelAction(new Runnable() {
                @Override
                public void run()
                {
                    cancelled.set(true);
                    if (currentDialog == null) return;
                    currentDialog.setCancelAction(null);
                    currentDialog.close();
                }
            });

        OutputStream outStream = new BufferedOutputStream(fos);

        executor.init(false, 1);
        executor.execute(new DownloadHandler(url, outStream, size, cancelled));
        currentDialog.showAndWait();
        executor.waitForProcesses(display);
        currentDialog = null;

        if (cancelled.get())
            return;

        // If we get this far, we've succeeded in downloading the archive.
        // Now unpack it.

        if (unpackArchive(destFile))
        {
            try
            {
                // Delete the file we downloaded if we unpacked it.
                destFile.delete();
            }
            catch (Exception ex)
            {
                // Nothing else to do if we fail here.
            }
            // Update the case date based on the new copy.
            userDefaultCasesDate = getUserDefaultCasesDate();
        }
    }


    /**
     * Main program should call this before exiting, to shut down any threads
     * that might still be running.
     */
    public void shutdown()
    {
        if (currentDialog != null)
            currentDialog.close();
        executor.shutdownNow();
    }

}
