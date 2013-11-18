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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * Utility Composite holding a list and a label
 */
public class LabeledList
    extends Composite
{
    private Label  label;
    private List   list;
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

        list = new List(this, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        FormData fd_list = new FormData();
        fd_list.bottom = new FormAttachment(100, -margin);
        fd_list.left = new FormAttachment(0, 2*margin);
        fd_list.right = new FormAttachment(100, -2*margin);
        list.setLayoutData(fd_list);
        
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

        fd_list.top = new FormAttachment(button1, offset);
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
    public void setButtonsVisible(boolean isVisible)
    {
        button1.setVisible(isVisible);
        button2.setVisible(isVisible);
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
