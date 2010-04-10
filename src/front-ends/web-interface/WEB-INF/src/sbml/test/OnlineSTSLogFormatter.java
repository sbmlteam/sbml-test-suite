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
