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
// Copyright (C) 2010-2011 jointly by the following organizations: 
//     1. California Institute of Technology, Pasadena, CA, USA
//     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
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
    {
        this.tagStrings = tags;
        this.numBits    = allTags.size();
        this.tagBits    = new TagBits(tags, allTags);
        this.tagCode    = tagBits.longValue(numBits);
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

    public Vector<String> getCaseTags()
    {
        return this.tagStrings;
    };
    
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

    private Vector<String> levelsAndVersions = new Vector<String>();
    private Vector<String> tagStrings = new Vector<String>();
    private TagBits tagBits;
    private long tagCode;
    private String caseName;
    private int caseNum;
    private int numBits;

}// end of class
