//
// @file LabeledList.java
// @brief Utility Composite holding a list and a label
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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

/**
 * Utility Composite holding a list and a label
 */
public class LabeledList
    extends Composite
{

    private Label  label;
    private List   list;
    private Button cmdAdd;


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

        label = new Label(this, SWT.NONE);
        FormData fd_lblNewLabel = new FormData();
        fd_lblNewLabel.left = new FormAttachment(0, 10);
        label.setLayoutData(fd_lblNewLabel);
        label.setText("New Label");

        list = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        FormData fd_list = new FormData();
        fd_list.bottom = new FormAttachment(100, -10);
        fd_list.right = new FormAttachment(100, -10);
        fd_list.left = new FormAttachment(0, 10);
        list.setLayoutData(fd_list);

        cmdAdd = new Button(this, SWT.NONE);
        fd_list.top = new FormAttachment(cmdAdd, 6);
        fd_lblNewLabel.top = new FormAttachment(cmdAdd, 5, SWT.TOP);
        cmdAdd.setToolTipText("Add selected component tags to selection.");
        cmdAdd.setText("Add");
        FormData fd_button = new FormData();
        fd_button.width = 75;
        fd_button.top = new FormAttachment(0, 5);
        fd_button.right = new FormAttachment(100, -10);
        cmdAdd.setLayoutData(fd_button);

    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }


    /**
     * @return the add button
     */
    public Button getAddButton()
    {
        return cmdAdd;
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
    public List getList()
    {
        return list;
    }


    /**
     * toggles visibility of the add button
     * 
     * @param isVisible
     *            indication whether the button should be visible
     */
    public void setButtonVisible(boolean isVisible)
    {
        cmdAdd.setVisible(isVisible);
    }


    /**
     * set the label text
     * 
     * @param label
     *            the text
     */
    public void setLabel(String label)
    {
        this.label.setText(label);
    }


    /**
     * Sets the list
     * 
     * @param list
     *            the list
     */
    public void setList(List list)
    {
        this.list = list;
    }

}
