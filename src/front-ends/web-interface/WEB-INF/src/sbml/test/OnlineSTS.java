//
// @file    OnlineSTS.java
// @brief   Utility methods for the Online SBML Test Suite
// @author  Michael Hucka
// @date    Created 2010-04-08 <mhucka@caltech.edu>
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

package sbml.test;

import java.applet.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class OnlineSTS
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    /**
     * Returns the advertised base URL for the Online SBML Test Suite.
     * 
     * This returns a string such as "http://sbml.org/Software/SBML_Test_Suite"
     * pointing to the location advertised as the root/base of the system.
     * The Online STS does redirections when some paths under this root are
     * accessed, but this is the path we tell users to go to.
     *
     * Having an API call for this makes it possible to avoid hard-wiring the
     * path all over the JSP files.
     *
     * @param request the servlet request object handed to the caller by
     * the application server
     *
     * @return the full advertised URL to the Online SBML Test Suite.
     */
    public static final String getHomeURL(HttpServletRequest request)
    {
        return STS_HOME_URL;
    }


    /**
     * Returns the URL for images used in our front end.
     * 
     * Having an API call for this makes it possible to avoid hard-wiring the
     * path all over the JSP files.
     *
     * @param request the servlet request object handed to the caller by
     * the application server
     *
     * @return the full URL for the servlet images
     */
    public static final String getImageURL(HttpServletRequest request)
    {
        if (imageURL == null)
            imageURL = getServiceRootURL(request) + "/web/images";

        return imageURL;
    }


    /**
     * Returns the root context path of this request, as seen by the requester.
     * 
     * The result of this is a string like "http://sbml.org:8080/test_suite".
     * This is used in JSP files to contextualize certain requests, so that
     * the whole test suite doesn't care about the precise webapps directory
     * where it is placed.  Consequently, we can have a test version of the
     * system running at, say, "http://sbml.org:8080/test_suite_t", without
     * changing the contents of any of the files.
     *
     * @param request the servlet request object handed to the caller by
     * the application server
     *
     * @return the full context URL path, as a string
     */
    public static final String getServiceRootURL(HttpServletRequest request)
    {
        if (serviceRootURL == null)
            serviceRootURL = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        return serviceRootURL;
    }

    // 
    // -------------------------- Private variables ---------------------------
    // 

    private static String STS_HOME_URL =
        "http://sbml.org/Software/SBML_Test_Suite";

    // The following are cache variables:

    private static String serviceRootURL;
    private static String imageURL;

} // end of class
