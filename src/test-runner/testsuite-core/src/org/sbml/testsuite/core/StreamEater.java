//
// @file   StreamEater.java
// @brief  Helper class to consume output from background processes
// @author Michael Hucka
// @date   2013-03-01 <mhucka@caltech.edu>
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

package org.sbml.testsuite.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


/**
 * Class to eat the output from a process.
 * 
 * I implemented a version of this for an older version of the SBML Test
 * Runner.  The version below is modified from that, and also borrows ideas 
 * from http://www.javaworld.com/jw-12-2000/jw-1229-traps.html?page=4
 */
public class StreamEater
    extends Thread
{
    private InputStream stream;
    private String record;

    public StreamEater(InputStream stream)
    {
        this.stream     = stream;
        this.record     = new String();
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                record += line + "\n";
        }
        catch (IOException e)
        {
            // FIXME
        }
    }

    public String getOutput()
    {
        return record;
    }
}
