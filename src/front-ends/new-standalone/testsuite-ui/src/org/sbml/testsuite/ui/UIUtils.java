//
// @file   UIUtils.java
// @brief  Utilities for the package org.sbml.testsuite.ui
// @author Michael Hucka
// @date   2013-01-07 <mhucka@caltech.edu>
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
//

package org.sbml.testsuite.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


/**
 * Utility class for org.sbml.testsuite.ui
 */
public class UIUtils
{
    /**
     * Returns true if the key press involved a modifier key.
     * 
     * This handles platform-specific combination, such as using control
     * on Windows vs Command on Mac OS X.
     *
     * @return true if the key had a modifier set, false otherwise.
     */
    public static boolean isModifierKey(KeyEvent e)
    {
        // SWT.MOD1 is supposed to be set to SWT.CONTROL on Windows and to
        // SWT.COMMAND on the Macintosh.
        return (e.stateMask & SWT.MOD1) == SWT.MOD1;
    }


    public static void addCloseKeyListener(Control control, final Shell shell)
    {
        control.addKeyListener(new KeyListener() {
                public void keyReleased(KeyEvent e) { return; }
                public void keyPressed(KeyEvent e) 
                {
                    if (isModifierKey(e) && e.keyCode == 'w')
                        shell.close();      // Will invoke shell close() listener.
                }
            });
    }


    public static void addTraverseKeyListener(Control control, final Shell shell)
    {
        control.addListener(SWT.Traverse, new Listener() {
                public void handleEvent (final Event event) {
                    if (event.detail == SWT.TRAVERSE_ESCAPE) {
                        shell.close();
                    }
                }
            });
    }
}
