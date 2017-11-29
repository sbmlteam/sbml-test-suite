//
// @file   Program.java
// @brief  Starts the main form
// @author Frank T. Bergmann
// @author Michael Hucka
// @date   Created 2012-06-06 <fbergman@caltech.edu>
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


/**
 * Starts the Main Window
 */
public class Program
{
    static {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                           "SBML Test Runner");
        System.setProperty("apple.awt.application.name",
                           "SBML Test Runner");
    }

    /**
     * Main entry point for the test runner
     * @param args command line arguments
     */
    public final static void main(String[] args)
    {
        try
        {
            if (UIUtils.isWindows())
                RegisterWindowsID.register();

            final MainWindow window = new MainWindow();
            window.open();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        window.shutdown();
                    }
                }));
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


    /**
     * Returns the version number as an array of 3 integers.
     * <p>
     * This gets the information from the manifest file in the application
     * JAR file.  It will fail to get a number if you are not running this
     * from the JAR file (e.g., if you are running this from within Eclipse).
     * The number is the string version turned into a 3-part array, [X, Y, Z],
     * where X is the major number, Y is the minor number, etc.
     * <p>
     * @return an array of int
     */
    public static int[] getVersionNumbers()
    {
        if (Program.class.getPackage().getImplementationVersion() == null)
            return null;
        else
        {
            try
            {
                String v = Program.class.getPackage().getImplementationVersion();
                String[] parts = v.split("[_.]");
                int[] num = new int[3];
                num[0] = Integer.parseInt(parts[0]);
                num[1] = Integer.parseInt(parts[1]);
                num[2] = Integer.parseInt(parts[2]);
                return num;
            }
            catch (Exception e)
            {
                return null;
            }
        }

    }
}
