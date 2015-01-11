//
// @file    CaseSummaryMap.java
// @brief   Parses the .cases-tags-map file and provides utilities around it
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
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
//
// This extends the TreeMap class.  This constructs a TreeMap where the keys
// are case numbers and values are CaseSummary objects.
//
// size() is not an appropriate way to find out the highest case number,
// because case numbers may not be contiguous.  Instead, there's a separate
// method, getHighestCaseNumber().

package sbml.test;

import java.io.*;
import java.util.*;
import java.applet.*;


public class CaseSummaryMap
    extends TreeMap<Integer, CaseSummary>
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public CaseSummaryMap(File casesDir)
        throws Exception
    {
        this.casesDir = casesDir;
        parseTagsMapFile();
    }


    public CaseSummaryMap(String caseDirPath)
        throws Exception
    {
        this(new File(caseDirPath));
    }


    /**
     * Returns an array of all known tags.
     */
    public Vector<String> getKnownTags()
    {
        return knownTags;
    }


    /**
     * Returns the highest-numbered case number known, or -1 if
     * this map is currently empty.
     *
     * @return the highest-numbered test case in this map, or -1
     */
    public int getHighestCaseNumber()
    {
        if (lastKey() != null)
            return lastKey().intValue();
        else
            return -1;
    }


    public Vector<String> getAllCaseNames()
    {
        Vector<String> v = new Vector<String>();
        for (CaseSummary c : values())
            v.add(c.getCaseName());
        return v;
    }


    // /**
    //  * Returns a count of cases that have any of the given tags.
    //  *
    //  * @return the number of cases that have the given tags.
    //  */
    // public int countTaggedAny(Vector<String> tags)
    // {
    //     return countTaggedAny(new TagBits(tags, knownTags));
    // }


    // /**
    //  * Return a count of cases that have any of the given tags.
    //  * This version uses a TagBits object.
    //  *
    //  * @return the number of cases that have the given tags.
    //  */
    // public int countTaggedAny(TagBits tagBits)
    // {
    //     int count = 0;
    //     for (CaseSummary cs : this.values())
    //         if (cs.getTagBits().intersects(tagBits))
    //             count++;
    //     return count;
    // }


    // /**
    //  * Return a count of cases that have <em>all</em> of the given tags.
    //  *
    //  * @return the number of cases that have the given tags.
    //  */
    // public int countTaggedAll(Vector<String> tags)
    // {
    //     return countTaggedAll(new TagBits(tags, knownTags));
    // }


    // /**
    //  * Return a count of cases that have <em>all</em> of the given tags.
    //  * This version uses a TagBits object.
    //  *
    //  * @return the number of cases that have the given tags.
    //  */
    // public int countTaggedAll(TagBits tagBits)
    // {
    //     int count = 0;
    //     for (CaseSummary cs : this.values())
    //         if (cs.getTagBits().contains(tagBits))
    //             count++;
    //     return count;
    // }



    /**
     * Generates a table of unique bit masks (as long ints) in which each
     * mask represents a possible tag combinations that exists in our test
     * cases.
     *
     * This uses the following principles.
     * (1) Case bit masks can be represented as long integers.
     * (3) Java's TreeSet doesn't store duplicates, so simply shoving every
     * case tag mask into a TreeSet one at a time results in a set of
     * unique tag combinations at the end.
     */
    public Long[] getUniqueTagCombinations()
    {
        TreeSet<Long> unique = new TreeSet<Long>();

        for (CaseSummary cs : this.values())
            unique.add(cs.getTagCode());

        return unique.toArray(new Long[0]);
    }


    /**
     * Structurally modifies this TreeMap by removing all CaseSummary
     * objects not containing <em>all</em> of the given tags.  (The objects
     * must contain all the tags, not only some.)
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryMap, they
     * should perhaps clone the CaseSummaryMap before calling this method.
     */
    public void retainIfTagged(TagBits tagBits)
        throws Exception
    {
        if (tagBits == null)
            return;

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null && ! e.getValue().getTagBits().contains(tagBits))
                it.remove();
        }
    };


    public void retainIfTagged(Vector<String> tags)
        throws Exception
    {
        if (tags == null || tags.size() == 0)
            return;

        checkKnownTags(tags);
        retainIfTagged(new TagBits(tags, knownTags));
    };


    public void retainIfTagged(String[] tags)
        throws Exception
    {
        if (tags == null || tags.length == 0)
            return;

        checkKnownTags(tags);
        retainIfTagged(new TagBits(tags, knownTags));
    };


    public void retainIfTagged(String tag)
        throws Exception
    {
        if (tag == null)
            return;

        checkKnownTags(tag);
        retainIfTagged(new TagBits(tag, knownTags));
    };


    /**
     * Structurally modifies this map by removing any cases that
     * do <em>not</em> have the given level & version combination.
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryMap, they
     * should perhaps clone the CaseSummaryMap before calling this method.
     */
    public void retainIfHasLevelAndVersion(String lv)
    {
        if (lv == null)
            return;

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null && ! e.getValue().hasLevelAndVersion(lv))
                it.remove();
        }
    }


    /**
     * Removes all cases that have <em>any</em> of the given tags.  (Objects
     * do not need to contain all the tags; even one will count.)
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryMap, they
     * should perhaps clone the CaseSummaryMap before calling this method.
     */
    public void removeIfTagged(TagBits tagBits)
        throws Exception
    {
        if (tagBits == null)
            return;

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null && tagBits.intersects(e.getValue().getTagBits()))
                it.remove();
        }
    };


    public void removeIfTagged(Vector<String> tags)
        throws Exception
    {
        if (tags == null || tags.size() == 0)
            return;

        checkKnownTags(tags);
        removeIfTagged(new TagBits(tags, knownTags));
    };


    public void removeIfTagged(String[] tags)
        throws Exception
    {
        if (tags == null || tags.length == 0)
            return;

        checkKnownTags(tags);
        removeIfTagged(new TagBits(tags, knownTags));
    };


    public void removeIfTagged(String tag)
        throws Exception
    {
        if (tag == null)
            return;

        checkKnownTags(tag);
        removeIfTagged(new TagBits(tag, knownTags));
    };


    /**
     * Removes all cases that have <em>any</em> of the given level+version
     * combinations.
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryMap, they
     * should perhaps clone the CaseSummaryMap before calling this method.
     */
    public void removeIfHasLevelAndVersion(Vector<String> levelsAndVersions)
    {
        if (levelsAndVersions == null || levelsAndVersions.size() == 0)
            return;

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null)
                for (String lv : levelsAndVersions)                
                    if (e.getValue().hasLevelAndVersion(lv))
                    {
                        it.remove();
                        break;
                    }
        }
    };


    public void removeIfHasLevelAndVersion(String[] levelsAndVersions)
    {
        if (levelsAndVersions == null || levelsAndVersions.length == 0)
            return;


        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null)
                for (int i = 0; i < levelsAndVersions.length; i++)
                    if (e.getValue().hasLevelAndVersion(levelsAndVersions[i]))
                    {
                        it.remove();
                        break;
                    }
        }
    };


    public void removeIfHasLevelAndVersion(String oneLV)
    {
        if (oneLV == null)
            return;

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null && e.getValue().hasLevelAndVersion(oneLV))
                it.remove();
        }
    };


    public void removeIfInvolvesPackagesOtherThan(String[] packages)
    {
        if (packages == null)
            return;

        HashSet<String> okPackages = new HashSet<String>(Arrays.asList(packages));

        Iterator<Map.Entry<Integer, CaseSummary>> it = entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, CaseSummary> e = it.next();
            if (e.getValue() != null)
                for (String casePkg : e.getValue().getCasePackages())
                    if (! okPackages.contains(casePkg))
                    {
                        it.remove();
                        break;
                    }
        }
    }


    // 
    // --------------------------- Private methods ----------------------------
    // 

    private void parseTagsMapFile()
        throws Exception
    {
        sanityCheckCasesDir(casesDir);
        mapFile = new File(casesDir, TAGS_MAP_FILE_NAME);
        sanityCheckMapFile(mapFile);

        Scanner fileReader = new Scanner(mapFile);

        // 1st line is an ordered list of all possible component & test tags.

        if (! fileReader.hasNext())
            throw new Exception("Unexpected end of file in '" + mapFile + "'.");
        if (fileReader.hasNextInt())    // Should *not* be a number.
            throw new Exception("1st line in '" + mapFile + "' is not tag list");
        Scanner allTagsReader = new Scanner(fileReader.nextLine());
        while (allTagsReader.hasNext())
            knownTags.add(allTagsReader.next());

        // Each subsequent line in the tags map file is a case number
        // followed by all its tags.  We're going to read each line, parse
        // each item in the line as a token using Scanner methods, and
        // store the line's contents in this vector.

        while (fileReader.hasNext())
        {
            Scanner tagreader        = new Scanner(fileReader.nextLine());
            String caseNum           = tagreader.next();
            Vector<String> tags      = new Vector<String>();
            Vector<String> lv        = new Vector<String>();
            HashSet<String> packages = new HashSet<String>();

            packages.add("core");       // Core is always present.

            // Rest of line consists of tags, including "level.version" tags.

            while (tagreader.hasNext())
            {
                String tag = tagreader.next();
                int pos    = tag.indexOf(':');

                if (tag.length() == 3)  // Only level.version tags are 3 chars.
                    lv.add(tag);
                else if (pos > 0)
                    packages.add(tag.substring(0, pos));
                else
                    tags.add(tag);
            }

            CaseSummary theCase = new CaseSummary(caseNum);
            theCase.setTags(tags, knownTags);
            theCase.setLevelsAndVersions(lv);
            theCase.setPackages(packages);

            this.put(Integer.parseInt(caseNum), theCase);
        }
    }


    private void sanityCheckCasesDir(File dir)
        throws Exception
    {
        if (dir == null || ! dir.exists())
            throw new Exception("Can't find cases directory '" + dir + "'.");

        if (! dir.isDirectory())
            throw new Exception("'" + dir + "' is not a directory.");

        if (! dir.canRead())
            throw new Exception("'" + dir + "' is unreadable.");
    }


    private void sanityCheckMapFile(File mfile)
        throws Exception
    {
        if (mfile == null)
            throw new Exception("Can't find tags map file.");

        if (! mfile.exists())
            throw new Exception("Can't find tags map file '" + mfile + "'.");

        if (! mfile.isFile())
            throw new Exception("'" + mfile + "' is not a file.");

        if (! mfile.canRead())
            throw new Exception("'" + mfile + "' is unreadable.");
    }


    private void checkKnownTags(String tag)
        throws Exception
    {
        if (! knownTags.contains(tag))
            throw new Exception("No such tag known: '" + tag + "'");
    }


    private void checkKnownTags(String[] tags)
        throws Exception
    {
        for (int i = 0; i < tags.length; i++)
            if (! knownTags.contains(tags[i]))
                throw new Exception("No such tag known: '" + tags[i] + "'");
    }


    private void checkKnownTags(Vector<String> tags)
        throws Exception
    {
        for (String tag : tags)
            if (! knownTags.contains(tag))
                throw new Exception("No such tag known: '" + tag + "'");
    }


    // 
    // -------------------------- Private variables ---------------------------
    // 

    private File casesDir = null;
    private File mapFile = null;

    /*
     * Ordered array of all known tags.  This serves as our tag index
     * reference.  The index of a given tag in this array serves as its bit
     * position in the coding scheme.
     */
    private Vector<String> knownTags = new Vector<String>();

    // 
    // -------------------------- Private constants ---------------------------
    // 

    private final static String TAGS_MAP_FILE_NAME = ".cases-tags-map";

}// end of class
