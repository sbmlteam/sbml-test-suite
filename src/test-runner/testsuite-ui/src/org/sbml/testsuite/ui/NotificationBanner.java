//
// @file   NotificationBanner.java
// @brief  Banner used to show notifications such as when filters are in effect
// @author Michael Hucka
// @@date  Created 2013-04-01 <mhucka@caltech.edu>
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
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Shell;


public class NotificationBanner
    extends CLabel
{
    private FormAttachment bottomAttachment = new FormAttachment(0, 0);
    private int            initialBottomOffset = 0;

    public NotificationBanner(Shell shell, int style, int topOffset)
    {
        super(shell, style);

        initialBottomOffset = topOffset;
        bottomAttachment.offset = topOffset;

        FormData formData = new FormData();
        formData.top = new FormAttachment(0, topOffset);
        formData.bottom = bottomAttachment;
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        setLayoutData(formData);
    }


    public void show(boolean doShow)
    {
        // Return if we're already showing the banner.
        if (doShow == (bottomAttachment.offset != initialBottomOffset))
            return;

        if (doShow)
        {
            FontData[] fontData = getFont().getFontData();
            int tallest = 0;
            for (int i = 0; i < fontData.length; ++i)
                if (fontData[i].getHeight() > tallest)
                    tallest = fontData[i].getHeight();
            tallest += 6;
            bottomAttachment.offset += tallest;
            setVisible(true);
        }
        else
        {
            bottomAttachment.offset = initialBottomOffset;
            setVisible(false);
        }

        computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        getShell().layout();
    }
}
