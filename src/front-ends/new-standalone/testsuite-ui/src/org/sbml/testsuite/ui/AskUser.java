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

public class AskUser 
{

    static public boolean yesNo(final String question, final String title,
                                final Shell shell)
    {
        final MessageBox messageBox
            = new MessageBox(shell, SWT.ICON_QUESTION | SWT.NO | SWT.YES);
        messageBox.setMessage(question);
        messageBox.setText(title);
        boolean response = (messageBox.open() == SWT.YES);
        return response;
    }
    

    static public boolean saveCancel(final String question, final String title,
                                     final Shell shell)
    {
        final MessageBox messageBox
            = new MessageBox(shell, SWT.ICON_QUESTION | SWT.NO | SWT.YES);
        messageBox.setMessage(question);
        messageBox.setText(title);
        boolean response = (messageBox.open() == SWT.YES);
        return response;
    }
    

    /**
     * Main for testing only.
     * 
     * @param args
     */
    public static void main(String[] argv) 
    {
        final Display display = new Display();
        final Shell shell = new Shell(display);        
        
        boolean r = yesNo("My question?", "This is the dialog title", shell);
        yesNo("The answer was " + (r ? "yes" : "no"), "Title", shell);

        r = saveCancel("save?", "This is the dialog title", shell);
        yesNo("The answer was " + (r ? "yes" : "no"), "Title", shell);
    }

}
