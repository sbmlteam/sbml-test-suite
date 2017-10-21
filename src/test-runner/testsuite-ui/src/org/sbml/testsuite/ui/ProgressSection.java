//
// @file   ProgressSection.java
// @brief  The "test run progress" section of the main window
// @author Michael Hucka
// @@date  Created 2013-03-14 <mhucka@caltech.edu>
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class ProgressSection
    extends PanelSection
{
    Label countLabel;
    CustomProgressBar progressBar;
    int totalCases   = 0;
    int doneCount    = 0;
    String titleBase = "Test run status";

    public ProgressSection(Composite comp, int topOffset, int bottomOffset)
    {
        super(comp, "", topOffset, bottomOffset);

        int nudge = (UIUtils.isLinux() ? 2 : 0);

        FormData fd_message = new FormData();
        fd_message.top = new FormAttachment(comp, topOffset + 21 + nudge);
        fd_message.left = new FormAttachment(0, 15);
        fd_message.right = new FormAttachment(0, 110);
        message.setLayoutData(fd_message);

        countLabel = new Label(comp.getShell(), SWT.RIGHT);
        FormData fd_countLabel = new FormData();
        fd_countLabel.top = new FormAttachment(comp, topOffset + 21 + nudge);
        fd_countLabel.right = new FormAttachment(100, -15);
        fd_countLabel.left = new FormAttachment(100, -110);
        countLabel.setLayoutData(fd_countLabel);
        countLabel.moveAbove(this);

        progressBar = new CustomProgressBar(comp.getShell(), SWT.HORIZONTAL);
        FormData fd_progressBar = new FormData();
        fd_progressBar.top = new FormAttachment(comp, topOffset + 22 + nudge);
        fd_progressBar.bottom = new FormAttachment(100, -15);
        fd_progressBar.left = new FormAttachment(0, 110);
        fd_progressBar.right = new FormAttachment(100, -110);
        progressBar.setLayoutData(fd_progressBar);
        progressBar.resetSteps();
        if (!UIUtils.isMacOSX()) progressBar.moveAbove(this);

        // Default values -- this should be reset immediately by caller.

        this.setText(titleBase);
        message.setText(RunStatus.NotStarted.getTextName());
        countLabel.setText("");
    }
        

    @Override
    public void setMessageFont(Font font)
    {
        message.setFont(font);
        countLabel.setFont(font);
    }


    @Override
    public void setMessageTextColor(Color color)
    {
        message.setForeground(color);
        countLabel.setForeground(color);
    }


    public void setSelectedCount(int count)
    {
        if (count >= 0)
            this.setText(titleBase + ": " + count + " selected");
        else
            this.setText(titleBase);
    }


    public void setMaxCount(int total)
    {
        totalCases = total;
        updateDoneCountLabel();
        progressBar.setMaxSteps(total);
        forceRefresh();
    }


    public int getDoneCount()
    {
        return doneCount;
    }


    public void setDoneCount(int count)
    {
        doneCount = count;
        updateDoneCountLabel();
        progressBar.updateProgressSteps(count);
        forceRefresh();
    }


    public void incrementDoneCount()
    {
        // WARNING: don't call an SWT .update() method here for countLabel or
        // the progress bar; it dramatically slows down GUI updates.
        // Currently, this method is called inside the QueuedTestRunner, and
        // calling .update()'s every time causes the Test Runner GUI elements
        // such as the Tree object to badly lag behind the run progress.

        doneCount++;
        updateDoneCountLabel();
        progressBar.updateProgressStep();
    }


    public void setStatus(RunStatus status)
    {
        message.setText(status.getTextName());
    }


    public void forceRefresh()
    {
        countLabel.update();
        progressBar.update();
    }


    private void updateDoneCountLabel()
    {
        // This purposefully does not call .update() on countLabel, because
        // this method is called from incrementDoneCount();

        if (totalCases > 0)
            countLabel.setText(doneCount + "/" + totalCases);
        else
            countLabel.setText("");
    }
}

