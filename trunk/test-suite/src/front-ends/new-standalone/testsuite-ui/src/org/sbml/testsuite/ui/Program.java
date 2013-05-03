//
// @file   Program.java
// @brief  Starts the main form
// @author Frank T. Bergmann
// @author Michael Hucka
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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


/**
 * Starts the Main Window
 */
public class Program
{

    /**
     * Main entry point for the test runner
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            if (UIUtils.isWindows())
                RegisterWindowsID.register();

            MainWindow window = new MainWindow();
            window.open();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Returns the version number for this application.
     * <p>
     * This gets the information from the manifest file in the application
     * JAR file.  It will fail to get a number if you are not running this
     * from the JAR file (e.g., if you are running this from within Eclipse).
     * <p>
     * @return a string containing the version number.
     */
    public static String getVersion()
    {
        if (Program.class.getPackage().getImplementationVersion() == null)
            return "unset";
        else
            return Program.class.getPackage().getImplementationVersion();
    }

}
