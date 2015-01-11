//
// @file    TagBits.java
// @brief   Extension of BitSet for working with STS tags
// @author  Michael Hucka
// @date    Created 2011-04-04 <mhucka@caltech.edu>
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

import java.util.*;


public class TagBits
    extends BitSet
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public TagBits(Vector<String> tags, Vector<String> allPossibleTags)
        throws Exception
    {
        // Get the index of each tag in the list of all tags, and set its bit.
        for (String tag : tags)
        {
            if (allPossibleTags.indexOf(tag) == -1)
                throw new Exception("-1 for " + tag);

            set(allPossibleTags.indexOf(tag));
        }
    }


    public TagBits(String[] tags, Vector<String> allPossibleTags)
    {
        for (int i = 0; i < tags.length; i++)
            set(allPossibleTags.indexOf(tags[i]));
    }


    public TagBits(String tag, Vector<String> allPossibleTags)
    {
        set(allPossibleTags.indexOf(tag));
    }


    public TagBits(Vector<String> tags, String[] allPossibleTags)
    {
        for (String tag : tags)
            for (int i = 0; i < allPossibleTags.length; i++)
                if (tag.equals(allPossibleTags[i]))
                    set(i);
    }


    public TagBits(String[] tags, String[] allPossibleTags)
    {
        for (int i = 0; i < allPossibleTags.length; i++)
            for (int j = 0; j < tags.length; j++)
                if (tags[j].equals(allPossibleTags[i]))
                    set(i);
    }


    /**
     * This is similar to BitSet's intersects(), except that instead of
     * returning true if any bit is set in common between the two objects,
     * this one returns true only if all the set-bits in the given BitSet
     * are also set in this one.
     *
     * For the life of me, I can't understand why Java's BitSet doesn't
     * have an equivalent to this method.
     */
    public boolean contains(BitSet other)
    {
        for (int i = other.length(); i-- > 0;)
            if ((get(i) & other.get(i)) != other.get(i))
                return false;
        return true;
    }


    public final long longValue()
    {
        return longValue(64);
    }


    /**
     * Optimization possible by knowing how many bits are being actively used,
     * so that the calculation doesn't have to iterate over all 64 positions.
     */
    public final long longValue(int numBits)
    {
        long accumulator = 0;
        for (int i = numBits; i-- > 0;)
            if (get(i))
                accumulator += (long) 1 << (long) i;
        return accumulator;
    }
}
