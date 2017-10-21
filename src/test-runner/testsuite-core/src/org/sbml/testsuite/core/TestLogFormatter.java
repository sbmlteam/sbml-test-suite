//
// @file TestLogFormatter.java
// @brief Log Formatter for the test suite
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Log Formatter for the test suite
 */
public class TestLogFormatter
    extends java.util.logging.Formatter
{

    /**
     * Formats the given log record.
     * 
     * @param rec
     *            the record to format
     */
    public String format(LogRecord rec)
    {
        String msg = rec.getMessage();
        StringBuffer buf = new StringBuffer(msg.length() + 200);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                                              "yyyy-MM-dd kk:mm:ss");

        buf.append(LOG_PREFIX);

        if (rec.getLevel().intValue() == Level.WARNING.intValue())
            buf.append(" WARNING");
        else if (rec.getLevel().intValue() == Level.SEVERE.intValue())
            buf.append(" ERROR");

        buf.append(" ");
        buf.append(dateFormatter.format(new Date()));
        buf.append(" ");

        buf.append(formatMessage(rec));
        buf.append('\n');
        return buf.toString();
    }


    /**
     * Returns the current LogPrefix
     * 
     * @return the log prefix
     */
    public static String getLogPrefix()
    {
        return LOG_PREFIX;
    }


    /**
     * Sets the log prefix to be used.
     * 
     * @param logPrefix
     *            the new prefix.
     */
    public static void setLogPrefix(String logPrefix)
    {
        LOG_PREFIX = logPrefix;
    }

    /** What we call ourselves in log messages. **/
    private static String LOG_PREFIX = "Test Suite";
}
