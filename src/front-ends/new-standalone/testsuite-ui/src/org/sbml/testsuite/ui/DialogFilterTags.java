//
// @file DialogFilterTags.java
// @brief Filter dialog for test / component tags
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

/**
 * Filter dialog for test / component tags
 */
public class DialogFilterTags
    extends Dialog
{

    private Label            lblDescription;
    private LabeledList      lblLstComponentTags;
    private LabeledList      lblLstSelectedTags;
    private LabeledList      lblLstTestTags;
    protected Vector<String> result;
    protected Shell          shlFilterTags;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public DialogFilterTags(Shell parent, int style)
    {
        super(parent, style);
        createContents();
        setText("SWT Dialog");
        shlFilterTags.layout();
    }


    protected void addComponentTags()
    {
        Vector<String> temp = getSelection();
        Vector<String> temp1 = getSelectedComponentTags();

        for (String tag : temp1)
        {
            if (!temp.contains(tag))
            {
                lblLstSelectedTags.getList().add(tag);
            }
        }

    }


    protected void addTestTags()
    {
        Vector<String> temp = getSelection();
        Vector<String> temp1 = getSelectedTestTags();

        for (String tag : temp1)
        {
            if (!temp.contains(tag))
            {
                lblLstSelectedTags.getList().add(tag);
            }
        }
    }


    protected void cancelPressed()
    {
        result = null;
        shlFilterTags.close();

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


    protected void clearSelectedTags()
    {
        lblLstSelectedTags.getList().removeAll();

    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        shlFilterTags = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shlFilterTags.setImage(SWTResourceManager.getImage(DialogFilterTags.class,
                                                           "/data/sbml_256.png"));
        shlFilterTags.setMinimumSize(new Point(570, 410));
        shlFilterTags.setSize(570, 410);
        shlFilterTags.setText("Filter Tags");
        shlFilterTags.setLayout(new FormLayout());

        lblDescription = new Label(shlFilterTags, SWT.NONE);
        FormData fd_lblDescription = new FormData();
        fd_lblDescription.left = new FormAttachment(0, 10);
        fd_lblDescription.right = new FormAttachment(100, -10);
        fd_lblDescription.bottom = new FormAttachment(0, 31);
        fd_lblDescription.top = new FormAttachment(0, 10);
        lblDescription.setLayoutData(fd_lblDescription);
        lblDescription.setText("New Label");

        Button cmdOk = new Button(shlFilterTags, SWT.NONE);
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
        shlFilterTags.setDefaultButton(cmdOk);

        Button cmdCancel = new Button(shlFilterTags, SWT.NONE);
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

        SashForm sashForm = new SashForm(shlFilterTags, SWT.NONE);
        FormData fd_sashForm = new FormData();
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -6);
        fd_sashForm.top = new FormAttachment(lblDescription, 6);
        fd_sashForm.right = new FormAttachment(100, 0);
        fd_sashForm.left = new FormAttachment(0, 0);
        sashForm.setLayoutData(fd_sashForm);

        SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

        lblLstComponentTags = new LabeledList(sashForm_1, SWT.NONE);
        lblLstComponentTags.getAddButton()
                           .addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   addComponentTags();
                               }
                           });
        ((FormData) lblLstComponentTags.getList().getLayoutData()).right = new FormAttachment(
                                                                                              100,
                                                                                              -10);
        lblLstComponentTags.setLabel("Component Tags: ");
        lblLstComponentTags.getAddButton()
                           .setToolTipText("Add selected component tags");

        lblLstTestTags = new LabeledList(sashForm_1, SWT.NONE);
        lblLstTestTags.getAddButton()
                      .addSelectionListener(new SelectionAdapter() {
                          @Override
                          public void widgetSelected(SelectionEvent arg0)
                          {
                              addTestTags();
                          }
                      });
        lblLstTestTags.getAddButton().setToolTipText("Add selected test tags");
        lblLstTestTags.setLabel("Test Tags: ");
        sashForm_1.setWeights(new int[] {1, 1});

        Composite composite_1 = new Composite(sashForm, SWT.NONE);
        composite_1.setLayout(new FormLayout());

        lblLstSelectedTags = new LabeledList(composite_1, SWT.NONE);
        ((FormData) lblLstSelectedTags.getAddButton().getLayoutData()).top = new FormAttachment(
                                                                                                0,
                                                                                                5);
        FormData fd_labeledList_1 = new FormData();
        fd_labeledList_1.top = new FormAttachment(0);
        fd_labeledList_1.left = new FormAttachment(0);
        fd_labeledList_1.bottom = new FormAttachment(100);
        fd_labeledList_1.right = new FormAttachment(100);
        lblLstSelectedTags.setLayoutData(fd_labeledList_1);
        lblLstSelectedTags.setLabel("Selected Tags: ");
        lblLstSelectedTags.setButtonVisible(false);

        Button cmdClear = new Button(lblLstSelectedTags, SWT.NONE);
        FormData fd_cmdClear = new FormData();
        fd_cmdClear.top = new FormAttachment(0, 5);
        fd_cmdClear.right = new FormAttachment(
                                               lblLstSelectedTags.getAddButton());
        cmdClear.setLayoutData(fd_cmdClear);
        cmdClear.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                clearSelectedTags();
            }
        });
        cmdClear.setToolTipText("Clear all selected tags");
        cmdClear.setText("Clear");

        Button cmdRemove = new Button(lblLstSelectedTags, SWT.NONE);
        FormData fd_cmdRemove = new FormData();
        fd_cmdRemove.top = new FormAttachment(0, 5);
        fd_cmdRemove.right = new FormAttachment(100, -10);
        cmdRemove.setLayoutData(fd_cmdRemove);
        cmdRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                removeSelectedTags();
            }
        });
        cmdRemove.setText("Remove");
        sashForm.setWeights(new int[] {4, 5});
        lblLstTestTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                addTestTags();
            }
        });
        lblLstSelectedTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                removeSelectedTags();
            }
        });
        lblLstComponentTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                addComponentTags();
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
     * @return the selected component tags
     */
    public Vector<String> getSelectedComponentTags()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstComponentTags.getList()
                                                       .getSelection()));

        return result;
    }


    /**
     * @return the vector of all selected tags
     */
    public Vector<String> getSelectedTags()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstSelectedTags.getList().getSelection()));

        return result;
    }


    /**
     * @return selection of test tags
     */
    public Vector<String> getSelectedTestTags()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstTestTags.getList().getSelection()));

        return result;
    }


    /**
     * @return current selection
     */
    public Vector<String> getSelection()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstSelectedTags.getList().getItems()));

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
     * @return the title of the dialog
     */
    public String getTitle()
    {
        return shlFilterTags.getText();
    }


    protected void okPressed()
    {
        result = getSelection();
        shlFilterTags.close();

    }


    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Vector<String> open()
    {
        shlFilterTags.open();
        Display display = getParent().getDisplay();
        while (!shlFilterTags.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    protected void removeSelectedTags()
    {
        Vector<String> temp = getSelectedTags();
        for (String tag : temp)
        {
            lblLstSelectedTags.getList().remove(tag);
        }
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
     * Set the component tags
     * 
     * @param string
     *            of comma separated items
     */
    public void setComponentTags(String items)
    {
        setComponentTags(Util.split(items));
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
        shlFilterTags.setLocation(x, y);
    }


    /**
     * Set the selected tags
     * 
     * @param items
     *            the current selection
     */
    public void setSelectedTags(Collection<String> items)
    {
        lblLstSelectedTags.getList().removeAll();
        for (String tag : items)
            lblLstSelectedTags.getList().add(tag);
    }


    /**
     * Set the selected tags
     * 
     * @param string
     *            of comma separated items the current selection
     */
    public void setSelectedTags(String items)
    {
        setSelectedTags(Util.split(items));
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
     * Set the test tags
     * 
     * @param string
     *            of comma separated items
     */
    public void setTestTags(String items)
    {
        setTestTags(Util.split(items));
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
}
