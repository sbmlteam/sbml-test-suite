//
// @file BusyBar.java
// @brief Show a progress bar while doing something in a Runnable.
// @author Michael Hucka
// @date Created 2017-12-04 <mhucka@caltech.edu> based on ProgressDialogy.java
//    by Frank T. Bergmann from 2012.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.sbml.testsuite.core.Util;

/**
 * BusyBar basic progress dialog
 */
public class BusyBar
    extends Dialog
{
    private Shell shell;
    private CustomProgressBar progressBar;
    private Label message;
    private Label icon;

    /**
     * Create the dialog.
     *
     * @param parent
     * @param string to mention in the dialog text
     * 
     * @wbp.parser.constructor
     */
    public BusyBar(Shell parent)
    {
        super(parent, SWT.None);
        createContents("");
    }


    /**
     * Create the dialog.
     *
     * @param parent
     * @param string to mention in the dialog text
     * 
     * @wbp.parser.constructor
     */
    public BusyBar(Shell parent, String text)
    {
        super(parent, SWT.None);
        createContents(text);
    }


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public BusyBar(Shell parent, String text, int style)
    {
        super(parent, style);
        createContents(text);
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents(String text)
    {
        int margin = 12;
        int totalWidth = 400;
        int totalHeight = 115 + (!UIUtils.isMacOSX() ? margin/2 : 0);
        int buttonWidth = 80;

        shell = new Shell(getParent(), SWT.CLOSE | SWT.TITLE);
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setMinimumSize(new Point(totalWidth, totalHeight));
        shell.setSize(totalWidth, totalHeight);
        shell.setText("SBML Test Runner");
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
        message.setText(text);

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

        progressBar = new CustomProgressBar(compBar, SWT.INDETERMINATE);
        progressBar.resetSteps();

        shell.pack();
        shell.layout();
        shell.setVisible(false);
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
     * @return the dialogs shell
     */
    public Shell getShell()
    {
        return shell;
    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shell.getSize();
    }


    public void showWhile(final Display display, final Runnable runnable)
    {
        if (runnable == null || shell.isDisposed())
            return;

        center(shell.getParent().getBounds());
        shell.open();
        shell.setVisible(true);
        try
        {
            Util.sleep(100);
            display.asyncExec(new Runnable() {
                    @Override
                    public void run()
                    {
                        runnable.run();
                        shell.close();
                    }
                });
            while (shell != null && !shell.isDisposed())
                if (!display.readAndDispatch())
                    display.sleep();
        }
        finally
        {
            if (shell != null && !shell.isDisposed())
                shell.dispose();
        }
    }


    /**
     * Shows the dialog and enter a read-dispatch loop until the dialog
     * is disposed.  This is meant to be invoked after the caller starts
     * a thread to do whatever work is needed.  The thread should call
     * the close() method on the dialog when the work is finished.
     */
    public void open()
    {
        if (shell.isDisposed())
            return;
        shell.open();
        final Display display = shell.getDisplay();
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
        final Display display = shell.getDisplay();
        final Shell thisShell = this.shell;
        display.syncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (thisShell != null && !thisShell.isDisposed())
                        thisShell.close();
                }
            });
    }


    public void dispose()
    {
        if (shell != null && !shell.isDisposed())
            shell.dispose();
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
     * Sets the text element
     * 
     * @param styledText
     *            the text to set
     */
    public void updateText(final String text)
    {
        if (shell.isDisposed()) return;
        final Display display = shell.getDisplay();
        display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (message.isDisposed()) return;
                    message.setText(text);
                    if (shell.isDisposed()) return;
                    shell.layout();
                }
            });
    }
}
