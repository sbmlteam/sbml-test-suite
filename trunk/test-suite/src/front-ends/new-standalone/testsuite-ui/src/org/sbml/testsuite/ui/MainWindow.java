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
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
    class DropdownSelectionListener
        extends SelectionAdapter
    {
        private final ToolItem         dropdown;
        private final Vector<MenuItem> items;
        private final Menu             menu;


        public DropdownSelectionListener(ToolItem dropdown)
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
                        changeWrapper(selected.getText(), true);
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
            //            dropdown.setText("");
        }


        public void select(String lastWrapperName)
        {
            select(lastWrapperName, true);
        }


        public void select(String lastWrapperName, boolean askToFlushCache)
        {
            dropdown.setText(lastWrapperName);
            changeWrapper(lastWrapperName, askToFlushCache);
        }


        @Override
        public void widgetSelected(SelectionEvent event)
        {
            markAsRunning(false);
            updatePlotsForSelection(treeItemForCase(lastCaseDone()));
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
    

    /*
     * Strictly speaking, Label is documented in the SWT API docs as being
     * something that is not intended to be subclassed.  However, I believe
     * the intention behind that is to avoid distinctions in widgets.  Here,
     * the purpose is to encapsulate our functionality for the status box
     * in a convenient manner.
     */
    class StatusMessageLabel
        extends Label
    {
        public StatusMessageLabel(Composite parent)
        {
            super(parent, SWT.WRAP);
        }

        public void updateStatus(TestCase testCase)
        {
            String msg = "MODEL SUMMARY: " + testCase.getSynopsis()
                + "\nCOMPONENT TAGS: " + testCase.getComponentTagsString() + "."
                + "\nTEST TAGS: " + testCase.getTestTagsString() + ".";

            setText(msg);
        }

        public void updateStatus(String text)
        {
            setText(text);
        }

        @Override
        protected void checkSubclass()
        {
            // Disable the check that prevents subclassing of SWT components.
        }
    }


    private Composite                 cmpDifferences;
    private Composite                 cmpGraphs;
    //    private String                    current;
    private GridLayout                gl_gridDifferences;
    private GridLayout                gl_gridGraphs;

    private StatusMessageLabel        lblStatusMessage;

    private MenuItem                  menuItemOpen;
    private MenuItem                  menuItemShowOnlyProblematic;
    private MenuItem                  menuItemShowOnlyReally;
    private MenuItem                  menuItemShowOnlySupported;
    private MenuItem                  menuItemtest;

    private MainModel                 model;

    private final DecimalFormat       sciformat;

    protected Shell                   shell;

    private ToolBar                   toolBar;
    private ToolItem                  wrapperPopupMenuButton;
    private ToolItem                  buttonRun;

    private Color                     foregroundColor;
    private Color                     backgroundColor;

    private Tree                      tree;

    private DropdownSelectionListener wrapperPopupMenuListener;

    private ResultMap                 dlgMap;
    private FormData                  fd_sashForm;

    private int                       initialCaseStartIndex = 0;
    private boolean                   running = false;
    private boolean                   restart = false;
    private boolean                   closing = false;
    private boolean                   ignoreDoubleClicks = false;
    private SortedSet<String>         doneSet
        = Collections.synchronizedSortedSet(new TreeSet<String>());

    private CustomProgressBar         progressBar;

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


    private void addTreeItems()
    {
        addTreeItems(null);
    }


    private void addTreeItems(final FilterFunction func)
    {
        tree.removeAll();
        tree.clearAll(true);
        tree.setToolTipText("");

        WrapperConfig lastWrapper = model.getLastWrapper();

        for (final TestCase test : model.getSuite().getSortedCases())
        {
            ResultType result = lastWrapper.getCachedResult(test.getId());
            if (result == null)
            {
                continue;
            }
            if (func == null || func.filter(test, result))
            {
                final TreeItem treeItem = new TreeItem(tree, SWT.NONE);
                treeItem.setData(result);
                treeItem.setImage(ResultColor.getImageForResultType(result));
                treeItem.setText(test.getId());
            }
        }

        /*
        if (tree.getItemCount() > 0)
        {
            tree.select(tree.getItem(0));
            updatePlotsForSelection(tree.getItem(0));
        }
        */
    }


    private void changeWrapper(String wrapperName, boolean confirmFlush)
    {
        WrapperConfig newWrapper = model.getSettings().getWrapper(wrapperName);
        if (! newWrapper.canRun())
        {
            informUserBadWrapper(newWrapper);
            return;
        }

        TreeItem viewing = lastSelection();

        Vector<String> selection = getSelectedCases();
        model.getSettings().setLastWrapper(newWrapper);
        newWrapper.beginUpdate(model.getSuite());
        addTreeItems();
        setSelectedCases(selection);

        if (confirmFlush
            && haveResults(tree.getItems())
            && Tell.confirm(shell, "Should the current test results be\n"
                            + "discarded? (Doing so would be\n"
                            + "appropriate if they are from a\n"
                            + "different simulator.)"))
        {
            deleteSelectedResults(tree.getItems());
            doneSet.clear();
            progressBar.resetSteps();
            tree.deselectAll();
            tree.select(tree.getItem(0));
            initialCaseStartIndex = 0;
            updatePlotsForSelection(tree.getItem(0));
        }
        else
        {
            TreeItem toSelect = treeItemForCase(lastCaseWithCachedValue());
            if (toSelect != null)
                recenterTree(toSelect);
        }
    }


    private String lastCaseWithCachedValue()
    {
        WrapperConfig wrapper = model.getLastWrapper();
        if (wrapper == null)
            return null;
        SortedMap<String, DelayedResult> cache = wrapper.getCache();
        String lastNotUnknown = null;
        for (Map.Entry<String, DelayedResult> entry : cache.entrySet())
            if (entry.getValue().getResult() != null
                && entry.getValue().getResult() != ResultType.Unknown)
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
//        tree.select(item);
    }


    /**
     * Create contents of the window.
     */
    protected void createContents()
    {
        shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.MIN | SWT.RESIZE);
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setMinimumSize(new Point(850, 650));
        shell.setSize(850, 650);
        shell.setText("SBML Test Runner");
        shell.setLayout(new FormLayout());
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

        // Set up a listener to stop things if the user presses the escape key.
        shell.addListener(SWT.Traverse, new Listener() {
                @Override
                public void handleEvent (final Event event)
                {
                    if (event.detail == SWT.TRAVERSE_ESCAPE)
                        markAsRunning(false);
                }
            });

        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        MenuItem menuItemNewSubmenu = new MenuItem(menu, SWT.CASCADE);
        menuItemNewSubmenu.setText("&File");

        Menu menu_1 = new Menu(menuItemNewSubmenu);
        menuItemNewSubmenu.setMenu(menu_1);

        menuItemOpen = new MenuItem(menu_1, SWT.NONE);
        menuItemOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (Tell.confirm(shell,
                                 "All existing results will be discarded. Proceed?"))
                {
                    running = false;
                    restart = true;
                    fileOpen();
                }                    
            }
        });
        menuItemOpen.setText("&Open Cases Archive");
        menuItemOpen.setAccelerator(SWT.MOD1 + 'O');

        if (!UIUtils.isMacOSX())
        {
            new MenuItem(menu_1, SWT.SEPARATOR);
            MenuItem menuItemQuit = new MenuItem(menu_1, SWT.NONE);
            menuItemQuit.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        arg0.doit = quitWithConfirmation();
                    }
                });
            menuItemQuit.setText("Quit");
            menuItemQuit.setAccelerator(SWT.MOD1 + 'Q');
        }

        MenuItem menuItemNewSubmenu_1 = new MenuItem(menu, SWT.CASCADE);
        menuItemNewSubmenu_1.setText("F&ilter");

        Menu menu_2 = new Menu(menuItemNewSubmenu_1);
        menuItemNewSubmenu_1.setMenu(menu_2);

        menuItemShowOnlyProblematic = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlyProblematic.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlyProblematic();
            }
        });
        menuItemShowOnlyProblematic.setText("Show Only Problematic Entries");

        menuItemShowOnlyReally = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlyReally.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlyReallyProblematic();
            }
        });
        menuItemShowOnlyReally.setText("Show Only Really Problematic Entries");

        new MenuItem(menu_2, SWT.SEPARATOR);

        menuItemShowOnlySupported = new MenuItem(menu_2, SWT.CHECK);
        menuItemShowOnlySupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlySupported();
            }
        });
        menuItemShowOnlySupported.setText("Show Only Supported Tests");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem menuItemFilterByTags = new MenuItem(menu_2, SWT.NONE);
        menuItemFilterByTags.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterByTags();
            }
        });
        menuItemFilterByTags.setText("Filter by Tags");

        MenuItem menuItemFilterByTest = new MenuItem(menu_2, SWT.NONE);
        menuItemFilterByTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterByRange();
            }
        });
        menuItemFilterByTest.setText("Filter by Test Range");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem menuItemExcludeTestsBy = new MenuItem(menu_2, SWT.NONE);
        menuItemExcludeTestsBy.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterExcludeByTag();
            }
        });
        menuItemExcludeTestsBy.setText("Exclude Tests By Tag");

        MenuItem menuItemExcludeTestsBy_1 = new MenuItem(menu_2, SWT.NONE);
        menuItemExcludeTestsBy_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterExcludeByRange();
            }
        });
        menuItemExcludeTestsBy_1.setText("Exclude Tests By Range");

        menuItemtest = new MenuItem(menu, SWT.CASCADE);
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

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem menuItemRunSelected = new MenuItem(menu_3, SWT.NONE);
        menuItemRunSelected.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                doneSet.clear();
                running = false;
                restart = true;
                runSelected();
            }
        });
        menuItemRunSelected.setText("Run Selected");
        menuItemRunSelected.setAccelerator(SWT.MOD1 + 'R');

        MenuItem menuItemRunByTag = new MenuItem(menu_3, SWT.NONE);
        menuItemRunByTag.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                doneSet.clear();
                running = false;
                restart = true;
                progressBar.resetSteps();
                runByTag();
            }
        });
        menuItemRunByTag.setText("Run By Tag");

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem menuItemRunAllTests = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllTests.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                doneSet.clear();
                running = false;
                restart = true;
                progressBar.resetSteps();
                runAllTests();
            }
        });
        menuItemRunAllTests.setText("Run All Tests");

        MenuItem menuItemRunAllSupported = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllSupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                doneSet.clear();
                running = false;
                restart = true;
                progressBar.resetSteps();
                runAllSupported();
            }
        });
        menuItemRunAllSupported.setText("Run All Supported");

        MenuItem menuItemRunAllNew = new MenuItem(menu_3, SWT.NONE);
        menuItemRunAllNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                doneSet.clear();
                running = false;
                restart = true;
                progressBar.resetSteps();
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

        MenuItem menuItemhelp = new MenuItem(menu, SWT.CASCADE);
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
        
        SashForm sashForm = new SashForm(shell, SWT.NONE);
        sashForm.setSashWidth(5);
        fd_sashForm = new FormData();
        if (!UIUtils.isMacOSX()) fd_sashForm.top = new FormAttachment(100, 0);
        //        fd_sashForm.bottom = new FormAttachment(100, -120);
        fd_sashForm.bottom = new FormAttachment(100, -140);
        fd_sashForm.right = new FormAttachment(100, -6);
        fd_sashForm.left = new FormAttachment(0, 6);
        sashForm.setLayoutData(fd_sashForm);

        int offset = 20 - UIUtils.scaledFontSize(20);
        Group statusGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        statusGroup.setText("Test case information");
        FormData fd_statusGroup = new FormData();
        fd_statusGroup.top = new FormAttachment(sashForm, 6);
        fd_statusGroup.left = new FormAttachment(0, 4 + offset/2);
        fd_statusGroup.right = new FormAttachment(100, -4 - offset/2);
        //        fd_statusGroup.bottom = new FormAttachment(100, -5);
        fd_statusGroup.bottom = new FormAttachment(100, -35);
        statusGroup.setLayoutData(fd_statusGroup);

        lblStatusMessage = new StatusMessageLabel(shell);
        FormData fd_lblStatusMessage = new FormData();
        fd_lblStatusMessage.top = new FormAttachment(sashForm, 27);
        fd_lblStatusMessage.left = new FormAttachment(0, 15);
        //        fd_lblStatusMessage.bottom = new FormAttachment(100, -10);
        fd_lblStatusMessage.bottom = new FormAttachment(100, -40);
        fd_lblStatusMessage.right = new FormAttachment(100, -15);
        lblStatusMessage.setLayoutData(fd_lblStatusMessage);
        lblStatusMessage.addKeyListener(UIUtils.createCloseKeyListener(shell));
        lblStatusMessage.updateStatus("");
        Font statusFont = UIUtils.getFont("SansSerif", 12, SWT.ITALIC);
        lblStatusMessage.setFont(statusFont);
        lblStatusMessage.setForeground(foregroundColor);
        lblStatusMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        if (! UIUtils.isMacOSX())
            lblStatusMessage.moveAbove(statusGroup);

        // Group progressGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        // FormData fd_progressGroup = new FormData();
        // fd_progressGroup.top = new FormAttachment(sashForm, 110);
        // fd_progressGroup.bottom = new FormAttachment(100, -8);
        // fd_progressGroup.left = new FormAttachment(0, 8);
        // fd_progressGroup.right = new FormAttachment(100, -8);
        // progressGroup.setLayoutData(fd_progressGroup);

        progressBar = new CustomProgressBar(shell, SWT.HORIZONTAL);
        FormData fd_progressBar = new FormData();
        fd_progressBar.top = new FormAttachment(sashForm, 110);
        fd_progressBar.bottom = new FormAttachment(100, -10);
        if (UIUtils.isMacOSX())
        {
            fd_progressBar.left = new FormAttachment(0, 7);
            fd_progressBar.right = new FormAttachment(100, -7);
        }
        else
        {
            fd_progressBar.left = new FormAttachment(0, 6);
            fd_progressBar.right = new FormAttachment(100, -6);
        }
        progressBar.setLayoutData(fd_progressBar);        
        progressBar.resetSteps();

        tree = new Tree(sashForm, SWT.BORDER | SWT.MULTI);
        // tree.
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

                updatePlotsForSelection(tree.getSelection());
            }
        });
        tree.setSortDirection(SWT.UP);

        Menu treeContextMenu = new Menu(shell);
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

        // TODO: Remove when implemented
        menuItemEditTestCase.setEnabled(false);
        menuItemSaveSedML.setEnabled(false);

        tree.setMenu(treeContextMenu);

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
        fd_sashForm_1.bottom = new FormAttachment(100);
        fd_sashForm_1.right = new FormAttachment(100);
        fd_sashForm_1.top = new FormAttachment(0);
        fd_sashForm_1.left = new FormAttachment(0);
        sashForm_1.setLayoutData(fd_sashForm_1);
        FormData fd_lbl = new FormData();
        fd_lbl.top = new FormAttachment(0);
        fd_lbl.left = new FormAttachment(0);
        fd_lbl.right = new FormAttachment(100);
        fd_lbl.bottom = new FormAttachment(100);

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
    }


    protected void deleteSelectedResults()
    {
        deleteSelectedResults(tree.getSelection());
    }


    protected void deleteSelectedResults(TreeItem[] selection)
    {
        for (TreeItem item : selection)
        {
            deleteSelectedResult(item);
        }
    }


    private void deleteSelectedResult(TreeItem item)
    {
        model.getLastWrapper().deleteResult(model.getSuite()
                                                 .get(item.getText()));
        item.setImage(ResultColor.gray.getImage());
        item.setData(ResultType.Unknown);
    }


    protected void editPreferences()
    {
        PreferenceDialog dialog = new PreferenceDialog(shell);

        dialog.setTestSuiteSettings(model.getSettings());
        dialog.center(shell.getBounds());

        TestSuiteSettings result = dialog.open();
        if (result != null)
        {
            String lastWrapper = result.getLastWrapperName();
            model.setSettings(result);
            boolean askToFlushCache = false;
            if (lastWrapper != null && lastWrapper.length() > 0)
            {
                model.getSettings().setLastWrapper(lastWrapper);
                askToFlushCache = true;
            }
            result.saveAsDefault();
            refreshList(askToFlushCache);
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
        if (fileName != null)
        {
            openArchive(new File(fileName));
            deleteSelectedResults(tree.getItems());
            progressBar.resetSteps();
            if (tree.getItemCount() > 0)
            {
                tree.setSelection(tree.getItem(0));
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

        if (shell != null && ! shell.isDisposed())
            shell.dispose();
    }


    protected void filterByRange()
    {
        DialogFilterRange dialog = new DialogFilterRange(shell, SWT.None);
        dialog.setDescription("Please select the range of tests to include.");
        dialog.center(shell.getBounds());
        if (tree.getItemCount() > 0)
            dialog.setRange(tree.getItem(0).getText(),
                            tree.getItem(tree.getItemCount() - 1).getText());

        final Range selection = dialog.open();
        if (selection != null)
        {
            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    double current = Double.parseDouble(test.getId());
                    return current >= selection.lower
                        && current <= selection.upper;
                }
            });
        }

    }


    protected void filterByTags()
    {
        DialogFilterTags dialog = new DialogFilterTags(shell, SWT.None);
        dialog.setDescription("Please select component and test tags to include.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shell.getBounds());
        final Vector<String> selection = dialog.open();
        if (selection != null)
        {
            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    return test.matches(selection);
                }
            });
        }

    }


    protected void filterExcludeByRange()
    {
        DialogFilterRange dialog = new DialogFilterRange(shell, SWT.None);
        dialog.setDescription("Please select the range of tests to exclude.");
        dialog.center(shell.getBounds());
        if (tree.getItemCount() > 0)
            dialog.setRange(tree.getItem(0).getText(),
                            tree.getItem(tree.getItemCount() - 1).getText());
        final Range selection = dialog.open();
        if (selection != null)
        {
            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    double current = Double.parseDouble(test.getId());
                    return !(current >= selection.lower && current <= selection.upper);
                }
            });
        }
    }


    protected void filterExcludeByTag()
    {
        DialogFilterTags dialog = new DialogFilterTags(shell, SWT.None);
        dialog.setDescription("Please select component and test tags to exclude.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shell.getBounds());
        final Vector<String> selection = dialog.open();
        if (selection != null)
        {
            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    return !test.matches(selection);
                }
            });
        }
    }


    protected void filterShowOnlyProblematic()
    {
        if (menuItemShowOnlyProblematic.getSelection())
        {
            menuItemShowOnlyReally.setSelection(false);
            menuItemShowOnlySupported.setSelection(false);

            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case Match:
                        return false;
                    case CannotSolve:
                    case NoMatch:
                    case Unknown:
                    case Error:
                    default:
                        return true;
                    }
                }
            });
        }
        else
        {
            addTreeItems();
        }

    }


    protected void filterShowOnlyReallyProblematic()
    {
        if (menuItemShowOnlyReally.getSelection())
        {
            menuItemShowOnlyProblematic.setSelection(false);
            menuItemShowOnlySupported.setSelection(false);

            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case Match:
                    case CannotSolve:
                        return false;
                    case NoMatch:
                    case Unknown:
                    case Error:
                    default:
                        return true;
                    }
                }
            });
        }
        else
        {
            addTreeItems();
        }
    }


    protected void filterShowOnlySupported()
    {
        if (menuItemShowOnlySupported.getSelection())
        {
            menuItemShowOnlyProblematic.setSelection(false);
            menuItemShowOnlyReally.setSelection(false);

            addTreeItems(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    switch (result)
                    {
                    case CannotSolve:
                        return false;
                    case Match:
                    case NoMatch:
                    case Unknown:
                    case Error:
                    default:
                        return true;
                    }
                }
            });
        }
        else
        {
            addTreeItems();
        }
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
        boolean askToFlushCache = false;
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
                askToFlushCache = openArchive(destFile);
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

            return;
        }
        refreshList(askToFlushCache);
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
            }

        });

        toolBar = shell.getToolBar();
    }   


    /**
     * Open the window.
     */
    public void open()
    {
        Monitor primary = getDisplay().getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();

        final int doubleClickTime = getDisplay().getDoubleClickTime();
        final Runnable doubleTimer = new Runnable() {
                public void run() {
                    ignoreDoubleClicks = false;
                };
        };
        
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        shell.setLocation(x, y);

        toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
        fd_sashForm.top = new FormAttachment(toolBar);
        FormData fd_toolBar = new FormData();
        fd_toolBar.right = new FormAttachment(100);
        fd_toolBar.top = new FormAttachment(0);
        fd_toolBar.left = new FormAttachment(0);
        toolBar.setLayoutData(fd_toolBar);

        if (UIUtils.isMacOSX()) macify(getDisplay());

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
                showMap();
            }
        });
        
        // Stretchable fills work only on Mac OS X anyway, so we only bother to
        // do the following there.
        if (UIUtils.isMacOSX())
        {
        	// sep0 offsets the width of the wrapper selector button on the other
        	// side of the toolbar, so that the stretchable fill separators are
        	// equal on both sides of the rewind/run/stop buttons.  That keeps
        	// the buttons in the middle when the whole panel is resized.
        	ToolItem sep0 = new ToolItem(toolBar, SWT.SEPARATOR);
        	sep0.setWidth(174);
        }
        else
        {
            ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
            nonMacPadding.setText(" ");
            nonMacPadding.setEnabled(false);
        }
        
        ToolItem sep1 = new ToolItem(toolBar, SWT.SEPARATOR);
        sep1.setWidth(SWT.SEPARATOR_FILL);
        
        if (! UIUtils.isMacOSX())
        {
        	ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
        	nonMacPadding.setText(" ");
        	nonMacPadding.setEnabled(false);
        }
        
        ToolItem buttonRestart = new ToolItem(toolBar, SWT.NONE);
        buttonRestart.setImage(UIUtils.getImageResource("fast_backward_shadowed.png"));
        buttonRestart.setHotImage(UIUtils.getImageResource("fast_backward_shadowed_highlighted.png"));
        buttonRestart.setToolTipText("Clear results and reset to case 00001");
        buttonRestart.addSelectionListener(new SelectionAdapter() {
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

                if (Tell.confirm(shell,
                                 "All existing results will be discarded. Proceed?"))
                {
                    running = false;
                    restart = true;
                    initialCaseStartIndex = 0;
                    deleteSelectedResults(tree.getItems());
                    progressBar.resetSteps();
                    if (tree.getItemCount() > 0)
                    {
                        tree.setSelection(tree.getItem(0));
                        updatePlotsForSelection(tree.getItem(0));
                    }
                }

            }
        });

        ToolItem sep2 = new ToolItem(toolBar, SWT.NONE);
        sep2.setText(" ");
        sep2.setEnabled(false);
        
        buttonRun = new ToolItem(toolBar, SWT.NONE);
        markAsRunning(false);
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

                if (!running)
                {
                    if (tree.getSelectionCount() > 1)
                    {
                        restart = true;
                        runSelected();
                    }
                    else
                        runAllTests();
                }
                else
                {
                    markAsRunning(false);
                    TreeItem item = treeItemForCase(lastCaseWithCachedValue());
                    if (item != null)
                        recenterTree(item);
                }
            }

        });

        ToolItem sep3 = new ToolItem(toolBar, SWT.NONE);
        sep3.setText(" ");
        sep3.setEnabled(false);

        ToolItem buttonRunFast = new ToolItem(toolBar, SWT.NONE);
        buttonRunFast.setImage(UIUtils.getImageResource("forward_shadowed.png"));
        buttonRunFast.setHotImage(UIUtils.getImageResource("forward_shadowed_highlighted.png"));
        buttonRunFast.setToolTipText("Run only cases that still lack results");
        buttonRunFast.addSelectionListener(new SelectionAdapter() {
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

                if (!running)
                {
                    if (tree.getSelectionCount() > 1)
                    {
                        restart = true;
                        runAllNewTestsInSelection();
                    }
                    else
                        runAllNewTests();
                }
                else
                {
                    markAsRunning(false);
                    TreeItem item = treeItemForCase(lastCaseWithCachedValue());
                    if (item != null)
                        recenterTree(item);
                }
            }
        });
        
        if (! UIUtils.isMacOSX())
        {
        	ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
        	nonMacPadding.setText(" ");
        	nonMacPadding.setEnabled(false);
        }
        
        ToolItem sep4 = new ToolItem(toolBar, SWT.SEPARATOR);
        sep4.setWidth(SWT.SEPARATOR_FILL);
        
        if (! UIUtils.isMacOSX())
        {
        	ToolItem nonMacPadding = new ToolItem(toolBar, SWT.NONE);
        	nonMacPadding.setText(" ");
        	nonMacPadding.setEnabled(false);
        }
        
        if (UIUtils.isMacOSX())
            wrapperPopupMenuButton = new ListToolItem(toolBar, "(no wrapper chosen)", -3);
        else
        {
            wrapperPopupMenuButton = new ToolItem(toolBar, SWT.DROP_DOWN);
            wrapperPopupMenuButton.setText("(no wrapper chosen)");
        }
        wrapperPopupMenuButton.setToolTipText("Switch the wrapper used to run the application");
        wrapperPopupMenuListener = new DropdownSelectionListener(wrapperPopupMenuButton);
        wrapperPopupMenuButton.addSelectionListener(wrapperPopupMenuListener);
        
        ToolItem sep5 = new ToolItem(toolBar, SWT.NONE);
        sep5.setText(" ");
        sep5.setEnabled(false);
        
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
               
        toolBar.pack();
        toolBar.layout();
        toolBar.redraw();

        shell.pack();
        shell.open();
        shell.layout();

        loadModel();

        progressBar.setMaxSteps(tree.getItemCount()); // Initial default.
        TreeItem toSelect = treeItemForCase(lastCaseWithCachedValue());
        if (toSelect != null)
        {
            updatePlotsForSelection(toSelect);
            recenterTree(toSelect);
            tree.deselectAll();
            tree.select(toSelect);
            if (tree.indexOf(toSelect) > 0)
                initialCaseStartIndex = tree.indexOf(toSelect);
        }

        try
        {
            while (! shell.isDisposed())
            {
                if (! getDisplay().readAndDispatch())
                {
                    getDisplay().sleep();
                }
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
        }
        else
        {
            buttonRun.setImage(UIUtils.getImageResource("play_shadowed.png"));
            buttonRun.setHotImage(UIUtils.getImageResource("play_shadowed_highlighted.png"));
            buttonRun.setToolTipText("Run all tests");
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
                           + "This might take some time ...\n\n");

            dialog.openWithoutWait();
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
                Util.sleep(1000);
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


    protected void refreshList(boolean confirm)
    {
        wrapperPopupMenuListener.clear();

        if (model.getWrappers().size() == 0)
        {
            // no wrappers ... let the users enter some
            editPreferences();
            return;
        }
        else
        {
            for (WrapperConfig config : model.getWrappers())
                wrapperPopupMenuListener.add(config.getName());
            String last = model.getSettings().getLastWrapperNameOrDefault();
            wrapperPopupMenuListener.select(last, confirm);
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

        QueuedTestRunner(TestCase theCase, TreeItem theItem, String thePath,
                         WrapperConfig theWrapper, Display display)
        {
            this.testCase = theCase;
            this.currentItem = theItem;
            this.path = thePath;
            this.wrapper = theWrapper;
            this.display = display;
            this.caseId = theCase.getId();
            this.runOutcome = null;
        }


        @Override
        public void run()
        {
            // The user may have interrupted the runner while this process
            // was queued up for execution. Start by checking for that.

            if (!running) return;

            // This next call does synchronous execution of the wrapper.

            runOutcome = wrapper.run(testCase, path, 250,
                new CancelCallback() {
                    public boolean cancellationRequested()
                    {
                        return !running;
                    }
            });

            // Check if we were interrupted while running the wrapper.  If we
            // were, we don't want to mark the result as completed.

            if (!running) return;

            doneSet.add(caseId);
            final ResultType resultType = wrapper.getResultType(testCase);
            display.asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    updateItemStatus(currentItem, resultType);
                    progressBar.updateProgressStep();

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
        final WrapperConfig lastWrapper = model.getLastWrapper();
        if (lastWrapper == null || !lastWrapper.canRun())
            return;

        if (selection == null || selection.length == 0)
        {
            Tell.inform(shell, "There is nothing to run!");
            return;
        }

        markAsRunning(true);
        String absolutePath = model.getSuite().getCasesDirectory()
                                   .getAbsolutePath();

        int selectionIndex = 0;
        if (initialCaseStartIndex > 0)
        {
             selectionIndex = initialCaseStartIndex;
             initialCaseStartIndex = 0;
        }
        if (restart)                    // Fresh run.
        {
            doneSet.clear();
            restart = false;
            progressBar.resetSteps();
            if (selection.length > 0)
                progressBar.setMaxSteps(selection.length - selectionIndex);
        }
        else                            // Continuing an interrupted run.
        {
            // Find, in the selection[] list, the last case that has a result.
            // The next one after that in selection[] is the next case to do.
            // If we don't find it, we start from 0 in selection[].
            int index = lastCaseDoneInSelection(selection);
            if (index >= 0)
                selectionIndex = ++index;
            if (doneSet.isEmpty() && selection.length > 0)
                progressBar.setMaxSteps(selection.length - selectionIndex);
        }

        // Explicitly clear results for whatever we're about to recalculate
        int rememberSelectionIndex = selectionIndex;
        while (selectionIndex < selection.length)
            deleteSelectedResult(selection[selectionIndex++]);
        selectionIndex = rememberSelectionIndex;

        if (selectionIndex < selection.length)
            recenterTree(selection[selectionIndex]);

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
            runOutcome = lastWrapper.run(testCase, absolutePath);
            updateItemStatus(item, lastWrapper.getResultType(testCase));
            progressBar.updateProgressStep();
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
            if (item != null && doneSet != null
                && doneSet.contains(item.getText()))
                doneSet.remove(item.getText());
            lblStatusMessage.updateStatus("Stopped.");
            return;
        }

        // If we get here, we can hopefully continue with the remaining cases.

        executor.init(lastWrapper.isConcurrencyAllowed());
        while (selectionIndex < selection.length)
        {
            if (! running) break;

            TreeItem item = selection[selectionIndex];
            TestCase testCase = model.getSuite().get(item.getText());
            executor.execute(new QueuedTestRunner(testCase, item, absolutePath,
                                                  lastWrapper, display));
            selectionIndex++;
        }
        executor.waitForProcesses(getDisplay());

        // At this point, either all tests have concluded or the user
        // interrupted execution.

        int lastIndex = lastCaseDoneInSelection(selection);
        if (lastIndex < 0)
            lastIndex = selection.length - 1;

        final TreeItem lastItem = selection[lastIndex];
        
        tree.setSelection(lastItem);
        tree.showSelection();
        
        if (! running)                  // interrupted
        {
            recenterTree(lastItem);

            lastItem.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run()
                {
                    updatePlotsForSelection(lastItem);
                }
            });

            TestCase lastCase = model.getSuite().get(lastItem.getText());
            lblStatusMessage.updateStatus(lastCase.getId());
        }
        else
        {
            // lblStatusMessage.updateStatus("Done.");
            markAsRunning(false);
            doneSet.clear();
        }
    }


    protected void runAllNewTests()
    {
        runAllNewTests(tree.getItems());
    }


    protected void runAllNewTests(TreeItem[] selection)
    {
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : selection)
        {
            // if (item.getImage().equals(grey))
            if (item.getData() == ResultType.Unknown)
                items.add(item);
        }
        reRunTests(items.toArray(new TreeItem[0]));
    }


    protected void runAllNewTestsInSelection()
    {
        runAllNewTests(tree.getSelection());
    }


    protected void runAllSupported()
    {
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : tree.getItems())
        {
            // if (!item.getImage().equals(yellow))
            if (item.getData() != ResultType.CannotSolve)
                items.add(item);
        }
        reRunTests(items.toArray(new TreeItem[0]));
    }


    protected void runAllTests()
    {
        reRunTests(tree.getItems());
    }


    protected void runByTag()
    {
        DialogFilterTags dialog = new DialogFilterTags(shell, SWT.None);
        dialog.setDescription("Please select component and test tags to include.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shell.getBounds());
        final Vector<String> selection = dialog.open();
        if (selection != null)
        {
            reRunFiltered(new FilterFunction() {

                @Override
                public boolean filter(TestCase test, ResultType result)
                {
                    return test.matches(selection);
                }
            });
        }
    }


    protected void runSelected()
    {
        reRunTests(tree.getSelection());
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
        dlgMap = new ResultMap(shell, SWT.None, model.getSuite(),
                               model.getLastWrapper());
        dlgMap.center(shell.getBounds());
        dlgMap.setData(model.getLastWrapper().getCache());
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
                updatePlotsForSelection(item);

            }
        });
        dlgMap.open();
        dlgMap = null;
    }


    private void updatePlotsForSelection(TreeItem treeItem)
    {
        if (treeItem == null) return;
        //        if (current != null && current.equals(treeItem.getText())) return;

        // clear existing graphs
        for (int i = cmpGraphs.getChildren().length - 1; i >= 0; i--)
        {
            cmpGraphs.getChildren()[i].dispose();
        }

        for (int i = cmpDifferences.getChildren().length - 1; i >= 0; i--)
        {
            cmpDifferences.getChildren()[i].dispose();
        }

        // generate new graphs

        //        current = treeItem.getText();
        String      itemName = treeItem.getText();
        TestCase currentItem = model.getSuite().get(itemName);
        
        if ("FluxBalanceSteadyState".equals(currentItem.getTestType()))
            return;                     // FIXME

        ResultType resultType = model.getLastWrapper().getResultType(currentItem);
        treeItem.setImage(ResultColor.getImageForResultType(resultType));
        treeItem.setData(resultType);
        // .getCachedResult(current));

        ResultSet expected = model.getSuite().get(itemName).getExpectedResult();
        ResultSet actual   = model.getLastWrapper().getResultSet(currentItem);
        ResultSet diff     = ResultSet.diff(expected, actual);

        gl_gridGraphs.numColumns = 2;
        gl_gridDifferences.numColumns = 1;

        if (expected != null)
            addChartForData(cmpGraphs, expected,
                            "Expected results for #" + itemName);
        if (actual != null)
            addChartForData(cmpGraphs, actual,
                            "Simulator results for #" + itemName);
        if (diff != null)
            addChartForData(cmpDifferences, diff, "Difference plot");

        lblStatusMessage.updateStatus(currentItem);

        cmpGraphs.layout();
        cmpDifferences.layout();
    }


    protected void updatePlotsForSelection(TreeItem[] selection)
    {
        if (selection == null || selection.length == 0) return;
        updatePlotsForSelection(selection[0]);
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
        if (doneSet.isEmpty())
            return null;
        else
            return doneSet.last();
    }


    private int lastCaseDoneInSelection(TreeItem[] selection)
    {
        if (selection == null)
            return -1;
        else if (doneSet.isEmpty())
            return -1;
        else 
        {
            String lastCaseWithResult = doneSet.last();
            for (int i = 0; i < selection.length; i++)
            {
                if (selection[i] == null)
                    continue;
                if (lastCaseWithResult.equals(selection[i].getText()))
                    return i;
            }
            return -1;
        }
    }


    private void informUserBadWrapper(WrapperConfig wrapper)
    {
        if (wrapper == null) 
            Tell.inform(shell, "Empty wrapper selection");

        if (! wrapper.canRun())
        {
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
            if (item.getData() != ResultType.Unknown)
                return true;
        return false;
    }


    public void updateItemStatus(final TreeItem currentItem,
                                 final ResultType resultType)
    {
        if (currentItem == null) return;
        if (currentItem.isDisposed()) return;

        currentItem.setImage(ResultColor.getImageForResultType(resultType));
        currentItem.setData(resultType);
        currentItem.getParent().update();

        if (dlgMap != null)
        {
            dlgMap.updateElement(currentItem.getText(), resultType);
        }
    }
}
