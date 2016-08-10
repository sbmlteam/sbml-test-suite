//
// @file    UserTestCase.java
// @brief   Store a user's uploaded results for one case
// @author  Michael Hucka
// @date    Created 2010-02-26 <mhucka@caltech.edu>
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2010-2015 jointly by the following organizations: 
//     1. California Institute of Technology, Pasadena, CA, USA
//     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
//     3. University of Heidelberg, Heidelberg, Germany
//
// Copyright (C) 2008-2009 California Institute of Technology (USA).
//
// Copyright (C) 2004-2007 jointly by the following organizations:
//     1. California Institute of Technology (USA) and
//     2. University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available on the Web at
// http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import java.io.*;
import java.math.*;
import java.util.*;
import java.applet.*;

public class UserTestCase
    extends TestCase
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public UserTestCase(File refCasesDir, File userCasesDir, String caseName)
        throws IOException, Exception
    {
        super(refCasesDir, caseName);

        if (userCasesDir == null)
            throw new IOException("Null parameter 'userCasesDir'.");

        this.userCasesDir = userCasesDir;
        this.userDataFile = pathToDataFile(userCasesDir, caseName);

        // Do some sanity checking

        if (! userDataFile.exists())
            throw new Exception("Nonexistent user data file "
                                + userDataFile.getName() + ".");

        if (! userDataFile.canRead())
            throw new Exception("Unreadable user data file: "
                                + userDataFile.getName() + ".");
    }

    public UserTestCase(String refCasesDir, String userCasesDir, String caseName)
        throws IOException, Exception
    {
        this(new File(refCasesDir), new File(userCasesDir), caseName);
    }

    static public File pathToDataFile(File userCasesDir, String caseName)
    {
        return new File(userCasesDir, caseName + "-results.csv");
    }

    public File   getUserDataFile()      { return userDataFile; }
    public String getUserDataFileName()  { return userDataFile.getPath(); }

    public double[][] getUserData()
        throws Exception
    {
        if (userData == null)
            userData = parseDataFile(userDataFile, getTestNumRows(),
                                     getTestNumVars(), true);
        return userData;
    }


    // 
    // -------------------------- Private variables --------------------------- 
    // 

    private File userCasesDir;
    private File userDataFile;
    private double[][] userData;

} // end of class
