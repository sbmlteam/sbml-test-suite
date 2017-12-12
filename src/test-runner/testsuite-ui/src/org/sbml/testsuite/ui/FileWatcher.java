//
// @file   FileWatcher.java
// @brief  Interface class for file modification watchers
// @author Michael Hucka
// @date   Created 2017-11-16 <mhucka@caltech.edu>
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

import java.nio.file.Path;
import java.io.File;

public interface FileWatcher
{
    public void initWatch(Path path);
    public void initWatch(File path);
    public void initWatch(String path);

    public void addListener(Path file, FileListener listener);
    public void addListener(File file, FileListener listener);
    public void addListener(String file, FileListener listener);

    public void removeListener(Path file);
    public void removeListener(File file);
    public void removeListener(String file);

    public void clearPaths();
}
