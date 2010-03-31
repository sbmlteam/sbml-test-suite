// 
// @file    FormBean.java
// @brief   Servlet to get selections from user test case selection web page
// @author  Kimberly Begley
// @author  Michael Hucka
// @date    Created Jul 30, 2008, 9:25:21 AM
// @id      $Id$
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
