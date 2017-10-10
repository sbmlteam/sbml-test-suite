//
// @file    OnlineSTS.java
// @brief   Utility methods for the Online SBML Test Suite
// @author  Michael Hucka
// @date    Created 2010-04-08 <mhucka@caltech.edu>
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

import java.applet.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * This class provides static methods for general purposes used by the JSP
 * files.  A calling JSP file should invoke OnlineSTS.init() before doing
 * anything else, and then can proceed to call OnlineSTS.logInvocation(request)
 * to log the JSP invocation to the Tomcat/Catalina log file on the server.
 *
 * There's a private static variable at the end of this file that sets the
 * URL to the Test Suite home page.  If the location is every changed, the
 * value will need to be updated.
 */
public class OnlineSTS
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    /**
     * Set up our logger and anything needed.  A calling JSP file should call
     * this method before calling any other OnlineSTS method.  It's safe to
     * call this init() method more than once.
     */
    public static void init()
    {
        if (log != null) return;     // We've been here before.

        log = Logger.getLogger(STS_LOGGER_SUBSYSTEM);
        if (log != null)
        {
            // Remove other loggers, to prevent left-overs when the app is
            // reloaded.  (Not sure if this is the best thing to do, but so
            // far it's worked and solved the problem that reloading
            // resulted in new instances of ConsoleHandler getting added.)

            Handler[] handlers = log.getHandlers();
            for (Handler h : handlers)
            {
                System.err.println("Removing " + h);
                log.removeHandler(h);
            }
        
            // Set up our logger with custom formatting.

            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.INFO);
            ch.setFormatter(new OnlineSTSLogFormatter());
            log.addHandler(ch);
            log.setUseParentHandlers(false);
        }
        else
        {
            System.err.println("STS ERROR: Can't create logger object.");
        }

        log.info("Logger initialized.");
    }


    /**
     * Log the fact that the caller has been invoked.  This takes a single
     * argument, the "request" object that's automatically provided to 
     * JSP files by Tomcat.  The result of calling this will be a line in
     * the log file that looks like this:
     *
     * Test Suite 2011-09-11 08:28:41 [131.215.15.99] (http://sbml.org:8080/test_suite/web/uploadresults.jsp) Loaded.
     *
     * Note that it is the caller's file name that is reported, and not
     * *this* file name.  (The method walks up the Java call stack to
     * figure out who the caller was.)
     */
    public static final void logInvocation(HttpServletRequest request)
    {
        if (log == null)
            init();

        if (request == null)
            log.info("Null HTTP servlet request variable.");
        else
            log.info(withIP(request, "(" + request.getRequestURL() + ") Loaded."));
    }


    public static final void logInfo(String msg)
    {
        if (log == null)
            init();

        log.info(deduceCallingClass() + " " + msg);
    }


    public static final void logInfo(HttpServletRequest request, String msg)
    {
        if (log == null)
            init();

        log.info(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logDebug(String msg)
    {
        if (log == null)
            init();

        log.fine(deduceCallingClass() + " " + msg);
    }


    public static final void logDebug(HttpServletRequest request, String msg)
    {
        if (log == null)
            init();

        log.info(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logWarning(String msg)
    {
        if (log == null)
            init();

        log.warning(deduceCallingClass() + " " + msg);
    }


    public static final void logWarning(HttpServletRequest request, String msg)
    {
        if (log == null)
            init();

        log.warning(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logError(String msg)
    {
        if (log == null)
            init();

        log.severe(deduceCallingClass() + " " + msg);
    }


    public static final void logError(HttpServletRequest request, String msg)
    {
        if (log == null)
            init();

        log.severe(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logThrowing(String c, String m, Throwable th)
    {
        if (log == null)
            init();

        log.throwing(c, m, th);
    }


    public static final String withIP(HttpServletRequest request, String msg)
    {
        return "[" + request.getRemoteHost() + "] " + msg;
    }


    /**
     * Returns the advertised base URL for the SBML Test Suite.
     * 
     * This returns a string such as "http://sbml.org/Software/SBML_Test_Suite"
     * pointing to the location advertised as the home of the system.
     *
     * @param request the servlet request object handed to the caller by
     * the application server
     *
     * @return the full advertised URL to the SBML Test Suite.
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
    // --------------------------- Private methods ----------------------------
    // 

    private static String deduceCallingClass()
    {
        final Throwable tmpThrowable    = new Throwable();
        final StackTraceElement[] stack = tmpThrowable.getStackTrace();
        final StackTraceElement entry   = stack[2]; // Want the caller, not us.

        return "(" + entry.getFileName() + ":" + entry.getLineNumber() + ")";
    }

    // 
    // -------------------------- Private variables ---------------------------
    // 

    private static String STS_HOME_URL =
        "http://sbml.org/Software/SBML_Test_Suite";

    /** Cache variable for the tomcat service root URL. **/
    private static String serviceRootURL;

    /** Cache variable for the directory where we store images. **/
    private static String imageURL;

    /** Cache variable for our log handler object. **/
    private static Logger log;

    /** How we identify ourselves to the logger system. **/
    private static String STS_LOGGER_SUBSYSTEM = "sbml.test";

} // end of class
