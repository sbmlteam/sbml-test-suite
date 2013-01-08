//
// @file MainWindow.java
// @brief MainWindow of the SBML Test suite runner
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
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
import org.sbml.testsuite.core.FilterFunction;
import org.sbml.testsuite.core.ResultType;
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
            if (event.detail == SWT.ARROW)
            {
                ToolItem item = (ToolItem) event.widget;
                Rectangle rect = item.getBounds();
                Point pt = item.getParent()
                               .toDisplay(new Point(rect.x, rect.y));
                menu.setLocation(pt.x, pt.y + rect.height);
                menu.setVisible(true);
            }
            else
            {
                changeWrapper(dropdown.getText());
            }
        }
    }

    static int imageSize = 12;


    /**
     * Utility function determining the color for the given result type
     * 
     * @param result
     *            the result
     * @return the color for the result
     */
    public static int getColorForResult(ResultType result)
    {
        switch (result)
        {
        case CannotSolve:
            return SWT.COLOR_YELLOW;
        case Match:
            return SWT.COLOR_GREEN;
        case NoMatch:
            return SWT.COLOR_RED;
        case Unknown:
        default:
            return SWT.COLOR_DARK_GRAY;
        }
    }


    /**
     * @return true if running on OS X
     */
    public static boolean isMacOSX()
    {
        String osName = System.getProperty("os.name");
        return osName.startsWith("Mac OS X");
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

    private final Color               black;
    boolean                           closing;
    private Composite                 cmpDifferences;
    private Composite                 cmpGraphs;
    private final int[]               colors = new int[] {SWT.COLOR_BLUE,
        SWT.COLOR_CYAN, SWT.COLOR_DARK_BLUE, SWT.COLOR_DARK_CYAN,
        SWT.COLOR_DARK_GREEN, SWT.COLOR_DARK_MAGENTA, SWT.COLOR_DARK_RED,
        SWT.COLOR_DARK_YELLOW, SWT.COLOR_GRAY, SWT.COLOR_GREEN,
        SWT.COLOR_MAGENTA, SWT.COLOR_RED     };
    private String                    current;
    private final Image               green;
    private final Image               grey;
    private GridLayout                gridDifferences;
    private GridLayout                gridGraphs;

    private Label                     lblNewLabel;
    private MenuItem                  mntmopen;
    private MenuItem                  mntmShowOnlyProblematic;
    private MenuItem                  mntmShowOnlyReally;

    private MenuItem                  mntmShowOnlySupported;

    private MenuItem                  mntmtest;
    private MainModel                 model;
    private final Image               red;

    private final DecimalFormat       sciformat;

    protected Shell                   shlSbmlTestSuite;

    private ToolItem                  tltmWrappers;

    private ToolBar                   toolBar;

    private Tree                      tree;

    private DropdownSelectionListener wrapperListener;

    private final Image               yellow;
    private ResultMap                 dlgMap;
    private FormData                  fd_sashForm;
    private Display                   display;


    /**
     * Default constructor
     */
    public MainWindow()
    {
        sciformat = new DecimalFormat("##0.##E0");
        createContents();
        black = SWTResourceManager.getColor(SWT.COLOR_BLACK);
        grey = getImage(SWT.COLOR_DARK_GRAY);
        green = getImage(SWT.COLOR_GREEN);
        yellow = getImage(SWT.COLOR_YELLOW);
        red = getImage(SWT.COLOR_RED);

    }


    private void addChartForData(Composite composite, ResultSet result,
                                 String title)
    {
        Chart chart1 = new Chart(composite, SWT.NONE);
        chart1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        // chart1.getLegend().setVisible(false);
        if (title == null || title.length() == 0)
        {
            chart1.getTitle().setVisible(false);
        }
        else
        {
            chart1.getTitle().setText(title);
            chart1.getTitle().setForeground(black);
        }
        chart1.getAxisSet().getXAxis(0).getTitle().setVisible(false);
        chart1.getAxisSet().getXAxis(0).getTick().setForeground(black);

        chart1.getAxisSet().getYAxis(0).getTitle().setVisible(false);
        chart1.getAxisSet().getYAxis(0).getTick().setForeground(black);

        chart1.getLegend().setPosition(SWT.BOTTOM);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        chart1.setLayoutData(gridData);

        ISeriesSet seriesSet = chart1.getSeriesSet();
        double[] time = result.getTimeColumn();

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int i = 1; i < result.getNumColumns(); i++)
        {
            double[] ySeries = result.getColumn(i);

            min = Math.min(min, getMin(ySeries));
            max = Math.max(max, getMax(ySeries));

            ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                                                                      result.getHeaders()
                                                                            .get(i));
            series.setLineWidth(2);
            series.setLineColor(SWTResourceManager.getColor(colors[i
                % colors.length]));
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
                setImage(treeItem, result);
                treeItem.setData(result);
                treeItem.setText(test.getId());
            }

        }

        if (tree.getItemCount() > 0)
        {
            tree.select(tree.getItem(0));
            updateSelection(tree.getItem(0));
        }
    }


    private void changeWrapper(String wrapperName)
    {
        WrapperConfig current = model.getSettings().getWrapper(wrapperName);
        if (current == null) return;

        Vector<String> selection = getSelection();
        model.getSettings().setLastWrapper(current);
        current.beginUpdate(model.getSuite());
        addTreeItems();
        setSelection(selection);
    }


    /**
     * Create contents of the window.
     */
    protected void createContents()
    {
        shlSbmlTestSuite = new Shell();
        shlSbmlTestSuite.setImage(SWTResourceManager.getImage(MainWindow.class,
                                                              "/data/sbml_256.png"));
        shlSbmlTestSuite.setMinimumSize(new Point(800, 600));
        shlSbmlTestSuite.setSize(450, 300);
        shlSbmlTestSuite.setText("SBML Test Suite");
        shlSbmlTestSuite.addListener(SWT.Close, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                closing = true;
                fileQuit();
            }
        });
        shlSbmlTestSuite.setLayout(new FormLayout());

        Menu menu = new Menu(shlSbmlTestSuite, SWT.BAR);
        shlSbmlTestSuite.setMenuBar(menu);

        MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
        mntmNewSubmenu.setText("&File");

        Menu menu_1 = new Menu(mntmNewSubmenu);
        mntmNewSubmenu.setMenu(menu_1);

        mntmopen = new MenuItem(menu_1, SWT.NONE);
        mntmopen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                fileOpen();
            }

        });
        mntmopen.setText("&Open");

        new MenuItem(menu_1, SWT.SEPARATOR);

        MenuItem mntmQuit = new MenuItem(menu_1, SWT.NONE);
        mntmQuit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                fileQuit();
            }
        });
        mntmQuit.setText("Quit");

        MenuItem mntmNewSubmenu_1 = new MenuItem(menu, SWT.CASCADE);
        mntmNewSubmenu_1.setText("F&ilter");

        Menu menu_2 = new Menu(mntmNewSubmenu_1);
        mntmNewSubmenu_1.setMenu(menu_2);

        mntmShowOnlyProblematic = new MenuItem(menu_2, SWT.CHECK);
        mntmShowOnlyProblematic.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlyProblematic();
            }
        });
        mntmShowOnlyProblematic.setText("Show only problematic Entries");

        mntmShowOnlyReally = new MenuItem(menu_2, SWT.CHECK);
        mntmShowOnlyReally.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlyReallyProblematic();
            }
        });
        mntmShowOnlyReally.setText("Show only really problematic Entries");

        new MenuItem(menu_2, SWT.SEPARATOR);

        mntmShowOnlySupported = new MenuItem(menu_2, SWT.CHECK);
        mntmShowOnlySupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterShowOnlySupported();
            }
        });
        mntmShowOnlySupported.setText("Show only supported Tests");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem mntmFilterByTags = new MenuItem(menu_2, SWT.NONE);
        mntmFilterByTags.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterByTags();
            }
        });
        mntmFilterByTags.setText("Filter by Tags");

        MenuItem mntmFilterByTest = new MenuItem(menu_2, SWT.NONE);
        mntmFilterByTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterByRange();
            }
        });
        mntmFilterByTest.setText("Filter by Test Range");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem mntmExcludeTestsBy = new MenuItem(menu_2, SWT.NONE);
        mntmExcludeTestsBy.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterExcludeByTag();
            }
        });
        mntmExcludeTestsBy.setText("Exclude Tests By Tag");

        MenuItem mntmExcludeTestsBy_1 = new MenuItem(menu_2, SWT.NONE);
        mntmExcludeTestsBy_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                filterExcludeByRange();
            }
        });
        mntmExcludeTestsBy_1.setText("Exclude Tests By Range");

        mntmtest = new MenuItem(menu, SWT.CASCADE);
        mntmtest.setText("&Test");

        Menu menu_3 = new Menu(mntmtest);
        mntmtest.setMenu(menu_3);

        MenuItem mntmEditTestCase = new MenuItem(menu_3, SWT.NONE);
        mntmEditTestCase.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                editTestCase();
            }
        });
        mntmEditTestCase.setText("Edit Test case");

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem mntmRunSelected = new MenuItem(menu_3, SWT.NONE);
        mntmRunSelected.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runSelected();
            }
        });
        mntmRunSelected.setText("Run Selected");

        MenuItem mntmRunByTag = new MenuItem(menu_3, SWT.NONE);
        mntmRunByTag.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runByTag();
            }
        });
        mntmRunByTag.setText("Run By Tag");

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem mntmRunAllTests = new MenuItem(menu_3, SWT.NONE);
        mntmRunAllTests.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runAllTests();
            }
        });
        mntmRunAllTests.setText("Run All Tests");

        MenuItem mntmRunAllSupported = new MenuItem(menu_3, SWT.NONE);
        mntmRunAllSupported.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runAllSupported();
            }
        });
        mntmRunAllSupported.setText("Run All Supported");

        MenuItem mntmRunAllNew = new MenuItem(menu_3, SWT.NONE);
        mntmRunAllNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                runAllNewTests();
            }
        });
        mntmRunAllNew.setText("Run All New Tests");

        new MenuItem(menu_3, SWT.SEPARATOR);

        MenuItem mntmDeleteSelectedResults = new MenuItem(menu_3, SWT.NONE);
        mntmDeleteSelectedResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                deleteSelectedResults();
            }
        });
        mntmDeleteSelectedResults.setText("Delete Selected Results");

        MenuItem mntmhelp = new MenuItem(menu, SWT.CASCADE);
        mntmhelp.setText("&Help");

        Menu menu_4 = new Menu(mntmhelp);
        mntmhelp.setMenu(menu_4);

        MenuItem mntmAbout = new MenuItem(menu_4, SWT.NONE);
        mntmAbout.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                showAbout();
            }
        });
        mntmAbout.setText("About");

        lblNewLabel = new Label(shlSbmlTestSuite, SWT.NONE);
        FormData fd_lblNewLabel = new FormData();
        fd_lblNewLabel.bottom = new FormAttachment(100, -5);
        fd_lblNewLabel.left = new FormAttachment(0, 5);
        fd_lblNewLabel.right = new FormAttachment(100, -5);
        lblNewLabel.setLayoutData(fd_lblNewLabel);
        lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        lblNewLabel.setText("Status");

        SashForm sashForm = new SashForm(shlSbmlTestSuite, SWT.NONE);
        fd_lblNewLabel.right = new FormAttachment(100);
        fd_sashForm = new FormData();
        fd_sashForm.bottom = new FormAttachment(lblNewLabel, -5);
        fd_sashForm.right = new FormAttachment(100);
        fd_sashForm.left = new FormAttachment(0);
        if (!isMacOSX()) fd_sashForm.top = new FormAttachment(100, 0);
        sashForm.setLayoutData(fd_sashForm);

        tree = new Tree(sashForm, SWT.BORDER | SWT.MULTI);
        // tree.
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

                updateSelection(tree.getSelection());
            }
        });
        tree.setSortDirection(SWT.UP);

        Menu treeContext = new Menu(shlSbmlTestSuite);
        MenuItem mnuOpenOriginalSBML = new MenuItem(treeContext, SWT.PUSH);
        mnuOpenOriginalSBML.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openOriginalSBML(tree.getSelection());
            }
        });
        mnuOpenOriginalSBML.setText("Open Original SBML File");
        MenuItem mnuOpenSimulatorResults = new MenuItem(treeContext, SWT.PUSH);
        mnuOpenSimulatorResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openSimulatorResult(tree.getSelection());
            }

        });
        mnuOpenSimulatorResults.setText("Open Simulator Result File");
        MenuItem mnuOpenExpectedResults = new MenuItem(treeContext, SWT.PUSH);
        mnuOpenExpectedResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openExpectedResult(tree.getSelection());
            }
        });
        mnuOpenExpectedResults.setText("Open Expected Result File");
        MenuItem mnuOpenDescription = new MenuItem(treeContext, SWT.PUSH);
        mnuOpenDescription.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openTestDescription(tree.getSelection());
            }
        });
        mnuOpenDescription.setText("Open Test Description");
        MenuItem mnuOpenTestDir = new MenuItem(treeContext, SWT.PUSH);
        mnuOpenTestDir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openTestDirectory(tree.getSelection());
            }

        });
        mnuOpenTestDir.setText("Open Test Directory");

        new MenuItem(treeContext, SWT.SEPARATOR);

        MenuItem mnuRunTest = new MenuItem(treeContext, SWT.PUSH);
        mnuRunTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                reRunTests(tree.getSelection());
            }
        });
        mnuRunTest.setText("Re-Run Test");

        new MenuItem(treeContext, SWT.SEPARATOR);

        MenuItem mnuSaveSedML = new MenuItem(treeContext, SWT.PUSH);
        mnuSaveSedML.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                saveSedML(tree.getSelection());
            }
        });
        mnuSaveSedML.setText("Save SED-ML");

        new MenuItem(treeContext, SWT.SEPARATOR);

        MenuItem mnuDeleteSelectd = new MenuItem(treeContext, SWT.PUSH);
        mnuDeleteSelectd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                deleteSelectedResults(tree.getSelection());
            }
        });
        mnuDeleteSelectd.setText("Delete Selected Result");

        // TODO: Remove when implemented
        mntmEditTestCase.setEnabled(false);
        mnuSaveSedML.setEnabled(false);

        tree.setMenu(treeContext);

        // Implement a "fake" tooltip
        final Listener labelListener = new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                Label label = (Label) event.widget;
                Shell shell = label.getShell();
                switch (event.type)
                {
                case SWT.MouseDown:
                    Event e = new Event();
                    e.item = (TreeItem) label.getData("_TREEITEM");
                    // Assuming table is single select, set the selection as if
                    // the mouse down event went through to the table
                    tree.setSelection(new TreeItem[] {(TreeItem) e.item});
                    tree.notifyListeners(SWT.Selection, e);
                    shell.dispose();
                    tree.setFocus();
                    break;
                case SWT.MouseExit:
                    shell.dispose();
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
                Display display = shlSbmlTestSuite.getDisplay();
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
                        tip = new Shell(shlSbmlTestSuite, SWT.ON_TOP
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

        Composite composite = new Composite(sashForm, SWT.NONE);
        composite.setLayout(new FormLayout());

        SashForm sashForm_1 = new SashForm(composite, SWT.VERTICAL);
        FormData fd_sashForm_1 = new FormData();
        fd_sashForm_1.bottom = new FormAttachment(100);
        fd_sashForm_1.right = new FormAttachment(100);
        fd_sashForm_1.top = new FormAttachment(0);
        fd_sashForm_1.left = new FormAttachment(0);
        sashForm_1.setLayoutData(fd_sashForm_1);

        cmpGraphs = new Composite(sashForm_1, SWT.NONE);
        cmpGraphs.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gridGraphs = new GridLayout(1, true);
        cmpGraphs.setLayout(gridGraphs);

        cmpDifferences = new Composite(sashForm_1, SWT.NONE);
        cmpDifferences.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gridDifferences = new GridLayout(1, true);
        cmpDifferences.setLayout(gridDifferences);
        sashForm_1.setWeights(new int[] {1, 1});
        sashForm.setWeights(new int[] {104, 343});

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
        item.setImage(grey);
    }


    protected void editPreferences()
    {
        PreferenceDialog dialog = new PreferenceDialog(shlSbmlTestSuite,
                                                       SWT.NONE);

        dialog.setTestSuiteSettings(model.getSettings());
        dialog.center(shlSbmlTestSuite.getBounds());

        TestSuiteSettings result = dialog.open();
        if (result != null)
        {
            String lastWrapper = model.getSettings().getLastWrapperName();
            model.setSettings(result);
            if (lastWrapper != null && lastWrapper.length() > 0)
                model.getSettings().setLastWrapper(lastWrapper);
            result.saveAsDefault();
            refreshList();
        }

    }


    protected void editTestCase()
    {
        // TODO Auto-generated method stub
        // System.out.println("edit test");
    }


    private void fileOpen()
    {
        FileDialog dlg = new FileDialog(shlSbmlTestSuite, SWT.OPEN);
        dlg.setFilterNames(new String[] {"SBML Testsuite archive files",
            "All files"});
        dlg.setFilterExtensions(new String[] {"*.zip", "*.*"});
        dlg.setText("Browse for test suite archive");
        String fileName = dlg.open();
        if (fileName != null)
        {
            openArchive(new File(fileName));
        }
    }


    private void fileQuit()
    {
        if (!closing) this.shlSbmlTestSuite.close();

        System.exit(0);

    }


    protected void filterByRange()
    {
        DialogFilterRange dialog = new DialogFilterRange(shlSbmlTestSuite,
                                                         SWT.None);
        dialog.setDescription("Please select the range of tests to include.");
        dialog.center(shlSbmlTestSuite.getBounds());
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
        DialogFilterTags dialog = new DialogFilterTags(shlSbmlTestSuite,
                                                       SWT.None);
        dialog.setDescription("Please select component and test tags to include.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shlSbmlTestSuite.getBounds());
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
        DialogFilterRange dialog = new DialogFilterRange(shlSbmlTestSuite,
                                                         SWT.None);
        dialog.setDescription("Please select the range of tests to exclude.");
        dialog.center(shlSbmlTestSuite.getBounds());
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
        DialogFilterTags dialog = new DialogFilterTags(shlSbmlTestSuite,
                                                       SWT.None);
        dialog.setDescription("Please select component and test tags to exclude.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shlSbmlTestSuite.getBounds());
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
        if (mntmShowOnlyProblematic.getSelection())
        {
            mntmShowOnlyReally.setSelection(false);
            mntmShowOnlySupported.setSelection(false);

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
        if (mntmShowOnlyReally.getSelection())
        {

            mntmShowOnlyProblematic.setSelection(false);
            mntmShowOnlySupported.setSelection(false);

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
        if (mntmShowOnlySupported.getSelection())
        {
            mntmShowOnlyProblematic.setSelection(false);
            mntmShowOnlyReally.setSelection(false);

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


    private Image getImage(int color)
    {
        Display display = shlSbmlTestSuite.getDisplay();
        Image image = new Image(display, imageSize, imageSize);
        GC gc = new GC(image);

        gc.setBackground(display.getSystemColor(color));
        gc.setForeground(display.getSystemColor(color));
        gc.fillOval(1, 1, imageSize - 2, imageSize - 2);

        gc.dispose();
        return image;
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


    private Vector<String> getSelection()
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
                Util.copyInputStream(getClass().getResourceAsStream("/data/sbml-test-cases.zip"),
                                     new BufferedOutputStream(
                                                              new FileOutputStream(
                                                                                   destFile)));
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

            return;
        }
        refreshList();

    }


    private void macify(Display display)
    {
        if (!isMacOSX()) return;

        CocoaUIEnhancer enhancer = new CocoaUIEnhancer("SBML Test Suite");
        enhancer.hookApplicationMenu(display, new Listener() {
            @Override
            public void handleEvent(Event arg0)
            {
                fileQuit();

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

        toolBar = shlSbmlTestSuite.getToolBar();

    }


    /**
     * Open the window.
     */
    public void open()
    {
        display = Display.getDefault();

        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shlSbmlTestSuite.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        shlSbmlTestSuite.setLocation(x, y);

        toolBar = new ToolBar(shlSbmlTestSuite, SWT.FLAT | SWT.RIGHT);
        fd_sashForm.top = new FormAttachment(toolBar);
        FormData fd_toolBar = new FormData();
        fd_toolBar.right = new FormAttachment(100);
        fd_toolBar.top = new FormAttachment(0);
        fd_toolBar.left = new FormAttachment(0);
        toolBar.setLayoutData(fd_toolBar);

        if (isMacOSX()) macify(display);

        ToolItem tltmShowMap = new ToolItem(toolBar, SWT.NONE);
        tltmShowMap.setImage(SWTResourceManager.getImage(MainWindow.class,
                                                         "/data/map_22x22.png"));
        tltmShowMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                showMap();
            }
        });
        tltmShowMap.setText("Show Map");

        tltmWrappers = new ToolItem(toolBar, SWT.DROP_DOWN);
        tltmWrappers.setText("Switch Wrapper");
        wrapperListener = new DropdownSelectionListener(tltmWrappers);
        tltmWrappers.addSelectionListener(wrapperListener);

        ToolItem tltmPreferences = new ToolItem(toolBar, SWT.NONE);
        tltmPreferences.setImage(SWTResourceManager.getImage(MainWindow.class,
                                                             "/data/applications-system.png"));
        tltmPreferences.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                editPreferences();
            }
        });
        tltmPreferences.setText("Preferences");

        ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
        tltmRefresh.setImage(SWTResourceManager.getImage(MainWindow.class,
                                                         "/data/view-refresh.png"));
        tltmRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                refreshList();
            }
        });

        shlSbmlTestSuite.open();
        shlSbmlTestSuite.layout();

        toolBar.pack();
        toolBar.layout();
        toolBar.redraw();

        loadModel();

        while (!shlSbmlTestSuite.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }


    private void openArchive(File file)
    {
        shlSbmlTestSuite.setEnabled(false);
        try
        {

            ProgressDialog dialog = new ProgressDialog(shlSbmlTestSuite,
                                                       SWT.None);
            dialog.center(shlSbmlTestSuite.getBounds());
            dialog.getStyledText()
                  .setText("The new testsuite archive will now be extracted and loaded. This might take a while. \n\n\nExtracting Testsuite archive ...\n\n");

            dialog.openWithoutWait();
            Util.unzipArchive(file, new CancelCallback() {

                @Override
                public boolean cancellationRequested()
                {
                    display.readAndDispatch();
                    return false;
                }
            });
            dialog.getStyledText().append("Reading Testsuite ...\n\n");
            display.readAndDispatch();
            File casesDir = new File(Util.getInternalTestSuiteDir(),
                                     "/cases/semantic/");
            if (!casesDir.isDirectory() && casesDir.exists())
            {
                dialog.getStyledText()
                      .append("Error: Archive was no testsuite archive ...\n\n");
                display.readAndDispatch();
                Util.sleep(1000);
            }
            else
            {
                model.setTestSuiteDir(casesDir);
                display.readAndDispatch();
                dialog.getStyledText().append("Update UI ...\n\n");
                display.readAndDispatch();
                refreshList();
            }
            dialog.close();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
            shlSbmlTestSuite.setEnabled(true);
        }

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


    protected void refreshList()
    {

        wrapperListener.clear();

        if (model.getWrappers().size() == 0)
        {
            // no wrappers ... let the users enter some
            editPreferences();
            return;
        }

        for (WrapperConfig config : model.getWrappers())
            wrapperListener.add(config.getName());
        wrapperListener.select(model.getSettings()
                                    .getLastWrapperNameOrDefault());

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


    protected void reRunTests(TreeItem[] selection)
    {
        final WrapperConfig lastWrapper = model.getLastWrapper();
        if (lastWrapper == null || !lastWrapper.canRun()) return;
        deleteSelectedResults(selection);
        String absolutePath = model.getSuite().getCasesDirectory()
                                   .getAbsolutePath();
        for (final TreeItem item : selection)
        {

            final TestCase test = model.getSuite().get(item.getText());
            lblNewLabel.setText("Running test: " + test.getId());
            lastWrapper.run(test, absolutePath, 500, new CancelCallback() {

                @Override
                public boolean cancellationRequested()
                {
                    Display current2 = Display.getCurrent();
                    current2.readAndDispatch();
                    return false;
                }
            });

            item.getDisplay().asyncExec(new Runnable() {

                @Override
                public void run()
                {
                    ResultType resultType = lastWrapper.getResultType(test);
                    setImage(item, resultType);
                    if (dlgMap != null)
                    {
                        dlgMap.updateElement(item.getText(), resultType);
                    }
                    tree.update();
                }
            });

            shlSbmlTestSuite.update();
            Display current3 = Display.getCurrent();
            current3.readAndDispatch();
        }

        Display current2 = Display.getCurrent();
        current2.update();

        final TreeItem last = selection[selection.length - 1];
        last.getDisplay().asyncExec(new Runnable() {

            @Override
            public void run()
            {
                updateSelection(last);
            }
        });

    }


    protected void runAllNewTests()
    {
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : tree.getItems())
        {
            if (item.getImage().equals(grey)) items.add(item);
        }
        reRunTests(items.toArray(new TreeItem[0]));
    }


    protected void runAllSupported()
    {
        Vector<TreeItem> items = new Vector<TreeItem>();
        for (TreeItem item : tree.getItems())
        {
            if (!item.getImage().equals(yellow)) items.add(item);
        }
        reRunTests(items.toArray(new TreeItem[0]));

    }


    protected void runAllTests()
    {
        reRunTests(tree.getItems());
    }


    protected void runByTag()
    {
        DialogFilterTags dialog = new DialogFilterTags(shlSbmlTestSuite,
                                                       SWT.None);
        dialog.setDescription("Please select component and test tags to include.");
        dialog.setComponentTags(model.getSuite().getComponentTags());
        dialog.setTestTags(model.getSuite().getTestTags());
        dialog.center(shlSbmlTestSuite.getBounds());
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
        System.err.println("Exporting SedML not yet supported");

    }


    private void setImage(TreeItem item, ResultType result)
    {
        if (item == null || result == null) return;
        switch (result)
        {
        case CannotSolve:
            item.setImage(yellow);
            break;
        case Match:
            item.setImage(green);
            break;
        case NoMatch:
            item.setImage(red);
            break;
        case Unknown:
        default:
            item.setImage(grey);
            break;
        }
    }


    private void setSelection(Vector<String> selection)
    {
        for (String text : selection)
        {
            TreeItem item = getItem(text);
            if (item != null) tree.select(item);
        }

    }


    protected void showAbout()
    {
        AboutDialog dialog = new AboutDialog(shlSbmlTestSuite, SWT.None);
        dialog.center(shlSbmlTestSuite.getBounds());
        dialog.open();
    }


    protected void showMap()
    {
        dlgMap = new ResultMap(shlSbmlTestSuite, SWT.None, model.getSuite(),
                               model.getLastWrapper());
        dlgMap.center(shlSbmlTestSuite.getBounds());
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
                updateSelection(item);

            }
        });
        dlgMap.open();
        dlgMap = null;
    }


    private void updateSelection(TreeItem treeItem)
    {
        if (treeItem == null) return;
        if (current != null && current.equals(treeItem.getText())) return;

        current = treeItem.getText();

        TestCase currentItem = model.getSuite().get(treeItem.getText());

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
        setImage(treeItem, model.getLastWrapper().getResultType(currentItem));
        // .getCachedResult(current));

        ResultSet expected = model.getSuite().get(current).getExpectedResult();
        ResultSet data = model.getLastWrapper().getResultSet(currentItem);

        ResultSet diff = ResultSet.diff(expected, data);

        gridGraphs.numColumns = 2;
        gridDifferences.numColumns = 1;

        if (expected != null)
            addChartForData(cmpGraphs, expected, "Expected Result");
        if (data != null) addChartForData(cmpGraphs, data, "Simulator Result");
        if (diff != null)
            addChartForData(cmpDifferences, diff, "Difference Plot");

        lblNewLabel.setText(currentItem.getStatusText());

        cmpGraphs.layout();
        cmpDifferences.layout();

    }


    protected void updateSelection(TreeItem[] selection)
    {
        if (selection == null || selection.length == 0) return;
        updateSelection(selection[0]);

    }
}
