//
// @file   RegisterWindowsID.java
// @brief  Register the app in a way that allows Windows taskbar pinning
// @author Michael Hucka
// @date   Created 2013-05-03 <mhucka@caltech.edu>
//
// ----------------------------------------------------------------------------
// This code is based on the following answer to a StackOverflow question:
// http://stackoverflow.com/a/1928830/743730
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

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;

public class RegisterWindowsID
{
    public static void register()
    {
        final String appID = "SBML Test Runner";
        
        // FIXME: the next call can return an error code. Should log it.
        if (SetCurrentProcessExplicitAppUserModelID(new WString(appID)).longValue() != 0)
            System.out.println("unable to set");

        /* Testing the value.

        final PointerByReference r = new PointerByReference();
        if  (GetCurrentProcessExplicitAppUserModelID(r).longValue() == 0)
        {
            final Pointer p = r.getValue();
            System.out.println(p.getString(0, true)); // here we leak native memory by lazyness
        }      
        else
            System.out.println("No value");
        */
    }

    private static native NativeLong GetCurrentProcessExplicitAppUserModelID(PointerByReference appID);
    private static native NativeLong SetCurrentProcessExplicitAppUserModelID(WString appID);

    static
    {
        Native.register("shell32");
    }
}
