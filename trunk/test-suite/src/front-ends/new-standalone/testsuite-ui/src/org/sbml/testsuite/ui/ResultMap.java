//
// @file ResultMap.java
// @brief ResultMap is a dialog providing overview information about the test
// results
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
import java.util.Arrays;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.DelayedResult;
import org.sbml.testsuite.core.ResultType;
import org.sbml.testsuite.core.TestCase;
import org.sbml.testsuite.core.TestSuite;
import org.sbml.testsuite.core.Util;
import org.sbml.testsuite.core.WrapperConfig;

/**
 * ResultMap is a dialog providing overview information about the test results
 */
public class ResultMap
    extends Dialog
{

    protected Object                       result;
    protected Shell                        shell;
    private Canvas                         canvas;
    private HashMap<String, DelayedResult> data;
    private String[]                       keySets;
    int                                    length = 11;
    int                                    gap    = 2;
    private int                            itemsPerLine;
    TestSuite                              suite;
    WrapperConfig                          wrapper;
    private String                         lastName;

    private ActionListener                 singleClickAction;
    private ActionListener                 reRunAction;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ResultMap(Shell parent, int style)
    {
        super(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
        setText("Result Map");
        createContents();
    }


    /**
     * Craetes a new dialog with test suite and selected wrapper
     * 
     * @param parent
     * @param style
     * @param suite
     *            test suite
     * @param wrapper
     *            selected wrapper configuration
     */
    public ResultMap(Shell parent, int style, TestSuite suite,
                     WrapperConfig wrapper)
    {
        this(parent, style);
        this.suite = suite;
        this.wrapper = wrapper;
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
        shell = new Shell(getParent(), getStyle());
        shell.setImage(SWTResourceManager.getImage(ResultMap.class,
                                                   "/data/sbml_256.png"));
        shell.setSize(600, 450);
        shell.setText(getText());
        shell.setLayout(new FormLayout());

        canvas = new Canvas(shell, SWT.NONE);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent arg0)
            {
                if (arg0.button == 1 && singleClickAction != null)
                {
                    singleClickAction.actionPerformed(new ActionEvent(
                                                                      ResultMap.this,
                                                                      0,
                                                                      lastName));
                }
            }
        });

        canvas.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent arg0)
            {
                String name = getIdFromPoint(arg0);
                if (name.length() > 0)
                {
                    if (suite != null)
                        canvas.setToolTipText(suite.get(name).getToolTip());
                    else
                        canvas.setToolTipText(name);
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

        Button cmdClose = new Button(shell, SWT.NONE);
        cmdClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                shell.close();
            }
        });
        fd_canvas.bottom = new FormAttachment(100, -41);

        Menu menu = new Menu(canvas);
        canvas.setMenu(menu);

        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastName);
                if (test != null) Util.openFile(test.getSBMLFile());
            }
        });
        menuItem.setText("Open Original SBML File");

        MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
        menuItem_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

                TestCase test = suite.get(lastName);
                if (test != null) Util.openFile(wrapper.getResultFile(test));
            }
        });
        menuItem_1.setText("Open Simulator Result File");

        MenuItem menuItem_2 = new MenuItem(menu, SWT.NONE);
        menuItem_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastName);
                if (test != null) Util.openFile(test.getExpectedResultFile());
            }
        });
        menuItem_2.setText("Open Expected Result File");

        MenuItem menuItem_3 = new MenuItem(menu, SWT.NONE);
        menuItem_3.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastName);
                if (test != null) Util.openFile(test.getDescriptionHTML());
            }
        });
        menuItem_3.setText("Open Test Description");

        MenuItem menuItem_4 = new MenuItem(menu, SWT.NONE);
        menuItem_4.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                TestCase test = suite.get(lastName);
                if (test != null) Util.openFile(test.getCaseDirectory());
            }
        });
        menuItem_4.setText("Open Test Directory");

        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem menuItem_6 = new MenuItem(menu, SWT.NONE);
        menuItem_6.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (reRunAction != null)
                {
                    reRunAction.actionPerformed(new ActionEvent(ResultMap.this,
                                                                1, lastName));
                }
            }
        });
        menuItem_6.setText("Re-Run Test");

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

        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem menuItem_10 = new MenuItem(menu, SWT.NONE);
        menuItem_10.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {

            }
        });
        menuItem_10.setText("Delete Selected Result");
        FormData fd_cmdClose = new FormData();
        fd_cmdClose.width = 75;
        fd_cmdClose.bottom = new FormAttachment(100, -10);
        fd_cmdClose.right = new FormAttachment(100, -10);
        cmdClose.setLayoutData(fd_cmdClose);
        cmdClose.setText("&Close");

    }


    /**
     * @return the canvas
     */
    public Canvas getCanvas()
    {
        return canvas;
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
        String name = "";
        int x = xpos / (length + gap);
        int y = ypos / (length + gap);
        int index = y * itemsPerLine + x;
        if (index < keySets.length && index >= 0) name = keySets[index];
        // System.out.println("X: " + x + " Y: " + y + " index: " + index +
        // " element: " + name );
        if (name.length() > 0) lastName = name;
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
     * @return the size of the dialog
     */
    public Point getSize()
    {
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
        if (suite == null) return null;
        return suite.get(getIdFromPoint(xpos, ypos));
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
     * 
     * @return the result
     */
    public Object open()
    {

        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return result;
    }


    protected void paintCanvas(GC gc)
    {
        if (keySets == null || keySets.length == 0)
        {
            gc.drawString("No Data ...", 10, 10);
            return;
        }
        itemsPerLine = canvas.getBounds().width / (length + gap);

        int x = gap;
        int y = gap;
        int i = 0;
        for (String key : keySets)
        {
            int color = 0;
            switch (data.get(key).getResult())
            {
            case Match:
                color = SWT.COLOR_GREEN;
                break;
            case CannotSolve:
                color = SWT.COLOR_YELLOW;
                break;
            case NoMatch:
                color = SWT.COLOR_RED;
                break;
            case Unknown:
            default:
                color = SWT.COLOR_DARK_GRAY;
                break;
            }

            gc.setBackground(shell.getDisplay().getSystemColor(color));
            gc.fillRectangle(x, y, length, length);

            x += length + gap;

            if ((i + 1) % itemsPerLine == 0)
            {
                x = gap;
                y += length + gap;
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
     * Initializes the canvas from the cached results
     * 
     * @param cache
     *            cached results
     */
    public void setData(HashMap<String, DelayedResult> cache)
    {
        data = cache;
        if (data != null && data.keySet().size() > 0)
            keySets = data.keySet().toArray(new String[0]);
        else
            keySets = new String[0];
        Arrays.sort(keySets);
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


    /**
     * Updates the element with the given id
     * 
     * @param id
     *            the id
     * @param result
     *            a delayed result
     */
    public void updateElement(String id, DelayedResult result)
    {
        if (canvas.isDisposed()) return;

        data.put(id, result);

        canvas.redraw();
        // canvas.update();
    }


    /**
     * Updates the element with given id to the new result
     * 
     * @param id
     *            the id
     * @param result
     *            the result type
     */
    public void updateElement(String id, ResultType result)
    {
        updateElement(id, new DelayedResult(result));
    }
}
