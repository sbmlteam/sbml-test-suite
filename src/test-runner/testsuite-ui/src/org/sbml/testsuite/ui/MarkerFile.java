//
// @file   MarkerFile.java
// @brief  Class to handle a marker file we leave in the output directory
// @author Michael Hucka
// @date  Created 2013-03-01 <mhucka@caltech.edu>
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MarkerFile
{
    private final static String MARKER_FILE = ".sbml-test-suite.txt";


    public static void write(String dir, String program)
    {
        if (dir == null) return;

        File path = new File(dir);
        if (! path.exists() || ! path.isDirectory() || ! path.canWrite())
            return;

        try
        {
            FileWriter fw = new FileWriter(dir + File.separator + MARKER_FILE);
            fw.write(program, 0, program.length());
            fw.flush();
            fw.close();
        }
        catch (IOException e)
        {
            // FIXME
        }

    }
    

    public static boolean exists(String dir)
    {
        File markerFile = createMarkerFilePath(dir);
        return markerFile.exists() && markerFile.canRead();
    }


    public static boolean exists(File dir)
    {
        if (dir != null)
            return exists(dir.getPath());
        else
            return false;
    }


    public static String getContents(String dir)
    {
        if (dir == null) return null;

        try
        {
            Scanner fileReader = new Scanner(createMarkerFilePath(dir));
            final String nextLine = fileReader.nextLine();
            fileReader.close();
            return nextLine;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static String getContents(File dir)
    {
        if (dir != null)
            return getContents(dir.getPath());
        else
            return null;
    }


    public static void remove(String dir)
    {
        if (dir != null)
            try
            {
                File markerFile = createMarkerFilePath(dir);
                markerFile.delete();
            }
            catch (Exception e)
            {
                // FIXME log the failure, but go on anyway.
            }
    }


    public static void remove(File dir)
    {
        remove(dir.getPath());
    }


    private static File createMarkerFilePath(String dir)
    {
        return new File(dir + File.separator + MARKER_FILE);
    }
}
