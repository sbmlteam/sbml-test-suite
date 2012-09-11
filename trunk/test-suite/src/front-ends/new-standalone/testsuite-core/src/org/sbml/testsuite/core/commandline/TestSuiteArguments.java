//
// @file TestSuiteArguments.java
// @brief TestSuiteArguments is a parser for command line arguments (unused)
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

package org.sbml.testsuite.core.commandline;

import java.io.PrintStream;

/**
 * TestSuiteArguments is a parser for command line arguments (unused)
 */
public class TestSuiteArguments
{
    /**
     * Constructor parsing arguments
     * 
     * @param args
     *            the arguments
     */
    public TestSuiteArguments(String args[])
    {

    }


    /**
     * @return flag indicating whether valid arguments were provided
     */
    public boolean isValid()
    {
        return false;
    }


    /**
     * prints the usage information.
     * 
     * @param stream
     *            the stream to print to
     */
    public void printArguments(PrintStream stream)
    {
        printArguments(stream, null);
    }


    /**
     * prints the usage information.
     * 
     * @param stream
     *            the stream to print to
     * @param message
     *            an optional message
     */
    public void printArguments(PrintStream stream, String message)
    {
        stream.println("SBML TestSuite (core)");
        stream.println("=====================");
        stream.println();
        if (message == null || message.length() == 0)
        {
            stream.println(message);
            stream.println();
        }
        stream.println("Usage: ");
    }
}
