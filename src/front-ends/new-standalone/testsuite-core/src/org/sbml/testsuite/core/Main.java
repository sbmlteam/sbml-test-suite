//
// @file Main.java
// @brief Command line interface to the core api (unused)
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
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

package org.sbml.testsuite.core;

import org.sbml.testsuite.core.commandline.TestSuiteArguments;

/**
 * Unused command line interface for the core api.
 */
public class Main
{

    /**
     * the test suite, a specific
     * test case, modify cached test suite parameters or even run test cases.
     * 
     * @param rawArgs
     *            the command line arguments
     */
    public static void main(String[] rawArgs)
    {
        TestSuiteArguments args = new TestSuiteArguments(rawArgs);
        if (args.isValid())
        {
            args.printArguments(System.err);
            System.exit(1);
        }
    }

}
