//
// @file   Tags.java
// @brief  Class encapsulating descriptions of test & component tags
// @author Michael Hucka
// @date   2013-11-12 <mhucka@caltech.edu>
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

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;


public class Tags
{
    private static final String TAGS_FILE = "all-tags.txt";
    private static TreeMap<String, String> tags;


    /**
     * Returns a text description for a given tag name.
     */
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
     * The format of this file is meant to be easily parsed by software and humans.
     * Every line consists of 3 things, in this order:
     *
     *   1. the tag name
     *   2. a single tab character
     *   3. a summary of the levels & versions for which the tag is valid
     *   4. a single tab character
     *   5. text summarizing the meaning of the tag
     *
     * Lines beginning with the pound-sign character '#' are ignored.
     * Blank lines are ignored.
     *
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

        Scanner fileReader = new Scanner(tagsStream);
        Pattern ignorePattern = Pattern.compile("^#.*|^\\s*$");

        while (fileReader.hasNext())
        {
            String line = fileReader.nextLine();

            if (ignorePattern.matcher(line).matches())
                continue;
            else
            {
                String[] parts = line.split("\\t");
                if (parts.length != 3)
                    continue;           // Something's wrong -- just move on.

                tags.put(parts[0], parts[2]);
            }
        }

        fileReader.close();
    }
}
