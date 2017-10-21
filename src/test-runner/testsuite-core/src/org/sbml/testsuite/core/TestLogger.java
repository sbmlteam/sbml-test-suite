//
// @file TestLogger.java
// @brief Logger for the test suite
// @author Michael Hucka
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
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
//
package org.sbml.testsuite.core;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestLogger from the online testsystem, adapted for the core API
 */
public class TestLogger
{
    /**
     * Set up our logger and anything needed. A calling JSP file should call
     * this method before calling any other OnlineSTS method. It's safe to
     * call this init() method more than once.
     */
    public static void init()
    {
        if (log != null) return; // We've been here before.

        log = Logger.getLogger(STS_LOGGER_SUBSYSTEM);
        if (log != null)
        {
            // Remove other loggers, to prevent left-overs when the app is
            // reloaded. (Not sure if this is the best thing to do, but so
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
            ch.setFormatter(new TestLogFormatter());
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
     * Log information
     * @param msg the message
     */
    public static final void logInfo(String msg)
    {
        if (log == null) init();

        log.info(deduceCallingClass() + " " + msg);
    }


    /**
     * Log debug message
     * @param msg the message
     */
    public static final void logDebug(String msg)
    {
        if (log == null) init();

        log.fine(deduceCallingClass() + " " + msg);
    }


    /**
     * Log a warning
     * @param msg the message
     */
    public static final void logWarning(String msg)
    {
        if (log == null) init();

        log.warning(deduceCallingClass() + " " + msg);
    }


    /**
     * Log an error
     * @param msg the message
     */
    public static final void logError(String msg)
    {
        if (log == null) init();

        log.severe(deduceCallingClass() + " " + msg);
    }


    /**
     * Log an exception
     * @param c the class
     * @param m the method
     * @param th the exception
     */
    public static final void logThrowing(String c, String m, Throwable th)
    {
        if (log == null) init();

        log.throwing(c, m, th);
    }


    /**
     * Determine the calling class by examining the stack trace
     * 
     * @return the calling class and line number
     */
    protected static String deduceCallingClass()
    {
        final Throwable tmpThrowable = new Throwable();
        final StackTraceElement[] stack = tmpThrowable.getStackTrace();
        final StackTraceElement entry = stack[2]; // Want the caller, not us.

        return "(" + entry.getFileName() + ":" + entry.getLineNumber() + ")";
    }

    /** Cache variable for our log handler object. **/
    protected static Logger log;

    /** How we identify ourselves to the logger system. **/
    protected static String STS_LOGGER_SUBSYSTEM = "org.sbml.testsuite";
}
