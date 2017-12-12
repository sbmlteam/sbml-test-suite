//
// @file   WrapperProblem.java
// @brief  Class to encapsulate common failure situations
// @author Michael Hucka
// @@date  Created 2017-10-27 <mhucka@caltech.edu>
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

public enum WrapperProblem
{
    noProblem            ("The wrapper program appears ready for use"),
    noWrapperGiven       ("The wrapper program has not been specified"),
    noSuchFile           ("The specified program does not exist"),
    noSuchDirectory      ("The output directory does not exist"),
    cannotReadWrapper    ("The wrapper program is unreadable"),
    cannotExecuteWrapper ("The wrapper program is not executable"),
    cannotWriteDirectory ("The output directory is not writable to the Test Runner"),
    unknownError         ("A problem exists with the wrapper");

    private final String description;

    WrapperProblem(String description)
    {
        this.description = description;
    }

    public String getDescription() { return description; }
}
