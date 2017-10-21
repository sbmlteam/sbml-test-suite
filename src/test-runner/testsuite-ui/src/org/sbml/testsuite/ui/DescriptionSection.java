//
// @file   DescriptionSection.java
// @brief  The "test case description" section of the main window
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

import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.sbml.testsuite.core.TestCase;


public class DescriptionSection
    extends PanelSection
{
    public DescriptionSection(Composite comp, int topOffset, int bottomOffset)
    {
        super(comp, "Test case description", topOffset, bottomOffset);

        int nudge = (UIUtils.isLinux() ? 2 : 0);

        FormData fd_message = new FormData();
        fd_message.top = new FormAttachment(comp, 27 + nudge);
        fd_message.left = new FormAttachment(0, 15);
        fd_message.bottom = new FormAttachment(100, -70);
        fd_message.right = new FormAttachment(100, -15);
        message.setLayoutData(fd_message);
    }

    public void setMessage(TestCase testCase)
    {
        Vector<String> lvVector = testCase.getSupportedVersions();
        String supported = new String();

        for (int i = 0; i < lvVector.size(); i++)
        {
            if (i > 0) supported += ", ";
            String lv = lvVector.get(i);
            supported += "L" + lv.charAt(0) + "V" + lv.charAt(2);
        }

        String modelSummary = "Model Summary";
        String sbmlLV       = "SBML Levels/Versions";
        String testType     = "Test Type";
        String compTags     = "Component Tags";
        String testTags     = "Test Tags";

        // FIXME this is very specific to FBC and time series, and should
        // be generalized to handle other types in the future.
        String testTypeValue;
        if ("FluxBalanceSteadyState".equals(testCase.getTestType()))
            testTypeValue = "Steady state";
        else
            testTypeValue = "Time course";

        String msg = modelSummary + ": " + testCase.getSynopsis()
            +     "\n" + sbmlLV   + ": " + supported + "."
            +     "\n" + testType + ": " + testTypeValue + "."
            +     "\n" + compTags + ": " + testCase.getComponentTagsString() + "."
            +     "\n" + testTags + ": " + testCase.getTestTagsString() + ".";

        setMessage(msg);

        StyleRange modelSummaryStyle = new StyleRange();
        StyleRange sbmlLVStyle       = new StyleRange();
        StyleRange testTypeStyle     = new StyleRange();
        StyleRange compTagsStyle     = new StyleRange();
        StyleRange testTagsStyle     = new StyleRange();

        Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);

        modelSummaryStyle.start  = msg.indexOf(modelSummary);
        modelSummaryStyle.length = modelSummary.length();
        modelSummaryStyle.fontStyle = SWT.BOLD;
        modelSummaryStyle.foreground = black;

        sbmlLVStyle.start  = msg.indexOf(sbmlLV);
        sbmlLVStyle.length = sbmlLV.length();
        sbmlLVStyle.fontStyle = SWT.BOLD;
        sbmlLVStyle.foreground = black;

        testTypeStyle.start  = msg.indexOf(testType);
        testTypeStyle.length = testType.length();
        testTypeStyle.fontStyle = SWT.BOLD;
        testTypeStyle.foreground = black;

        compTagsStyle.start  = msg.indexOf(compTags);
        compTagsStyle.length = compTags.length();
        compTagsStyle.fontStyle = SWT.BOLD;
        compTagsStyle.foreground = black;

        testTagsStyle.start  = msg.indexOf(testTags);
        testTagsStyle.length = testTags.length();
        testTagsStyle.fontStyle = SWT.BOLD;
        testTagsStyle.foreground = black;

        message.setStyleRange(modelSummaryStyle);
        message.setStyleRange(sbmlLVStyle);
        message.setStyleRange(testTypeStyle);
        message.setStyleRange(compTagsStyle);
        message.setStyleRange(testTagsStyle);
    }
}
