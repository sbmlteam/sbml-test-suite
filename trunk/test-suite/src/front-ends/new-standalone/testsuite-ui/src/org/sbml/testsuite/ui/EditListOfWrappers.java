//
// @file EditListOfWrappers.java
// @brief Composite for editing the list of wrapper configurations
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

import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * Composite for editing the list of wrapper configurations
 */
public class EditListOfWrappers
    extends Composite
{

    Vector<WrapperConfig> wrappers;
    List                  lstWrappers;
    int                   lastSelected = -1;
    EditWrappers          compWrapper;


    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public EditListOfWrappers(Composite parent, int style)
    {
        super(parent, style);

        wrappers = new Vector<WrapperConfig>();

        setLayout(new GridLayout(1, false));

        SashForm sashForm = new SashForm(this, SWT.NONE);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                                            1));

        Composite composite = new Composite(sashForm, SWT.NONE);
        composite.setLayout(new FormLayout());

        lstWrappers = new List(composite, SWT.BORDER);

        lstWrappers.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

                int index = lstWrappers.getSelectionIndex();
                selectWrapper(index);

            }
        });
        FormData fd_list = new FormData();
        fd_list.top = new FormAttachment(0, 10);
        fd_list.left = new FormAttachment(0, 10);
        fd_list.right = new FormAttachment(100, -10);
        lstWrappers.setLayoutData(fd_list);

        Button btnadd = new Button(composite, SWT.NONE);
        btnadd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                addNewWrapper();
            }
        });
        fd_list.bottom = new FormAttachment(btnadd, -6);
        FormData fd_btnadd = new FormData();
        fd_btnadd.width = 60;
        fd_btnadd.left = new FormAttachment(0, 10);
        fd_btnadd.bottom = new FormAttachment(100, -10);
        // fd_btnadd.right = new FormAttachment(100, -92);

        btnadd.setLayoutData(fd_btnadd);
        btnadd.setText("&Add");

        Button btnremove = new Button(composite, SWT.NONE);
        btnremove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

                removeWrapper();

            }
        });
        btnremove.setText("&Remove");
        FormData fd_btnremove = new FormData();
        fd_btnremove.width = 60;
        fd_btnremove.top = new FormAttachment(lstWrappers, 6);
        fd_btnremove.right = new FormAttachment(100, -10);
        // fd_btnremove.left = new FormAttachment(btnadd, 6);
        btnremove.setLayoutData(fd_btnremove);

        compWrapper = new EditWrappers(sashForm, SWT.NONE);
        sashForm.setWeights(new int[] {1, 3});

    }


    /**
     * Adds a new wrapper to the list
     */
    public void addNewWrapper()
    {
        WrapperConfig config = new WrapperConfig();
        config.setName("newWrapper");
        lstWrappers.add(config.getName());
        wrappers.add(config);
        lstWrappers.select(wrappers.size() - 1);
        selectWrapper(wrappers.size() - 1);
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }


    /**
     * Persist last changes
     */
    public void commitPrevious()
    {
        if (lastSelected != -1)
        {
            wrappers.get(lastSelected).updateFrom(compWrapper.toConfig());
            lstWrappers.setItem(lastSelected, wrappers.get(lastSelected)
                                                      .getName());
        }
    }


    /**
     * @return the current list of wrapper configurations
     */
    public Vector<WrapperConfig> getWrappers()
    {
        return wrappers;
    }


    /**
     * Loads the wrapper (and selection) into the composite
     * 
     * @param wrappers
     *            the wrappers
     * @param lastWrapper
     *            the last selected wrapper
     */
    public void
            loadWrappers(Vector<WrapperConfig> wrappers, String lastWrapper)
    {
        this.wrappers = wrappers;
        lstWrappers.removeAll();

        for (int i = 0; i < wrappers.size(); i++)
        {
            lstWrappers.add(wrappers.get(i).getName());
            if (lastWrapper.equals(wrappers.get(i).getName()))
                lstWrappers.setSelection(i);
        }

        selectWrapper(lstWrappers.getSelectionIndex());

    }


    /**
     * Removes the selected wrapper
     */
    public void removeWrapper()
    {
        int index = lstWrappers.getSelectionIndex();
        if (index < 0 || index > wrappers.size()) return;

        lastSelected = -1;
        wrappers.remove(index);
        lstWrappers.remove(index);

        index = index - 1;
        if (index < 0 || index > wrappers.size())
        {
            index = 0;
        }

        lstWrappers.setSelection(index);
        selectWrapper(index);
    }


    private void selectWrapper(int index)
    {
        if (index < 0 || index > wrappers.size() || wrappers.size() == 0)
        {
            addNewWrapper();
            return;
        }
        compWrapper.setVisible(true);
        this.layout();
        commitPrevious();

        lastSelected = index;
        compWrapper.loadFrom(wrappers.get(index));
    }
}
