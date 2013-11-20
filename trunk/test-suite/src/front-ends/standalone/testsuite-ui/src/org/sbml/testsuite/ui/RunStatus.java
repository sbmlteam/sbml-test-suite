//
// @file   RunState.java
// @brief  Class to encapsulate the success/failure of running the wrapper
// @author Michael Hucka
// @@date  Created 2013-01-26 <mhucka@caltech.edu>
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


public enum RunStatus
{
    NotStarted  ("Not started"),
    NotRunnable ("Not runnable"),
    Running     ("Running"),
    Paused      ("Paused"),
    Done        ("Done");

    private final String text;

    RunStatus(String textName)
    {
        this.text = textName;
    }

    public String getTextName()
    {
        return this.text;
    }

}
