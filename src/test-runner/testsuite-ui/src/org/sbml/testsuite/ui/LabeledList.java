//
// @file   LabeledList.java
// @brief  A composite holding a selectable list, a title, and buttons
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

import java.util.Collection;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


/**
 * Utility Composite holding a list and a label
 */
public class LabeledList
    extends Composite
{
    private Label  label;
    private Table  table;
    private Button button1;
    private Button button2;


    /**
     * Create the composite.
     *
     * @param parent
     * @param style
     */
    public LabeledList(Composite parent, int style)
    {
        super(parent, SWT.NONE);
        setLayout(new FormLayout());

        int margin = 5;
        int offset = 20 - UIUtils.scaledFontSize(20);

        label = new Label(this, SWT.NONE);
        FormData fd_lblLabel = new FormData();
        fd_lblLabel.left = new FormAttachment(0, 2*margin);
        label.setLayoutData(fd_lblLabel);
        label.setText("New Label");

        table = new Table(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        FormData fd_table = new FormData();
        fd_table.bottom = new FormAttachment(100, -margin);
        fd_table.left = new FormAttachment(0, 2*margin);
        fd_table.right = new FormAttachment(100, -2*margin);
        table.setLayoutData(fd_table);

        int buttonWidth = 80;

        // button1 is left of button2, but we use button2 to align button1.

        button2 = new Button(this, SWT.NONE);
        button2.setText("Button 2");
        FormData fd_button2 = new FormData();
        fd_button2.width = buttonWidth;
        fd_button2.top = new FormAttachment(0, margin);
        fd_button2.right = new FormAttachment(100, -(margin + offset));
        button2.setLayoutData(fd_button2);

        button1 = new Button(this, SWT.NONE);
        button1.setText("Button 1");
        FormData fd_button1 = new FormData();
        fd_button1.width = buttonWidth;
        fd_button1.top = new FormAttachment(0, margin);
        fd_button1.right = new FormAttachment(button2, -offset);
        button1.setLayoutData(fd_button1);

        fd_table.top = new FormAttachment(button1, offset);
        fd_lblLabel.top = new FormAttachment(button1, margin, SWT.TOP);

        pack();
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }


    /**
     * @return the include button
     */
    public Button getButton1()
    {
        return button1;
    }


    /**
     * @return the exclude button
     */
    public Button getButton2()
    {
        return button2;
    }


    /**
     * @return the label text
     */
    public String getLabel()
    {
        return label.getText();
    }


    /**
     * @return the list
     */
    public Table getTable()
    {
        return table;
    }


    /**
     * Toggles visibility of the add button
     *
     * @param isVisible
     *            indication whether the button should be visible
     */
    public void setButtonsVisible(boolean isVisible)
    {
        button1.setVisible(isVisible);
        button2.setVisible(isVisible);
    }


    /**
     * Sets the label text.
     *
     * @param label
     *            the text
     */
    public void setLabel(String label)
    {
        this.label.setText(label);
    }


    /**
     * Adds a mouse listener to the Control containing the list of items.
     */
    public void addMouseListener(final MouseAdapter listener)
    {
        table.addMouseListener(listener);
    }


    /**
     * Returns the set of currently selected items, as an array of strings.
     */
    public String[] getSelection()
    {
        TableItem[] items = table.getSelection();
        String[] selection = new String[items.length];
        for (int i = 0; i < items.length; i++)
            selection[i] = items[i].getText();
        return selection;
    }


    /**
     * Add a single string to the list of items shown.
     */
    public void add(String name)
    {
        if (name == null || name.length() == 0) return;
        if (indexOf(name) != -1) return;
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(name);
    }


    /**
     * Add all the given strings to the list of items shown.
     */
    public void add(String[] names)
    {
        if (names == null) return;
        for (String name : names)
            add(name);
    }


    /**
     * Add a collection of strings to the list of items shown.
     */
    public void add(Collection<String> collection)
    {
        if (collection == null) return;
        for (String name : collection)
            add(name);
    }


    /**
     * Remove the named item from the list.
     */
    public void remove(String name)
    {
        if (name == null) return;
        int index = indexOf(name);
        if (index > -1) table.remove(index);
    }


    /**
     * Remove all the given items from the list.
     */
    public void remove(String[] names)
    {
        if (names == null) return;
        for (String name : names)
            remove(name);
    }


    /**
     * Remove all items from the list.
     */
    public void removeAll()
    {
        table.removeAll();
    }


    /**
     * Returns the control that implements the list.
     *
     * This is useful for getting at the layout or other properties of the
     * control.  However, do not use this to reset the contents of the control
     * directly.
     */
    public Control getControl()
    {
        return table;
    }


    /**
     * Return all the Item objects in the list.
     */
    public Collection<Item> getItems()
    {
        Vector<Item> result = new Vector<Item>();
        for (TableItem item : table.getItems())
            result.add(item);
        return result;
    }


    /**
     * Return all the list items, as plain strings.
     */
    public Collection<String> getStrings()
    {
        Vector<String> result = new Vector<String>();
        for (TableItem item : table.getItems())
            result.add(item.getText());
        return result;
    }


    /**
     * Return an integer index for the named item.
     */
    public int indexOf(String name)
    {
        if (name == null) return -1;
        int totalItems = table.getItemCount();
        for (int i = 0; i < totalItems; i++)
            if (table.getItem(i).getText().equals(name))
                return i;
        return -1;
    }
}
