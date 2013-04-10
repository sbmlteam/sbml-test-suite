//
// @file   MainWindow.java
// @brief  MainWindow of the SBML Test suite runner
// @author Frank T. Bergmann
// @author Michael Hucka
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.CancelCallback;
import org.sbml.testsuite.core.DelayedResult;
import org.sbml.testsuite.core.FilterFunction;
import org.sbml.testsuite.core.LevelVersion;
import org.sbml.testsuite.core.ResultType;
import org.sbml.testsuite.core.RunOutcome;
import org.sbml.testsuite.core.TestCase;
import org.sbml.testsuite.core.TestSuiteSettings;
import org.sbml.testsuite.core.Util;
import org.sbml.testsuite.core.WrapperConfig;
import org.sbml.testsuite.core.data.ResultSet;
import org.sbml.testsuite.ui.model.MainModel;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.Range;


/**
 * MainWindow of the SBML Test suite runner
 */
public class MainWindow
{
    class WrapperMenuListener
        extends SelectionAdapter
    {
        private final ToolItem         dropdown;
        private final Vector<MenuItem> items;
        private final Menu             menu;


        public WrapperMenuListener(ToolItem dropdown)
        {
            this.dropdown = dropdown;
            items = new Vector<MenuItem>();
            menu = new Menu(dropdown.getParent().getShell());
        }


        public void add(String item)
        {
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            menuItem.setText(item);
            menuItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event)
                {
                    MenuItem selected = (MenuItem) event.widget;
                    dropdown.setText(selected.getText());
                    WrapperConfig current = model.getLastWrapper();
                    if (selected.getText() != current.getName())
                        changeWrapper(selected.getText());
                }
            });
            if (items.size() == 0) dropdown.setText(item);
            items.add(menuItem);
        }


        public void clear()
        {
            for (int i = items.size() - 1; i >= 0; i--)
            {
                MenuItem current = items.get(i);
                current.dispose();
            }
            items.clear();
        }


        public void select(String lastWrapperName)
        {
            dropdown.setText(lastWrapperName);
            changeWrapper(lastWrapperName);
        }


        @Override
        public void widgetSelected(SelectionEvent event)
        {
            // If a run was in progress, we need to stop it, and since we're
            // stopping, we also have to show something in the window.  We
            // show either the last case done in the selection, or if that 
            // fails for some reason, the first item in the selection region.

            boolean wasRunning = running;
            markAsRunning(false);
            if (wasRunning)
            {
                TreeItem[] selection = tree.getSelection();
                int lastDoneIndex = lastCaseDoneInSelection(selection);
                TreeItem lastDone;
                if (lastDoneIndex > 0)
                    lastDone = selection[lastDoneIndex];
                else
                    lastDone = selection[0];
                updatePlotsForSelection(lastDone);
            }

            // Now deal with the actual menu selection.

            ToolItem item = (ToolItem) event.widget;
            Rectangle rect = item.getBounds();
            Point pt = item.getParent()
                    .toDisplay(new Point(rect.x, rect.y));
            menu.setLocation(pt.x - 10,
                             pt.y + rect.height/2 + rect.height/4 + 1);
            menu.setVisible(true);
            menu.getParent().update();
        }
    }
    

    class LVSelectionMenuListener
        extends SelectionAdapter
    {
        private final ToolItem         dropdown;
        private final Vector<MenuItem> items;
        private final Menu             menu;
        private SelectionAdapter selectionListener
            = new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event)
                    {
                        MenuItem selected = (MenuItem) event.widget;
                        if (selected != null)
                        {
                            currentLV = (LevelVersion) selected.getData();
                            if (currentLV != null && ! currentLV.isHighest())
                            {
                                dropdown.setText(currentLV.toString());
                            }
                            else
                            {
                                dropdown.setText(HIGHEST_LV_TEXT);
                                currentLV = new LevelVersion(0, 0);
                            }
                            if (model != null)
                                model.getSettings().setLastLevelVersion(currentLV);
                        }
                    }
                };


        public LVSelectionMenuListener(ToolItem dropdown)
        {
            this.dropdown = dropdown;
            items = new Vector<MenuItem>();
            menu = new Menu(dropdown.getParent().getShell());
            
            addOption(HIGHEST_LV_TEXT);
            addOption(new LevelVersion(3, 1));
            addOption(new LevelVersion(2, 4));
            addOption(new LevelVersion(2, 3));
            addOption(new LevelVersion(2, 2));
            addOption(new LevelVersion(2, 1));
            addOption(new LevelVersion(1, 2));
        }


        /**
         * Used to set the option for HIGHEST_LV_TEXT.
         * Note that it doesn't set the menuItem data field, so
         * the data field value will be null.  That's what we use
         * to indicate "highest level + version" internally.
         */
        private void addOption(String text)
        {
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            menuItem.setText(text);            
            menuItem.addSelectionListener(selectionListener);
            items.add(menuItem);
        }


        private void addOption(LevelVersion lv)
        {
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            String itemText = lv.toString();
            menuItem.setText(itemText);
            menuItem.setData(lv);
            menuItem.addSelectionListener(selectionListener);
            items.add(menuItem);
        }


        /** 
         * This is used by other code in this program to set a specific
         * value; this is not triggered by the user's selection.
         */
        public void select(LevelVersion lv)
        {
            if (lv != null && ! lv.isHighest())
                dropdown.setText(lv.toString());
            else
                dropdown.setText(HIGHEST_LV_TEXT);
        }


        @Override
        public void widgetSelected(SelectionEvent event)
        {
            ToolItem item = (ToolItem) event.widget;
            Rectangle rect = item.getBounds();
            Point pt = item.getParent()
                    .toDisplay(new Point(rect.x, rect.y));
            menu.setLocation(pt.x - 10,
                             pt.y + rect.height/2 + rect.height/4 + 1);
            menu.setVisible(true);
            menu.getParent().update();
        }
    }


    private final String              HIGHEST_LV_TEXT
        = new String("Highest Level+Version");

    private final String              ITEM_RESULT = "RESULT";
    private final String              ITEM_RERUN  = "RERUN";

    private Composite                 cmpDifferences;
    private Composite                 cmpGraphs;
    //    private String                    current;
    private GridLayout                gl_gridDifferences;
    private GridLayout                gl_gridGraphs;

    private MenuItem                  menuItemOpen;
    private MenuItem                  menuItemShowMap;
    private MenuItem                  menuItemRefreshResults;
    private MenuItem                  menuItemSelectAll;
    private MenuItem                  menuItemDeselectAll;
    private MenuItem                  menuItemJumpToCase;
    private MenuItem                  menuItemRunSelected;
    private MenuItem                  menuItemShowOnlyProblematic;
    private MenuItem                  menuItemShowOnlyReally;
    private MenuItem                  menuItemShowOnlySupported;
    private MenuItem                  menuItemtest;

    private MainModel                 model;

    private final DecimalFormat       sciformat;

    protected Shell                   shell;

    private ToolBar                   toolBar;
    private ToolItem                  wrapperMenuButton;
    private ToolItem                  lvSelectionMenuButton;
    private ToolItem                  buttonRun;

    private WrapperMenuListener       wrapperMenuListener;
    private LVSelectionMenuListener   lvSelectionMenuListener;

    private Tree                      tree;
    private ResultMap                 dlgMap = null;
    private FormData                  fd_sashForm;

    private LevelVersion              currentLV = new LevelVersion(0, 0);
    private boolean                   running = false;
    private boolean                   restart = true;
    private boolean                   closing = false;
    private boolean                   ignoreDoubleClicks = false;

    private NotificationBanner        notificationBanner;
    private DescriptionSection        descriptionSection;
    private ProgressSection           progressSection;

    private Vector<String>            includedTags;
    private Vector<String>            excludedTags;
    private Vector<Integer>           includedCases;
    private Vector<Integer>           excludedCases;

    private Color                     foregroundColor;
    private Color                     backgroundColor;
    private final Display             display;
    private Thread                    uiThread = null;
    private TaskExecutor              executor = new TaskExecutor();


    /**
     * Default constructor
     */
    public MainWindow()
    {
        Display.setAppName("SBML Test Runner");
        display = new Display();
        uiThread = Thread.currentThread();
        sciformat = new DecimalFormat("##0.##E0");
        foregroundColor = new Color(getDisplay(), 60, 60, 60);
        backgroundColor = SWTResourceManager.getColor(SWT.COLOR_WHITE);
        createContents();
    }


    public Display getDisplay()
    {
        return Display.findDisplay(uiThread);
    }


    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            MainWindow window = new MainWindow();
            window.open();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void addChartForData(Composite composite, ResultSet result,
                                 String title)
    {
        Chart chart1 = new Chart(composite, SWT.NONE);
        chart1.setBackground(backgroundColor);
        Font titleFont = UIUtils.getFont("SansSerif", 12, SWT.ITALIC);

        chart1.getTitle().setFont(titleFont);
        if (title == null || title.length() == 0)
        {
            chart1.getTitle().setVisible(false);
        }
        else
        {
            // The extra spaces are to avoid truncation of the last character
            // on the right, which seems to happen (noticeably on Windows)
            // because the font is italic and (evidently) something in SWT or
            // SWT Charts isn't computing the label width properly.

            chart1.getTitle().setText(" " + title + " ");
            chart1.getTitle().setForeground(foregroundColor);
        }

        Font tickFont = UIUtils.getFont("SansSerif", 10, SWT.NORMAL);
        
        chart1.getAxisSet().getXAxis(0).getTitle().setVisible(false);
        chart1.getAxisSet().getXAxis(0).getTick().setForeground(foregroundColor);
        chart1.getAxisSet().getXAxis(0).getTick().setFont(tickFont);

        chart1.getAxisSet().getYAxis(0).getTitle().setVisible(false);
        chart1.getAxisSet().getYAxis(0).getTick().setForeground(foregroundColor);
        chart1.getAxisSet().getYAxis(0).getTick().setFont(tickFont);

        Font legendFont = UIUtils.getFont("SansSerif", 10, SWT.NORMAL);
        
        chart1.getLegend().setPosition(SWT.BOTTOM);
        chart1.getLegend().setFont(legendFont);

        /* The following use of a custom formatter is a hack to avoid getting a
           truncated number on the right-hand side of the x-axis.  SWT Chart
           doesn't provide a way to control the width of axis labels, nor the
           padding on the inside of the graph, and I could not solve the
           problem by manipulating the layout parameters of the Control
           inside of which these graphs are placed.  The ugly hack I resorted
           to here is fudging the formatter used by SWT Chart to generate the
           label strings.  This custom formatter adds an extra space
           character on the right of the labels.  The result makes the
           truncation of the last x-axis label irrelevant. */

        RightPaddedDecimalFormat fmt = new RightPaddedDecimalFormat("###.##");

        chart1.getAxisSet().getXAxis(0).getTick().setFormat(fmt);

        GridData gd_chart1 = new GridData();
        gd_chart1.horizontalAlignment = GridData.FILL;
        gd_chart1.verticalAlignment = GridData.FILL;
        gd_chart1.grabExcessHorizontalSpace = true;
        gd_chart1.grabExcessVerticalSpace = true;
        gd_chart1.horizontalIndent = 5;

        chart1.setLayoutData(gd_chart1);

        ISeriesSet seriesSet = chart1.getSeriesSet();
        double[] time = result.getTimeColumn();

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        PlotColorGenerator.reset();
        for (int i = 1; i < result.getNumColumns(); i++)
        {
            double[] ySeries = result.getColumn(i);

            min = Math.min(min, getMin(ySeries));
            max = Math.max(max, getMax(ySeries));

            ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                                                                      result.getHeaders()
                                                                            .get(i));
            series.setLineWidth(2);
            series.setLineColor(PlotColorGenerator.nextColor());
            series.setAntialias(SWT.ON);
            series.setSymbolType(PlotSymbolType.NONE);
            series.setYSeries(ySeries);
            series.setXSeries(time);
        }

        min = Math.min(min, 0);
        max *= 1.1;

        if (max < 1E-5)
            chart1.getAxisSet().getYAxis(0).getTick().setFormat(sciformat);

        chart1.getAxisSet().getXAxis(0)
              .setRange(new Range(getMin(time), getMax(time)));

        if (max - min < 1E-30 || Double.isNaN(max) || Double.isNaN(min))
            chart1.getAxisSet().getYAxis(0).adjustRange();
        else
            chart1.getAxisSet().getYAxis(0).setRange(new Range(min, max));

    }


    private void showMessageNotAvailable(Composite comp, int level, int version)
    {
        Label msg = new Label(comp, SWT.CENTER);
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        msg.setLayoutData(gd);
        msg.setText("Test case not available in SBML Level " + level
                    + " Version " + version + " format.");
    }


    private void addTreeItems()
    {
        addTreeItems(null);
    }


    private void addTreeItems(final FilterFunction func)
    {
        tree.removeAll();
        tree.clearAll(true);
        tree.setToolTipText("");

        final WrapperConfig wrapper = model.getLastWrapper();

        BusyIndicator.showWhile(getDisplay(), new Runnable() {
                public void run()
                {
                    // The .update() is to get the busy cursor to show up.
                    // Otherwise, on the mac, it doesn't get shown.
                    getDisplay().update();

                    ResultType result = ResultType.Unknown;
                    Image img = ResultColor.getImageForResultType(result);

                    for (final TestCase test : model.getSuite().getSortedCases())
                    {
                        if (!wrapperIsViewOnly(wrapper))
                        {
                            result = wrapper.getCachedResult(test.getId());
                            img = ResultColor.getImageForResultType(result);
                        }
                        if (func == null || func.filter(test, result))
                        {                        
                            TreeItem treeItem = new TreeItem(tree, SWT.NONE);
                            treeItem.setData(ITEM_RESULT, result);
                            treeItem.setImage(img);
                            treeItem.setText(test.getId());
                            if (result == ResultType.Unknown || result == null)
                                treeItem.setData(ITEM_RERUN, true);
                            else
                                treeItem.setData(ITEM_RERUN, false);
                        }
                    }
                }
            });

        progressSection.setMaxCount(tree.getItemCount());
    }


    private void selectTreeItems(final FilterFunction func)
    {
        tree.deselectAll();
        final WrapperConfig wrapper = model.getLastWrapper();
        BusyIndicator.showWhile(getDisplay(), new Runnable() {
                public void run()
                {
                    // The .update() is to get the busy cursor to show up.
                    // Otherwise, on the mac, it doesn't get shown.
                    getDisplay().update();

                    for (final TreeItem item : tree.getItems())
                    {
                        TestCase test = model.getSuite().get(item.getText());
                        ResultType result = ResultType.Unknown;
                        if (!wrapperIsViewOnly(wrapper))
                            result = wrapper.getCachedResult(test.getId());
                        if (func == null || func.filter(test, result))
                            tree.select(item);
                    }
                }
            });
    }


    private void markTreeItemsForRerun()
    {
        markTreeItemsForRerun(tree.getItems());
    }


    private void markTreeItemsForRerun(TreeItem[] selection)
    {
        for (TreeItem item : selection)
            item.setData(ITEM_RERUN, true);
    }


    private void changeWrapper(String wrapperName)
    {
        final WrapperConfig newWrapper
            = model.getSettings().getWrapper(wrapperName);

        if (! newWrapper.canRun())
        {
            informUserBadWrapper(newWrapper);
            return;
        }

        BusyIndicator.showWhile(getDisplay(), new Runnable() {
            public void run()
            {
                model.getSettings().setLastWrapper(newWrapper);
                model.getSettings().setLastLevelVersion(currentLV);
                newWrapper.beginUpdate(model.getSuite(), currentLV);
                clearFilters();
                tree.deselectAll();
                if (tree.getItemCount() > 0)
                {
                    tree.select(tree.getItem(0));
                    progressSection.setMaxCount(tree.getItemCount());
                    progressSection.setSelectedCount(1);
                    progressSection.setDoneCount(0);
                    progressSection.setStatus(RunStatus.NotStarted);
                    updatePlotsForSelection(tree.getItem(0));
                }
            }
        });

        if (wrapperIsViewOnly(newWrapper))
            progressSection.setStatus(RunStatus.NotRunnable);
    }


    private void syncFiles()
    {
        WrapperConfig wrapper = model.getLastWrapper();
        if (wrapper == null)
            return;
        wrapper.beginUpdate(model.getSuite(), currentLV);
        addTreeItems();
        clearFilters();
    }


    private String lastCaseWithCachedValue()
    {
        WrapperConfig wrapper = model.getLastWrapper();
        if (wrapper == null)
            return null;
        SortedMap<String, DelayedResult> cache = wrapper.getCache();
        String lastNotUnknown = null;
        for (Map.Entry<String, DelayedResult> entry : cache.entrySet())
            if (entry.getValue() != null
                && entry.getValue().getResult() != ResultType.Unknown
                && entry.getValue().getResult() != ResultType.Unavailable)
                lastNotUnknown = entry.getKey();
        return lastNotUnknown;
    }


    private TreeItem treeItemForCase(String theCase)
    {
        if (theCase == null || theCase.equals(""))
            return null;
        TreeItem[] items = tree.getItems();
        for (int i = 0; i < items.length; i++)
            if (items[i].getText().equals(theCase))
                return items[i];
        return null;
    }


    private int linesVisibleInTree()
    {
        int visibleCount = 0;
        Rectangle rect = tree.getClientArea();
        TreeItem item = tree.getTopItem();
        while (item != null)
        {
            visibleCount++;
            Rectangle itemRect = item.getBounds();
            if (itemRect.y + itemRect.height > rect.y + rect.height)
                break;
            else
            {
                int nextIndex = tree.indexOf(item) + 1;
                if (nextIndex >= tree.getItemCount())
                    break;
                else
                    item = tree.getItem(nextIndex);
            }
        }
        return visibleCount;
    }


    private void recenterTree(TreeItem item)
    {
        int itemIndex    = tree.indexOf(item);
        int visibleLines = linesVisibleInTree();
        int newTopIndex  = itemIndex - visibleLines/2;
        if (newTopIndex < 0) 
            newTopIndex = 0;
        tree.setTopItem(tree.getItem(newTopIndex));
        progressSection.setSelectedCount(1);
//        tree.select(item);
    }


    /**
     * Create contents of the window.
     */
    protected void createContents()
    {
        shell = new Shell(SWT.CLOSE | SWT.INHERIT_DEFAULT | SWT.TITLE | SWT.BORDER | SWT.MIN | SWT.MAX | SWT.RESIZE);
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setMinimumSize(new Point(850, 650));
        shell.setSize(850, 650);
        shell.setText("SBML Test Runner");
        shell.addKeyListener(UIUtils.createCloseKeyListener(shell));
        shell.addListener(SWT.Close, new Listener() {
                @Override
                public void handleEvent(Event event)
                {
                    event.doit = quitWithConfirmation();
                }
            });
        shell.addListener(SWT.Dispose, new Listener() {
                @Override
                public void handleEvent(Event event)
                {
                    event.doit = false; // We let the close event handle it.
                }
            });

        // Set up a listener to stop things if the user presses the escape
        // key.  WARNING: hitting escape seems to prevent updates to the menu
        // bar.  If you try to change a menu item's text as part of the
        // SWT.Traverse event handling, it doesn't work.  This means we can't
        // do something like menuItemRunSelected.setText("Paused") here.

        shell.addListener(SWT.Traverse, new Listener() {
                @Override
                public void handleEvent (final Event event)
                {
                    if (event.detail == SWT.TRAVERSE_ESCAPE && running)
                        markAsRunning(false);
                }
            });

        shell.setLayout(new FormLayout());

        createMenuBar(shell);
        createToolBar(shell);

        Font statusFont = UIUtils.getFont("SansSerif", 12, SWT.ITALIC);

        // --------------------- notifications of filters ---------------------

        if (UIUtils.isMacOSX())
            notificationBanner = new NotificationBanner(shell, SWT.CENTER, 0);
        else
            notificationBanner = new NotificationBanner(shell, SWT.CENTER, 45);
        notificationBanner.setFont(statusFont);
        notificationBanner.setForeground(backgroundColor);
        notificationBanner.setBackground(foregroundColor);
        notificationBanner.show(false);

        // ------------------ middle: tree & plot panels ----------------------

        int offset = 20 - UIUtils.scaledFontSize(20);

        SashForm sashForm = new SashForm(shell, SWT.NONE);
        sashForm.setSashWidth(5);
        fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(notificationBanner, offset, SWT.BOTTOM);
        fd_sashForm.bottom = new FormAttachment(100, -200);
        fd_sashForm.left = new FormAttachment(0, 6);
        fd_sashForm.right = new FormAttachment(100, -6);
        sashForm.setLayoutData(fd_sashForm);

        tree = new Tree(sashForm, SWT.BORDER | SWT.MULTI);
        tree.setForeground(foregroundColor);
        tree.setSortDirection(SWT.UP);

        createContextualMenuForTree(tree);

        // Note: do NOT use a KeyListener for the following.  For unknown
        // reasons, doing so will result in tree.getTopItem() returning the
        // first item in the tree, instead of the correct item, and this will
        // screw up the behavior of the select/deselect operations.  I spent
        // 4 hours debugging one night before finally discovering that the
        // difference lay in using a plain listener vs a key listener. On the 
        // Mac, calling selectAll()/deselectAll() works properly from a plain
        // listener but NOT from within a KeyListener. (Insert expletive here.)

        tree.addListener(SWT.KeyUp, new Listener() {
            @Override
            public void handleEvent(Event e)
            {
                // Command-a to select.
                // Command-shift-a to deselect.
                if (UIUtils.isModifier(e) && e.keyCode == 'a')
                    selectAll();
                if (UIUtils.isModifier(e) && UIUtils.isShift(e) && e.keyCode == 'a')
                    deselectAll();
            }
        });

        Listener treeSelectionListener = new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                updatePlotsForSelection((TreeItem) event.item);
                delayedUpdate(new Runnable() {
                    public void run()
                    {
                        if (tree != null && !tree.isDisposed())
                            progressSection.setSelectedCount(tree.getSelectionCount());
                    }});
            }
        };
        tree.addListener(SWT.Selection, treeSelectionListener);
        tree.addListener(SWT.DefaultSelection, treeSelectionListener);

        // The following changes the selection color of tree items by making
        // it almost transparent.  This is needed because otherwise, on Mac
        // OS, the system's coloring of select items may make the case result
        // color impossible to distinguish (depending on the system's/user's
        // choice of selection color).
        
        if (UIUtils.isMacOSX())
        {
            final Color listSelectionColor
                = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
            final Color blackColor
                = getDisplay().getSystemColor(SWT.COLOR_BLACK);
            tree.addListener(SWT.EraseItem, new Listener() {
                    final public void handleEvent(final Event event)
                    {
                        // First check if this item is really selected.

                        if ((event.detail & SWT.SELECTED) == 0) return; 

                        // Set the selection alpha to almost transparent. Set
                        // the foreground color to black to make it stand out.

                        GC gc = event.gc;				
                        gc.setAntialias(SWT.ON);
                        gc.setAlpha(10);
                        gc.setBackground(listSelectionColor);
                        gc.setAlpha(255);
                        gc.setForeground(blackColor);
                        gc.fillRectangle(0, event.y,
                                         tree.getClientArea().width,
                                         event.height);

                        event.detail &= ~SWT.HOT;
                        event.detail &= ~SWT.SELECTED;
                    }
                });
        }

        /* I found the tooltip in the tree to get in my way far too much.
           Instead of a tooltip, the system now has a larger message box
           where info is displayed when a case number is clicked.

        // Implement a "fake" tooltip
        final Listener labelListener = new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                Label label = (Label) event.widget;
                Shell theShell = label.getShell();
                switch (event.type)
                {
                case SWT.MouseDown:
                    Event e = new Event();
                    e.item = (TreeItem) label.getData("_TREEITEM");
                    // Assuming table is single select, set the selection as if
                    // the mouse down event went through to the table
                    tree.setSelection(new TreeItem[] {(TreeItem) e.item});
                    tree.notifyListeners(SWT.Selection, e);
                    theShell.dispose();
                    tree.setFocus();
                    break;
                case SWT.MouseExit:
                    theShell.dispose();
                    break;
                }
            }
        };

        Listener tableListener = new Listener() {
            Label label = null;
            Shell tip   = null;

            @Override
            public void handleEvent(Event event)
            {
                switch (event.type)
                {
                case SWT.Dispose:
                case SWT.KeyDown:
                case SWT.MouseMove:
                {
                    if (tip == null) break;
                    tip.dispose();
                    tip = null;
                    label = null;
                    break;
                }
                case SWT.MouseHover:
                {
                    TreeItem item = tree.getItem(new Point(event.x, event.y));
                    if (item != null)
                    {
                        if (tip != null && !tip.isDisposed()) tip.dispose();
                        tip = new Shell(shell, SWT.ON_TOP
                            | SWT.NO_FOCUS | SWT.TOOL);
                        tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                        FillLayout layout = new FillLayout();
                        layout.marginWidth = 2;
                        tip.setLayout(layout);
                        label = new Label(tip, SWT.NONE);
                        label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                        label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                        label.setData("_TREEITEM", item);
                        label.setText(model.getSuite().get(item.getText())
                                           .getToolTip());
                        label.addListener(SWT.MouseExit, labelListener);
                        label.addListener(SWT.MouseDown, labelListener);
                        Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                        Rectangle rect = item.getBounds(0);
                        Point pt = tree.toDisplay(rect.x, rect.y);
                        tip.setBounds(pt.x, pt.y, size.x, size.y);
                        tip.setVisible(true);
                    }

                }
                }
            }
        };
        tree.addListener(SWT.Dispose, tableListener);
        tree.addListener(SWT.KeyDown, tableListener);
        tree.addListener(SWT.MouseMove, tableListener);
        tree.addListener(SWT.MouseHover, tableListener);
        */
        tree.addKeyListener(UIUtils.createCloseKeyListener(shell));

        Composite composite = new Composite(sashForm, SWT.NONE);
        composite.setLayout(new FormLayout());

        SashForm sashForm_1 = new SashForm(composite, SWT.VERTICAL);
        sashForm_1.setSashWidth(5);
        FormData fd_sashForm_1 = new FormData();
        fd_sashForm_1.top = new FormAttachment(0);
        fd_sashForm_1.bottom = new FormAttachment(100);
        fd_sashForm_1.left = new FormAttachment(0);
        fd_sashForm_1.right = new FormAttachment(100);
        sashForm_1.setLayoutData(fd_sashForm_1);
        FormData fd_lbl = new FormData();
        fd_lbl.top = new FormAttachment(0);
        fd_lbl.bottom = new FormAttachment(100);
        fd_lbl.left = new FormAttachment(0);
        fd_lbl.right = new FormAttachment(100);

        cmpGraphs = new Composite(sashForm_1, SWT.BORDER);
        cmpGraphs.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gl_gridGraphs = new GridLayout(1, true);
        gl_gridGraphs.marginRight = 15;
        cmpGraphs.setLayout(gl_gridGraphs);

        cmpDifferences = new Composite(sashForm_1, SWT.BORDER);
        cmpDifferences.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gl_gridDifferences = new GridLayout(1, true);
        gl_gridDifferences.marginRight = 15;
        cmpDifferences.setLayout(gl_gridDifferences);
        sashForm_1.setWeights(new int[] {262, 262});
        sashForm.setWeights(new int[] {120, 725});

        // -------------------- bottom info sections ------------------------

        descriptionSection = new DescriptionSection(sashForm, 6, -60);
        descriptionSection.setMessageFont(statusFont);
        descriptionSection.setMessageTextColor(foregroundColor);
        descriptionSection.setMessage("");

        progressSection = new ProgressSection(sashForm, 140, -10);
        progressSection.setMessageFont(statusFont);
        progressSection.setMessageTextColor(foregroundColor);
        progressSection.setStatus(RunStatus.NotStarted);
    }


    private void createMenuBar(Shell shell)
    {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        final MenuItem menuFile = new MenuItem(menuBar, SWT.CASCADE);
        menuFile.setText("&File");

        final Menu menuFileMenuItems = new Menu(menuFile);
        menuFile.setMenu(menuFileMenuItems);

        menuItemShowMap = new MenuItem(menuFileMenuItems, SWT.NONE);
        menuItemShowMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                delayedUpdate(new Runnable() {
                    public void run()
                    {
                        showMap();
                    }
                });
            }
        });
        menuItemShowMap.setText("Show Results Map\tCtrl+M");
        menuItemShowMap.setAccelerator(SWT.MOD1 + 'M');

        new MenuItem(menuFileMenuItems, SWT.SEPARATOR);

        menuItemRefreshResults = new MenuItem(menuFileMenuItems, SWT.NONE);
        menuItemRefreshResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                syncFiles();
            }
        });
        menuItemRefreshResults.setText("Refresh Test Results\tCtrl+G");
        menuItemRefreshResults.setAccelerator(SWT.MOD1 + 'G');

        new MenuItem(menuFileMenuItems, SWT.SEPARATOR);

        menuItemOpen = new MenuItem(menuFileMenuItems, SWT.NONE);
        menuItemOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                running = false;
                restart = true;
                fileOpen();
            }
        });
        menuItemOpen.setText("Open Cases Archive\tCtrl+O");
        menuItemOpen.setAccelerator(SWT.MOD1 + 'O');

        if (!UIUtils.isMacOSX())
        {
            MenuItem menuItemPrefs = new MenuItem(menuFileMenuItems, SWT.NONE);
            menuItemPrefs.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        editPreferences();
                    }
                });
            menuItemPrefs.setText("Properties");

            new MenuItem(menuFileMenuItems, SWT.SEPARATOR);

            MenuItem menuItemQuit = new MenuItem(menuFileMenuItems, SWT.NONE);
            menuItemQuit.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        arg0.doit = quitWithConfirmation();
                    }
                });
            menuItemQuit.setText("Quit\tCtrl+Q");
            menuItemQuit.setAccelerator(SWT.MOD1 + 'Q');
        }

        MenuItem menuItemEdit = new MenuItem(menuBar, SWT.CASCADE);
        menuItemEdit.setText("&Edit");

        Menu menu_edit = new Menu(menuItemEdit);
        menuItemEdit.setMenu(menu_edit);

        menuItemSelectAll = new MenuItem(menu_edit, SWT.NONE);
        menuItemSelectAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                selectAll();
            }
        });
        menuItemSelectAll.setText("Select All\tCtrl+A");
        menuItemSelectAll.setAccelerator(SWT.MOD1 | 'A');

        menuItemDeselectAll = new MenuItem(menu_edit, SWT.NONE);
        menuItemDeselectAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                deselectAll();
            }
        });
        menuItemDeselectAll.setText("Deselect All\tCtrl+Shift+A");
        menuItemDeselectAll.setAccelerator(SWT.MOD1 + SWT.SHIFT + 'A');

        new MenuItem(menu_edit, SWT.SEPARATOR);

        menuItemJumpToCase = new MenuItem(menu_edit, SWT.NONE);
        menuItemJumpToCase.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                delayedUpdate(new Runnable() {
                    public void run()
                    {
                        TreeItem item = getCaseFromUser();
                        if (item == null)
                            return;
                        else
                        {
                            deselectAll();
                            tree.select(item);
                            updatePlotsForSelection(item);
                            recenterTree(item);
                            progressSection.setSelectedCount(1);
                        }
                    }
                });
            }
        });
        menuItemJumpToCase.setText("Jump to Case\tCtrl+J");
        menuItemJumpToCase.setAccelerator(SWT.MOD1 + 'J');

        MenuItem menuItemNewSubmenu_1 = new MenuItem(menuBar, SWT.CASCADE);
        menuItemNewSubmenu_1.setText("F&ilter");

        Menu menu_2 = new Menu(menuItemNewSubmenu_1);
        menuItemNewSubmenu_1.setMenu(menu_2);

        MenuItem menuItemFilter = new MenuItem(menu_2, SWT.NONE);
        menuItemFilter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                delayedUpdate(new Runnable() {
                    public void run()
                    {
                        filterShowByTagOrNumber();
                    }
               });
            }
        });
        menuItemFilter.setText("Filter Visible Test Cases\tCtrl+T");
        menuItemFilter.setAccelerator(SWT.MOD1 + 'T');

        new MenuItem(menu_2, SWT.SEPARATOR);

        menuItemShowOnlyProblematic = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlyProblematic.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (menuItemShowOnlyProblematic.getSelection())
                {
                    menuItemShowOnlyReally.setSelection(false);
                    menuItemShowOnlySupported.setSelection(false);
                    filterShowOnlyProblematic();
                }
                else
                {
                    clearFilters();
                }
            }
        });
        menuItemShowOnlyProblematic.setText("Show Only Problematic Entries");

        menuItemShowOnlyReally = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlyReally.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (menuItemShowOnlyReally.getSelection())
                {
                    menuItemShowOnlyProblematic.setSelection(false);
                    menuItemShowOnlySupported.setSelection(false);
                    filterShowOnlyReallyProblematic();
                }
                else
                {
                    clearFilters();
                }
            }
        });
        menuItemShowOnlyReally.setText("Show Only Really Problematic Entries");

        new MenuItem(menu_2, SWT.SEPARATOR);

        menuItemShowOnlySupported = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlySupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (menuItemShowOnlySupported.getSelection())
                {
                    menuItemShowOnlyProblematic.setSelection(false);
                    menuItemShowOnlyReally.setSelection(false);
                    filterShowOnlySupported();
                }
                else
                {
                    clearFilters();
                }
            }
        });
        menuItemShowOnlySupported.setText("Show Only Supported Tests");

        menuItemtest = new MenuItem(menuBar, SWT.CASCADE);
        menuItemtest.setText("&Test");

        Menu menu_3 = new Menu(menuItemtest);
        menuItemtest.setMenu(menu_3);

        MenuItem menuItemEditTestCase = new MenuItem(menu_3, SWT.NONE);
        menuItemEditTestCase.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                editTestCase();
            }
        });
        menuItemEditTestCase.setText("Edit Test case");
        // TODO: Remove when implemented
        menuItemEditTestCase.setEnabled(false);

        new MenuItem(menu_3, SWT.SEPARATOR);

        menuItemRunSelected = new MenuItem(menu_3, SWT.NONE);
        menuItemRunSelected.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runOrPause();
            }
        });
        menuItemRunSelected.setText("Run Selected\tCtrl+R");
        menuItemRunSelected.setAccelerator(SWT.MOD1 + 'R');

        MenuItem menuItemRunByFilter = new MenuItem(menu_3, SWT.NONE);
        menuItemRunByFilter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                resetForRun();
                runByFilter();
            }
        });
        menuItemRunByFilter.setText("Run By Tag and/or Number\tCtrl+Shift+R");
        menuItemRunByFilter.setAccelerator(SWT.MOD1 + SWT.SHIFT + 'R');

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem menuItemRunAllTests = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllTests.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                resetForRun();
                runAllTests();
            }
        });
        menuItemRunAllTests.setText("Run All Tests");

        MenuItem menuItemRunAllSupported = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllSupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                resetForRun();
                runAllSupported();
            }
        });
        menuItemRunAllSupported.setText("Run All Supported Tests");

        MenuItem menuItemRunAllNew = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                resetForRun();
                runAllNewTests();
            }
        });
        menuItemRunAllNew.setText("Run All New Tests");

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem menuItemDeleteSelectedResults = new MenuItem(menu_3, SWT.NONE);
        menuItemDeleteSelectedResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                deleteSelectedResults();
            }
        });
        menuItemDeleteSelectedResults.setText("Delete Selected Results");

        MenuItem menuItemhelp = new MenuItem(menuBar, SWT.CASCADE);
        menuItemhelp.setText("&Help");

        Menu menu_4 = new Menu(menuItemhelp);
        menuItemhelp.setMenu(menu_4);

        MenuItem menuItemAbout = new MenuItem(menu_4, SWT.NONE);
        menuItemAbout.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                showAbout();
            }
        });
        menuItemAbout.setText("About");

        if (UIUtils.isMacOSX()) macify(getDisplay());
    }


    private void createToolBar(Shell shell)
    {
        final int doubleClickTime = getDisplay().getDoubleClickTime();
        final Runnable doubleTimer = new Runnable() {
                public void run() {
                    ignoreDoubleClicks = false;
                };
        };

        if (UIUtils.isMacOSX())
            toolBar = shell.getToolBar();

        if (toolBar == null)
        {
            toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
            FormData fd_toolBar = new FormData();
            fd_toolBar.top = new FormAttachment(0);
            fd_toolBar.left = new FormAttachment(0);
            fd_toolBar.right = new FormAttachment(100);
            toolBar.setLayoutData(fd_toolBar);
        }

        ToolItem buttonShowMap = new ToolItem(toolBar, SWT.NONE);
        buttonShowMap.setImage(UIUtils.getImageResource("show_thumbnails_shadowed.png"));
        buttonShowMap.setHotImage(UIUtils.getImageResource("show_thumbnails_shadowed_highlighted.png"));
        buttonShowMap.setToolTipText("Show the map of results");
        buttonShowMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }

                if (dlgMap == null)
                    showMap();
                else
                    closeMap();
            }
        });

        ToolItem sep1 = new ToolItem(toolBar, SWT.NONE);
        sep1.setText(" ");
        sep1.setEnabled(false);

        if (UIUtils.isMacOSX())
            lvSelectionMenuButton = new ListToolItem(toolBar, HIGHEST_LV_TEXT, -3);
        else
        {
            lvSelectionMenuButton = new ToolItem(toolBar, SWT.DROP_DOWN);
            lvSelectionMenuButton.setText(HIGHEST_LV_TEXT);
        }
        lvSelectionMenuButton.setToolTipText("Set the SBML Level and Version");
        lvSelectionMenuListener = new LVSelectionMenuListener(lvSelectionMenuButton);
        lvSelectionMenuButton.addSelectionListener(lvSelectionMenuListener);
        
        if (! UIUtils.isMacOSX())
        {
            ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
            nonMacPadding.setText(" ");
            nonMacPadding.setEnabled(false);
        }
        
        ToolItem sep2 = new ToolItem(toolBar, SWT.SEPARATOR);
        sep2.setWidth(SWT.SEPARATOR_FILL);
        
        if (! UIUtils.isMacOSX())
        {
            ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
            nonMacPadding.setText(" ");
            nonMacPadding.setEnabled(false);
        }
        else
        {
            // Without this, the centering looks off on the Mac.
            ToolItem sepMiddle = new ToolItem(toolBar, SWT.SEPARATOR);
            sepMiddle.setWidth(1);
        }

        ToolItem buttonSelectAll = new ToolItem(toolBar, SWT.NONE);
        buttonSelectAll.setImage(UIUtils.getImageResource("multiple_checked.png"));
        buttonSelectAll.setHotImage(UIUtils.getImageResource("multiple_checked_highlighted.png"));
        buttonSelectAll.setToolTipText("Select all test cases");
        buttonSelectAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }
                selectAll();
            }
        });

        ToolItem buttonDeselectAll = new ToolItem(toolBar, SWT.NONE);
        buttonDeselectAll.setImage(UIUtils.getImageResource("multiple_unchecked.png"));
        buttonDeselectAll.setHotImage(UIUtils.getImageResource("multiple_unchecked_highlighted.png"));
        buttonDeselectAll.setToolTipText("Deselect all test cases");
        buttonDeselectAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }
                deselectAll();
            }
        });
        
        buttonRun = new ToolItem(toolBar, SWT.NONE);
        buttonRun.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }
                runOrPause();
            }

        });

        ToolItem buttonFilter = new ToolItem(toolBar, SWT.NONE);
        buttonFilter.setImage(UIUtils.getImageResource("filter_shadowed.png"));
        buttonFilter.setHotImage(UIUtils.getImageResource("filter_shadowed_highlighted.png"));
        buttonFilter.setToolTipText("Filter the visible test cases by tags and/or numbers");
        buttonFilter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }

                // The timerExec() below is to compensate for the SWT problem
                // described at http://stackoverflow.com/q/15825088/743730

                getDisplay().timerExec(100, new Runnable() {
                    public void run()
                    {
                        filterShowByTagOrNumber();
                    }
                });
                resetForRun();
            }
        });

        ToolItem buttonSyncFiles = new ToolItem(toolBar, SWT.NONE);
        buttonSyncFiles.setImage(UIUtils.getImageResource("refresh_shadowed.png"));
        buttonSyncFiles.setHotImage(UIUtils.getImageResource("refresh_shadowed_highlighted.png"));
        buttonSyncFiles.setToolTipText("Refresh all test results from output files");
        buttonSyncFiles.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }
                syncFiles();
                resetForRun();
            }
        });

        if (! UIUtils.isMacOSX())
        {
            ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
            nonMacPadding.setText(" ");
            nonMacPadding.setEnabled(false);
        }
        
        ToolItem sep3 = new ToolItem(toolBar, SWT.SEPARATOR);
        sep3.setWidth(SWT.SEPARATOR_FILL);
        
        if (! UIUtils.isMacOSX())
        {
            ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
            nonMacPadding.setText(" ");
            nonMacPadding.setEnabled(false);
        }
        
        if (UIUtils.isMacOSX())
            wrapperMenuButton = new ListToolItem(toolBar, "(no wrapper chosen)", -3);
        else
        {
            wrapperMenuButton = new ToolItem(toolBar, SWT.DROP_DOWN);
            wrapperMenuButton.setText("(no wrapper chosen)");
        }
        wrapperMenuButton.setToolTipText("Switch the wrapper used to run the application");
        wrapperMenuListener = new WrapperMenuListener(wrapperMenuButton);
        wrapperMenuButton.addSelectionListener(wrapperMenuListener);
        
        ToolItem sep4 = new ToolItem(toolBar, SWT.NONE);
        sep4.setText(" ");
        sep4.setEnabled(false);
        
        ToolItem buttonPreferences = new ToolItem(toolBar, SWT.NONE);
        buttonPreferences.setImage(UIUtils.getImageResource("settings_shadowed.png"));
        buttonPreferences.setHotImage(UIUtils.getImageResource("settings_shadowed_highlighted.png"));
        buttonPreferences.setToolTipText("Preferences");
        buttonPreferences.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (ignoreDoubleClicks)
                    return;
                else
                {
                    ignoreDoubleClicks = true;
                    getDisplay().timerExec(doubleClickTime, doubleTimer);
                }
                editPreferences();
            }
        });
    }


    private void createContextualMenuForTree(final Tree tree)
    {
        Menu treeContextMenu = new Menu(tree.getShell());
        MenuItem menuItemOpenOriginalSBML = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemOpenOriginalSBML.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    openOriginalSBML(tree.getSelection());
                }
            });
        menuItemOpenOriginalSBML.setText("Open Original SBML File");
        MenuItem menuItemOpenSimulatorResults = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemOpenSimulatorResults.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    openSimulatorResult(tree.getSelection());
                }

            });
        menuItemOpenSimulatorResults.setText("Open Simulator Result File");
        MenuItem menuItemOpenExpectedResults = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemOpenExpectedResults.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    openExpectedResult(tree.getSelection());
                }
            });
        menuItemOpenExpectedResults.setText("Open Expected Result File");
        MenuItem menuItemOpenDescription = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemOpenDescription.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    openTestDescription(tree.getSelection());
                }
            });
        menuItemOpenDescription.setText("Open Test Description");
        MenuItem menuItemOpenTestDir = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemOpenTestDir.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    openTestDirectory(tree.getSelection());
                }

            });
        menuItemOpenTestDir.setText("Open Test Directory");

        new MenuItem(treeContextMenu, SWT.SEPARATOR);

        MenuItem menuItemRunTest = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemRunTest.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    reRunTests(tree.getSelection());
                }
            });
        menuItemRunTest.setText("Rerun Test");

        new MenuItem(treeContextMenu, SWT.SEPARATOR);

        MenuItem menuItemSaveSedML = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemSaveSedML.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    saveSedML(tree.getSelection());
                }
            });
        menuItemSaveSedML.setText("Save SED-ML");
        // TODO: Remove when implemented
        menuItemSaveSedML.setEnabled(false);

        new MenuItem(treeContextMenu, SWT.SEPARATOR);

        MenuItem menuItemDeleteSelected = new MenuItem(treeContextMenu, SWT.PUSH);
        menuItemDeleteSelected.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    deleteSelectedResults(tree.getSelection());
                }
            });
        menuItemDeleteSelected.setText("Delete Selected Result");

        tree.setMenu(treeContextMenu);
    }


    private TreeItem getCaseFromUser()
    {
        String num = Tell.simpleQuery(shell, "Case number:");
        if (num == null) return null;
        if (!isInteger(num))
        {
            Tell.error(shell, "Input does not appear to be a number.",
                       "The input must be given as a number from 1 to "
                       + getHighestCaseNumber() + ".");
            return null;
        }
        TreeItem item = findTreeItemByNumber(num);
        if (item != null)
            return item;
        else
        {
            Tell.error(shell, "Cannot find a test case numbered " + num + "!",
                       "The valid range of test case numbers is 1 to " +
                       getHighestCaseNumber() + ".");
            return null;
        }
    }

    private void resetForRun()
    {
        markTreeItemsForRerun();
        running = false;
        restart = true;
        progressSection.setStatus(RunStatus.NotStarted);
        int selectedCount = tree.getSelectionCount();
        progressSection.setSelectedCount(selectedCount);
        if (selectedCount > 0)
            progressSection.setMaxCount(selectedCount);
        else
            progressSection.setMaxCount(tree.getItemCount());
        progressSection.setDoneCount(0);
    }


    private void selectAll()
    {
        tree.selectAll();
        progressSection.setSelectedCount(tree.getSelectionCount());
        clearPlots();
    }


    private void deselectAll()
    {
        tree.deselectAll();
        progressSection.setSelectedCount(0);
        clearPlots();
    }


    protected void deleteSelectedResults()
    {
        deleteSelectedResults(tree.getSelection());
    }


    protected void deleteSelectedResults(TreeItem[] selection)
    {
        if (selection != null && selection.length > 0)
        {
            if (! Tell.confirm(shell, "Delete the selected result"
                               + (selection.length > 1 ? "s" : "")                               
                               + "? This will\ndelete the file"
                               + (selection.length > 1 ? "s" : "")
                               + " containing the output data\n"
                               + "from the wrapper/simulator for"
                               + (selection.length > 1 ? " these" : " this")
                               + " case" + (selection.length > 1 ? "s" : "")
                               + ".\nThis action cannot be undone. Proceed?"))
                return;
        }

        for (TreeItem item : selection)
            deleteSelectedResult(item);
    }


    private void deleteSelectedResult(TreeItem item)
    {
        model.getLastWrapper().deleteResult(model.getSuite()
                                                 .get(item.getText()));
        item.setImage(ResultColor.gray.getImage());
        item.setData(ITEM_RESULT, ResultType.Unknown);
        item.setData(ITEM_RERUN, true);
    }


    protected void invalidateSelectedResults()
    {
        invalidateSelectedResults(tree.getSelection());
    }


    protected void invalidateSelectedResults(TreeItem[] selection)
    {
        for (TreeItem item : selection)
            invalidateSelectedResult(item);
    }


    private void invalidateSelectedResult(TreeItem item)
    {
        item.setImage(ResultColor.gray.getImage());
        item.setData(ITEM_RESULT, ResultType.Unknown);
        item.setData(ITEM_RERUN, true);
    }


    protected void editPreferences()
    {
        PreferenceDialog dialog = new PreferenceDialog(shell);
        dialog.center(shell.getBounds());
        dialog.setTestSuiteSettings(model.getSettings());

        TestSuiteSettings result = dialog.open();
        if (result != null)
        {
            String lastWrapperName = result.getLastWrapperName();
            model.setSettings(result);
            if (lastWrapperName != null && lastWrapperName.length() > 0)
                model.getSettings().setLastWrapper(lastWrapperName);
            else
                model.getSettings().setLastWrapper("-- no wrapper --");
            result.saveAsDefault();
            updateWrapperList();
        }
        else if (model.getLastWrapper() == null)
        {
            // No wrappers selected and none defined.
            // We default to the "-- no wrapper --" case.
            model.getSettings().setLastWrapper("-- no wrapper --");            
            updateWrapperList();
        }
    }


    protected void editTestCase()
    {
        // TODO Auto-generated method stub
        // System.out.println("edit test");
    }


    private void fileOpen()
    {
        FileDialog dlg = new FileDialog(shell, SWT.OPEN);
        dlg.setFilterNames(new String[] {"SBML Test Suite cases archives",
            "All files"});
        dlg.setFilterExtensions(new String[] {"*.zip", "*.*"});
        dlg.setText("Browse for test suite archive");
        String fileName = dlg.open();
        if (fileName != null && openArchive(new File(fileName)))
        {
            invalidateSelectedResults(tree.getItems());
            progressSection.setStatus(RunStatus.NotStarted);
            progressSection.setDoneCount(0);
            progressSection.setMaxCount(tree.getItemCount());
            if (tree.getItemCount() > 0)
            {
                tree.setSelection(tree.getItem(0));
                progressSection.setSelectedCount(1);
                updatePlotsForSelection(tree.getItem(0));
            }
        }
    }


    private boolean quitWithConfirmation()
    {
        if (closing)
        {
            quit();
            return true;
        }
        else
        {
            if (! running || Tell.confirm(shell, 
                                          "Processes are still running.\n"
                                          + "Stop them and exit anyway?"))
            {
                if (running)
                {
                    running = false;
                    executor.waitForProcesses(getDisplay());
                    Util.sleep(2000);
                }
                closing = true;
                quit();
                return true;
            }
        }
        return false;
    }


    private void quit()
    {
        running = false;
        if (executor == null) return;
        executor.shutdownNow();

        model.getSettings().saveAsDefault();

        if (shell != null && ! shell.isDisposed())
            shell.dispose();
    }


    /**
     * @return false if no filter has been introduced (i.e., the user
     * cancelled), true otherwise
     */
    protected boolean filter()
    {
        boolean filterWasInEffect = notificationBanner.isVisible();

        FilterDialog dialog = new FilterDialog(shell, SWT.None);
        //        dialog.setDescription("Please select component and test tags to include and/or exclude.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());

        if (filterWasInEffect)
        {
            // A filter is currently in effect. Repopulate the dialog box.
            dialog.setIncludedTags(includedTags);
            dialog.setExcludedTags(excludedTags);
            dialog.setIncludedCases(includedCases);
            dialog.setExcludedCases(excludedCases);
        }

        dialog.center(shell.getBounds());
        boolean nonEmptyFilter = dialog.open();
        if (nonEmptyFilter)
        {
            includedTags  = dialog.getIncludedTags();
            excludedTags  = dialog.getExcludedTags();
            includedCases = dialog.getIncludedCases();
            excludedCases = dialog.getExcludedCases();
        }
        else if (filterWasInEffect)
        {
            // If a filter WASN'T in effect before we started, and the user
            // didn't introduce one now, we clear the filter lists.

            includedTags  = null;
            excludedTags  = null;
            includedCases = null;
            excludedCases = null;
        }

        return includedTags != null || excludedTags != null
            || includedCases != null || excludedCases != null;
    }


    private void clearFilters()
    {
        menuItemShowOnlyProblematic.setSelection(false);
        menuItemShowOnlyReally.setSelection(false);
        menuItemShowOnlySupported.setSelection(false);
        notificationBanner.show(false);
        addTreeItems();
    }


    protected void filterShowByTagOrNumber()
    {
        if (filter())
        {
            final int[] count = new int[1];
            count[0] = 0;
            addTreeItems(new FilterFunction() {
                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    if (test == null) return false;
                    if (!includedCases.isEmpty() && !includedCases.contains(test.getIndex()))
                        return false;
                    if (excludedCases.contains(test.getIndex()))
                        return false;
                    if (!includedTags.isEmpty() && !test.matches(includedTags))
                        return false;
                    if (test.matches(excludedTags))
                        return false;
                    count[0]++;
                    return true;
                }
            });
            notificationBanner.setText("Filtering is in effect: "
                                       + (model.getSuite().getNumCases() - count[0])
                                       + " cases omitted.");
            notificationBanner.show(true);
        }
        else
        {
            clearFilters();
        }
    }


    protected void filterShowOnlyProblematic()
    {
        addTreeItems(new FilterFunction() {
                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case Match:          // Tool's results match expected results for case.
                    case Unavailable:    // Case is not available as requested (e.g. no such L/V).
                        return false;

                    case NoMatch:        // Tool's results don't match expected results for case.
                    case CannotSolve:    // Tool does not support one or more tags of the case.
                    case Unsupported:    // Tool does not support one or more tags of the case.
                    case Unknown:        // No result returned by tool.
                    case Error:          // Encountered error while trying to run wrapper.
                    default:
                        return true;
                    }
                }
            });
        notificationBanner.setText("Filtering is in effect: "
                                   + "showing cases with problematic results");
        notificationBanner.show(true);
    }


    protected void filterShowOnlyReallyProblematic()
    {
        addTreeItems(new FilterFunction() {
                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case Match:          // Tool's results match expected results for case.
                    case CannotSolve:    // Tool does not support one or more tags of the case.
                    case Unsupported:    // Tool does not support one or more tags of the case.
                    case Unavailable:    // Case is not available as requested (e.g. no such L/V).
                        return false;

                    case NoMatch:        // Tool's results don't match expected results for case.
                    case Unknown:        // No result returned by tool.
                    case Error:          // Encountered error while trying to run wrapper.
                    default:
                        return true;
                    }
                }
            });
        notificationBanner.setText("Filtering is in effect: "
                                   + "showing cases with really problematic results");
        notificationBanner.show(true);
    }


    protected void filterShowOnlySupported()
    {
        addTreeItems(new FilterFunction() {
                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case CannotSolve:    // Tool does not support one or more tags of the case.
                    case Unsupported:    // Tool does not support one or more tags of the case.
                    case Unavailable:    // Case is not available as requested (e.g. no such L/V).
                        return false;

                    case Match:          // Tool's results match expected results for case.
                    case NoMatch:        // Tool's results don't match expected results for case.
                    case Unknown:        // No result returned by tool.
                    case Error:          // Encountered error while trying to run wrapper.
                    default:
                        return true;
                    }
                }
            });
        notificationBanner.setText("Filtering is in effect: "
                                   + "showing only supported cases");
        notificationBanner.show(true);
    }


    private TreeItem getItem(String text)
    {
        for (TreeItem item : tree.getItems())
            if (item.getText().equals(text)) return item;
        return null;
    }


    private double getMax(double[] ySeries)
    {
        double max = Double.MIN_VALUE;
        if (ySeries != null) for (double d : ySeries)
            max = Math.max(max, d);
        return max;
    }


    private double getMin(double[] ySeries)
    {
        double min = Double.MAX_VALUE;
        if (ySeries != null) for (double d : ySeries)
            min = Math.min(min, d);
        return min;
    }


    private Vector<String> getSelectedCases()
    {
        Vector<String> result = new Vector<String>();
        for (TreeItem item : tree.getSelection())
            result.add(item.getText());
        return result;
    }


    /**
     * Initializes the model for the application.
     * 
     * If no suite exists it extracts the bundled test suite archive.
     */
    public void loadModel()
    {
        model = new MainModel();
        if (model.getSuite() == null || model.getSuite().getNumCases() == 0)
        {
            // extract archive
            File destDir = new File(Util.getUserDir());
            File destFile = new File(destDir, ".testsuite.zip");
            try
            {
                Util.copyInputStream(UIUtils.getFileResource("sbml-test-cases.zip"),
                                     new BufferedOutputStream(new FileOutputStream(destFile)));
                openArchive(destFile);
                destFile.delete();
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            currentLV = model.getSettings().getLastLevelVersion();
            lvSelectionMenuListener.select(currentLV);
        }

        updateWrapperList();
        updateStatuses();
    }


    private void macify(Display display)
    {
        if (!UIUtils.isMacOSX()) return;

        CocoaUIEnhancer enhancer = new CocoaUIEnhancer("SBML Test Runner");
        enhancer.hookApplicationMenu(display, new Listener() {
            @Override
            public void handleEvent(Event arg0)
            {
                arg0.doit = quitWithConfirmation();
            }

        }, new Listener() {
            @Override
            public void handleEvent(Event arg0)
            {
                showAbout();
            }

        }, new Listener() {

            @Override
            public void handleEvent(Event arg0)
            {
                editPreferences();
                updateStatuses();
            }

        });
    }   


    /**
     * Open the window.
     */
    public void open()
    {
        Monitor primary = getDisplay().getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        shell.setLocation(x, y);
              
        toolBar.pack();
        toolBar.layout();
        toolBar.redraw();

        shell.pack();
        shell.layout();

        loadModel();

        markAsRunning(false);
        progressSection.setMaxCount(tree.getItemCount()); // Initial default.
        if (wrapperIsViewOnly(model.getLastWrapper()))
            progressSection.setStatus(RunStatus.NotRunnable);
        else
            progressSection.setStatus(RunStatus.NotStarted);

        TreeItem toSelect = treeItemForCase(lastCaseWithCachedValue());
        if (toSelect != null)
        {
            tree.deselectAll();
            tree.select(toSelect);
            progressSection.setSelectedCount(1);
            updatePlotsForSelection(toSelect);
            recenterTree(toSelect);
        }

        shell.open();

        try
        {
            while (! shell.isDisposed())
            {
                if (! getDisplay().readAndDispatch())
                    getDisplay().sleep();
            }
        }
        finally
        {
            if (shell != null && ! shell.isDisposed())
                shell.dispose();
            if (display != null && ! display.isDisposed())
                display.dispose();
        }
        System.exit(0);
    }


    private void markAsRunning(boolean newRunState)
    {
        running = newRunState;
        if (running)
        {
            buttonRun.setImage(UIUtils.getImageResource("pause_shadowed.png"));
            buttonRun.setHotImage(UIUtils.getImageResource("pause_shadowed_highlighted.png"));
            buttonRun.setToolTipText("Pause");
            progressSection.setStatus(RunStatus.Running);
        }
        else
        {
            buttonRun.setImage(UIUtils.getImageResource("play_shadowed.png"));
            buttonRun.setHotImage(UIUtils.getImageResource("play_shadowed_highlighted.png"));
            buttonRun.setToolTipText("Run selected tests, or continue from pause");

            if (model != null)
            {
                WrapperConfig currentWrapper = model.getLastWrapper();
                if (currentWrapper == null || wrapperIsViewOnly(currentWrapper))
                    progressSection.setStatus(RunStatus.NotRunnable);
                else
                    progressSection.setStatus(RunStatus.Paused);
            }
        }
        buttonRun.getParent().redraw();
    }


    private boolean openArchive(File file)
    {
        boolean success = false;
        shell.setEnabled(false);
        try
        {
            ProgressDialog dialog = new ProgressDialog(shell, file);
            dialog.center(shell.getBounds());
            dialog.getStyledText()
                  .setText("Unpacking the archive. "
                           + "This may take some time ...\n\n");
            dialog.openWithoutWait();
            dialog.getParent().redraw();
            dialog.getParent().update();
            getDisplay().readAndDispatch();
            Util.unzipArchive(file, new CancelCallback() {
                @Override
                public boolean cancellationRequested()
                {
                    getDisplay().readAndDispatch();
                    return false;
                }
            });
            dialog.getStyledText().append("Extracting the test case files ...\n\n");
            getDisplay().readAndDispatch();
            File casesDir = new File(Util.getInternalTestSuiteDir(),
                                     "/cases/semantic/");
            if (!casesDir.isDirectory() && casesDir.exists())
            {
                dialog.getStyledText()
                      .append("Error: the archive was not in the expected format!"
                              + "\n\nAborting.");
                getDisplay().readAndDispatch();
                Tell.error(shell, "The SBML test case archive is not in "
                           + "\nthe expected format.",
                           "Perhaps it has been moved or corrupted.");
                success = false;
            }
            else
            {
                model.setTestSuiteDir(casesDir);
                getDisplay().readAndDispatch();
                dialog.getStyledText().append("Updating the list of tests ...\n\n");
                getDisplay().readAndDispatch();
                success = true;
            }
            dialog.close();
        }
        catch (Exception e)
        {
            success = false;
        }
        finally
        {
            shell.setEnabled(true);
        }
        return success;
    }


    protected void openExpectedResult(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            TestCase test = model.getSuite().get(item.getText());
            Util.openFile(test.getExpectedResultFile());
        }
    }


    protected void openOriginalSBML(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            TestCase test = model.getSuite().get(item.getText());
            Util.openFile(test.getSBMLFile());
        }
    }


    protected void openSimulatorResult(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            TestCase test = model.getSuite().get(item.getText());

            Util.openFile(model.getLastWrapper().getResultFile(test));
        }
    }


    protected void openTestDescription(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            TestCase test = model.getSuite().get(item.getText());
            Util.openFile(test.getDescriptionHTML());
        }
    }


    protected void openTestDirectory(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            TestCase test = model.getSuite().get(item.getText());
            Util.openFile(test.getCaseDirectory());
        }
    }


    protected void updateWrapperList()
    {
        wrapperMenuListener.clear();

        if (model.getWrappers().size() == 0)
        {
            // no wrappers ... let the users enter some
            editPreferences();
            return;
        }
        else
        {
            for (WrapperConfig config : model.getWrappers())
                wrapperMenuListener.add(config.getName());
            String last = model.getSettings().getLastWrapperNameOrDefault();
            wrapperMenuListener.select(last);
        }
    }


    protected void updateStatuses()
    {
        WrapperConfig currentWrapper = model.getLastWrapper();
        if (wrapperIsViewOnly(currentWrapper))
        {
            progressSection.setStatus(RunStatus.NotRunnable);
        }
    }


    protected void reRunFiltered(FilterFunction func)
    {
        Vector<TreeItem> items = new Vector<TreeItem>();
        WrapperConfig lastWrapper = model.getLastWrapper();

        for (TreeItem item : tree.getItems())
        {
            TestCase test = model.getSuite().get(item.getText());

            ResultType result = lastWrapper.getCachedResult(test.getId());
            if (result == null)
            {
                continue;
            }
            if (func == null || func.filter(test, result))
            {
                items.add(item);
            }

        }
        reRunTests(items.toArray(new TreeItem[0]));
    }


    class QueuedTestRunner
        implements Runnable
    {
        private TestCase testCase;
        private TreeItem currentItem;
        private String path;
        private WrapperConfig wrapper;
        private Display display;
        private String caseId;
        private RunOutcome runOutcome;
        private LevelVersion levelVersion;

        QueuedTestRunner(TestCase theCase, LevelVersion lv, TreeItem item,
                         String path, WrapperConfig wrapper, Display display)
        {
            this.testCase = theCase;
            this.currentItem = item;
            this.path = path;
            this.wrapper = wrapper;
            this.display = display;
            this.caseId = theCase.getId();
            this.runOutcome = null;
            this.levelVersion = lv;
        }


        @Override
        public void run()
        {
            // The user may have interrupted the runner while this process
            // was queued up for execution. Start by checking for that.

            if (!running) return;

            // This next call does synchronous execution of the wrapper.

            runOutcome = wrapper.run(testCase, levelVersion, path, 250,
                new CancelCallback() {
                    public boolean cancellationRequested()
                    {
                        return !running;
                    }
                });

            final ResultType resultType = wrapper.getResultType(testCase,
                                                                levelVersion);
            display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    if (currentItem.isDisposed()) return;

                    updateItemStatus(currentItem, resultType);
                    progressSection.incrementDoneCount();

                    // If this is the item currently being displayed, update
                    // the plots, because they're the ones being shown.
                    if (currentItem == lastSelection())
                        updatePlotsForSelection(currentItem);
                }
            });
        }
    }


    protected void reRunTests(TreeItem[] selection)
    {
        final WrapperConfig wrapper = model.getLastWrapper();
        if (wrapper == null || !wrapper.canRun())
            informUserBadWrapper(wrapper);

        if (selection == null || selection.length == 0)
        {
            Tell.inform(shell, "There is nothing to run!");
            return;
        }

        markAsRunning(true);
        String absolutePath = model.getSuite().getCasesDirectory()
                                   .getAbsolutePath();

        int selectionIndex = 0;
        if (restart)                    // Fresh run.
        {
            markTreeItemsForRerun(selection);
            restart = false;
            if (selection.length > 0)
            {
                progressSection.setSelectedCount(selection.length);
                progressSection.setMaxCount(selection.length);
            }
            progressSection.setDoneCount(0);
            progressSection.setStatus(RunStatus.NotStarted);
        }
        else                            // Continuing an interrupted run.
        {
            // Find, in the selection[] list, the last case that has a result.
            // The next one after that in selection[] is the next case to do.
            // If we don't find it, we start from 0 in selection[].
            int index = lastCaseDoneInSelection(selection);
            if (index >= 0)
                selectionIndex = ++index;
            if (lastCaseDone() == null && selection.length > 0)
            {
                progressSection.setDoneCount(0);
                progressSection.setMaxCount(selection.length);
            }
            else
            {
                progressSection.setDoneCount(selectionIndex);
                progressSection.setMaxCount(tree.getItemCount());
            }
            progressSection.setStatus(RunStatus.NotStarted);
        }

        // Explicitly clear results for whatever we're about to recalculate
        int rememberSelectionIndex = selectionIndex;
        while (selectionIndex < selection.length)
            invalidateSelectedResult(selection[selectionIndex++]);
        selectionIndex = rememberSelectionIndex;

        // if (selectionIndex < selection.length)
        //   recenterTree(selection[selectionIndex]);

        // Problem: we can't tell if running the wrapper will produce an
        // error until we try.  If it fails, we want to inform the user.
        // But, we don't want to queue up 1000+ processes and then face 1000+
        // failures and try to report them all.  Solution: run the first one
        // separately and watch for errors, then go on and run the rest.

        RunOutcome runOutcome = null;
        if (selectionIndex < selection.length)
        {
            TreeItem item = selection[selectionIndex];
            TestCase testCase = model.getSuite().get(item.getText());
            progressSection.setStatus(RunStatus.Running);
            runOutcome = wrapper.run(testCase, currentLV.getLevel(),
                                     currentLV.getVersion(), absolutePath, null);
            updateItemStatus(item, wrapper.getResultType(testCase,
                                                         currentLV.getLevel(),
                                                         currentLV.getVersion()));
            progressSection.incrementDoneCount();
            selectionIndex++;
        }

        if (runOutcome != null
            && runOutcome.getCode() != RunOutcome.Code.success)
        {
            Tell.error(shell, "Encountered a problem while attempting to"
                       + "\nrun the wrapper. Execution stopped. Please"
                       + "\ncheck the wrapper and its configuration.",
                       runOutcome.getMessage());
            markAsRunning(false);
            // Clear this result.
            selectionIndex--;
            TreeItem item = selection[selectionIndex];
            descriptionSection.setMessage("Stopped.");
            return;
        }

        // If we get here, we can hopefully continue with the remaining cases.

        MarkerFile.write(wrapper.getOutputPath(), wrapper.getProgram());

        executor.init(wrapper.isConcurrencyAllowed());
        progressSection.setStatus(RunStatus.Running);
        while (selectionIndex < selection.length)
        {
            if (! running) break;

            TreeItem item = selection[selectionIndex];
            TestCase testCase = model.getSuite().get(item.getText());
            executor.execute(new QueuedTestRunner(testCase, currentLV, item,
                                                  absolutePath, wrapper,
                                                  display));
            selectionIndex++;
        }
        executor.waitForProcesses(getDisplay());

        // At this point, if multithreading is being used, all cases have been
        // queued up but probably have not yet finished execution.  So the
        // question we face is, what do we show the user?

        if (! running)                  // interrupted
        {
            int lastIndex = lastCaseDoneInSelection(selection);
            if (lastIndex < 0)
                lastIndex = selection.length - 1;

            final TreeItem lastItem = selection[lastIndex];
        
            tree.setSelection(lastItem);
            tree.showSelection();
        
            recenterTree(lastItem);

            lastItem.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    updatePlotsForSelection(lastItem);
                }
            });

            TestCase lastCase = model.getSuite().get(lastItem.getText());
            descriptionSection.setMessage(lastCase.getId());
        }
        else
        {
            // Not interrupted. Show the last case in the selection.

            markAsRunning(false);
            progressSection.setStatus(RunStatus.Done);
        }
    }


    protected void runOrPause()
    {
        WrapperConfig wrapper = model.getLastWrapper();
        if (wrapper == null || !wrapper.canRun())
            informUserBadWrapper();
        else if (wrapperIsViewOnly(wrapper))
        {
            progressSection.setStatus(RunStatus.NotRunnable);
            return;
        }
        else if (!running)      // We're not running currently.
        {
            if (tree.getSelectionCount() > 0)
            {
                restart = true;
                runSelectedTests();
            }
            else
                runAllTests();
        }
        else                    // Pause or fresh start
        {
            markAsRunning(false);
            TreeItem item = treeItemForCase(lastCaseWithCachedValue());
            if (item != null)
                recenterTree(item);
            else
                progressSection.setStatus(RunStatus.NotStarted);
        }
    }


    protected void runAllNewTests()
    {
        if (wrapperIsNotRunnable()) return;
        runAllNewTests(tree.getItems());
    }


    protected void runAllNewTests(TreeItem[] selection)
    {
        if (wrapperIsNotRunnable()) return;
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : selection)
        {
            // if (item.getImage().equals(grey))
            if (item.getData(ITEM_RESULT) == ResultType.Unknown)
                items.add(item);
        }
        TreeItem[] itemsArray = items.toArray(new TreeItem[0]);
        tree.setSelection(itemsArray);
        reRunTests(itemsArray);
    }


    protected void runAllNewTestsInSelection()
    {
        if (wrapperIsNotRunnable()) return;
        runAllNewTests(tree.getSelection());
    }


    protected void runAllSupported()
    {
        if (wrapperIsNotRunnable()) return;
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : tree.getItems())
        {
            if (item.getData(ITEM_RESULT) != ResultType.CannotSolve)
                items.add(item);
        }
        TreeItem[] itemsArray = items.toArray(new TreeItem[0]);
        tree.setSelection(itemsArray);
        reRunTests(itemsArray);
    }


    protected void runAllTests()
    {
        if (wrapperIsNotRunnable()) return;
        selectAll();
        reRunTests(tree.getItems());
    }


    protected void runByFilter()
    {
        if (wrapperIsNotRunnable()) return;

        // To allow the filter dialog to be used to run a subset of cases when
        // a filter was previously applied, we store the filter values and
        // provide a blank filter to the user.

        Vector<String>  previousIncludedTags  = includedTags;
        Vector<String>  previousExcludedTags  = excludedTags;
        Vector<Integer> previousIncludedCases = includedCases;
        Vector<Integer> previousExcludedCases = excludedCases;
        
        includedTags  = null;
        excludedTags  = null;
        includedCases = null;
        excludedCases = null;

        if (filter())
        {
            selectTreeItems(new FilterFunction() {
                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    if (test == null) return false;
                    if (!includedCases.isEmpty() &&
                        !includedCases.contains(test.getIndex()))
                        return false;
                    if (excludedCases.contains(test.getIndex()))
                        return false;
                    if (!includedTags.isEmpty() && !test.matches(includedTags))
                        return false;
                    if (test.matches(excludedTags))
                        return false;
                    return true;
                }
            });
            runSelectedTests();
        }

        includedTags  = previousIncludedTags;
        excludedTags  = previousExcludedTags;
        includedCases = previousIncludedCases;
        excludedCases = previousExcludedCases;
    }


    protected void runSelectedTests()
    {
        if (wrapperIsNotRunnable()) return;

        TreeItem[] selection = tree.getSelection();
        reRunTests(selection);
        int lastDoneIndex = lastCaseDoneInSelection(selection);
        final TreeItem lastDone;
        if (lastDoneIndex > 0)
            lastDone = selection[lastDoneIndex];
        else
            lastDone = selection[0];
    }


    protected void saveSedML(TreeItem[] selection)
    {
        System.err.println("Exporting SED-ML not yet supported");
    }


    private void setSelectedCases(Vector<String> selection)
    {
        tree.deselectAll();
        for (String text : selection)
        {
            TreeItem item = getItem(text);
            if (item != null) tree.select(item);
        }
    }


    protected void showAbout()
    {
        AboutDialog dialog = new AboutDialog(shell, SWT.None);
        dialog.center(shell.getBounds());
        dialog.open();
    }


    protected void showMap()
    {
        dlgMap = new ResultMap(shell, model.getSuite(), model.getLastWrapper());
        dlgMap.center(shell.getBounds());
        dlgMap.setData(treeToSortedMap(tree));
        dlgMap.setReRunAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TreeItem item = getItem(e.getActionCommand());
                if (item == null) return;
                reRunTests(new TreeItem[] {item});
            }
        });

        dlgMap.setSingleClickAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TreeItem item = getItem(e.getActionCommand());
                if (item == null) return;
                tree.deselectAll();
                tree.select(item);
                recenterTree(item);
                updatePlotsForSelection(item);
            }
        });
        dlgMap.open();
        dlgMap = null;
    }


    protected void closeMap()
    {
        dlgMap.close();
    }


    private SortedMap<String, Color> treeToSortedMap(Tree tree)
    {
        SortedMap<String, Color> map = 
            Collections.synchronizedSortedMap(new TreeMap<String, Color>());

        for (TreeItem item : tree.getItems())
        {
            ResultType result = (ResultType) item.getData(ITEM_RESULT);
            map.put(item.getText(), ResultColor.getColorForResultType(result));
        }
        return map;
    }


    private void clearPlots()
    {
        // Clear existing graphs.

        if (cmpGraphs == null || cmpGraphs.isDisposed()) return;
        if (cmpDifferences == null || cmpDifferences.isDisposed()) return;

        for (int i = cmpGraphs.getChildren().length - 1; i >= 0; i--)
            cmpGraphs.getChildren()[i].dispose();
        for (int i = cmpDifferences.getChildren().length - 1; i >= 0; i--)
            cmpDifferences.getChildren()[i].dispose();
        cmpGraphs.layout();
        cmpDifferences.layout();
    }


    private void updatePlotsForSelection(TreeItem treeItem)
    {
        if (treeItem == null) return;
        if (treeItem.isDisposed()) return;

        clearPlots();

        gl_gridGraphs.numColumns = 2;
        gl_gridDifferences.numColumns = 1;

        // Show description of the case in any case.

        String      itemName = treeItem.getText();
        TestCase currentCase = model.getSuite().get(itemName);

        descriptionSection.setMessage(currentCase);

        // Check if we have something to show.

        int level   = currentLV.getLevel();
        int version = currentLV.getVersion();

        if (!currentCase.supportsLevelVersion(level, version))
        {
            showMessageNotAvailable(cmpDifferences, level, version);
            cmpDifferences.layout();
            return;
        }

        if ("FluxBalanceSteadyState".equals(currentCase.getTestType()))
            return;                     // FIXME

        // Generate new graphs.

        ResultSet expected = model.getSuite().get(itemName).getExpectedResult();
        if (expected != null)
            addChartForData(cmpGraphs, expected,
                            "Expected results for #" + itemName);

        if ((Boolean) treeItem.getData(ITEM_RERUN))
            treeItem.setImage(ResultColor.getImageForResultType(ResultType.Unknown));
        else
        {
            WrapperConfig wrapper = model.getLastWrapper();
            ResultType resultType = wrapper.getResultType(currentCase, level, version);
            treeItem.setImage(ResultColor.getImageForResultType(resultType));
            treeItem.setData(ITEM_RESULT, resultType);

            ResultSet actual   = wrapper.getResultSet(currentCase);
            ResultSet diff     = ResultSet.diff(expected, actual);

            if (actual != null)
                addChartForData(cmpGraphs, actual,
                                "Simulator results for #" + itemName);
            if (diff != null)
                addChartForData(cmpDifferences, diff, "Difference plot");
        }

        cmpGraphs.layout();
        cmpDifferences.layout();
    }


    protected void updatePlotsForSelection(TreeItem[] selection)
    {
        if (selection == null || selection.length == 0) return;
        updatePlotsForSelection(selection[0]);
    }


    public void updateItemStatus(final TreeItem currentItem,
                                 final ResultType resultType)
    {
        if (currentItem == null) return;
        if (currentItem.isDisposed()) return;

        currentItem.setImage(ResultColor.getImageForResultType(resultType));
        currentItem.setData(ITEM_RESULT, resultType);
        currentItem.setData(ITEM_RERUN, false);
        currentItem.getParent().update();

        if (dlgMap != null)
        {
            dlgMap.updateElement(currentItem.getText(), 
                                 ResultColor.getColorForResultType(resultType));
        }
    }


    private TreeItem lastSelection()
    {
        TreeItem[] selected = tree.getSelection();
        if (selected.length == 0)
            return null;
        else if (selected.length > 0)
            return selected[selected.length - 1];
        else
            return selected[0];
    }


    private String lastCaseDone()
    {
        if (tree == null) return null;

        for (int i = tree.getItemCount() - 1; i >= 0; i--)
        {
            TreeItem item = tree.getItem(i);
            if (item != null && ! (Boolean) item.getData(ITEM_RERUN))
                return item.getText();
        }
        return null;
    }


    private int lastCaseDoneInSelection(TreeItem[] selection)
    {
        if (selection == null)
            return -1;
        else 
        {
            for (int i = selection.length - 1; i >= 0; i--)
            {
                TreeItem item = selection[i];
                if (item == null)
                    continue;
                if (! (Boolean) item.getData(ITEM_RERUN)
                    && (ResultType) item.getData(ITEM_RESULT) != ResultType.Unavailable)
                    return i;
            }
            return -1;
        }
    }


    private boolean wrapperIsNoWrapper(WrapperConfig wrapper)
    {
        if (wrapper == null) return false;
        return "-- no wrapper --".equals(wrapper.getName());
    }


    private boolean wrapperIsViewOnly(WrapperConfig wrapper)
    {
        if (wrapper == null) return false;
        return wrapperIsNoWrapper(wrapper) || wrapper.isViewOnly();
    }


    private boolean wrapperIsNotRunnable()
    {
        if (model.getLastWrapper() == null)
            return true;
        if (wrapperIsViewOnly(model.getLastWrapper()))
        {
            progressSection.setStatus(RunStatus.NotRunnable);
            return true;
        }
        else
            return false;        
    }


    private void informUserBadWrapper()
    {
        informUserBadWrapper(model.getLastWrapper());
    }


    private void informUserBadWrapper(WrapperConfig wrapper)
    {
        if (wrapper == null) 
            Tell.inform(shell, "Empty wrapper selection");

        if (! wrapper.canRun())
        {
            if (wrapperIsNoWrapper(wrapper))
                Tell.inform(shell, "The special '-- no wrapper --' can only "
                            + "\nbe used for browsing the SBML Test Suite; "
                            + "\nit is not runnable.");
            else
                Tell.inform(shell, "Something is wrong with the definition of "
                            + "\nthe selected wrapper. Running tests will "
                            + "\nnot be possible until the definition is "
                            + "\ncorrected or a different wrapper is "
                            + "\nselected.");
        }
    }


    private boolean haveResults(TreeItem[] selection)
    {
        for (TreeItem item : selection)
            if (item.getData(ITEM_RESULT) != ResultType.Unknown
                && item.getData(ITEM_RESULT) != ResultType.Unavailable)
                return true;
        return false;
    }


    private TestCase findCaseByNumber(int num)
    {
        for (final TestCase test : model.getSuite().getSortedCases())
            if (Integer.parseInt(test.getId()) == num)
                return test;
        return null;
    }


    private TestCase findCaseByNumber(String num)
    {
        return findCaseByNumber(Integer.parseInt(num));
    }


    private TreeItem findTreeItemByNumber(int num)
    {
        for (final TreeItem item : tree.getItems())
            if (item != null && Integer.parseInt(item.getText()) == num)
                return item;
        return null;
    }


    private TreeItem findTreeItemByNumber(String num)
    {
        return findTreeItemByNumber(Integer.parseInt(num));
    }

    
    private int getHighestCaseNumber()
    {
        if (tree == null) return 0;
        TreeItem[] items = tree.getItems();
        return items.length;
    }


    private boolean isInteger(String str)
    {
        try  
        {  
            int d = Integer.parseInt(str);  
        }  
        catch (NumberFormatException e)  
        {  
            return false;  
        }  
        return true;  
    }


    // Some SWT UI operations don't work properly if attempted immediately.
    // This is particularly the case in selection listeners on Tree: the
    // listener does not get called at the right time w.r.t. selections in
    // the tree.  On Mac OS X 10.8 at least, if you select-all in the tree,
    // then click a single item, the values of tree.getSelected() and
    // tree.getSelectionCount() at the time the selection listener is called
    // are the values *prior* to the selection.  E.g., getSelectionCount() is
    // the entire count in the tree, not 1 as it should be.  It is possible
    // that the selection listener is not called at the right time (maybe
    // it's called before the internal SWT code updates the tree item count?)
    // or else there's a need for a delay.  In either case, forking off the
    // update to a separate thread that waits before doing its work seems to
    // solve the problem reliably.  The following code encapsulates this
    // operation and makes it easier to do elsewhere in our code.

    private void delayedUpdate(final Runnable runnable)
    {
        final Display display = getDisplay();
        display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    display.timerExec(100, runnable);
                }
            });
    }
}
