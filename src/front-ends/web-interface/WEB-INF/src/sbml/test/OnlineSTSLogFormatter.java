//
// @file    OnlineSTSLogFormatter.java
// @brief   java.util.logger log formatting class used by OnlineSTS.
// @author  Michael Hucka
// @date    Created 2010-01-27 <mhucka@caltech.edu>
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


public class OnlineSTSLogFormatter extends java.util.logging.Formatter
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public String format(LogRecord rec)
    {
        String msg       = rec.getMessage();
        StringBuffer buf = new StringBuffer(msg.length() + 200);
        SimpleDateFormat dateFormatter =
            new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        buf.append(STS_LOG_PREFIX);

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

    // 
    // -------------------------- Private variables ---------------------------
    // 

    /** What we call ourselves in log messages. **/
    private static String STS_LOG_PREFIX = "Test Suite";

}
