//
// @file   ResultMap.java
// @brief  Dialog providing an overview of the test results
// @author Frank T. Bergmann
// @author Michael Hucka
// @@date  Created 2012-06-06 <fbergman@caltech.edu>
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.SortedMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.ResultType;
import org.sbml.testsuite.core.TestCase;
import org.sbml.testsuite.core.TestSuite;
import org.sbml.testsuite.core.Util;
import org.sbml.testsuite.core.WrapperConfig;


/**
 * ResultMap is a dialog providing overview information about the test results.
 */
public class ResultMap
{
    private Shell                    shell;
    private Canvas                   canvas;
    private SortedMap<String, Color> data;
    private String[]                 caseNames;
    private int                      itemsPerLine;
    private TestSuite                suite;
    private WrapperConfig            wrapper;
    private String                   lastName;
    private String                   lastItemclicked;

    private ActionListener           singleClickAction;
    private ActionListener           reRunAction;
    private ActionListener           refreshFileAction;
    private ActionListener           viewOutputAction;

    private MenuItem                 menuItemRerunTest;
    private MenuItem                 menuItemRefreshResults;
    private MenuItem                 menuItemOpenResultsFile;
    private MenuItem                 menuItemDeleteResults;
    private MenuItem                 menuItemViewOutput;

    private final static int         SQUARE_WIDTH = 11;
    private final static int         SQUARE_GAP = 2;
    private final static int         EDGE_PADDING = 20;
    private final static int         DIALOG_STYLE
        = SWT.DIALOG_TRIM | SWT.TOOL | SWT.RESIZE | SWT.MODELESS;


    /**
     * Constructor that uses the test suite and selected wrapper
     * 
     * @param parent
     * @param suite
     *            test suite
     * @param wrapper
     *            selected wrapper configuration
     */
    public ResultMap(TestSuite suite, WrapperConfig wrapper)
    {
        this.wrapper = wrapper;
        this.suite = suite;
        createContents();
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shell == null || shell.isDisposed()) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        final String defaultHelpMsg = "Hover the pointer over a square for "
            + "more information, left-click to jump to that result in the "
            + "main window, and right-click for more options.";

        shell = new Shell(DIALOG_STYLE);
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setLayout(new FormLayout());

        Listener hideListener = new Listener() {
            public void handleEvent(Event event)
            {
                close();
                event.doit = false;
            }
        };

