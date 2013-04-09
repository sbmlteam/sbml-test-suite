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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.sbml.testsuite.core.Util;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Control;

/**
 * Filter dialog for test / component tags
 */
public class TagsDialog
    extends Dialog
{
    private Tree           treeCompTags;
    private Tree           treeTestTags;
    private Shell          shell;
    private Vector<String> selectedTags;
    private boolean        exitOK = false;


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
        shell.layout();
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
     * Create contents of the dialog.
     */
    private void createContents()
    {
        int totalWidth = 300;
        int totalHeight = 500;
        int buttonWidth = 80;
        int margin = 5;
        int offset = 20 - UIUtils.scaledFontSize(20);

        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setMinimumSize(new Point(totalWidth, totalHeight));
        shell.setSize(totalWidth, totalHeight);
        shell.setText("Choose tags");
        shell.setLayout(new FormLayout());

        // Top button for handy clear-all command.

        Button cmdClearAll = new Button(shell, SWT.NONE);
        cmdClearAll.setText("Clear all");
        cmdClearAll.setToolTipText("Clear all selections");
        FormData fd_cmdClearAll = new FormData();
        fd_cmdClearAll.width = buttonWidth;
        fd_cmdClearAll.top = new FormAttachment(0, margin + offset);
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

        Button cmdOk = new Button(shell, SWT.NONE);
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
        cmdOk.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shell));

        Button cmdCancel = new Button(shell, SWT.NONE);
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
        cmdCancel.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shell));

        shell.setDefaultButton(cmdOk);
        shell.addListener(SWT.Traverse, new Listener() {
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

        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        FormData fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(cmdClearAll, offset);
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -(margin + offset));
        fd_sashForm.left = new FormAttachment(0, margin);
        fd_sashForm.right = new FormAttachment(100, -margin);
        sashForm.setLayoutData(fd_sashForm);

        Composite compComponentTags = new Composite(sashForm, SWT.NONE);
        compComponentTags.setLayout(new FormLayout());

        Label lblComponentTags = new Label(compComponentTags, SWT.NONE);
        lblComponentTags.setText("Select Component Tags:");
        FormData fd_lblComponentTags = new FormData();
        fd_lblComponentTags.top = new FormAttachment(0, margin);
        fd_lblComponentTags.left = new FormAttachment(0, margin);
        lblComponentTags.setLayoutData(fd_lblComponentTags);

        Button btnClearCompTags = new Button(compComponentTags, SWT.NONE);
        btnClearCompTags.setText("Clear");
        btnClearCompTags.setToolTipText("Clear tag selections");
        FormData fd_btnClearCompTags = new FormData();
        fd_btnClearCompTags.width = buttonWidth;
        fd_btnClearCompTags.top = new FormAttachment(0);
        fd_btnClearCompTags.right = new FormAttachment(100, -offset);
        btnClearCompTags.setLayoutData(fd_btnClearCompTags);
        btnClearCompTags.addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   clearSelectedComponentTags();
                               }
                           });

        treeCompTags = new Tree(compComponentTags, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        FormData fd_treeCompTags = new FormData();
        fd_treeCompTags.top = new FormAttachment(lblComponentTags, 2*margin + offset);
        fd_treeCompTags.bottom = new FormAttachment(100, -margin);
        fd_treeCompTags.left = new FormAttachment(0, margin + 1);
        fd_treeCompTags.right = new FormAttachment(100, -(margin + 1));
        treeCompTags.setLayoutData(fd_treeCompTags);
        // NB: the code is not identical for the 2 trees.
        treeCompTags.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                // Make double-click toggle the selection status.

                Point point = new Point(event.x, event.y);
                TreeItem item = treeCompTags.getItem(point);
                if (item == null) return;
                item.setChecked(!item.getChecked());
            }
        });

        Composite compTestTags = new Composite(sashForm, SWT.NONE);
        compTestTags.setLayout(new FormLayout());

        Label lblTestTags = new Label(compTestTags, SWT.NONE);
        FormData fd_lblTestTags = new FormData();
        fd_lblTestTags.top = new FormAttachment(0, margin);
        fd_lblTestTags.left = new FormAttachment(0, margin);
        lblTestTags.setLayoutData(fd_lblTestTags);
        lblTestTags.setText("Select Component Tags:");

        Button btnClearTestTags = new Button(compTestTags, SWT.NONE);
        btnClearTestTags.setText("Clear");
        btnClearTestTags.setToolTipText("Clear tag selections");
        FormData fd_btnClearTestTags = new FormData();
        fd_btnClearTestTags.width = buttonWidth;
        fd_btnClearTestTags.top = new FormAttachment(0);
        fd_btnClearTestTags.right = new FormAttachment(100, -offset);
        btnClearTestTags.setLayoutData(fd_btnClearTestTags);
        btnClearTestTags.addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   clearSelectedTestTags();
                               }
                           });

        treeTestTags = new Tree(compTestTags, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        FormData fd_treeTestTags = new FormData();
        fd_treeTestTags.top = new FormAttachment(lblTestTags, 2*margin + offset);
        fd_treeTestTags.bottom = new FormAttachment(100, -margin);
        fd_treeTestTags.left = new FormAttachment(0, margin + 1);
        fd_treeTestTags.right = new FormAttachment(100, -(margin + 1));
        treeTestTags.setLayoutData(fd_treeTestTags);
        treeTestTags.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                // Make double-click toggle the selection status.

                Point point = new Point(event.x, event.y);
                TreeItem item = treeTestTags.getItem(point);
                if (item == null) return;
                item.setChecked(!item.getChecked());
            }
        });

        // Final set-up.

        sashForm.setWeights(new int[] {1, 1});

        shell.pack();
        shell.setTabList(new Control[]{cmdCancel, cmdClearAll, 
                                               sashForm, cmdOk});
        shell.layout();
    }


    /**
     * @return true if the user clicked OK, false if they clicked Cancel.
     */
    public boolean open()
    {
        // Remember the populated lists in case the user cancels out.
        // If the user clicks OK, the value of selectedTags will be changed.
        // Callers get the value returned by getSelectedTags();

        selectedTags = readTags();

        // Open the dialog and wait for the user to close it.

        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }

         return exitOK;
    }


    /**
     * Set component tags
     * 
     * @param items
     *            the component tags
     */
    public void setComponentTags(Collection<String> items)
    {
        treeCompTags.removeAll();
        treeCompTags.clearAll(true);
        for (String tag : items)
        {
            TreeItem treeItem = new TreeItem(treeCompTags, SWT.NONE);
            treeItem.setText(tag);
        }
    }


    /**
     * Set the test tags
     * 
     * @param items
     *            the current selection
     */
    public void setTestTags(Collection<String> items)
    {
        treeTestTags.removeAll();
        treeTestTags.clearAll(true);
        for (String tag : items)
        {
            TreeItem treeItem = new TreeItem(treeTestTags, SWT.NONE);
            treeItem.setText(tag);
        }
    }


    /**
     * Sets the title of the dialog
     * 
     * @param text
     *            the title text
     */
    public void setTitle(String text)
    {
        shell.setText(text);
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
        if (treeCompTags == null) return;
        if (treeTestTags == null) return;

        //        String[] tagsArray = tags.toArray(new String[0]);

    outer: for (String tag : tags)
        {
            for (TreeItem item : treeCompTags.getItems())
                if (tag.equals(item.getText()))
                {
                    item.setChecked(true);
                    continue outer;
                }
            for (TreeItem item : treeTestTags.getItems())
                if (tag.equals(item.getText()))
                {
                    item.setChecked(true);
                    continue outer;
                }
        }
    }


    private void okPressed()
    {
        selectedTags = readTags();
        exitOK = true;
        shell.close();
    }


    private void cancelPressed()
    {
        shell.close();
    }


    private void clearSelectedComponentTags()
    {
        for (TreeItem item : treeCompTags.getItems())
            item.setChecked(false);
    }


    private void clearSelectedTestTags()
    {
        for (TreeItem item : treeTestTags.getItems())
            item.setChecked(false);
    }


    private Vector<String> readTags()
    {
        Vector<String> result = new Vector<String>();
        for (TreeItem item : treeCompTags.getItems())
            if (item.getChecked())
                result.add(item.getText());
        for (TreeItem item : treeTestTags.getItems())
            if (item.getChecked())
                result.add(item.getText());
        return result;
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
}
