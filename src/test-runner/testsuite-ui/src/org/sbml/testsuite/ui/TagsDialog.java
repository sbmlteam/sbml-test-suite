//
// @file   TagsDialog.java
// @brief  Get a list of tags from the user
// @author Michael Hucka
// @author Frank T. Bergmann
// @date   2013-04-03 based on Frank's code of 2012-06-06
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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Filter dialog for test / component tags
 */
public class TagsDialog
    extends Dialog
{
    private Tree           treeCompTags;
    private Tree           treeTestTags;
    private Tree           treePackages;
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
        int totalWidth = 330;
        int totalHeight = 400;
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
                clearSelectedPackages();
            }
        });

        // Buttons at the bottom.  Done here so that the middle section can
        // refer to the buttons to anchor the bottom of their layout.

        Button cmdCancel = new Button(shell, SWT.NONE);
        FormData fd_cmdCancel = new FormData();
        fd_cmdCancel.width = buttonWidth;
        fd_cmdCancel.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdCancel.right = new FormAttachment(100, -(margin + offset));
        cmdCancel.setLayoutData(fd_cmdCancel);
        cmdCancel.setText("Cancel");
        cmdCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                cancelPressed();
            }
        });
        cmdCancel.setFocus();

        Button cmdOk = new Button(shell, SWT.NONE);
        cmdOk.setText("OK");
        FormData fd_cmdOk = new FormData();
        fd_cmdOk.width = buttonWidth;
        fd_cmdOk.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdOk.right = new FormAttachment(cmdCancel, -margin);
        cmdOk.setLayoutData(fd_cmdOk);
        cmdOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                okPressed();
            }
        });

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

        // There is a sashForm for the 3 sections (packages, component tags,
        // test tags).  Each section is a composite, and internally uses
        // form layout.

        SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
        FormData fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(cmdClearAll, (margin + offset));
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -(margin + offset));
        fd_sashForm.left = new FormAttachment(0, margin);
        fd_sashForm.right = new FormAttachment(100, -margin);
        sashForm.setLayoutData(fd_sashForm);
        sashForm.setSashWidth(2*margin);

        Composite compPackages = new Composite(sashForm, SWT.NONE);
        compPackages.setLayout(new FormLayout());

        Label lblPackages = new Label(compPackages, SWT.NONE);
        FormData fd_lblPackages = new FormData();
        fd_lblPackages.left = new FormAttachment(0, margin);
        // Note: bottom alignment is set after btnClearPackages is defined below.
        lblPackages.setLayoutData(fd_lblPackages);
        lblPackages.setText("Unsupported Packages:");

        Button btnClearPackages = new Button(compPackages, SWT.NONE);
        btnClearPackages.setText("Clear");
        btnClearPackages.setToolTipText("Clear package selections");
        FormData fd_btnClearPackages = new FormData();
        fd_btnClearPackages.width = buttonWidth;
        fd_btnClearPackages.top = new FormAttachment(0);
        fd_btnClearPackages.right = new FormAttachment(100, -offset);
        btnClearPackages.setLayoutData(fd_btnClearPackages);
        btnClearPackages.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                clearSelectedPackages();
                updateTagsForAllPackages(treeCompTags);
                updateTagsForAllPackages(treeTestTags);
            }
        });

        fd_lblPackages.bottom = new FormAttachment(btnClearPackages, -margin, SWT.BOTTOM);

        treePackages = new Tree(compPackages, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        FormData fd_treePackages = new FormData();
        fd_treePackages.top = new FormAttachment(btnClearPackages, offset/2);
        fd_treePackages.bottom = new FormAttachment(100, -margin);
        fd_treePackages.left = new FormAttachment(0, margin + 1);
        fd_treePackages.right = new FormAttachment(100, -(margin + 1));
        treePackages.setLayoutData(fd_treePackages);
        treePackages.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                TreeItem item = (TreeItem) event.item;
                updateTagsForPackage(treeCompTags, item.getText(), item.getChecked());
                updateTagsForPackage(treeTestTags, item.getText(), item.getChecked());
            }
        });

        treePackages.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                // Make double-click toggle the selection status.

                Point point = new Point(event.x, event.y);
                TreeItem item = treePackages.getItem(point);
                if (item == null) return;
                item.setChecked(!item.getChecked());
                updateTagsForPackage(treeCompTags, item.getText(), item.getChecked());
                updateTagsForPackage(treeTestTags, item.getText(), item.getChecked());
            }
        });

        Composite compComponentTags = new Composite(sashForm, SWT.NONE);
        compComponentTags.setLayout(new FormLayout());

        Label lblComponentTags = new Label(compComponentTags, SWT.NONE);
        lblComponentTags.setText("Unsupported Component Tags:");
        FormData fd_lblComponentTags = new FormData();
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

                // If we deselect tags, some will have been tags belonging to
                // packages. That means we no longer have that entire package
                // unsupported.  So, unmark the packages affected.
                clearPackagesByClearedTags(treeCompTags);
            }
        });

        fd_lblComponentTags.bottom = new FormAttachment(btnClearCompTags, -margin, SWT.BOTTOM);

        treeCompTags = new Tree(compComponentTags, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        FormData fd_treeCompTags = new FormData();
        fd_treeCompTags.top = new FormAttachment(btnClearCompTags, offset/2);
        fd_treeCompTags.bottom = new FormAttachment(100, -margin);
        fd_treeCompTags.left = new FormAttachment(0, margin + 1);
        fd_treeCompTags.right = new FormAttachment(100, -(margin + 1));
        treeCompTags.setLayoutData(fd_treeCompTags);
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

        // This next code is used for both tags trees.  Its purpose is this:
        // if we deselected a tag from a package, and that package was
        // previously selected, then unselect it.

        SelectionAdapter pkgUpdateListener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                TreeItem item = (TreeItem) event.item;
                if (!item.getChecked())
                {
                    String pkgPrefix = getPackagePrefix(item.getText());
                    if (pkgPrefix != null)
                        setPackageSelection(pkgPrefix, false);
                }
            }
        };

        treeCompTags.addSelectionListener(pkgUpdateListener);

        Composite compTestTags = new Composite(sashForm, SWT.NONE);
        compTestTags.setLayout(new FormLayout());

        Label lblTestTags = new Label(compTestTags, SWT.NONE);
        FormData fd_lblTestTags = new FormData();
        fd_lblTestTags.left = new FormAttachment(0, margin);
        lblTestTags.setLayoutData(fd_lblTestTags);
        lblTestTags.setText("Unsupported Test Tags:");

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
                // If we deselect tags, some will have been tags belonging to
                // packages. That means we no longer have that entire package
                // unsupported.  So, unmark the packages affected.
                clearPackagesByClearedTags(treeCompTags);
            }
        });

        fd_lblTestTags.bottom = new FormAttachment(btnClearTestTags, -margin, SWT.BOTTOM);

        treeTestTags = new Tree(compTestTags, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        FormData fd_treeTestTags = new FormData();
        fd_treeTestTags.top = new FormAttachment(btnClearTestTags, offset/2);
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
        treeTestTags.addSelectionListener(pkgUpdateListener);

        // Now set the heights of the 3 sections in the sash by setting the
        // height of one of the bigger sections.  The others will be scaled
        // according to the ratio given below.

        fd_treeTestTags.height = 135;
        sashForm.setWeights(new int[] {1, 2, 2});

        // Add keyboard bindings for cancelling out of this: command-. on
        // Macs and control-w elsewhere.  (This actually will make command-w
        // do the same thing on Macs, but that's okay.)

        final Listener closeKeyListener = new Listener() {
            @Override
            public void handleEvent (final Event event)
            {
                if (UIUtils.isModifier(event)
                    && ((UIUtils.isMacOSX() && event.keyCode == '.')
                        || event.keyCode == 'w'))
                    cancelPressed();
            }
        };
        final Display display = shell.getDisplay();
        display.addFilter(SWT.KeyDown, closeKeyListener);
        shell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent notUsed)
            {
                display.removeFilter(SWT.KeyDown, closeKeyListener);
            }
        });

        UIUtils.createShellCloseListener(shell);

        // Create tool-tip handler for the list of tags.

        final CustomToolTipHandler tooltip = new CustomToolTipHandler(shell);
        tooltip.activateHoverHelp(treeCompTags);
        tooltip.activateHoverHelp(treeTestTags);

        // Final set-up.

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
            treeItem.setData("TIP_TEXT_LABEL", "The meaning of this tag: ");
            treeItem.setData("TIP_TEXT", Tags.getTagDescription(tag));
            addPackage(tag);
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
            treeItem.setData("TIP_TEXT_LABEL", "The meaning of this tag: ");
            treeItem.setData("TIP_TEXT", Tags.getTagDescription(tag));
            addPackage(tag);
        }
    }


    private final String getPackagePrefix(final String tag)
    {
        final int colonIndex = tag.indexOf(':');
        if (colonIndex > -1)
            return tag.substring(0, colonIndex);
        else
            return null;
    }


    private void addPackage(String tag)
    {
        String pkgPrefix = getPackagePrefix(tag);
        if (pkgPrefix != null)
        {
            for (TreeItem item : treePackages.getItems())
                if (item.getText().equals(pkgPrefix))
                    return;
            TreeItem treeItem = new TreeItem(treePackages, SWT.NONE);
            treeItem.setText(pkgPrefix);
        }
    }


    private void updateTagsForPackage(Tree tree, String prefix, boolean checked)
    {
        for (TreeItem item : tree.getItems())
            if (prefix.equals(getPackagePrefix(item.getText())))
                item.setChecked(checked);
    }


    private void updateTagsForAllPackages(Tree tree)
    {
        for (TreeItem item : tree.getItems())
        {
            String tagPrefix = getPackagePrefix(item.getText());
            if (tagPrefix != null)
            {
                for (TreeItem pkgItem : treePackages.getItems())
                    if (tagPrefix.equals(pkgItem.getText()))
                        item.setChecked(pkgItem.getChecked());
            }
        }
    }


    private void setPackageSelection(String pkg, boolean checked)
    {
        if (pkg == null || pkg.length() == 0) return;

        for (TreeItem item : treePackages.getItems())
            if (pkg.equals(item.getText()))
            {
                item.setChecked(checked);
                return;
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
        if (treePackages == null) return;

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
            for (TreeItem item : treePackages.getItems())
                if (tag.equals(item.getText()))
                {
                    item.setChecked(true);
                    continue outer;
                }
        }

        // If any packages are marked, then go back and check all tags
        // prefixed by that package tag.

        for (TreeItem item : treePackages.getItems())
            if (item.getChecked())
            {
                String pkg = item.getText();
                updateTagsForPackage(treeCompTags, pkg, true);
                updateTagsForPackage(treeTestTags, pkg, true);
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


    private void clearSelectedPackages()
    {
        for (TreeItem item : treePackages.getItems())
            item.setChecked(false);
    }


    private void clearPackagesByClearedTags(Tree tree)
    {
        for (TreeItem item : tree.getItems())
        {
            String pkg = getPackagePrefix(item.getText());
            if (pkg != null)
                setPackageSelection(pkg, false);
        }
    }


    private Vector<String> readTags()
    {
        Vector<String> result = new Vector<String>();

        // If a whole package is marked unsupported, we collapse the list of
        // other tags prefixed by that package, and leave only the package.

        Vector<String> packages = new Vector<String>();
        for (TreeItem item : treePackages.getItems())
            if (item.getChecked())
                packages.add(item.getText());

        result.addAll(readTagsTree(treeCompTags, packages));
        result.addAll(readTagsTree(treeTestTags, packages));

        // Finally, add the package tags, if any:

        for (String pkg : packages)
            result.add(pkg);

        return result;
    }


    private Vector<String> readTagsTree(Tree tree, Vector<String> packages)
    {
        Vector<String> result = new Vector<String>();
        for (TreeItem item : tree.getItems())
            if (item.getChecked())
            {
                // If it doesn't have a package prefix, we add the tag.  If
                // it has a prefix, then we only add the tag if the whole
                // package is not listed as unsupported.

                String pkg = getPackagePrefix(item.getText());
                if (pkg == null || !packages.contains(pkg))
                    result.add(item.getText());
            }
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
