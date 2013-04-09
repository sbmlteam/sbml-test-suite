//
// @file   TagsDialog.java
// @brief  Get a list of tags from the user
// @author Michael Hucka
// @author Frank T. Bergmann
// @date   2013-04-03 based on Frank's code of 2012-06-06
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.sbml.testsuite.core.Util;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Control;

/**
 * Filter dialog for test / component tags
 */
public class TagsDialog
    extends Dialog
{
    private LabeledList      lblLstComponentTags;
    private LabeledList      lblLstTestTags;
    protected Shell          shlFilterTags;
    protected Vector<String> selectedTags;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public TagsDialog(Shell parent, int style)
    {
        super(parent, style);
        createContents();
        setText("Choose tags");
        shlFilterTags.layout();
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shlFilterTags == null) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        int totalWidth = 300;
        int totalHeight = 500;
        int buttonWidth = 80;
        int margin = 5;
        int offset = 20 - UIUtils.scaledFontSize(20);

        shlFilterTags = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shlFilterTags.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shlFilterTags.setMinimumSize(new Point(totalWidth, totalHeight));
        shlFilterTags.setSize(totalWidth, totalHeight);
        shlFilterTags.setText("Choose tags");
        shlFilterTags.setLayout(new FormLayout());

        // Top button for handy clear-all command.

        Button cmdClearAll = new Button(shlFilterTags, SWT.NONE);
        cmdClearAll.setText("Clear all");
        cmdClearAll.setToolTipText("Clear all selections");
        FormData fd_cmdClearAll = new FormData();
        fd_cmdClearAll.width = buttonWidth;
        fd_cmdClearAll.top = new FormAttachment(0, margin);
        fd_cmdClearAll.right = new FormAttachment(100, -(margin + offset));
        cmdClearAll.setLayoutData(fd_cmdClearAll);
        cmdClearAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                clearSelectedComponentTags();
                clearSelectedTestTags();
            }
        });

        // Buttons at the bottom.  Done here so that the middle section can
        // refer to the buttons to anchor the bottom of their layout.

        Button cmdOk = new Button(shlFilterTags, SWT.NONE);
        FormData fd_cmdOk = new FormData();
        fd_cmdOk.width = buttonWidth;
        fd_cmdOk.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdOk.right = new FormAttachment(100, -(margin + offset));
        cmdOk.setLayoutData(fd_cmdOk);
        cmdOk.setText("OK");
        cmdOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                okPressed();
            }
        });
        cmdOk.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shlFilterTags));

        Button cmdCancel = new Button(shlFilterTags, SWT.NONE);
        cmdCancel.setText("Cancel");
        FormData fd_cmdCancel = new FormData();
        fd_cmdCancel.width = buttonWidth;
        fd_cmdCancel.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdCancel.right = new FormAttachment(cmdOk, -margin);
        cmdCancel.setLayoutData(fd_cmdCancel);
        cmdCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                cancelPressed();
            }
        });
        cmdCancel.setFocus();
        cmdCancel.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shlFilterTags));

        shlFilterTags.setDefaultButton(cmdOk);
        shlFilterTags.addListener(SWT.Traverse, new Listener() {
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

        SashForm sashForm = new SashForm(shlFilterTags, SWT.VERTICAL);
        FormData fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(cmdClearAll, margin);
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -offset);
        fd_sashForm.left = new FormAttachment(0, 0);
        fd_sashForm.right = new FormAttachment(100, 0);
        sashForm.setLayoutData(fd_sashForm);

        lblLstComponentTags = new LabeledList(sashForm, SWT.MULTI);
        lblLstComponentTags.setLabel("Select Component Tags: ");
        lblLstComponentTags.getButton1().setVisible(false);

        lblLstComponentTags.getButton2().setText("Clear");
        lblLstComponentTags.getButton2()
                           .setToolTipText("Clear tag selections");
        lblLstComponentTags.getButton2()
                           .addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   clearSelectedComponentTags();
                               }
                           });
        ((FormData) lblLstComponentTags.getList().getLayoutData()).right
            = new FormAttachment(100, -10);

        lblLstTestTags = new LabeledList(sashForm, SWT.MULTI);
        lblLstTestTags.setLabel("Select Test Tags:");
        lblLstTestTags.getButton1().setVisible(false);

        lblLstTestTags.getButton2().setText("Clear");
        lblLstTestTags.getButton2()
                      .setToolTipText("Clear tag selections");
        lblLstTestTags.getButton2()
                      .addSelectionListener(new SelectionAdapter() {
                          @Override
                          public void widgetSelected(SelectionEvent arg0)
                          {
                              clearSelectedTestTags();
                          }
                      });

        sashForm.setWeights(new int[] {1, 1});

        shlFilterTags.pack();
        shlFilterTags.setTabList(new Control[]{cmdCancel, cmdClearAll, 
                                               sashForm, cmdOk});
        shlFilterTags.layout();
    }


    public boolean open()
    {
        // Remember the populated lists in case the user cancels out.

        selectedTags = readTags();

        // Open the dialog and wait for the user to close it.

        shlFilterTags.open();
        Display display = getParent().getDisplay();
        while (!shlFilterTags.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }

        // If there are any non-empty tags lists, return true.

        return selectedTags != null && !selectedTags.isEmpty();
    }


    /**
     * Set component tags
     * 
     * @param items
     *            the component tags
     */
    public void setComponentTags(Collection<String> items)
    {
        lblLstComponentTags.getList().removeAll();
        for (String tag : items)
            lblLstComponentTags.getList().add(tag);
    }


    /**
     * Set the test tags
     * 
     * @param items
     *            the current selection
     */
    public void setTestTags(Collection<String> items)
    {
        lblLstTestTags.getList().removeAll();
        for (String tag : items)
            lblLstTestTags.getList().add(tag);
    }


    /**
     * Sets the title of the dialog
     * 
     * @param text
     *            the title text
     */
    public void setTitle(String text)
    {
        shlFilterTags.setText(text);
    }


    /**
     * @return current case numbers
     */
    public Vector<String> getSelectedTags()
    {
        return selectedTags;
    }


    public void setSelectedTags(Collection<String> tags)
    {
        if (tags == null || tags.isEmpty()) return;

        // The List class's setSelection() method has this immensely useful
        // feature that it ignores items that aren't in the list.  This
        // makes our code very simple: we don't have to separate out the
        // lists of component and test tags ahead of time.

        String[] tagsArray = tags.toArray(new String[0]);
        lblLstComponentTags.getList().setSelection(tagsArray);
        lblLstTestTags.getList().setSelection(tagsArray);
    }


    private void okPressed()
    {
        selectedTags = readTags();
        shlFilterTags.close();
    }


    private void cancelPressed()
    {
        shlFilterTags.close();
    }


    private void clearSelectedComponentTags()
    {
        lblLstComponentTags.getList().deselectAll();
    }


    private void clearSelectedTestTags()
    {
        lblLstTestTags.getList().deselectAll();
    }


    private Vector<String> readTags()
    {
        Vector<String> result = new Vector<String>();
        result.addAll(Arrays.asList(lblLstComponentTags.getList().getSelection()));
        result.addAll(Arrays.asList(lblLstTestTags.getList().getSelection()));
        return result;
    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shlFilterTags.getSize();
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
        shlFilterTags.setLocation(x, y);
    }
}