        shell.addListener(SWT.Traverse, hideListener);
        shell.addListener(SWT.Close, hideListener);
        shell.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) { return; }
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (UIUtils.isModifierKey(e) && e.keyCode == 'w')
                    close();
            }
        });
        shell.addListener(SWT.Dispose, new Listener() {
                @Override
                public void handleEvent(Event event)
                {
                    close();
                }
            });

        canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent arg0)
            {
                if (arg0.button == 1 && singleClickAction != null)
                {
                    ActionEvent a = new ActionEvent(ResultMap.this, 0, lastName);
                    singleClickAction.actionPerformed(a);
                }
            }
        });

        /*
         * canvas.addMouseTrackListener(new MouseTrackAdapter() {
         * @Override
         * public void mouseHover(MouseEvent arg0) {
         * String name = getIdFromPoint(arg0);
         * if (name.length() > 0)
         * {
         * System.out.println(" result: " + data.get(name).getResult());
         * canvas.setToolTipText(name);
         * }
         * }
         * });
         */
        canvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent arg0)
            {
                paintCanvas(arg0.gc);
            }
        });

        FormData fd_canvas = new FormData();
        fd_canvas.top = new FormAttachment(0, 10);
        fd_canvas.left = new FormAttachment(0, 10);
        fd_canvas.right = new FormAttachment(100, -10);
        canvas.setLayoutData(fd_canvas);

        int offset = 20 - UIUtils.scaledFontSize(20);

        final Group messageGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
        FormData fd_group = new FormData();
        fd_group.left = new FormAttachment(0, 10);
        fd_group.right = new FormAttachment(100, -10);
        fd_group.bottom = new FormAttachment(100, -45 + offset);
        fd_group.top = new FormAttachment(100, -93);
        messageGroup.setLayoutData(fd_group);

        fd_canvas.bottom = new FormAttachment(messageGroup, -5);

        final StyledText message = new StyledText(shell, SWT.WRAP);
        FormData fd_message = new FormData();
        fd_message.left = new FormAttachment(0, EDGE_PADDING);
        fd_message.right = new FormAttachment(100, -EDGE_PADDING);
        fd_message.bottom = new FormAttachment(100, -50);
        fd_message.top = new FormAttachment(100, -85 + offset);
        message.setLayoutData(fd_message);
        if (!UIUtils.isMacOSX()) message.moveAbove(messageGroup);

        final Font defaultFont = UIUtils.createResizedFont("SansSerif", SWT.NORMAL, 0);
        final Font italicFont = UIUtils.createResizedFont("SansSerif", SWT.ITALIC, 0);

        message.setFont(italicFont);
        message.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
        message.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        message.setText(defaultHelpMsg);
        if (!UIUtils.isMacOSX()) message.moveAbove(messageGroup);

        final Color black = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);

        final String nameLabel = "Case: ";
        final String resultPrefix = "Result: \u2588 ";

        final StyleRange nameLabelStyle = new StyleRange();
        nameLabelStyle.start = 0;
        nameLabelStyle.length = 4;
        nameLabelStyle.fontStyle = SWT.BOLD;
        nameLabelStyle.foreground = black;

        final StyleRange nameStyle = new StyleRange();
        nameStyle.start = 6;
        nameStyle.length = 5;
        nameStyle.foreground = black;

        final StyleRange resultPrefixStyle = new StyleRange();
        resultPrefixStyle.start = 12;
        resultPrefixStyle.length = 6;
        resultPrefixStyle.fontStyle = SWT.BOLD;
        resultPrefixStyle.foreground = black;

        final StyleRange colorSquareStyle = new StyleRange();
        colorSquareStyle.start = 20;
        colorSquareStyle.length = 1;

        canvas.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent arg0)
            {
            	String name = getIdFromPoint(arg0);


                if (name != null)
                {
                    if (suite != null)
                        canvas.setToolTipText(suite.get(name).getToolTip());
                    else
                        canvas.setToolTipText(name);

                    Color color = data.get(name);
                    ResultType result = ResultColor.getResultTypeForColor(color);
                    String text = nameLabel + name + "\n"
                         + resultPrefix + result.getDescription();

                    colorSquareStyle.foreground = color;

                    message.setText(text);
                    message.setFont(defaultFont);
                    message.setStyleRange(nameLabelStyle);
                    message.setStyleRange(nameStyle);
                    message.setStyleRange(resultPrefixStyle);
                    message.setStyleRange(colorSquareStyle);
                }
                else
                {
                    message.setText(defaultHelpMsg);
                    message.setFont(italicFont);
                }
                if (!UIUtils.isMacOSX()) message.moveAbove(messageGroup);
            }
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent arg0) {

            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // remember the last item we actually clicked on 
                lastItemclicked = lastName;

            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {

            }
        });

        Button cmdClose = new Button(shell, SWT.NONE);
        cmdClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                close();
            }
        });
        shell.setDefaultButton(cmdClose);
        cmdClose.setFocus();

        Menu menu = new Menu(canvas);
        canvas.setMenu(menu);

        menuItemViewOutput = new MenuItem(menu, SWT.NONE);
        menuItemViewOutput.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (reRunAction != null)
                {
                    viewOutputAction.actionPerformed(new ActionEvent(ResultMap.this,
                                                                     1, lastItemclicked));
                }
            }
        });
        menuItemViewOutput.setText("View Wrapper Process Output");

        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastItemclicked);
                if (test != null) Util.openFile(test.getSBMLFile());
            }
        });
        menuItem.setText("Open original SBML file");

        menuItemOpenResultsFile = new MenuItem(menu, SWT.NONE);
        menuItemOpenResultsFile.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastItemclicked);
                if (test != null) Util.openFile(wrapper.getResultFile(test));
            }
        });
        menuItemOpenResultsFile.setText("Open simulator results file");

        MenuItem menuItem_2 = new MenuItem(menu, SWT.NONE);
        menuItem_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastItemclicked);
                if (test != null) Util.openFile(test.getExpectedResultFile());
            }
        });
        menuItem_2.setText("Open expected results file");

        MenuItem menuItem_3 = new MenuItem(menu, SWT.NONE);
        menuItem_3.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastItemclicked);
                if (test != null) Util.openFile(test.getDescriptionHTML());
            }
        });
        menuItem_3.setText("Open test description file");

        MenuItem menuItem_4 = new MenuItem(menu, SWT.NONE);
        menuItem_4.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastItemclicked);
                if (test != null) Util.openFile(test.getCaseDirectory());
            }
        });
        menuItem_4.setText("Open test directory");

        new MenuItem(menu, SWT.SEPARATOR);

        menuItemRerunTest = new MenuItem(menu, SWT.NONE);
        menuItemRerunTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (reRunAction != null)
                {
                    reRunAction.actionPerformed(new ActionEvent(ResultMap.this,
                                                                1, lastItemclicked));
                }
            }
        });
        menuItemRerunTest.setText("Rerun test");

        menuItemRefreshResults = new MenuItem(menu, SWT.NONE);
        menuItemRefreshResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (reRunAction != null)
                {
                    refreshFileAction.actionPerformed(new ActionEvent(ResultMap.this,
                                                                      1, lastItemclicked));
                }
            }
        });
        menuItemRefreshResults.setText("Refresh Result(s) from File");

        /*
        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem menuItem_8 = new MenuItem(menu, SWT.NONE);
        menuItem_8.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
            }
        });
        menuItem_8.setText("Save SED-ML");
        menuItem_8.setEnabled(false);
        */

        new MenuItem(menu, SWT.SEPARATOR);

        menuItemDeleteResults = new MenuItem(menu, SWT.NONE);
        menuItemDeleteResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

            }
        });
        menuItemDeleteResults.setText("Delete selected result");

        FormData fd_cmdClose = new FormData();
        fd_cmdClose.width = 75;
        fd_cmdClose.bottom = new FormAttachment(100, -5);
        fd_cmdClose.right = new FormAttachment(100, -8);
        cmdClose.setLayoutData(fd_cmdClose);
        cmdClose.setText("&Close");
        cmdClose.addKeyListener(UIUtils.createCloseKeyListener(shell));
        cmdClose.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));

        setName();
        shell.layout();
    }


    /**
     * @return the canvas
     */
    public Canvas getCanvas()
    {
        return canvas;
    }


    /**
     * Adjust the size of the overall map window to cover the given number
     * of cases.  This tries to make the proportions of the map window
     * square-ish.
     */
    public Rectangle preferredSizeForNumCases()
    {
        if (suite == null || shell == null)
            return null;

        int numCases = suite.getNumCases();
        int numSquaresSide = (int) Math.ceil(Math.sqrt(numCases));
        int pixelsSide = numSquaresSide * (SQUARE_WIDTH + SQUARE_GAP);
        int width = pixelsSide + (2 * EDGE_PADDING);
        int height = pixelsSide + 110; // Approximate, for text areas.
        int x = shell.getLocation().x + 10;
        int y = shell.getLocation().y + 10;
        return new Rectangle(x, y, width, height);
    }


    /**
     * Returns the test id under the given coordinates
     * 
     * @param xpos
     *            x position
     * @param ypos
     *            y position
     * @return the test id under the given coordinates
     */
    public String getIdFromPoint(int xpos, int ypos)
    {
        String name = null;
        int x = xpos / (SQUARE_WIDTH + SQUARE_GAP);
        int y = ypos / (SQUARE_WIDTH + SQUARE_GAP);
        int index = y * itemsPerLine + x;
        if (index < caseNames.length && index >= 0) name = caseNames[index];
        if (name != null) lastName = name;
        return name;
    }


    /**
     * Returns the test id under the mouse pointer
     * 
     * @param arg0
     *            mouse event
     * @return test id
     */
    public String getIdFromPoint(MouseEvent arg0)
    {
        return getIdFromPoint(arg0.x, arg0.y);
    }


    /**
     * @return action listener for re-run
     */
    public ActionListener getReRunAction()
    {
        return reRunAction;
    }


    /**
     * @return action listener for a single click
     */
    public ActionListener getSingleClickAction()
    {
        return singleClickAction;
    }


    /**
     * @return action listener for a single click
     */
    public ActionListener getRefreshFileAction()
    {
        return refreshFileAction;
    }


    /**
     * @return action listener for a single click
     */
    public ActionListener getViewOutputAction()
    {
        return viewOutputAction;
    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        if (shell == null || shell.isDisposed()) return null;
        return shell.getSize();
    }


    /**
     * Returns the test under the given coordinates
     * 
     * @param xpos
     *            x position
     * @param ypos
     *            y position
     * @return the test case under the given coordinates
     */
    public TestCase getTestFromPoint(int xpos, int ypos)
    {
        String caseName = getIdFromPoint(xpos, ypos);
        if (suite != null && caseName != null)
            return suite.get(caseName);
        else
            return null;
    }


    /**
     * Returns the test under the mouse pointer
     * 
     * @param arg0
     *            the mouse event
     * @return the test under the pointer
     */
    public TestCase getTestFromPoint(MouseEvent arg0)
    {
        return getTestFromPoint(arg0.x, arg0.y);
    }


    /**
     * Open the dialog.
     */
    public void open()
    {
        if (shell == null) return;
        UIUtils.restoreWindow(shell, this, preferredSizeForNumCases());
        updateContextualMenu();
        shell.open();
    }


    public void close()
    {
        if (shell != null && !shell.isDisposed())
            shell.setVisible(false);
        UIUtils.saveWindow(shell, this);
    }


    public void raise()
    {
        if (shell != null && !shell.isDisposed())
            shell.open();
    }


    protected void paintCanvas(GC gc)
    {
        if (gc == null) return;

        gc.setAntialias(SWT.ON);
        if (caseNames == null || caseNames.length == 0)
        {
            gc.drawString("No Data ...", 10, 10);
            return;
        }
        itemsPerLine = canvas.getBounds().width / (SQUARE_WIDTH + SQUARE_GAP);

        int x = SQUARE_GAP;
        int y = SQUARE_GAP;
        int i = 0;
        for (String caseName : caseNames)
        {
            Color color = data.get(caseName);

            /* For testing colors 
            if (i % 7 == 0)  color=ResultColor.blue.getColor();
            if (i % 2 == 0)  color=ResultColor.green.getColor();
            if (i % 6 == 0)  color=ResultColor.red.getColor();
            if (i % 5 == 0)  color=ResultColor.gray.getColor();
            if (i % 10 == 0) color=ResultColor.yellow.getColor();
            if (i % 13 == 0) color=ResultColor.white.getColor();
            if (i % 17 == 0) color=ResultColor.black.getColor();
            */
            
            gc.setBackground(color);
            gc.fillRoundRectangle(x, y, SQUARE_WIDTH, SQUARE_WIDTH, 5, 5);

            x += SQUARE_WIDTH + SQUARE_GAP;

            if ((i + 1) % itemsPerLine == 0)
            {
                x = SQUARE_GAP;
                y += SQUARE_WIDTH + SQUARE_GAP;
            }
            i++;
        }

    }


    /**
     * Sets the canvas
     * 
     * @param canvas
     *            the canvas
     */
    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }


    /**
     * Initializes the canvas from the given results map.
     * 
     * @param cache
     *            map of results.
     */
    public void setData(SortedMap<String, Color> results)
    {
        data = results;
        if (data != null && data.keySet().size() > 0)
            caseNames = data.keySet().toArray(new String[0]);
        else
            caseNames = new String[0];
        Arrays.sort(caseNames);
        canvas.update();
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


    /**
     * Sets the action listener for re-run
     * 
     * @param reRunAction
     *            the re-run action listener
     */
    public void setReRunAction(ActionListener reRunAction)
    {
        this.reRunAction = reRunAction;
    }


    /**
     * Sets the action listener for a single click
     * 
     * @param singleClickAction
     *            the single click action listener
     */
    public void setSingleClickAction(ActionListener singleClickAction)
    {
        this.singleClickAction = singleClickAction;
    }


    public void setRefreshFileAction(ActionListener action)
    {
        this.refreshFileAction = action;
    }


    public void setViewOutputAction(ActionListener action)
    {
        this.viewOutputAction = action;
    }


    public void setName()
    {
        shell.setText("Map of test results for wrapper \""
                      + wrapper.getName() + "\"");
    }


    public void updateWrapper(WrapperConfig wrapper)
    {
        if (wrapper == null) return;
        this.wrapper = wrapper;
        setName();
        updateContextualMenu();
    }


    private void updateContextualMenu()
    {
        if (wrapper == null) return;
        if (wrapper.isViewOnly())
        {
            menuItemRerunTest.setEnabled(false);
            menuItemViewOutput.setEnabled(false);
        }
        else if ("-- no wrapper --".equals(wrapper.getName()))
        {
            menuItemRerunTest.setEnabled(false);
            menuItemRefreshResults.setEnabled(false);
            menuItemOpenResultsFile.setEnabled(false);
            menuItemDeleteResults.setEnabled(false);
            menuItemViewOutput.setEnabled(false);
        }
        else
        {
            menuItemRerunTest.setEnabled(true);
            menuItemRefreshResults.setEnabled(true);
            menuItemOpenResultsFile.setEnabled(true);
            menuItemDeleteResults.setEnabled(true);
            menuItemViewOutput.setEnabled(true);
        }
    }


    /**
     * Updates the element with the given id
     * 
     * @param id
     *            the id
     * @param result
     *            a ResultType
     */
    public void updateCase(String id, Color color)
    {
        if (canvas.isDisposed()) return;
        data.put(id, color);
        canvas.redraw();
    }


    public void updateCase(String id, ResultType result)
    {
        updateCase(id, ResultColor.getColorForResultType(result));
    }
}
