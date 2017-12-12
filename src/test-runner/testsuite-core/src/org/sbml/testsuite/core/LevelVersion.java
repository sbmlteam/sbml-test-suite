//
// @file   LevelVersion.java
// @brief  Class to encapsulate an SBML Level+Version combination
// @author Michael Hucka
// @@date  Created 2013-02-26 <mhucka@caltech.edu>
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

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Persister;

/**
 * Holds an SBML Level/Version combination.
 */
@Default
public class LevelVersion
{
    private int    level;
    private int    version;
    private String text;

    public LevelVersion()
    {
        this.level = 0;
        this.version = 0;
        this.text = new String("Highest Level+Version");
    }

    public LevelVersion(int level, int version)
    {
        this.level = level;
        this.version = version;
        this.text = new String("SBML Level " + level + " Version " + version);
    }

    public int     getLevel()   { return level; }
    public int     getVersion() { return version; }
    public String  toString()   { return text; }
    public boolean isHighest()  { return level == 0; }
    public void    setHighest() { level = 0; }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof LevelVersion)) return false;

        LevelVersion other = (LevelVersion) obj;
        return (level == other.getLevel() && version == other.getVersion());
    }
}
