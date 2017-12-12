
package org.sbml.testsuite.web;

import javax.servlet.http.HttpServletRequest;

public class OnlineSTS
    extends org.sbml.testsuite.core.TestLogger
{

    public static final void logDebug(HttpServletRequest request, String msg)
    {
        if (log == null) init();

        log.info(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logError(HttpServletRequest request, String msg)
    {
        if (log == null) init();

        log.severe(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logWarning(HttpServletRequest request, String msg)
    {
        if (log == null) init();

        log.warning(withIP(request, deduceCallingClass() + " " + msg));
    }


    public static final void logInfo(HttpServletRequest request, String msg)
    {
        if (log == null) init();

        log.info(withIP(request, deduceCallingClass() + " " + msg));
    }


    /**
     * Log the fact that the caller has been invoked. This takes a single
     * argument, the "request" object that's automatically provided to
     * JSP files by Tomcat. The result of calling this will be a line in
     * the log file that looks like this:
     * 
     * Test Suite 2011-09-11 08:28:41 [131.215.15.99]
     * (http://sbml.org:8080/test_suite/web/uploadresults.jsp) Loaded.
     * 
     * Note that it is the caller's file name that is reported, and not
     * *this* file name. (The method walks up the Java call stack to
     * figure out who the caller was.)
     */
    public static final void logInvocation(HttpServletRequest request)
    {
        if (log == null) init();

        if (request == null)
            log.info("Null HTTP servlet request variable.");
        else
            log.info(withIP(request, "(" + request.getRequestURL()
                + ") Loaded."));
    }


    public static final String withIP(HttpServletRequest request, String msg)
    {
        return "[" + request.getRemoteHost() + "] " + msg;
    }

}
