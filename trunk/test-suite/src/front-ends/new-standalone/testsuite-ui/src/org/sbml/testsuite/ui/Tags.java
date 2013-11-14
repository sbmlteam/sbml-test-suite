//
// @file   TagDescription.java
// @brief  Class encapsulating descriptions of test & component tags
// @author Michael Hucka
// @date   2013-11-12 <mhucka@caltech.edu>
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

import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class Tags
{
    private static final String TAGS_FILE = "tags.txt";
    private static TreeMap<String, String> tags;


    static public Set<String> getAllTags()
    {
        if (tags == null) readTagsDescriptions();
        if (tags.isEmpty())
            return null;
        else
            return tags.keySet();
    }


    static public String getTagDescription(String tag)
    {
        if (tags == null) readTagsDescriptions();
        for (Map.Entry<String, String> entry : tags.entrySet())
        {
            if (entry.getKey().equals(tag))
                return entry.getValue();
        }
        return null;
    }


    /**
     * Reads the file of tags and their descriptions.
     * <p>
     * Each line of the file is assumed to have the following format:
     * <pre>
     *   tag: description
     * </pre>
     * where "tag" is the name of tag (e.g., "MultiCompartment").
     */
    static private void readTagsDescriptions()
    {
        // This initialization method only ever gets called once.  If we fail,
        // we will leave an empty value for "tags".  Callers will know "tags"
        // is not null, so they won't call readTagsDescriptions(), but there
        // won't be any content either, so iterators won't iterate.

        tags = new TreeMap<String, String>();

        InputStream tagsStream = UIUtils.getFileResourceStream(TAGS_FILE);
        if (tagsStream == null)
            return;

        Scanner fileReader = new Scanner(tagsStream).useDelimiter("\\s*:\\s*");
        if (! fileReader.hasNext())
            return;

        do
        {
            try
            {
                String tag = fileReader.next();
                String description = fileReader.next();
                tags.put(tag, description);
            }
            catch (NoSuchElementException e)
            {
                // File is misformatted or corrupted.
                return;
            }
            catch (IllegalStateException e)
            {
                // Something really serious happened.
                return;
            }
        }
        while (fileReader.hasNext());
    }
}
