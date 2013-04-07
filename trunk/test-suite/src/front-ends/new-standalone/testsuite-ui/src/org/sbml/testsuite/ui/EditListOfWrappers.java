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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * Composite for editing the list of wrapper configurations
 */
public class EditListOfWrappers
    extends Composite
{
    Vector<WrapperConfig> wrappers;
    List                  displayedWrappersList;
    int                   lastSelectedIndex = -1;
    WrapperConfig         lastSelectedState = new WrapperConfig();
    EditWrapper           wrapperForm;

    private final String  NO_WRAPPER = "-- no wrapper --";

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public EditListOfWrappers(Composite parent, int style)
    {
        super(parent, style);
        final Shell shell = parent.getShell();

        wrappers = new Vector<WrapperConfig>();

        GridLayout gl_parent = new GridLayout(1, false);
        gl_parent.marginWidth = 0;
        gl_parent.marginTop = 0;
        gl_parent.marginHeight = 0;
        setLayout(gl_parent);

        SashForm sashForm = new SashForm(this, SWT.NONE);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                            1, 1));

        Composite composite = new Composite(sashForm, SWT.NONE);
        FormLayout fl_composite = new FormLayout();
        composite.setLayout(fl_composite);

        displayedWrappersList = new List(composite, SWT.BORDER);
        displayedWrappersList.setToolTipText(
            "List of wrapper configurations for running the Test Suite "
            + "with your software application(s). You can define new "
            + "configurations using the form at right.");
        FormData fd_list = new FormData();
        fd_list.top = new FormAttachment(0, 10);
        fd_list.left = new FormAttachment(0, 0);
        fd_list.right = new FormAttachment(100, 0);
        fd_list.bottom = new FormAttachment(90, 0);        
        displayedWrappersList.setLayoutData(fd_list);
        displayedWrappersList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                int index = displayedWrappersList.getSelectionIndex();
                selectWrapper(index);
            }
        });
        displayedWrappersList.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button btnadd = new Button(composite, SWT.NONE);
        FormData fd_btnadd = new FormData();
        fd_btnadd.width = 95;
        if (UIUtils.isMacOSX())
            fd_btnadd.left = new FormAttachment(0, -5);
        else
            fd_btnadd.left = new FormAttachment(2, -5);
        fd_btnadd.top = new FormAttachment(displayedWrappersList, 4);
        btnadd.setLayoutData(fd_btnadd);
        btnadd.setText("&Add...");
        btnadd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                addNewWrapper();
            }
        });
        btnadd.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button btnremove = new Button(composite, SWT.NONE);
        FormData fd_btnremove = new FormData();
        fd_btnremove.width = 95;
        if (UIUtils.isMacOSX())
            fd_btnremove.right = new FormAttachment(displayedWrappersList, 5, SWT.RIGHT);
        else
            fd_btnremove.right = new FormAttachment(displayedWrappersList, 0, SWT.RIGHT);
        fd_btnremove.top = new FormAttachment(displayedWrappersList, 4);
        btnremove.setLayoutData(fd_btnremove);
        btnremove.setText("&Remove...");
        btnremove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                removeWrapper(shell);
            }
        });
        btnremove.addKeyListener(UIUtils.createCloseKeyListener(shell));

        wrapperForm = new EditWrapper(sashForm, SWT.NONE);
        sashForm.setWeights(new int[] {120, 300});
    }


    /**
     * Adds a new wrapper to the list
     */
    public void addNewWrapper()
    {
        WrapperConfig config = new WrapperConfig();
        config.setName("newWrapper");
        config.setArguments("%d %n %o %l %v");
        displayedWrappersList.add(config.getName());
        wrappers.add(config);
        displayedWrappersList.select(wrappers.size() - 1);
        selectWrapper(wrappers.size() - 1);
    }


    /**
     * Adds the "-- no wrapper --" wrapper to the list.
     */
    public void addNoWrapper()
    {
        WrapperConfig config = new WrapperConfig();
        config.setName(NO_WRAPPER);
        config.setArguments("");
        config.setViewOnly(true);
        displayedWrappersList.add(config.getName());
        wrappers.add(config);
        displayedWrappersList.select(wrappers.size() - 1);
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
        if (lastSelectedIndex != -1)
        {
            wrappers.get(lastSelectedIndex).updateFrom(wrapperForm.toConfig());
            displayedWrappersList.setItem(lastSelectedIndex,
                                          wrappers.get(lastSelectedIndex).getName());
        }
    }


    /**
     * @return the current list of wrapper configurations
     */
    public Vector<WrapperConfig> getWrappers()
    {
        return wrappers;
    }


    public WrapperConfig getSelectedWrapper()
    {
        int index = displayedWrappersList.getSelectionIndex();
        if (wrappers == null || wrappers.isEmpty())
            return null;
        if (index >= 0)
            return wrappers.get(index);
        else
            return wrappers.get(0);
    }


    /**
     * Loads the wrapper (and selection) into the composite
     * 
     * @param wrappers
     *            the wrappers
     * @param lastWrapper
     *            the last selected wrapper
     */
    public void loadWrappers(Vector<WrapperConfig> wrappers, 
                             String lastWrapper)
    {
        this.wrappers = (Vector<WrapperConfig>) wrappers.clone();
        displayedWrappersList.removeAll();

        for (int i = 0; i < wrappers.size(); i++)
        {
            displayedWrappersList.add(wrappers.get(i).getName());
            if (lastWrapper.equals(wrappers.get(i).getName()))
                displayedWrappersList.setSelection(i);
        }

        selectWrapper(displayedWrappersList.getSelectionIndex());
    }


    /**
     * Removes the selected wrapper
     */
    public void removeWrapper(Shell shell)
    {
        int index = displayedWrappersList.getSelectionIndex();
        if (index < 0 || index > wrappers.size()) return;

        if (NO_WRAPPER.equals(wrappers.get(index).getName()))
        {
            Tell.inform(shell, "This special pseudo-wrapper is meant to be "
                        + "\npermanently available and cannot be removed.");
            return;
        }

        lastSelectedIndex = -1;
        wrappers.remove(index);
        displayedWrappersList.remove(index);

        index = index - 1;
        if (index < 0 || index > wrappers.size())
        {
            index = 0;
        }

        displayedWrappersList.setSelection(index);
        selectWrapper(index);
    }


    private void selectWrapper(int index)
    {
        if (index < 0 || index > wrappers.size() || wrappers.size() == 0)
        {
            addNoWrapper();
            return;
        }
        wrapperForm.setVisible(true);
        this.layout();
        commitPrevious();

        lastSelectedIndex = index;
        lastSelectedState.updateFrom(wrappers.get(index));
        wrapperForm.loadFrom(wrappers.get(index));
    }


    public boolean changesPending()
    {
        WrapperConfig currentFormValues = wrapperForm.toConfig();
        return (lastSelectedIndex != displayedWrappersList.getSelectionIndex())
            || ! lastSelectedState.equals(currentFormValues);
    }
}
