//
// @file EditListOfWrappers.java
// @brief Composite for editing the list of wrapper configurations
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
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
import org.eclipse.swt.widgets.Shell;
import org.sbml.testsuite.core.WrapperConfig;


/**
 * Composite for editing the list of wrapper configurations
 */
public class EditListOfWrappers
    extends Composite
{
    private WrapperConfig         lastWrapperDefinition = new WrapperConfig();
    private WrapperList           wrapperList;
    private EditWrapper           wrapperForm;


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

        wrapperList = new WrapperList(composite, SWT.BORDER);
        wrapperList.setToolTipText(
            "List of wrapper configurations for running the Test Suite "
            + "with your software application(s). You can define new "
            + "configurations using the form at right.");
        FormData fd_list = new FormData();
        fd_list.top = new FormAttachment(0, 10);
        fd_list.left = new FormAttachment(0, 0);
        fd_list.right = new FormAttachment(100, 0);
        fd_list.bottom = new FormAttachment(90, 0);        
        wrapperList.setLayoutData(fd_list);
        wrapperList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                int index = wrapperList.getSelectionIndex();
                commitPrevious();
                if (index < 0)
                    return;
                // FIXME use event.item
                String wrapperName = wrapperList.getItem(index);
                wrapperForm.loadFrom(wrapperList.getWrapper(wrapperName));
                wrapperList.select(index);
            }
        });
        wrapperList.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Button btnadd = new Button(composite, SWT.NONE);
        FormData fd_btnadd = new FormData();
        fd_btnadd.width = 95;
        if (UIUtils.isMacOSX())
            fd_btnadd.left = new FormAttachment(0, -5);
        else
            fd_btnadd.left = new FormAttachment(2, -4);
        fd_btnadd.top = new FormAttachment(wrapperList, 4);
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
            fd_btnremove.right = new FormAttachment(wrapperList, 5, SWT.RIGHT);
        else
            fd_btnremove.right = new FormAttachment(wrapperList, 0, SWT.RIGHT);
        fd_btnremove.top = new FormAttachment(wrapperList, 4);
        btnremove.setLayoutData(fd_btnremove);
        btnremove.setText("&Remove...");
        btnremove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                int index = wrapperList.getSelectionIndex();
                if (index < 0)
                    return;
                String name = wrapperList.getItem(index);
                if (name.equals(WrapperList.noWrapperName()))
                {
                    Tell.inform(shell, "This special pseudo-wrapper is meant "
                                + "to be\npermanently available and cannot be "
                                + "removed.");
                    return;
                }

                removeWrapper(name);
            }
        });
        btnremove.addKeyListener(UIUtils.createCloseKeyListener(shell));

        wrapperForm = new EditWrapper(sashForm, SWT.NONE);

        sashForm.setWeights(new int[] {120, 325});
    }


    public void addNewWrapper()
    {
        commitPrevious();
        WrapperConfig config = new WrapperConfig();
        config.setName("newWrapper");
        config.setArguments("%d %n %o %l %v");
        wrapperList.add(config);
        wrapperList.select(config.getName());
        wrapperForm.loadFrom(config);
        wrapperForm.setFocusOnNameField();
    }


    /**
     * @return the current list of wrapper configurations
     */
    public Vector<WrapperConfig> getWrappers()
    {
        return wrapperList.getWrappersAsVector();
    }


    public WrapperConfig getSelectedWrapper()
    {
        return wrapperList.getSelectedWrapper();
    }


    /**
     * Loads the wrapper (and selection) into the composite
     * 
     * @param wrappers
     *            the wrappers
     * @param lastWrapper
     *            the last selected wrapper
     */
    public void loadWrappers(Vector<WrapperConfig> newWrappers, 
                             String lastWrapper)
    {
        // Make sure to copy the list, not just point to it.
        wrapperList.setItems(new Vector<WrapperConfig>(newWrappers));
        if (lastWrapper == null || lastWrapper.isEmpty())
        {
            String noWrapperName = WrapperList.noWrapperName();
            wrapperForm.loadFrom(new WrapperConfig(noWrapperName));
            wrapperList.select(noWrapperName);
        }
        else
        {
            wrapperForm.loadFrom(wrapperList.getWrapper(lastWrapper));
            wrapperList.select(lastWrapper);
        }
        lastWrapperDefinition = wrapperList.getWrapper(lastWrapper);
    }


    /**
     * Removes the selected wrapper
     */
    public void removeWrapper(String name)
    {
        wrapperList.remove(name);

        // If we just removed the top item in the list, we have to reset
        // the list pointer.
        if (wrapperList.getSelectionIndex() == -1)
            wrapperList.select(0);

        wrapperForm.loadFrom(wrapperList.getSelectedWrapper());
    }


    /**
     * Persist last changes.
     */
    public void commitPrevious()
    {
        if (wrapperList.getItemCount() == 1) return; // It's --no wrapper--.
        WrapperConfig config = wrapperForm.toConfig();
        if (WrapperList.noWrapperName().equals(config.getName())) return;
        wrapperList.remove(wrapperForm.getInitialName());
        wrapperList.add(config);
        lastWrapperDefinition = config;
    }


    public boolean changesPending()
    {
        WrapperConfig currentFormValues = wrapperForm.toConfig();
        String currentName = currentFormValues.getName();
        if (lastWrapperDefinition == null) return false;
        return (! lastWrapperDefinition.equals(currentFormValues)
                || ! wrapperForm.getInitialName().equals(currentName));
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }
}
