//
// @file    Tell.java
// @brief   Simple dialogs for user queries.
// @author  Michael Hucka
// @date    Created 2012-12-19 <mhucka@caltech.edu>
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.opalDialog.Dialog;
import org.mihalis.opal.opalDialog.Dialog.OpalDialogType;
import org.mihalis.opal.opalDialog.FooterArea;
import org.mihalis.opal.opalDialog.MessageArea;
import org.mihalis.opal.utils.ResourceManager;


public class Tell 
{
    private static Image questionIcon;
    private static Image warningIcon;
    private static Image infoIcon;
    private static Image errorIcon;

    static
    {
        // The SWT code for Display.getSystemImage() returns the same icons
        // for ICON_INFORMATION and ICON_QUESTION.  (I looked at the Java
        // code for SWT 4.2 -- it's hard-wired that way, perhaps because of
        // Mac OS X requirements.) IMHO, that's confusing.  I made our own
        // icon, based on ICON_INFORMATION.

        if (UIUtils.isMacOSX())
            questionIcon = UIUtils.getImageResource("icon_question.png");
        else
            questionIcon = Display.getCurrent().getSystemImage(SWT.ICON_QUESTION);

        warningIcon = Display.getCurrent().getSystemImage(SWT.ICON_WARNING);
        infoIcon    = Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION);
        errorIcon   = Display.getCurrent().getSystemImage(SWT.ICON_ERROR);
    }


    static public boolean confirm(final Shell shell, final String question)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Confirmation");
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(warningIcon);
        msgArea.setText(question);
        footerArea.setDefaultButtonIndex(1);
        return dialog.show() == 0;
    }


    static public boolean confirmWithDetails(final Shell shell,
                                             final String question,
                                             final String details)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Confirmation");
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(warningIcon);
        msgArea.setText(question);
        footerArea.setDefaultButtonIndex(1);
        if (details != null && details.length() > 0)
            footerArea.setDetailText(details);
        return dialog.show() == 0;
    }


    static public boolean saveCancel(final Shell shell, final String question)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Save");
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(questionIcon);
        msgArea.setText(question);
        footerArea.setDefaultButtonIndex(1);
        return dialog.show() == 0;
    }


    static public boolean inform(final Shell shell, final String info)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();

        dialog.setTitle(ResourceManager.getLabel(ResourceManager.INFORMATION));
        dialog.setButtonType(OpalDialogType.CLOSE);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(infoIcon);
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
        msgArea.setIcon(infoIcon);
        msgArea.setText(info);
        footerArea.addCheckBox("Proceed anyway", false);
        dialog.show();
        return footerArea.getCheckBoxValue();
    }


    static public boolean warn(final Shell shell, final String info)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();

        dialog.setTitle(ResourceManager.getLabel(ResourceManager.WARNING));
        dialog.setButtonType(OpalDialogType.CLOSE);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(warningIcon);
        msgArea.setText(info);
        return dialog.show() == 0;
    }


    static public boolean error(final Shell shell, final String msg,
                                final String details)
    {
        Dialog dialog         = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();
        FooterArea footerArea = dialog.getFooterArea();

        dialog.setTitle("Error");
        dialog.setButtonType(OpalDialogType.CLOSE);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(errorIcon);
        msgArea.setText(msg);
        if (details != null && details.length() > 0)
            footerArea.setDetailText(details);
        return dialog.show() == 0;
    }


    static public String simpleQuery(final Shell shell, final String title)
    {
        Dialog dialog = new Dialog(shell);
        MessageArea msgArea   = dialog.getMessageArea();

        dialog.setTitle(title);
        dialog.setButtonType(OpalDialogType.OK_CANCEL);
        dialog.setCenterPolicy(Dialog.CenterOption.CENTER_ON_DIALOG);
        msgArea.setIcon(questionIcon);
        msgArea.addTextBox("");

        // Add keyboard bindings for cancelling out of this: command-. on
        // Macs and control-w elsewhere.  (This actually will make command-w
        // do the same thing on Macs, but that's okay.)

        final Shell dialogShell = dialog.getShell();
        final Display display = shell.getDisplay();
        final Listener closeKeyListener = new Listener() {
            @Override
            public void handleEvent (final Event event)
            {
                if (UIUtils.isModifier(event)
                    && ((UIUtils.isMacOSX() && event.keyCode == '.')
                        || event.keyCode == 'w'))
                    dialogShell.close();
            }
        };
        display.addFilter(SWT.KeyDown, closeKeyListener);
        dialogShell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent notUsed)
            {
                display.removeFilter(SWT.KeyDown, closeKeyListener);
            }
        });

        if (dialog.show() == 0)
            return dialog.getMessageArea().getTextBoxValue();
        else
            return null;
    }
}
