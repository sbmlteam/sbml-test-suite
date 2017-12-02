//
// @file   HelpViewer.java
// @brief  A help viewer for the SBML Test Runner
// @author Michael Hucka
// @date   Created 2013-05-13 <mhucka@caltech.edu>
//
// The code in this file was originally based on the program located at
// http://www.eclipse.org/articles/Article-SWT-browser-widget/DocumentationViewer.java
// retrieved on 2013-05-13.  The original copyright and license are reproduced
// below:
//
// * Copyright (c) 2004 IBM Corporation.
// * All rights reserved. This program and the accompanying materials 
// * are made available under the terms of the Common Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/cpl-v10.html
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

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class HelpViewer
{
    private Browser browser;
    private String[] urls;
    private String[] titles;
    private int index;
    private Shell shell;
    private static String WINDOW_TITLE = "SBML Test Runner Help Viewer";


    public HelpViewer(Shell parent, File helpDir)
    {
        shell = new Shell(parent.getDisplay(), SWT.DIALOG_TRIM | SWT.RESIZE);
        createContents();
        readHelpText(helpDir);
    }


    private void createContents()
    {
        shell.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shell.setText(WINDOW_TITLE);
        shell.setLayout(new GridLayout());
        shell.setSize(750, 600);
        shell.setMinimumSize(400, 200);

        shell.addListener(SWT.Traverse, UIUtils.createEscapeKeyListener(shell));

        Composite compTools = new Composite(shell, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        compTools.setLayoutData(data);
        compTools.setLayout(new GridLayout(1, false));

        ToolBar navBar = new ToolBar(compTools, SWT.FLAT);
        GridData gd_navBar = new GridData(GridData.FILL_HORIZONTAL
                                          | GridData.HORIZONTAL_ALIGN_END);
        gd_navBar.horizontalAlignment = SWT.LEFT;
        navBar.setLayoutData(gd_navBar);

        final ToolItem back = new ToolItem(navBar, SWT.PUSH);
        back.setEnabled(false);
        back.setImage(UIUtils.getImageResource("backward-arrow.png"));
        back.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event)
            {
                browser.back();
            }
        });

        final ToolItem forward = new ToolItem(navBar, SWT.PUSH);
        forward.setEnabled(false);
        forward.setImage(UIUtils.getImageResource("forward-arrow.png"));
        forward.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event)
            {
                browser.forward();
            }
        });

        ToolItem sep1 = new ToolItem(navBar, SWT.NONE);
        sep1.setText("   ");
        sep1.setEnabled(false);

        final ToolItem bigger = new ToolItem(navBar, SWT.PUSH);
        bigger.setEnabled(true);
        bigger.setImage(UIUtils.getImageResource("bigger-font.png"));
        bigger.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event)
            {
                increaseFontSize();
            }
        });

        final ToolItem smaller = new ToolItem(navBar, SWT.PUSH);
        smaller.setEnabled(true);
        smaller.setImage(UIUtils.getImageResource("smaller-font.png"));
        smaller.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event)
            {
                decreaseFontSize();
            }
        });

        Composite comp = new Composite(shell, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        comp.setLayoutData(data);
        FillLayout fl_comp = new FillLayout();
        fl_comp.spacing = 5;
        comp.setLayout(fl_comp);

        final SashForm sashForm = new SashForm(comp, SWT.BORDER | SWT.HORIZONTAL);
        sashForm.setSashWidth(5);
        sashForm.setLayout(new FillLayout());

        final List list = new List(sashForm, SWT.SINGLE);
        GridData gd_list = new GridData(GridData.FILL_BOTH);
        list.setLayoutData(gd_list);

        list.addKeyListener(UIUtils.createCloseKeyListener(shell));
        list.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e)
            {
                int index = list.getSelectionIndex();
                if (index < 0)
                    return;
                browser.setUrl(urls[index]);
            }
        });

        try
        {
            browser = new Browser(sashForm, SWT.NONE);
        }
        catch (SWTError e)
        {
            Tell.error(shell, "Unable to open help browser",
                       e.getMessage());
            return;
        }

        browser.addKeyListener(UIUtils.createCloseKeyListener(shell));
        final LocationListener locationListener = new LocationListener() {
            public void changed(LocationEvent event)
            {
                Browser browser = (Browser)event.widget;
                back.setEnabled(browser.isBackEnabled());
                forward.setEnabled(browser.isForwardEnabled());
            }
            public void changing(LocationEvent event)
            {
            }
        };

        KeyListener fontSizeIncreaseListener = new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) { return; }
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (UIUtils.isModifierKey(e) && e.keyCode == '+')
                    increaseFontSize();
            }
        };
        browser.addKeyListener(fontSizeIncreaseListener);

        KeyListener fontSizeDecreaseListener = new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) { return; }
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (UIUtils.isModifierKey(e) && e.keyCode == '+')
                    increaseFontSize();
            }
        };
        browser.addKeyListener(fontSizeDecreaseListener);

        // Build a table of contents. Open each HTML file found in the given
        // folder to retrieve their title.

        final TitleListener tocTitleListener = new TitleListener() {
            public void changed(TitleEvent event)
            {
                titles[index] = event.title;
            }
        };
        final ProgressListener tocProgressListener = new ProgressListener() {
            public void changed(ProgressEvent event)
            {
            }
            public void completed(ProgressEvent event)
            {
                index++;
                if (index >= titles.length) // TOC has been completed.
                {
                    Browser thisBrowser = (Browser) event.widget;
                    thisBrowser.dispose();
                    thisBrowser = new Browser(sashForm, SWT.NONE);
                    browser = thisBrowser;
                    sashForm.layout(true);
                    browser.addLocationListener(locationListener);
                    list.removeAll();
                    for (int i = 0; i < titles.length; i++)
                        list.add(titles[i]);
                    list.select(0);
                    browser.setUrl(urls[0]);
                    browser.setFont(UIUtils.createResizedFont("SansSerif", SWT.BOLD, -1));
                    shell.setText(WINDOW_TITLE);
                    return;
                }
                browser.setUrl(urls[index]);
            }
        };
        browser.addTitleListener(tocTitleListener);
        browser.addProgressListener(tocProgressListener);

        sashForm.setWeights(new int[] {1, 15});
        list.setFocus();
        shell.layout();

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
                    shell.close();
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

        shell.addListener(SWT.Close, UIUtils.createShellCloseListener(shell));
    }


    private void increaseFontSize() {
        // You would think that calling browser.setFont() would work here.
        // But no, it doesn't; it has no effect at all.  So, we resort to
        // this hack: call JavaScript code in the browser to change the size
        // of the font in every element.

        browser.evaluate("var tags = ['body', 'td', 'h1', 'h2', 'h3', 'code'];"
                         + "for (var i = 0, i_max = tags.length; i < i_max; i++) {"
                         + "    var elements = document.getElementsByTagName(tags[i]);"
                         + "    for (var j = 0, j_max = elements.length; j < j_max; j++) {"
                         + "        var prop = window.getComputedStyle(elements[j], null).getPropertyValue('font-size');"
                         + "        if (prop) {"
                         + "            var size = parseFloat(prop);"
                         + "            elements[j].style.fontSize = parseInt(size, 10) + 1 + 'px';"
                         + "        }"
                         + "    }"
                         + "}");
    }


    private void decreaseFontSize() {
        // See comments for increaseFontSize().
        browser.evaluate("var tags = ['body', 'td', 'h1', 'h2', 'h3', 'code'];"
                         + "for (var i = 0, i_max = tags.length; i < i_max; i++) {"
                         + "    var elements = document.getElementsByTagName(tags[i]);"
                         + "    for (var j = 0, j_max = elements.length; j < j_max; j++) {"
                         + "        var prop = window.getComputedStyle(elements[j], null).getPropertyValue('font-size');"
                         + "        if (prop) {"
                         + "            var size = parseFloat(prop);"
                         + "            elements[j].style.fontSize = parseInt(size, 10) - 1 + 'px';"
                         + "        }"
                         + "    }"
                         + "}");
    }


    private void readHelpText(File helpDir)
    {
        File[] files = helpDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".html") || name.endsWith(".htm");
                }
            });
        if (files.length == 0) return;
        urls = new String[files.length];
        titles = new String[files.length];
        index = 0;
        for (int i = 0; i < files.length; i++)
        {
            try
            {
                String url = files[i].toURL().toString();
                urls[i] = url;
            }
            catch (MalformedURLException ex)
            {
            }
        }
        if (urls.length > 0)
            browser.setUrl(urls[0]);
    }


    public void open()
    {
        if (shell == null) return;
        shell.open();
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shell == null || shellBounds == null) return;

        Point dialogSize = shell.getSize();
        shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                          shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
    }
}
