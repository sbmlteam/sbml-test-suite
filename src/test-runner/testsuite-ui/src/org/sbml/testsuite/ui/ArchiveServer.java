//
// @file   ArchiveServer.java
// @brief  Class implementing getting GitHub test case archive info.
// @author Michael Hucka
// @date   2017-06-23 <mhucka@caltech.edu>
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

class ArchiveServer
{
    private static final String github_sts_release_address =
        "https://api.github.com/repos/sbmlteam/sbml-test-suite/releases";


    /**
     * Contact our GitHub release location, get the list of releases, look
     * for the most recent stable release, and set our internal value to the
     * Json blob for the GitHub info about it.  This specifically looks for
     * .zip files.
     *
     * @param type A string corresponding to the subset of the test suite
     *             cases to download.  At this time, it can be "semantic",
     *             "stochastic" or "syntactic".
     *
     * @returns true if we were successful, false otherwise.
     */
    public static CasesArchiveData getLatestArchiveData(String type)
        throws Exception
    {
        CasesArchiveData data = null;
        if (! validArchiveType(type))
            throw new Exception("'" + type + " is not a recognized archive type");

        try
        {
            URL release_url = new URL(github_sts_release_address);
            String sts_releases = IOUtils.toString(release_url);
            JsonArray ja = (JsonArray) Jsoner.deserialize(sts_releases);

            // We assume the most recent things come earlier in the list,
            // so we iterate and stop at the first suitable entry.
            for (int i = 0; i < ja.size(); i++)
            {
                JsonObject release = (JsonObject) ja.get(i);
                JsonArray assets = release.getCollection("assets");

                // Look for STS test cases archives specifically, and ignore
                // other things we might also release from the same place.
                for (int j = 0; j < assets.size(); j++)
                {
                    JsonObject item = (JsonObject) assets.get(j);
                    String name = item.getString("name");
                    if (name.startsWith("sbml-" + type) && name.endsWith("zip"))
                        data = new CasesArchiveData(type, item);
                }
            }
        }
        catch (MalformedURLException e)
        {
            // Should never happen, because we hardcode the URL.
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // Should never happen, because we hardcode the URL.
            e.printStackTrace();
        }
        catch (DeserializationException e)
        {
            throw new IOException("Failed to parse archive data from GitHub");
        }
        return data;
    }


    public static boolean validArchiveType(String type)
    {
        return type.equals("semantic") || type.equals("stochastic")
            || type.equals("syntactic");
    }
}
