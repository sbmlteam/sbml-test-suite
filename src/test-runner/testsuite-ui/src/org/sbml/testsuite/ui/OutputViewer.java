//
// @file   OutputViewer.java
// @brief  A simple viewer for the wrapper process output.
// @author Michael Hucka
// @date   Created 2013-05-25 <mhucka@caltech.edu>
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
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class OutputViewer
{
    private Shell shell;

    public OutputViewer(Shell parent, String title, String text)
    {
        shell = new Shell(parent.getDisplay(), SWT.DIALOG_TRIM | SWT.RESIZE);
        createContents(title, text);
    }


    private void createContents(String title, String text)
    {
        int buttonWidth = 80;
        int margin = 5;

        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setText(title);
        shell.setLayout(new FormLayout());
        shell.setSize(600, 400);
        shell.setMinimumSize(300, 300);

        shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));

        Composite comp = new Composite(shell, SWT.NONE);
        FormData fd_comp = new FormData();
        fd_comp.top = new FormAttachment(0, margin);
        fd_comp.left = new FormAttachment(0, margin);
        fd_comp.right = new FormAttachment(100, -margin);
        comp.setLayoutData(fd_comp);
        FillLayout layout = new FillLayout();
        comp.setLayout(layout);
        comp.addListener(SWT.KeyDown, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    // Need to ignore keyboard events here.  If you don't
                    // block window-close sequences like command-w, the
                    // widget will end up being disposed more than once,
                    // resulting in an exception.
                    event.doit = false;
                }
            });

        StyledText contents = new StyledText(comp, SWT.BORDER | SWT.WRAP);
        contents.setEditable(false);
        contents.setFont(UIUtils.createResizedFont("SansSerif", SWT.NORMAL, -1));

        if (text == null)
            contents.setText("(Re-run the case to obtain output)");
        else
        {
            contents.setText(text);

            Font italicFont
                = UIUtils.createResizedFont("SansSerif", SWT.ITALIC | SWT.BOLD, -1);

            String cmdLineHeaderText = "Command line executed";
            String outputLineHeaderText = "Output produced on standard output stream";
            String errorLineHeaderText = "Output produced on standard error stream";

            StyleRange cmdLineHeaderStyle = new StyleRange();
            StyleRange outputLineHeaderStyle = new StyleRange();
            StyleRange errorLineHeaderStyle = new StyleRange();

            cmdLineHeaderStyle.start = text.indexOf(cmdLineHeaderText);
            cmdLineHeaderStyle.length = cmdLineHeaderText.length();
            cmdLineHeaderStyle.font = italicFont;
            cmdLineHeaderStyle.underline = true;

            outputLineHeaderStyle.start = text.indexOf(outputLineHeaderText);
            outputLineHeaderStyle.length = outputLineHeaderText.length();
            outputLineHeaderStyle.font = italicFont;
            outputLineHeaderStyle.underline = true;

            errorLineHeaderStyle.start = text.indexOf(errorLineHeaderText);
            errorLineHeaderStyle.length = errorLineHeaderText.length();
            errorLineHeaderStyle.font = italicFont;
            errorLineHeaderStyle.underline = true;

            contents.setStyleRange(cmdLineHeaderStyle);
            contents.setStyleRange(outputLineHeaderStyle);
            contents.setStyleRange(errorLineHeaderStyle);
        }

        Button cmdClose = new Button(shell, SWT.NONE);
        cmdClose.setText("Close");
        fd_comp.bottom = new FormAttachment(cmdClose, -margin);
        FormData fd_cmdClose = new FormData();
        fd_cmdClose.width = buttonWidth;
        fd_cmdClose.top = new FormAttachment(100, -35);
        fd_cmdClose.right = new FormAttachment(100, -margin);
        cmdClose.setLayoutData(fd_cmdClose);
        cmdClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (shell == null || shell.isDisposed()) return;
                shell.close();
            }
        });
        cmdClose.setFocus();
        shell.setDefaultButton(cmdClose);

        shell.layout();

        // Add keyboard bindings for command-w and control-w.

        final Listener closeKeyListener = new Listener() {
            @Override
            public void handleEvent (final Event event)
            {
                if (shell == null || shell.isDisposed()) return;
                if (UIUtils.isModifier(event) && event.keyCode == 'w')
                    shell.close();
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
    }


    public void open()
    {
        if (shell == null || shell.isDisposed()) return;
        shell.open();
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shell == null || shellBounds == null) return;

        Point dialogSize = shell.getSize();
        shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                          shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
    }
}
