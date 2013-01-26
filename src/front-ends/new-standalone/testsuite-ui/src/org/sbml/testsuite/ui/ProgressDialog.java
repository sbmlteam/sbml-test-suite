//
// @file ProgressDialog.java
// @brief ProgressDialog basic progress dialog
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

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * ProgressDialog basic progress dialog
 */
public class ProgressDialog
    extends Dialog
{
    protected Object    result;
    private Shell       shell;
    private ProgressBar progressBar;
    private StyledText  styledText;


    /**
     * Create the dialog.
     *
     * @param parent
     * @param file to mention in the dialog text
     * 
     * @wbp.parser.constructor
     */
    public ProgressDialog(Shell parent, File file)
    {
        super(parent, SWT.SHEET | SWT.PRIMARY_MODAL);
        setText("Progress ...");
        createContents(file);
    }


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ProgressDialog(Shell parent, int style, File file)
    {
        super(parent, style);
        setText("Progress ...");
        createContents(file);
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
     * Closes the dialog
     */
    public void close()
    {
        shell.close();
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents(File file)
    {
        shell = new Shell(getParent(), getStyle());
        shell.setMinimumSize(new Point(350, 250));
        shell.setSize(350, 250);
        shell.setText("Processing " + file.getName());
        shell.setLayout(new FormLayout());

        styledText = new StyledText(shell, SWT.READ_ONLY | SWT.WRAP);
        FormData fd_styledText = new FormData();
        fd_styledText.top = new FormAttachment(10);
        fd_styledText.left = new FormAttachment(0, 10);
        fd_styledText.right = new FormAttachment(100, -10);
        fd_styledText.bottom = new FormAttachment(100, -30);
        styledText.setLayoutData(fd_styledText);
        styledText.setText("<p>Test</p>");
        styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));

        progressBar = new ProgressBar(shell, SWT.INDETERMINATE | SWT.BORDER);
        FormData fd_progressBar = new FormData();
        fd_progressBar.bottom = new FormAttachment(100, -10);
        fd_progressBar.right = new FormAttachment(100, -10);
        fd_progressBar.left = new FormAttachment(0, 10);
        progressBar.setLayoutData(fd_progressBar);
    }


    /**
     * @return the progress bar control
     */
    public ProgressBar getProgressBar()
    {
        return progressBar;
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


    /**
     * @return the text element
     */
    public StyledText getStyledText()
    {
        return styledText;
    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open()
    {
        openWithoutWait();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    /**
     * Opens the dialog without waiting
     */
    public void openWithoutWait()
    {
        shell.open();
        shell.layout();
        try { Thread.sleep(100); } catch (Exception e) { }
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
     * Sets the progress bar control
     * 
     * @param progressBar
     *            the control
     */
    public void setProgressBar(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }


    /**
     * Sets the dialogs shell
     * 
     * @param shell
     *            the shell
     */
    public void setShell(Shell shell)
    {
        this.shell = shell;
    }


    /**
     * Sets the styled text element
     * 
     * @param styledText
     *            the text to set
     */
    public void setStyledText(StyledText styledText)
    {
        this.styledText = styledText;
    }
}
