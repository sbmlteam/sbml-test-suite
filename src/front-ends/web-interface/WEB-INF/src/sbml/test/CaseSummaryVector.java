//
// @file    CaseSummaryVector.java
// @brief   Parses the .cases-tags-map file and provides utilities around it
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
//
// $Id$
// $HeadURL$
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright 2008-2010 California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation.  A copy of the license agreement is provided
// in the file named "LICENSE.txt" included with this software distribution
// and also available at http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------
//
// This extends the Java Vector class.  This constructs a vector whose
// entries are indexed by case numbers.  (I.e., entry 1 in the vector of
// cases is case 00001, entry 2 is case 00002, etc.)  Each entry is a
// vector of CaseSummary objects.
//
// Note that because case numbering starts at 1, but Java vector index
// starts at 1, the 0th item in the vector is not a real case.  Instead,
// it is a pseudo-entry that stores information about all known tags.
//
// size() on this vector returns the highest case number, but because that's
// potentially confusing (because vectors start at 0, but we're counting
// from 1), there's also a separate method, getHighestCaseNumber().

package sbml.test;

import java.io.*;
import java.util.*;
import java.applet.*;


public class CaseSummaryVector
    extends Vector<CaseSummary>
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public CaseSummaryVector(File casesDir)
        throws Exception
    {
        this.casesDir = casesDir;
        parseTagsMapFile();
    }


    public CaseSummaryVector(String caseDirPath)
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
     * Returns the highest-numbered case number known.
     *
     * This method exists to avoid possible confusion.  In reality, size()
     * on this CaseSummaryVector also returns the highest case number, but
     * because Java vectors start numbering at 0 and we count cases from 1,
     * it's safer to use this method, to avoid the instinctive desire to
     * add +/- 1 to size() in a mistaken attempt to compensate for the
     * difference in counting conventions.
     */
    public int getHighestCaseNumber()
    {
        return size();
    }


    /**
     * Return a count of cases that have any of the given tags.
     *
     * @return the number of cases that have the given tags.
     */
    public int countTaggedAny(Vector<String> tags)
    {
        return countTaggedAny(new TagBits(tags, knownTags));
    }


    /**
     * Return a count of cases that have any of the given tags.
     * This version uses a TagBits object.
     *
     * @return the number of cases that have the given tags.
     */
    public int countTaggedAny(TagBits tagBits)
    {
        int count = 0;
        for (CaseSummary cs : this)
            if (cs.getTagBits().intersects(tagBits))
                count++;
        return count;
    }


    /**
     * Return a count of cases that have <em>all</em> of the given tags.
     *
     * @return the number of cases that have the given tags.
     */
    public int countTaggedAll(Vector<String> tags)
    {
        return countTaggedAll(new TagBits(tags, knownTags));
    }


    /**
     * Return a count of cases that have <em>all</em> of the given tags.
     * This version uses a TagBits object.
     *
     * @return the number of cases that have the given tags.
     */
    public int countTaggedAll(TagBits tagBits)
    {
        int count = 0;
        for (CaseSummary cs : this)
            if (cs.getTagBits().contains(tagBits))
                count++;
        return count;
    }



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

        for (CaseSummary cs : this)
            unique.add(cs.getTagCode());

        return unique.toArray(new Long[0]);
    }


    /**
     * Structurally modifies this Vector by removing all CaseSummary
     * objects not containing <em>all</em> of the given tags.  (The objects
     * must contain all the tags, not only some.)
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryVector, they
     * should perhaps clone the CaseSummaryVector before calling this method.
     *
     * @return <code>true</code> if this vector has been modified
     */
    public boolean retainIfTagged(TagBits tagBits)
        throws Exception
    {
        if (tagBits == null)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            if (! cs.getTagBits().contains(tagBits))
                toRemove.add(cs);

        return this.removeAll(toRemove);
    };


    public boolean retainIfTagged(Vector<String> tags)
        throws Exception
    {
        if (tags == null || tags.size() == 0)
            return false;

        checkKnownTags(tags);
        return retainIfTagged(new TagBits(tags, knownTags));
    };


    public boolean retainIfTagged(String[] tags)
        throws Exception
    {
        if (tags == null || tags.length == 0)
            return false;

        checkKnownTags(tags);
        return retainIfTagged(new TagBits(tags, knownTags));
    };


    public boolean retainIfTagged(String tag)
        throws Exception
    {
        if (tag == null)
            return false;

        checkKnownTags(tag);
        return retainIfTagged(new TagBits(tag, knownTags));
    };


    /**
     * Structurally modifies this Vector by removing any cases that
     * do <em>not</em> have the given level & version combination.
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryVector, they
     * should perhaps clone the CaseSummaryVector before calling this method.
     *
     * @return <code>true</code> if this vector has been modified
     */
    public boolean retainIfHasLevelAndVersion(String lv)
    {
        if (lv == null)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            if (! cs.hasLevelAndVersion(lv))
                toRemove.add(cs);

        return this.removeAll(toRemove);
    }


    /**
     * Removes all cases that have <em>any</em> of the given tags.  (Objects
     * do not need to contain all the tags; even one will count.)
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryVector, they
     * should perhaps clone the CaseSummaryVector before calling this method.
     *
     * @return <code>true</code> if this vector has been modified
     */
    public boolean removeIfTagged(TagBits tagBits)
        throws Exception
    {
        if (tagBits == null)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            if (tagBits.intersects(cs.getTagBits()))
                toRemove.add(cs);

        return this.removeAll(toRemove);
    };


    public boolean removeIfTagged(Vector<String> tags)
        throws Exception
    {
        if (tags == null || tags.size() == 0)
            return false;

        checkKnownTags(tags);
        return removeIfTagged(new TagBits(tags, knownTags));
    };


    public boolean removeIfTagged(String[] tags)
        throws Exception
    {
        if (tags == null || tags.length == 0)
            return false;

        checkKnownTags(tags);
        return removeIfTagged(new TagBits(tags, knownTags));
    };


    public boolean removeIfTagged(String tag)
        throws Exception
    {
        if (tag == null)
            return false;

        checkKnownTags(tag);
        return removeIfTagged(new TagBits(tag, knownTags));
    };


    /**
     * Removes all cases that have <em>any</em> of the given level+version
     * combinations.
     *
     * Note that this is a destructive operation.  If the caller will need
     * to perform other operations on the original CaseSummaryVector, they
     * should perhaps clone the CaseSummaryVector before calling this method.
     *
     * @return <code>true</code> if this vector has been modified
     */
    public boolean removeIfHasLevelAndVersion(Vector<String> levelsAndVersions)
    {
        if (levelsAndVersions == null || levelsAndVersions.size() == 0)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            for (String lv : levelsAndVersions)
                if (cs.hasLevelAndVersion(lv))
                    toRemove.add(cs);

        return this.removeAll(toRemove);
    };


    public boolean removeIfHasLevelAndVersion(String[] levelsAndVersions)
    {
        if (levelsAndVersions == null || levelsAndVersions.length == 0)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            for (int i = 0; i < levelsAndVersions.length; i++)
                if (cs.hasLevelAndVersion(levelsAndVersions[i]))
                    toRemove.add(cs);

        return this.removeAll(toRemove);
    };


    public boolean removeIfHasLevelAndVersion(String oneLevelAndVersion)
    {
        if (oneLevelAndVersion == null)
            return false;

        Vector<CaseSummary> toRemove = new Vector<CaseSummary>();

        for (CaseSummary cs : this)
            if (cs.hasLevelAndVersion(oneLevelAndVersion))
                toRemove.add(cs);

        return this.removeAll(toRemove);
    };


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
            Scanner tagreader   = new Scanner(fileReader.nextLine());
            String caseNum      = tagreader.next();
            Vector<String> tags = new Vector<String>();
            Vector<String> lv   = new Vector<String>();

            // Rest of line consists of tags, including "level.version" tags.

            while (tagreader.hasNext())
            {
                String tag     = tagreader.next();
                char firstChar = tag.charAt(0);

                if (tag.length() == 3)  // Only level.version tags are 3 chars.
                    lv.add(tag);
                else
                    tags.add(tag);
            }

            CaseSummary theCase = new CaseSummary(caseNum);
            theCase.setTags(tags, knownTags);
            theCase.setLevelsAndVersions(lv);

            this.add(theCase);
        }

        // Cleanup.
        trimToSize();
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
