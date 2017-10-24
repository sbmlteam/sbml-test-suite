//
// @file   DialogFilterTags.java
// @brief  Filter dialog
// @author Frank T. Bergmann
// @author Michael Hucka
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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
import java.util.Scanner;
import java.util.TreeSet;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mihalis.opal.titledSeparator.TitledSeparator;
import org.sbml.testsuite.core.Util;


/**
 * Filter dialog for test / component tags
 */
public class FilterDialog
    extends Dialog
{
    private Label              lblDescription;
    private LabeledList        lblLstComponentTags;
    private LabeledList        lblLstTestTags;
    private LabeledList        lblLstIncludedTags;
    private LabeledList        lblLstExcludedTags;
    protected TreeSet<String>  includedTags;
    protected TreeSet<String>  excludedTags;
    protected TreeSet<Integer> includedCases;
    protected TreeSet<Integer> excludedCases;
    protected Shell            shlFilterTags;

    private Text               txtInclude;
    private Text               txtExclude;


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


    private void addTags(LabeledList list, String[] tags)
    {
        if (list == null || tags == null) return;
        list.add(tags);
        for (Item item : list.getItems())
        {
            item.setData("TIP_TEXT_LABEL", "The meaning of this tag: ");
            item.setData("TIP_TEXT", Tags.getTagDescription(item.getText()));
        }
    }


    private void removeTags(LabeledList list, String[] tags)
    {
        if (list == null || tags == null) return;
        list.remove(tags);
    }


    private void includeTags(String[] selected)
    {
        addTags(lblLstIncludedTags, selected);
        removeTags(lblLstExcludedTags, selected);
    }


    private void excludeTags(String[] selected)
    {
        addTags(lblLstExcludedTags, selected);
        removeTags(lblLstIncludedTags, selected);
    }


    private void removeIncludedTags(String[] selected)
    {
        if (selected == null || selected.length == 0) return;
        for (int i = 0; i < selected.length; i++)
        {
            lblLstIncludedTags.remove(selected[i]);
        }
    }


    private void removeExcludedTags(String[] selected)
    {
        if (selected == null || selected.length == 0) return;
        for (int i = 0; i < selected.length; i++)
        {
            lblLstExcludedTags.remove(selected[i]);
        }
    }


    protected void clearIncludedTags()
    {
        lblLstIncludedTags.removeAll();
    }


    protected void clearExcludedTags()
    {
        lblLstExcludedTags.removeAll();
    }


    protected String[] getSelectedIncludedTags()
    {
        return lblLstIncludedTags.getSelection();
    }


    protected String[] getSelectedExcludedTags()
    {
        return lblLstExcludedTags.getSelection();
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
        int totalWidth = 650;
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
        int nudge = offset/2;

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

        Color gray = shlFilterTags.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);

        TitledSeparator numbersTitledSeparator = new TitledSeparator(shlFilterTags, SWT.NONE);
        numbersTitledSeparator.setText("Filter by case numbers");
        numbersTitledSeparator.setForeground(gray);
        FormData fd_sep = new FormData(SWT.DEFAULT, 2*UIUtils.getDefaultFontHeight());
        fd_sep.top = new FormAttachment(cmdClearAll, (UIUtils.isMacOSX() ? -margin : 0));
        fd_sep.left = new FormAttachment(0, margin);
        fd_sep.right = new FormAttachment(100, -margin);
        numbersTitledSeparator.setLayoutData(fd_sep);

        Label numberUsageInfo = new Label(shlFilterTags, SWT.WRAP);
        FormData fd_numberUsageInfo = new FormData();
        fd_numberUsageInfo.top = new FormAttachment(numbersTitledSeparator, margin, SWT.BOTTOM);
        fd_numberUsageInfo.left = new FormAttachment(0, 3*margin);
        fd_numberUsageInfo.right = new FormAttachment(100, -3*margin);
        numberUsageInfo.setLayoutData(fd_numberUsageInfo);
        numberUsageInfo.setForeground(gray);
        numberUsageInfo.moveAbove(null);
        final Font textFont = UIUtils.createResizedFont(numberUsageInfo.getFont(), -1);
        numberUsageInfo.setFont(textFont);
        numberUsageInfo.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e)
                {
                    textFont.dispose();
                }
        });
        numberUsageInfo.setText("You can list individual case numbers, "
                                + "or indicate ranges using dashes. Use "
                                + "commas to separate numbers. \nExample: "
                                + "\"80-90, 95, 1100-1150\". Exclusions "
                                + "override inclusions.");

        Label lblInclude = new Label(shlFilterTags, SWT.RIGHT);
        FormData fd_lblInclude = new FormData();
        fd_lblInclude.top = new FormAttachment(numberUsageInfo, 2*margin + nudge);
        fd_lblInclude.left = new FormAttachment(0, 3*margin);
        lblInclude.setLayoutData(fd_lblInclude);
        lblInclude.moveAbove(null);
        lblInclude.setText("Include cases:");

        txtInclude = new Text(shlFilterTags, SWT.BORDER);
        FormData fd_txtInclude = new FormData();
        fd_txtInclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_txtInclude.left = new FormAttachment(lblInclude, margin);
        fd_txtInclude.right = new FormAttachment(0, 300);
        txtInclude.setLayoutData(fd_txtInclude);
        txtInclude.moveAbove(null);

        txtExclude = new Text(shlFilterTags, SWT.BORDER);
        FormData fd_txtExclude = new FormData();
        fd_txtExclude.top = new FormAttachment(numberUsageInfo, 2*margin);
        fd_txtExclude.left = new FormAttachment(100, -205);
        fd_txtExclude.right = new FormAttachment(100, -3*margin);
        txtExclude.setLayoutData(fd_txtExclude);
        txtExclude.moveAbove(null);

        Label lblExclude = new Label(shlFilterTags, SWT.RIGHT);
        FormData fd_lblExclude = new FormData();
        fd_lblExclude.top = new FormAttachment(numberUsageInfo, 2*margin + nudge);
        fd_lblExclude.right = new FormAttachment(txtExclude, -2*margin);
        lblExclude.setLayoutData(fd_lblExclude);
        lblExclude.moveAbove(null);
        lblExclude.setText("Exclude cases:");

        // Buttons at the bottom.  Done here so that the middle section can
        // refer to the buttons to anchor the bottom of their layout.

        Button cmdCancel = new Button(shlFilterTags, SWT.NONE);
        FormData fd_cmdCancel = new FormData();
        fd_cmdCancel.width = buttonWidth;
        fd_cmdCancel.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdCancel.right = new FormAttachment(100, -(2*margin + offset));
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

        Button cmdOk = new Button(shlFilterTags, SWT.NONE);
        cmdOk.setText("OK");
        FormData fd_cmdOk = new FormData();
        fd_cmdOk.width = buttonWidth;
        fd_cmdOk.bottom = new FormAttachment(100, -(margin + offset));
        fd_cmdOk.right = new FormAttachment(cmdCancel, -offset);
        cmdOk.setLayoutData(fd_cmdOk);
        cmdOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                okPressed();
            }
        });

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

        TitledSeparator tagsTitleSeparator = new TitledSeparator(shlFilterTags, SWT.NONE);
        tagsTitleSeparator.setText("Filter by tags");
        tagsTitleSeparator.setForeground(gray);
        FormData fd_tts = new FormData(SWT.DEFAULT, 2*UIUtils.getDefaultFontHeight());
        fd_tts.top = new FormAttachment(txtExclude, margin, SWT.BOTTOM);
        fd_tts.left = new FormAttachment(0, margin);
        fd_tts.right = new FormAttachment(100, -margin);
        tagsTitleSeparator.setLayoutData(fd_tts);
        tagsTitleSeparator.moveAbove(null);

        Label tagsUsageInfo = new Label(shlFilterTags, SWT.WRAP);
        FormData fd_tagsUsageInfo = new FormData();
        fd_tagsUsageInfo.top = new FormAttachment(tagsTitleSeparator, margin, SWT.BOTTOM);
        fd_tagsUsageInfo.left = new FormAttachment(0, 3*margin);
        fd_tagsUsageInfo.right = new FormAttachment(100, -3*margin);
        tagsUsageInfo.setLayoutData(fd_tagsUsageInfo);
        tagsUsageInfo.moveAbove(null);
        tagsUsageInfo.setForeground(gray);
        tagsUsageInfo.setFont(textFont);
        tagsUsageInfo.setText("Choose tags that test cases should "
                              + "include and/or not include. Exclusions "
                              + "override inclusions. If case numbers\nare "
                              + "also provided above, then tag filters "
                              + "are applied to the cases that remain "
                              + "after filtering by numbers.");

        SashForm sashForm = new SashForm(shlFilterTags, SWT.NONE);
        FormData fd_sashForm = new FormData();
        fd_sashForm.top = new FormAttachment(tagsUsageInfo, margin);
        fd_sashForm.bottom = new FormAttachment(cmdCancel, -margin);
        fd_sashForm.left = new FormAttachment(0, margin + 1);
        fd_sashForm.right = new FormAttachment(100, -margin - 1);
        sashForm.setLayoutData(fd_sashForm);
        if (!UIUtils.isMacOSX()) sashForm.moveAbove(null);

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
        lblLstComponentTags.addMouseListener(new MouseAdapter() {
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
        lblLstTestTags.addMouseListener(new MouseAdapter() {
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
        lblLstIncludedTags.addMouseListener(new MouseAdapter() {
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
        lblLstExcludedTags.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0)
            {
                removeExcludedTags(getSelectedExcludedTags());
            }
        });

        sashForm.setWeights(new int[] {5, 5});
        sashForm_left.setWeights(new int[] {1, 1});
        sashForm_right.setWeights(new int[] {1, 1});

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
        final Display display = shlFilterTags.getDisplay();
        display.addFilter(SWT.KeyDown, closeKeyListener);
        shlFilterTags.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent notUsed)
            {
                display.removeFilter(SWT.KeyDown, closeKeyListener);
            }
        });

        final CustomToolTipHandler tooltip
            = new CustomToolTipHandler(shlFilterTags);
        tooltip.activateHoverHelp(lblLstComponentTags.getControl());
        tooltip.activateHoverHelp(lblLstTestTags.getControl());
        tooltip.activateHoverHelp(lblLstIncludedTags.getControl());
        tooltip.activateHoverHelp(lblLstExcludedTags.getControl());

        UIUtils.createShellCloseListener(shlFilterTags);

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
        return lblLstComponentTags.getSelection();
    }


    /**
     * @return selection of test tags
     */
    public String[] getSelectedTestTags()
    {
        return lblLstTestTags.getSelection();
    }


    /**
     * @return current selection
     */
    public TreeSet<String> getIncludedTags()
    {
        return includedTags;
    }


    /**
     * @return current selection
     */
    public TreeSet<String> getExcludedTags()
    {
        return excludedTags;
    }


    /**
     * @return current case numbers
     */
    public TreeSet<Integer> getIncludedCases()
    {
        return includedCases;
    }


    /**
     * @return current selection
     */
    public TreeSet<Integer> getExcludedCases()
    {
        return excludedCases;
    }


    private TreeSet<String> readIncludedTags()
    {
        TreeSet<String> result = new TreeSet<String>();
        result.addAll(lblLstIncludedTags.getStrings());
        return result;
    }


    /**
     * @return current exclusions
     */
    public TreeSet<String> readExcludedTags()
    {
        TreeSet<String> result = new TreeSet<String>();
        result.addAll(lblLstExcludedTags.getStrings());
        return result;
    }


    /**
     * @return current exclusions
     */
    public TreeSet<Integer> readIncludedCases()
    {
        return parseCaseNumbers(txtInclude.getText());
    }


    /**
     * @return current exclusions
     */
    public TreeSet<Integer> readExcludedCases()
    {
        return parseCaseNumbers(txtExclude.getText());
    }


    private TreeSet<Integer> parseCaseNumbers(String text)
    {
        TreeSet<Integer> caseNumbers = new TreeSet<Integer>();

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
                {
                    /// FTB: This is another example of a dialog, that is unnecessary, 
                    ///      ignoring this would be the sensible thing to do without 
                    ///      the need for a confirmation.
                    // Tell.error(shlFilterTags,
                    //            "The text '" + item + "' cannot be parsed "
                    //            + "\nas a number. It will be ignored.",
                    //            "Only integer numbers, consisting of one or "
                    //            + "more digits,\nare valid in this context.");
                    groups.close();
                    return caseNumbers;
                }
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
        groups.close();
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
     */
    public void open()
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


    private void setList(LabeledList list, Collection<String> items)
    {
        if (items == null) return;
        list.removeAll();
        list.add(items);
        for (Item item : list.getItems())
        {
            item.setData("TIP_TEXT_LABEL", "The meaning of this tag: ");
            item.setData("TIP_TEXT", Tags.getTagDescription(item.getText()));
        }
    }


    /**
     * Set component tags
     *
     * @param items
     *            the component tags
     */
    public void setComponentTags(Collection<String> items)
    {
        setList(lblLstComponentTags, items);
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
        setList(lblLstIncludedTags, items);
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
        setList(lblLstExcludedTags, items);
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
        setList(lblLstTestTags, items);
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


    public void setIncludedCases(TreeSet<Integer> cases)
    {
        if (cases == null || cases.isEmpty()) return;
        txtInclude.setText(createCaseNumbersString(cases));
    }


    public void setExcludedCases(TreeSet<Integer> cases)
    {
        if (cases == null || cases.isEmpty()) return;
        txtExclude.setText(createCaseNumbersString(cases));
    }


    /*
     * Based on this code:
     * http://stackoverflow.com/a/5744861/743730
     */
    private String createCaseNumbersString(TreeSet<Integer> numbers)
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
