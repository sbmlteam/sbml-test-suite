//
// @file    AskUser.java
// @brief   Simple dialogs for user queries.
// @author  Michael Hucka
// @date    Created 2012-12-19 <mhucka@caltech.edu>
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import org.mihalis.opal.opalDialog.Dialog;
import org.mihalis.opal.opalDialog.MessageArea;
import org.mihalis.opal.opalDialog.FooterArea;
import org.mihalis.opal.opalDialog.Dialog.OpalDialogType;
import org.mihalis.opal.utils.ResourceManager;


public class AskUser 
{
    static public boolean confirm(final Shell shell, final String question)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Confirmation");
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
        msgArea.setText(question);
        footerArea.setDefaultButtonIndex(1);
        return dialog.show() == 0;
    }


    static public boolean inform(final Shell shell, final String info)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle(ResourceManager.getLabel(ResourceManager.INFORMATION));
        dialog.setButtonType(OpalDialogType.CLOSE);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION));
        msgArea.setText(info);
        return dialog.show() == 0;
    }

    
    static public boolean informWithOverride(final Shell shell,
                                             final String info)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle(ResourceManager.getLabel(ResourceManager.INFORMATION));
        dialog.setButtonType(OpalDialogType.CLOSE);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION));
        msgArea.setText(info);
        footerArea.addCheckBox("Proceed anyway", false);
        dialog.show();
        return footerArea.getCheckBoxValue();
    }

    
    static public boolean saveCancel(final Shell shell, final String question)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Save");
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_QUESTION));
        msgArea.setText(question);
        footerArea.setDefaultButtonIndex(1);
        return dialog.show() == 0;
    }
}
