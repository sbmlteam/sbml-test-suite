//
// @file    CasesTagsMap.java
// @brief   Parses the .cases-tags-map file and provides utilities around it
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
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
//
// This extends the Java Vector class.  This constructs a vector whose
// entries are indexed by case numbers.  (I.e., entry 1 in the vector of
// cases is case 00001, entry 2 is case 00002, etc.)  Each entry is a
// vector of strings; thus, this is actually a vector of vectors.
//
// The format of each entry is
//
//    "number" "tag1" "tag2" ...
//
// where "number" is the case number as a string (identical to the index
// number of that entry) and the rest of the strings are the tags present
// in that case's definition.  The tags include the level/version information
// as strings.  Example:
//
// 00001 Compartment Species Reaction Parameter InitialAmount 1.2 2.1 2.2 2.3 2.4
// 00002 Compartment Species Reaction Parameter InitialAmount 1.2 2.1 2.2 2.3 2.4
//
// The case number is stored as the first element in the data vector as a
// minor optimization, to save callers from having to convert the entry's
// index number to a string -- they can just do a .get(0) to get it.
//
// Note that because case numbering starts at 1, but Java vector index
// starts at 1, the 0th item in the vector is currently unused.  (Maybe
// someday we'll find a clever use for it.)

package sbml.test;

import java.io.*;
import java.util.*;
import java.applet.*;


public class CasesTagsMap
    extends Vector<Vector<String>>      // I love Java.
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public CasesTagsMap(File casesDir)
        throws Exception
    {
        this.casesDir = casesDir;
        parseTagsMapFile();
    }

    public CasesTagsMap(String caseDirPath)
        throws Exception
    {
        this(new File(caseDirPath));
    }

    public int getHighestCaseNumber()
    {
        return highestNumber;
    }

    // 
    // --------------------------- Private methods ----------------------------
    // 

    private void parseTagsMapFile()
        throws Exception
    {
        if (casesDir == null || ! casesDir.exists())
            throw new Exception("Cannot find test cases directory '"
                                + casesDir + "'.");

        if (! casesDir.isDirectory())
            throw new Exception("'" + casesDir + "' is not a directory.");

        if (! casesDir.canRead())
            throw new Exception("'" + casesDir + "' is unreadable.");

        mapFile = new File(casesDir, tagsMapFileName);
        if (mapFile == null || ! mapFile.exists())
            throw new Exception("Cannot find cases tags map file.");

        if (! mapFile.isFile())
            throw new Exception("'" + mapFile + "' is not a file.");

        if (! mapFile.canRead())
            throw new Exception("'" + mapFile + "' is unreadable.");

        // The first line in the tags map file is the highest case number.
        // If that's not the case, something is wrong.

        Scanner fileReader = new Scanner(mapFile);

        if (! fileReader.hasNext() || ! fileReader.hasNextInt())
            throw new Exception("'" + mapFile + "' not in expected format.");

        highestNumber = fileReader.nextInt();
        fileReader.nextLine();      // Skip past end of line.

        // Each subsequent line in the tags map file is a case number
        // followed by all its tags.  We're going to read each line, parse
        // each item in the line as a token using Scanner methods, and
        // store the line's contents in this vector.

        this.setSize(highestNumber + 1); // +1 because of the unused 0th elem.
        
        while (fileReader.hasNext())
        {
            Scanner tagreader   = new Scanner(fileReader.nextLine());
            String caseNum      = tagreader.next();
            Vector<String> data = new Vector<String>();

            // As a minor optimization, because we sometimes want the case
            // number as a string, we store the string-form of the case
            // number as the first element in the tags data vector.

            data.add(caseNum);

            // Now read the contents of the rest of the line and append
            // each item (which will be a tag) to the end of the vector.

            while (tagreader.hasNext())
                data.add(tagreader.next());

            // Store it under the index number "caseNum".

            this.insertElementAt(data, Integer.parseInt(caseNum));
        }
    }

    // 
    // -------------------------- Private variables ---------------------------
    // 

    private File casesDir = null;
    private File mapFile = null;
    private int highestNumber = 0;

    // 
    // -------------------------- Private constants ---------------------------
    // 

    private final static String tagsMapFileName = ".cases-tags-map";
}// end of class
