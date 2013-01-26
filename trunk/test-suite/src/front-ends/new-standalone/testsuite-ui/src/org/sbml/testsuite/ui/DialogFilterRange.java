//
// @file DialogFilterRange.java
// @brief Filter dialog specifying a range of ids
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.swtchart.Range;

/**
 * Filter dialog specifying a range of ids
 */
public class DialogFilterRange
    extends Dialog
{

    private Label   lblDescription;
    protected Range result;
    protected Shell shlFilterRange;
    private Text    txtStart;
    private Text    txtEnd;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public DialogFilterRange(Shell parent, int style)
    {
        super(parent, style);
        createContents();
        setText("SWT Dialog");
        shlFilterRange.layout();
    }


    protected void cancelPressed()
    {
        result = null;
        shlFilterRange.close();

    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shlFilterRange == null) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shlFilterRange = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shlFilterRange.setImage(UIUtils.getImageResource("sbml_256.png"));
        shlFilterRange.setMinimumSize(new Point(300, 160));
        shlFilterRange.setSize(147, 83);
        shlFilterRange.setText("Filter By Range");
        shlFilterRange.setLayout(new FormLayout());

        lblDescription = new Label(shlFilterRange, SWT.NONE);
        FormData fd_lblDescription = new FormData();
        fd_lblDescription.left = new FormAttachment(0, 10);
        fd_lblDescription.right = new FormAttachment(100, -10);
        fd_lblDescription.bottom = new FormAttachment(0, 31);
        fd_lblDescription.top = new FormAttachment(0, 10);
        lblDescription.setLayoutData(fd_lblDescription);
        lblDescription.setText("New Label");

        Button cmdOk = new Button(shlFilterRange, SWT.NONE);
        FormData fd_cmdOk = new FormData();
        fd_cmdOk.width = 75;
        fd_cmdOk.bottom = new FormAttachment(100, -10);
        fd_cmdOk.right = new FormAttachment(100, -95);
        cmdOk.setLayoutData(fd_cmdOk);
        cmdOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                okPressed();
            }
        });
        cmdOk.setText("OK");
        shlFilterRange.setDefaultButton(cmdOk);

        Button cmdCancel = new Button(shlFilterRange, SWT.NONE);
        cmdCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                cancelPressed();
            }
        });
        cmdCancel.setText("Cancel");
        FormData fd_cmdCancel = new FormData();
        fd_cmdCancel.width = 75;
        fd_cmdCancel.bottom = new FormAttachment(100, -10);
        fd_cmdCancel.right = new FormAttachment(100, -10);
        cmdCancel.setLayoutData(fd_cmdCancel);

        Label lblStart = new Label(shlFilterRange, SWT.NONE);
        FormData fd_lblStart = new FormData();
        fd_lblStart.left = new FormAttachment(lblDescription, 0, SWT.LEFT);
        lblStart.setLayoutData(fd_lblStart);
        lblStart.setText("Start: ");

        txtStart = new Text(shlFilterRange, SWT.BORDER | SWT.RIGHT);
        fd_lblStart.top = new FormAttachment(txtStart, 3, SWT.TOP);
        FormData fd_txtStart = new FormData();
        fd_txtStart.top = new FormAttachment(lblDescription, 3);
        fd_txtStart.right = new FormAttachment(100, -10);
        fd_txtStart.left = new FormAttachment(lblStart, 6);
        txtStart.setLayoutData(fd_txtStart);

        Label lblEnd = new Label(shlFilterRange, SWT.NONE);
        lblEnd.setText("End: ");
        FormData fd_lblEnd = new FormData();
        fd_lblEnd.left = new FormAttachment(lblDescription, 0, SWT.LEFT);
        lblEnd.setLayoutData(fd_lblEnd);

        txtEnd = new Text(shlFilterRange, SWT.BORDER | SWT.RIGHT);
        fd_lblEnd.top = new FormAttachment(txtEnd, 3, SWT.TOP);
        FormData fd_txtEnd = new FormData();
        fd_txtEnd.top = new FormAttachment(txtStart, 3);
        fd_txtEnd.right = new FormAttachment(lblDescription, 0, SWT.RIGHT);
        fd_txtEnd.left = new FormAttachment(txtStart, 0, SWT.LEFT);
        txtEnd.setLayoutData(fd_txtEnd);
        shlFilterRange.addListener(SWT.Traverse, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                switch (event.detail)
                {
                case SWT.TRAVERSE_RETURN:
                    okPressed();
                    break;
                case SWT.TRAVERSE_ESCAPE:
                    cancelPressed();
                    event.detail = SWT.TRAVERSE_NONE;
                    event.doit = false;
                    break;
                }
            }
        });

    }


    /**
     * @return the displayed description
     */
    public String getDescription()
    {
        return lblDescription.getText();
    }


    /**
     * @return the selected range or null in case of error
     */
    public Range getSelection()
    {
        try
        {
            return new Range(Double.parseDouble(txtStart.getText()),
                             Double.parseDouble(txtEnd.getText()));
        }
        catch (Exception ex)
        {
            return null;
        }

    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shlFilterRange.getSize();
    }


    /**
     * @return the title of the dialog
     */
    public String getTitle()
    {
        return shlFilterRange.getText();
    }


    protected void okPressed()
    {
        result = getSelection();
        shlFilterRange.close();

    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Range open()
    {
        shlFilterRange.open();
        Display display = getParent().getDisplay();
        while (!shlFilterRange.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    /**
     * Sets the description of the dialog
     * 
     * @param text
     *            the description
     */
    public void setDescription(String text)
    {
        lblDescription.setText(text);
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
        shlFilterRange.setLocation(x, y);
    }


    /**
     * Sets the selected range
     * 
     * @param start
     *            the begin of the range
     * @param end
     *            end of the range
     */
    public void setRange(String start, String end)
    {
        txtStart.setText(start);
        txtEnd.setText(end);

    }


    /**
     * Sets the title of the dialog
     * 
     * @param text
     *            the title text
     */
    public void setTitle(String text)
    {
        shlFilterRange.setText(text);
    }
}
