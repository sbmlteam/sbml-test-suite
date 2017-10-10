// 
// @file    FormBean.java
// @brief   Servlet to get selections from user test case selection web page
// @author  Kimberly Begley
// @author  Michael Hucka
// @date    Created Jul 30, 2008, 9:25:21 AM
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

import java.io.*;
import java.util.*;

/** 
 * Java Bean for interfacing the JSP front-end code in selecttests.jsp to
 * the processing code in process.jsp.
 */
public class FormBean
{
    // All are arrays because the calling page uses a form with multiple
    // choices for each one.  All the set & get methods also must take
    // arrays args and return arrays.

    private String[] levelAndVersion;
    private String[] ctags;
    private String[] ttags;
    private HashMap<String, String>  errors;

    public FormBean()
    {
        levelAndVersion = new String[]{"unset"};
        ctags           = new String[]{"unset"};
        ttags           = new String[]{"unset"};
        errors          = new HashMap<String, String>();
    }

    public void setLevelAndVersion(String[] lv)   { levelAndVersion = lv; }
    public void setCtags(String[] ctag)           { this.ctags = ctag; }
    public void setTtags(String[] ttag)           { this.ttags = ttag; }

    public String[] getLevelAndVersion()          { return levelAndVersion; }
    public String[] getCtags()                    { return ctags; }
    public String[] getTtags()                    { return ttags; }

    public void setErrors(String key, String msg) { errors.put(key, msg); }

    public String getErrorMsg(String s)
    {
        String errorMsg = (String) errors.get(s.trim());
        return (errorMsg == null) ? "" : errorMsg;
    }

}
