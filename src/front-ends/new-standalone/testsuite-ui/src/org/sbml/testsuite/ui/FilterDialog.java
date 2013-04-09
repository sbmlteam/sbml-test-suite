//
// @file   DialogFilterTags.java
// @brief  Filter dialog
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.sbml.testsuite.core.Util;
import org.eclipse.swt.widgets.Control;


/**
 * Filter dialog for test / component tags
 */
public class FilterDialog
    extends Dialog
{
    private Label             lblDescription;
    private LabeledList       lblLstComponentTags;
    private LabeledList       lblLstTestTags;
    private LabeledList       lblLstIncludedTags;
    private LabeledList       lblLstExcludedTags;
    protected Vector<String>  includedTags;
    protected Vector<String>  excludedTags;
    protected Vector<Integer> includedCases;
    protected Vector<Integer> excludedCases;
    protected Shell           shlFilterTags;

    private Text              txtInclude;
    private Text              txtExclude;


    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public FilterDialog(Shell parent, int style)
    {
        super(parent, style);
        createContents();
        setText("SWT Dialog");
    }


    private void addTagsToList(List list, String[] tags)
    {
        if (list == null || tags == null) return;

        for (int i = 0; i < tags.length; i++)
        {
            if (list.indexOf(tags[i]) == -1)
                list.add(tags[i]);
        }
    }


    private void removeTagsFromList(List list, String[] tags)
    {
        if (list == null || tags == null) return;

        for (int i = 0; i < tags.length; i++)
        {
            int index = list.indexOf(tags[i]);
            if (index >= 0)
                list.remove(index);
        }
    }


    private void includeTags(String[] selected)
    {
        addTagsToList(lblLstIncludedTags.getList(), selected);
        removeTagsFromList(lblLstExcludedTags.getList(), selected);
    }


    private void excludeTags(String[] selected)
    {
        addTagsToList(lblLstExcludedTags.getList(), selected);
        removeTagsFromList(lblLstIncludedTags.getList(), selected);
    }


    private void removeIncludedTags(String[] selected)
    {
        if (selected == null || selected.length == 0) return;
        for (int i = 0; i < selected.length; i++)
        {
            lblLstIncludedTags.getList().remove(selected[i]);
        }
    }


    private void removeExcludedTags(String[] selected)
    {
        if (selected == null || selected.length == 0) return;
        for (int i = 0; i < selected.length; i++)
        {
            lblLstExcludedTags.getList().remove(selected[i]);
        }
    }


    protected void clearIncludedTags()
    {
        lblLstIncludedTags.getList().removeAll();
    }


    protected void clearExcludedTags()
    {
        lblLstExcludedTags.getList().removeAll();
    }


    protected String[] getSelectedIncludedTags()
    {
        return lblLstIncludedTags.getList().getSelection();
    }


    protected String[] getSelectedExcludedTags()
    {
        return lblLstExcludedTags.getList().getSelection();
    }


    protected void clearIncludedCases()
    {
        txtInclude.setText("");
    }


    protected void clearExcludedCases()
    {
        txtExclude.setText("");
    }


    /**
     * Centers the dialog within the given rectangle
     * 
     * @param shellBounds
     *            the rectangle.
     */
    public void center(Rectangle shellBounds)
    {
        if (shlFilterTags == null) return;

        Point dialogSize = getSize();
        setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
                    shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
    }


    /**
     * Create contents of the dialog.
     */
    private void createContents()
    {
        int totalWidth = 625;
        int totalHeight = 600;

        shlFilterTags = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shlFilterTags.setImage(UIUtils.getImageResource("icon_256x256.png"));
        shlFilterTags.setMinimumSize(new Point(totalWidth, totalHeight));
        shlFilterTags.setSize(totalWidth, totalHeight);
        shlFilterTags.setText("Filter Tags");
        shlFilterTags.setLayout(new FormLayout());
        
        int margin = 5;
        int topLabelHeight = 30 + margin;
        int buttonWidth = 80;
        int offset = 20 - UIUtils.scaledFontSize(20);

        lblDescription = new Label(shlFilterTags, SWT.LEFT);
        FormData fd_lblDescription = new FormData();
        fd_lblDescription.top = new FormAttachment(0, 2*margin);
        fd_lblDescription.bottom = new FormAttachment(0, margin + topLabelHeight);
        fd_lblDescription.left = new FormAttachment(0, margin);
        fd_lblDescription.right = new FormAttachment(100, -(buttonWidth + 2*margin + offset));
        lblDescription.setLayoutData(fd_lblDescription);

        Button cmdClearAll = new Button(shlFilterTags, SWT.NONE);
        cmdClearAll.setText("Clear all");
        cmdClearAll.setToolTipText("Clear all selections");
        FormData fd_cmdClearAll = new FormData();
        fd_cmdClearAll.width = buttonWidth;
        fd_cmdClearAll.top = new FormAttachment(0, 2*margin + offset);
        fd_cmdClearAll.right = new FormAttachment(100, -(2*margin + offset) + 1);
        cmdClearAll.setLayoutData(fd_cmdClearAll);
        cmdClearAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                clearIncludedTags();
                clearExcludedTags();
                clearIncludedCases();
                clearExcludedCases();
            }
        });

        // Section for filtering by case numbers.

        int numberGroupHeight = 76 + 2*margin;

        Group numberGroup = new Group(shlFilterTags, SWT.SHADOW_ETCHED_IN);
        numberGroup.setText("Filter by case numbers");
        FormData fd1 = new FormData();
        fd1.top = new FormAttachment(shlFilterTags, topLabelHeight + 2*offset);
        fd1.bottom = new FormAttachment(0, topLabelHeight + margin + numberGroupHeight);
        fd1.left = new FormAttachment(0, margin);
        fd1.right = new FormAttachment(100, -margin);
        numberGroup.setLayoutData(fd1);

        Label spacer = new Label(shlFilterTags, SWT.NONE);
        FormData fd_spacer = new FormData();
        fd_spacer.height = margin;
        fd_spacer.top = new FormAttachment(numberGroup, 0, SWT.TOP);
        spacer.setLayoutData(fd_spacer);
        spacer.setText("");

        Label numberUsageInfo = new Label(shlFilterTags, SWT.WRAP);
        FormData fd_numberUsageInfo = new FormData();
        fd_numberUsageInfo.top = new FormAttachment(spacer, 3*margin);
        fd_numberUsageInfo.left = new FormAttachment(0, 3*margin);
        fd_numberUsageInfo.right = new FormAttachment(100, -3*margin);
        numberUsageInfo.setLayoutData(fd_numberUsageInfo);
        numberUsageInfo.moveAbove(numberGroup);
        Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
        numberUsageInfo.setForeground(gray);
        FontData[] fontData = numberUsageInfo.getFont().getFontData();
        for (int i = 0; i < fontData.length; ++i)
        {
            fontData[i].setHeight(UIUtils.scaledFontSize(11));
        }
        final Font newFont = new Font(Display.getCurrent(), fontData);
        numberUsageInfo.setFont(newFont);
        numberUsageInfo.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e)
                {
                    newFont.dispose();
                }
        });
        numberUsageInfo.setText("You can list individual case numbers, "
                                + "or indicate ranges using dashes. Use "
                                + "commas to separate numbers. \nExample: "
                                + "\"80-90, 95, 1100-1150\". Exclusions "
                                + "override inclusions.");

        Label lblInclude = new Label(shlFilterTags, SWT.RIGHT);
        FormData fd_lblInclude = new FormData();
        fd_lblInclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_lblInclude.left = new FormAttachment(0, 3*margin);
        lblInclude.setLayoutData(fd_lblInclude);
        lblInclude.moveAbove(numberGroup);
        lblInclude.setText("Include cases:");
        
        txtInclude = new Text(shlFilterTags, SWT.BORDER);
        FormData fd_txtInclude = new FormData();
        fd_txtInclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_txtInclude.left = new FormAttachment(lblInclude, margin);
        fd_txtInclude.right = new FormAttachment(0, 300);
        txtInclude.setLayoutData(fd_txtInclude);
        txtInclude.moveAbove(numberGroup);
        
        txtExclude = new Text(shlFilterTags, SWT.BORDER);
        FormData fd_txtExclude = new FormData();
        fd_txtExclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_txtExclude.left = new FormAttachment(100, -205);
        fd_txtExclude.right = new FormAttachment(100, -3*margin);
        txtExclude.setLayoutData(fd_txtExclude);
        txtExclude.moveAbove(numberGroup);

        Label lblExclude = new Label(shlFilterTags, SWT.RIGHT);
        FormData fd_lblExclude = new FormData();
        fd_lblExclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_lblExclude.right = new FormAttachment(txtExclude, -2*margin);
        lblExclude.setLayoutData(fd_lblExclude);
        lblExclude.moveAbove(numberGroup);
        lblExclude.setText("Exclude cases:");

        // Buttons at the bottom.  Done here so that the middle section can
        // refer to the buttons to anchor the bottom of their layout.

        Button cmdOk = new Button(shlFilterTags, SWT.NONE);
        FormData fd_cmdOk = new FormData();
        fd_cmdOk.width = buttonWidth;
        fd_cmdOk.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdOk.right = new FormAttachment(100, -(2*margin + offset));
        cmdOk.setLayoutData(fd_cmdOk);
        cmdOk.setText("OK");
        cmdOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                okPressed();
            }
        });
        cmdOk.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shlFilterTags));

        Button cmdCancel = new Button(shlFilterTags, SWT.NONE);
        cmdCancel.setText("Cancel");
        FormData fd_cmdCancel = new FormData();
        fd_cmdCancel.width = buttonWidth;
        fd_cmdCancel.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdCancel.right = new FormAttachment(cmdOk, -offset);
        cmdCancel.setLayoutData(fd_cmdCancel);
        cmdCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                cancelPressed();
            }
        });
        cmdCancel.setFocus();
        cmdCancel.addListener(SWT.KeyDown, UIUtils.createCancelKeyListener(shlFilterTags));

        shlFilterTags.setDefaultButton(cmdOk);
        shlFilterTags.addListener(SWT.Traverse, new Listener() {
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

        // Section for filtering by tags.

        Group tagsGroup = new Group(shlFilterTags, SWT.SHADOW_ETCHED_IN);
        tagsGroup.setText("Filter by tags");
        FormData fd_tagsGroup = new FormData();
        fd_tagsGroup.top = new FormAttachment(numberGroup, margin);
        fd_tagsGroup.bottom = new FormAttachment(cmdCancel, -(margin + offset));
        fd_tagsGroup.left = new FormAttachment(0, margin);
        fd_tagsGroup.right = new FormAttachment(100, -margin);
        tagsGroup.setLayoutData(fd_tagsGroup);

        Label spacer2 = new Label(shlFilterTags, SWT.NONE);
        FormData fd_spacer2 = new FormData();
        fd_spacer2.height = 2*margin;
        fd_spacer2.top = new FormAttachment(tagsGroup, 0, SWT.TOP);
        spacer2.setLayoutData(fd_spacer2);
        spacer2.setText("");

        Label tagsUsageInfo = new Label(shlFilterTags, SWT.WRAP);
        FormData fd_tagsUsageInfo = new FormData();
        fd_tagsUsageInfo.top = new FormAttachment(spacer2, 2*margin);
        fd_tagsUsageInfo.left = new FormAttachment(0, 3*margin);
        fd_tagsUsageInfo.right = new FormAttachment(100, -3*margin);
        tagsUsageInfo.setLayoutData(fd_tagsUsageInfo);
        tagsUsageInfo.moveAbove(tagsGroup);
        tagsUsageInfo.setForeground(gray);
        tagsUsageInfo.setFont(newFont);
        tagsUsageInfo.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e)
                {
                    newFont.dispose();
                }
        });
        tagsUsageInfo.setText("Choose tags that test cases should "
                              + "include and/or not include. Exclusions "
                              + "override inclusions. If case numbers\nare "
                              + "also provided above, then tag filters "
                              + "are applied to the cases that remain "
                              + "after filtering by numbers.");

        SashForm sashForm = new SashForm(shlFilterTags, SWT.NONE);
        FormData fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(tagsUsageInfo, margin);
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -3*margin);
        fd_sashForm.left = new FormAttachment(0, margin + 1);
        fd_sashForm.right = new FormAttachment(100, -margin - 1);
        sashForm.setLayoutData(fd_sashForm);
        if (!UIUtils.isMacOSX()) sashForm.moveAbove(tagsGroup);

        SashForm sashForm_left = new SashForm(sashForm, SWT.VERTICAL);

        lblLstComponentTags = new LabeledList(sashForm_left, SWT.MULTI);
        lblLstComponentTags.setLabel("Component Tags: ");
        lblLstComponentTags.getButton1().setText("Include");
        lblLstComponentTags.getButton1()
                           .setToolTipText("Include selected component tags");
        lblLstComponentTags.getButton1()
                           .addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   includeTags(getSelectedComponentTags());
                               }
                           });
        lblLstComponentTags.getButton2().setText("Exclude");
        lblLstComponentTags.getButton2()
                           .setToolTipText("Exclude selected component tags");
        lblLstComponentTags.getButton2()
                           .addSelectionListener(new SelectionAdapter() {
                               @Override
                               public void widgetSelected(SelectionEvent arg0)
                               {
                                   excludeTags(getSelectedComponentTags());
                               }
                           });
        ((FormData) lblLstComponentTags.getList().getLayoutData()).right
            = new FormAttachment(100, -10);
        lblLstComponentTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                includeTags(getSelectedComponentTags());
            }
        });

        lblLstTestTags = new LabeledList(sashForm_left, SWT.MULTI);
        lblLstTestTags.setLabel("Test Tags:");
        lblLstTestTags.getButton1().setText("Include");
        lblLstTestTags.getButton1()
                      .setToolTipText("Include selected test tags");
        lblLstTestTags.getButton1()
                      .addSelectionListener(new SelectionAdapter() {
                          @Override
                          public void widgetSelected(SelectionEvent arg0)
                          {
                              includeTags(getSelectedTestTags());
                          }
                      });
        lblLstTestTags.getButton2().setText("Exclude");
        lblLstTestTags.getButton2()
                      .setToolTipText("Exclude selected test tags");
        lblLstTestTags.getButton2()
                      .addSelectionListener(new SelectionAdapter() {
                          @Override
                          public void widgetSelected(SelectionEvent arg0)
                          {
                              excludeTags(getSelectedTestTags());
                          }
                      });
        lblLstTestTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                includeTags(getSelectedTestTags());
            }
        });

        SashForm sashForm_right = new SashForm(sashForm, SWT.VERTICAL);

        lblLstIncludedTags = new LabeledList(sashForm_right, SWT.MULTI);
        FormData fd_labeledList_1 = new FormData();
        fd_labeledList_1.top = new FormAttachment(0);
        fd_labeledList_1.left = new FormAttachment(0);
        fd_labeledList_1.bottom = new FormAttachment(100);
        fd_labeledList_1.right = new FormAttachment(100);
        lblLstIncludedTags.setLayoutData(fd_labeledList_1);
        lblLstIncludedTags.setLabel("Included Tags: ");

        lblLstIncludedTags.getButton1().setText("Clear");
        lblLstIncludedTags.getButton1()
                          .setToolTipText("Clear entire list");
        lblLstIncludedTags.getButton1()
                          .addSelectionListener(new SelectionAdapter() {
                                  @Override
                                  public void widgetSelected(SelectionEvent arg0)
                                  {
                                      clearIncludedTags();
                                  }
                              });
        lblLstIncludedTags.getButton2().setText("Remove");
        lblLstIncludedTags.getButton2()
                          .setToolTipText("Remove selected tag(s) from list");
        lblLstIncludedTags.getButton2()
                          .addSelectionListener(new SelectionAdapter() {
                                  @Override
                                  public void widgetSelected(SelectionEvent arg0)
                                  {
                                      removeIncludedTags(getSelectedIncludedTags());
                                  }
                              });
        lblLstIncludedTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                removeIncludedTags(getSelectedIncludedTags());
            }
        });

        lblLstExcludedTags = new LabeledList(sashForm_right, SWT.MULTI);
        FormData fd_labeledList_2 = new FormData();
        fd_labeledList_2.top = new FormAttachment(0);
        fd_labeledList_2.left = new FormAttachment(0);
        fd_labeledList_2.bottom = new FormAttachment(100);
        fd_labeledList_2.right = new FormAttachment(100);
        lblLstExcludedTags.setLayoutData(fd_labeledList_2);
        lblLstExcludedTags.setLabel("Excluded Tags: ");

        lblLstExcludedTags.getButton1().setText("Clear");
        lblLstExcludedTags.getButton1()
                          .setToolTipText("Clear entire list");
        lblLstExcludedTags.getButton1()
                          .addSelectionListener(new SelectionAdapter() {
                                  @Override
                                  public void widgetSelected(SelectionEvent arg0)
                                  {
                                      clearExcludedTags();
                                  }
                              });
        lblLstExcludedTags.getButton2().setText("Remove");
        lblLstExcludedTags.getButton2()
                          .setToolTipText("Remove excluded tag(s) from list");
        lblLstExcludedTags.getButton2()
                          .addSelectionListener(new SelectionAdapter() {
                                  @Override
                                  public void widgetSelected(SelectionEvent arg0)
                                  {
                                      removeExcludedTags(getSelectedExcludedTags());
                                  }
                              });
        lblLstExcludedTags.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                removeExcludedTags(getSelectedExcludedTags());
            }
        });

        sashForm.setWeights(new int[] {5, 5});
        sashForm_left.setWeights(new int[] {1, 1});
        sashForm_right.setWeights(new int[] {1, 1});

        shlFilterTags.pack();
        shlFilterTags.setTabList(new Control[]{cmdCancel, cmdClearAll,
                                               txtInclude, txtExclude, 
                                               sashForm, cmdOk});
        shlFilterTags.layout();
    }


    /**
     * @return the displayed description
     */
    public String getDescription()
    {
        return lblDescription.getText();
    }


    /**
     * @return the selected component tags
     */
    public String[] getSelectedComponentTags()
    {
        return lblLstComponentTags.getList().getSelection();
    }


    /**
     * @return selection of test tags
     */
    public String[] getSelectedTestTags()
    {
        return lblLstTestTags.getList().getSelection();
    }


    /**
     * @return current selection
     */
    public Vector<String> getIncludedTags()
    {
        return includedTags;
    }


    /**
     * @return current selection
     */
    public Vector<String> getExcludedTags()
    {
        return excludedTags;
    }


    /**
     * @return current case numbers
     */
    public Vector<Integer> getIncludedCases()
    {
        return includedCases;
    }


    /**
     * @return current selection
     */
    public Vector<Integer> getExcludedCases()
    {
        return excludedCases;
    }


    private Vector<String> readIncludedTags()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstIncludedTags.getList().getItems()));

        return result;
    }


    /**
     * @return current exclusions
     */
    public Vector<String> readExcludedTags()
    {
        Vector<String> result = new Vector<String>();

        result.addAll(Arrays.asList(lblLstExcludedTags.getList().getItems()));

        return result;
    }


    /**
     * @return current exclusions
     */
    public Vector<Integer> readIncludedCases()
    {
        return parseCaseNumbers(txtInclude.getText());
    }


    /**
     * @return current exclusions
     */
    public Vector<Integer> readExcludedCases()
    {
        return parseCaseNumbers(txtExclude.getText());
    }


    private Vector<Integer> parseCaseNumbers(String text)
    {
        Vector<Integer> caseNumbers = new Vector<Integer>();

        if (text == null || text.length() == 0) return caseNumbers;

        Scanner groups = new Scanner(text).useDelimiter("\\s*,+\\s*|\\s+");
        while (groups.hasNext())
        {
            String item = groups.next().trim();
            if (item == null || item.length() == 0) continue;

            int dashIndex = item.indexOf('-');
            if (dashIndex == -1) // Not actually a range, just one number.
            {
                Integer value = parseInt(item);
                if (value == null)
                    Tell.error(shlFilterTags,
                               "The text '" + item + "' cannot be parsed "
                               + "\nas a number. It will be ignored.",
                               "Only integer numbers, consisting of one or "
                               + "more digits,\nare valid in this context.");
                else
                    caseNumbers.add(value);
            }
            else
            {
                String start = item.substring(0, dashIndex).trim();
                String end   = item.substring(dashIndex + 1, item.length()).trim();

                Integer startIndex = parseInt(start);
                Integer endIndex   = parseInt(end);

                if (startIndex == null || endIndex == null)
                {
                    Tell.error(shlFilterTags,
                               "The text '" + item + "' cannot be parsed "
                               + "\nas a number range. It will be ignored.",
                               "Number ranges must have the following format:\n"
                               + "N-M\n"
                               + "where N and M are integers.");
                }
                else
                {
                    for (int i = startIndex; i <= endIndex; ++i)
                        caseNumbers.add(i);
                }
            }
        }
        Collections.sort(caseNumbers);
        return caseNumbers;
    }


    private Integer parseInt(String text)
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * @return the size of the dialog
     */
    public Point getSize()
    {
        return shlFilterTags.getSize();
    }


    /**
     * @return the title of the dialog
     */
    public String getTitle()
    {
        return shlFilterTags.getText();
    }


    /**
     * Open the dialog.
     * 
     * @return true if there is a non-empty value for either the included 
     * or excluded tags and/or case numbers lists.
     */
    public boolean open()
    {
        // Remember the populated lists in case the user cancels out.

        includedTags  = readIncludedTags();
        excludedTags  = readExcludedTags();
        includedCases = readIncludedCases();
        excludedCases = readExcludedCases();

        // Open the dialog and wait for the user to close it.

        shlFilterTags.open();
        Display display = getParent().getDisplay();
        while (!shlFilterTags.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        
        // If there are any non-empty tags lists, return true.

        return ((includedTags     != null && !includedTags.isEmpty())
                || (excludedTags  != null && !excludedTags.isEmpty())
                || (includedCases != null && !includedCases.isEmpty())
                || (excludedCases != null && !excludedCases.isEmpty()));                
    }


    private void okPressed()
    {
        includedTags = readIncludedTags();
        excludedTags = readExcludedTags();
        includedCases = readIncludedCases();
        excludedCases = readExcludedCases();
        shlFilterTags.close();
    }


    private void cancelPressed()
    {
        shlFilterTags.close();
    }


    /**
     * Set component tags
     * 
     * @param items
     *            the component tags
     */
    public void setComponentTags(Collection<String> items)
    {
        if (items == null) return;
        lblLstComponentTags.getList().removeAll();
        for (String tag : items)
            lblLstComponentTags.getList().add(tag);
    }


    /**
     * Set the component tags
     * 
     * @param string
     *            of comma separated items
     */
    public void setComponentTags(String items)
    {
        setComponentTags(Util.split(items));
    }


    /**
     * Set the selected tags
     * 
     * @param items
     *            the current selection
     */
    public void setIncludedTags(Collection<String> items)
    {
        if (items == null) return;
        lblLstIncludedTags.getList().removeAll();
        for (String tag : items)
            lblLstIncludedTags.getList().add(tag);
    }


    /**
     * Set the selected tags
     * 
     * @param string
     *            of comma separated items the current selection
     */
    public void setIncludedTags(String items)
    {
        setIncludedTags(Util.split(items));
    }


    /**
     * Set the selected tags
     * 
     * @param items
     *            the current selection
     */
    public void setExcludedTags(Collection<String> items)
    {
        if (items == null) return;
        lblLstExcludedTags.getList().removeAll();
        for (String tag : items)
            lblLstExcludedTags.getList().add(tag);
    }


    /**
     * Set the selected tags
     * 
     * @param string
     *            of comma separated items the current selection
     */
    public void setExcludedTags(String items)
    {
        setExcludedTags(Util.split(items));
    }


    /**
     * Set the test tags
     * 
     * @param items
     *            the current selection
     */
    public void setTestTags(Collection<String> items)
    {
        if (items == null) return;
        lblLstTestTags.getList().removeAll();
        for (String tag : items)
            lblLstTestTags.getList().add(tag);
    }


    /**
     * Set the test tags
     * 
     * @param string
     *            of comma separated items
     */
    public void setTestTags(String items)
    {
        setTestTags(Util.split(items));
    }


    public void setIncludedCases(Vector<Integer> cases)
    {
        if (cases == null || cases.isEmpty()) return;
        txtInclude.setText(createCaseNumbersString(cases));
    }


    public void setExcludedCases(Vector<Integer> cases)
    {
        if (cases == null || cases.isEmpty()) return;
        txtExclude.setText(createCaseNumbersString(cases));
    }


    /*
     * Based on this code:
     * http://stackoverflow.com/a/5744861/743730
     */
    private String createCaseNumbersString(Vector<Integer> numbers)
    {
        String output = new String();
        Integer start = null;
        Integer end = null;

        for (Integer num : numbers)
        {
            if (start == null || end == null) // Initialization.
            {
                start = num;
                end = num;
            }
            else if (end.equals(num - 1)) // Next number in range.
            {
                end = num;
            }
            else                        // There's a gap.
            {
                if (start.equals(end))  // Range length 1.
                    output += start + ", ";
                else if (start.equals(end - 1)) // Range length 2.
                    output += start + ", " + end + ", ";
                else                    // Range lenth 2+.
                    output += start + "-" + end + ", ";

                start = num;
                end = num;
            }
        }

        if (start.equals(end))
            output += start;
        else if (start.equals(end - 1))
            output += start + ", " + end;
        else
            output += start + "-" + end;

        return output;
    }


    /**
     * Sets the description of the dialog
     * 
     * @param text
     *            the description
     */
    public void setDescription(String text)
    {
        lblDescription.setText(text);
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
        shlFilterTags.setLocation(x, y);
    }


    /**
     * Sets the title of the dialog
     * 
     * @param text
     *            the title text
     */
    public void setTitle(String text)
    {
        shlFilterTags.setText(text);
    }
}
