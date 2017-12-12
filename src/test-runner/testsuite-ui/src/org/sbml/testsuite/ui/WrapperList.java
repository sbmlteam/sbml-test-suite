//
// @file   WrapperList.java
// @brief  Version of SWT's List that elements sorted in a specific way
// @author Michael Hucka
// @date   Created 2013-05-03 <mhucka@caltech.edu>
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.sbml.testsuite.core.WrapperConfig;


/**
 * Version of SWT's List that keeps elements sorted in a specific way and
 * allows easy association of data with items in the list.  The add(...)
 * operation puts an item into the list in alphabetical order, except for
 * the special item "-- no wrapper --".
 */
public class WrapperList
    extends List
{
    private final static String NO_WRAPPER = "-- no wrapper --";
    private HashMap<String, WrapperConfig> wrappers;


    /**
     * Constructor.  This automatically adds one item, the "-- no wrapper --"
     * pseudo-wrapper.
     */
    public WrapperList(Composite parent, int style)
    {
        super(parent, style);
        wrappers = new HashMap<String, WrapperConfig>();
        addNoNameWrapper();
    }


    /**
     * Add a single wrapper.  If a wrapper of the same name already exists,
     * the settings will be overwritten.
     */
    public void add(WrapperConfig config)
    {
        if (config == null) return;
        String name = config.getName();
        wrappers.remove(name);
        wrappers.put(name, config);
        if (indexOf(name) == -1)        // Need prevent dups explicitly.
            add(name);                  // This is List.add().
        sortList();
        select(name);
    }


    /**
     * Add all the given wrappers.
     */
    public void add(Vector<WrapperConfig> newWrappers)
    {
        if (newWrappers == null || newWrappers.isEmpty()) return;
        for (WrapperConfig config : newWrappers)
        {
            wrappers.remove(config.getName());
            wrappers.put(config.getName(), config);
        }
        setItemsSorted(wrappers.keySet().toArray(new String[0]));
    }


    /**
     * Replace the current contents with the new set of wrappers.
     */
    public void setItems(Vector<WrapperConfig> newWrappers)
    {
        if (newWrappers == null || newWrappers.isEmpty()) return;
        removeAll();
        wrappers.clear();
        addNoNameWrapper();
        add(newWrappers);
    }


    /**
     * Like List's setItems(String[]), but sorts the list first.
     */
    private void setItemsSorted(String[] items)
    {
        if (items == null || items.length == 0) return;
        Arrays.sort(items, new Comparator<String>() {
            public int compare(String s1, String s2)
            {
                if (s1.equals(NO_WRAPPER))
                    return 1;
                else if (s2.equals(NO_WRAPPER))
                    return -1;
                else
                    return s1.compareTo(s2);
            }

            public boolean equals(Object obj)
            {
                return this.equals(obj);
            }
        });
        setItems(items);                // This is List.setItems().
    }


    /**
     * Sort the current list of wrappers in place.
     */
    private void sortList()
    {
        setItemsSorted(getItems());
    }


    public void remove(String name)
    {
        if (name == null || name.equals(NO_WRAPPER)) return;

        // If there's a selection, remember what it was so we can move the
        // selection point.
        int selectionIndex = getSelectionIndex();
        wrappers.remove(name);
        if (indexOf(name) > -1)
            super.remove(name);
        select(--selectionIndex);       // Out of range values get ignored.
    }


    /**
     * We can get away with a select(...) that uses String because
     * the items in our list are kept unique.
     */
    public void select(String name)
    {
        if (name == null || name.isEmpty()) return;
        int index = indexOf(name);
        select(index);
        setSelection(index);
    }


    public String getSelected()
    {
        int index = getSelectionIndex();
        if (index > -1 && index < getItemCount())
            return getItem(index);
        return null;
    }


    public WrapperConfig getSelectedWrapper()
    {
        String selected = getSelected();
        if (selected != null)
            return wrappers.get(selected);
        return null;
    }


    public WrapperConfig getWrapper(String name)
    {
        return wrappers.get(name);
    }


    public WrapperConfig getWrapper(int index)
    {
        if (index > -1 && index < getItemCount())
            return wrappers.get(getItem(index));
        else
            return null;
    }


    public Vector<WrapperConfig> getWrappersAsVector()
    {
        Vector<WrapperConfig> v = new Vector<WrapperConfig>(wrappers.values());
        Collections.sort(v, new Comparator<WrapperConfig>() {
            public int compare(WrapperConfig w1, WrapperConfig w2)
            {
                if (w1.getName().equals(NO_WRAPPER))
                    return 1;
                else if (w2.getName().equals(NO_WRAPPER))
                    return -1;
                else
                    return w1.getName().compareTo(w2.getName());
            }

            public boolean equals(Object obj)
            {
                return this.equals(obj);
            }
        });
        return v;
    }


    public static String noWrapperName()
    {
        return NO_WRAPPER;
    }


    private void addNoNameWrapper()
    {
        WrapperConfig config = new WrapperConfig();
        config.setName(NO_WRAPPER);
        config.setArguments("");
        config.setViewOnly(true);
        add(config);
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components
    }
}
