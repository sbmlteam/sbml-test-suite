//
// @file   PanelSection.java
// @brief  Common code related to sections of the main display
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;

/*
 * Strictly speaking, Group is documented in the SWT API docs as being
 * something that is not intended to be subclassed.  However, I believe
 * the intention behind that is to avoid distinctions in widgets.  Here,
 * the purpose is to encapsulate our functionality in a convenient manner.
 * We don't reply on anything except standard API calls.  
 */

public class PanelSection
    extends Group
{
    protected StyledText message;

    public PanelSection(Composite comp, String title, 
                        int topOffset, int bottomOffset)
    {
        super(comp.getShell(), SWT.SHADOW_ETCHED_IN);

        this.setText(title);
        int hOffset = 20 - UIUtils.scaledFontSize(20);
        FormData fd_group = new FormData();
        fd_group.top = new FormAttachment(comp, topOffset);
        fd_group.left = new FormAttachment(0, 4 + hOffset/2);
        fd_group.right = new FormAttachment(100, -4 - hOffset/2);
        fd_group.bottom = new FormAttachment(100, bottomOffset);
        this.setLayoutData(fd_group);

        //        message = new Label(comp.getShell(), SWT.WRAP);
        message = new StyledText(comp.getShell(), SWT.WRAP);
        message.addKeyListener(UIUtils.createCloseKeyListener(comp.getShell()));        
        message.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        message.setEditable(false);
        if (! UIUtils.isMacOSX())
            message.moveAbove(this);
    }

        
    public void setMessageFont(Font font)
    {
        message.setFont(font);
    }


    public void setMessageTextColor(Color color)
    {
        message.setForeground(color);
    }


    public void setMessage(String text)
    {
        message.setLineAlignment(0, 1, SWT.LEFT);
        message.setText(text);
    }


    public void setMessageCentered(String text)
    {
        message.setText(text);
        message.setLineAlignment(0, 1, SWT.CENTER);
    }


    @Override
    protected void checkSubclass()
    {
        // Disable the check that prevents subclassing of SWT components.
    }
}
