//
// @file    CaseSummary.java
// @brief   
// @author  Michael Hucka
// @date    2011-09-11 <mhucka@caltech.edu>
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

import java.math.*;
import java.util.*;


public class CaseSummary
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public CaseSummary(String caseName)
    {
        this.caseName = caseName;
        this.caseNum  = Integer.parseInt(caseName);
    }

    public String getCaseName() { return caseName; }
    public int    getCaseNum()  { return caseNum; }

    public void setLevelsAndVersions(Vector<String> levelsAndVersions)
    {
        this.levelsAndVersions.addAll(levelsAndVersions);
    }

    public void setLevelsAndVersions(String[] levelsAndVersions)
    {
        for (String lv : levelsAndVersions)
            this.levelsAndVersions.add(lv);
    }

    public boolean hasLevelAndVersion(String lv)
    {
        return levelsAndVersions.contains(lv);
    }

    public void setTags(Vector<String> tags, Vector<String> allTags)
        throws Exception
    {
        this.tagStrings = tags;
        this.numTagBits = allTags.size();
        this.tagBits    = new TagBits(tags, allTags);
        this.tagCode    = tagBits.longValue(numTagBits);
    }

    public void setPackages(HashSet<String> packages)
        throws Exception
    {
        this.packages = packages;
    }

    public boolean caseHasTag(String tag)
    {
        return tagStrings.contains(tag);
    }

    public boolean caseHasTags(String[] tags)
    {
        for (String tag : tags)
            if (! tagStrings.contains(tag))
                return false;
        return true;
    }

    public boolean caseHasPackage(String pkg)
    {
        return packages.contains(pkg);
    }

    public boolean caseHasPackages(String[] pkgs)
    {
        for (String p : pkgs)
            if (! packages.contains(p))
                return false;
        return true;
    }

    public Vector<String> getCaseTags()
    {
        return this.tagStrings;
    }
    
    public HashSet<String> getCasePackages()
    {
        return this.packages;
    }

    public TagBits getTagBits()
    {
        return tagBits;
    }

    public long getTagCode()
    {
        return tagCode;
    }
    
    // 
    // -------------------------- Private variables ---------------------------
    // 

    private String caseName;
    private int caseNum;
    private Vector<String> levelsAndVersions = new Vector<String>();
    private Vector<String> tagStrings        = new Vector<String>();
    private HashSet<String> packages         = new HashSet<String>();
    private TagBits tagBits;
    private long tagCode;
    private int numTagBits;

}// end of class
