//
// @file Util.java
// @brief Util, a collection of static functions used all over the place
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

import com.sun.jna.platform.FileUtils;
import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;


/**
 * Util, a collection of static functions used all over the place.
 */
public class Util
{
    /**
     * SourceForge URL for the last 100 file releases in the SBML Test Suite
     * branch.
     */
    private final static String SF_RSS_QUERY = "http://sourceforge.net/projects/sbml/rss?path=/test-suite&limit=100";

    /**
     * Timeout duration for how long we're willing to wait on establishing
     * the connection to SourceForge. Time is in milliseconds;
     */
    private final static int    NET_CONNECTION_TIMEOUT = 5000;

    /**
     * Timeout duration for how long we're willing to wait on reading from
     * the connection to SourceForge. Time is in milliseconds;
     */
    private final static int    NET_READ_TIMEOUT       = 10000;

    /**
     * Size of the buffer used for reading from streams and network
     * connections.
     */
    private final static int    BUFFER_SIZE            = 1024;


    /**
     * Returns the size of the buffer we use for reading from streams and
     * network connections.
     */
    public static int getStreamReadBufferSize()
    {
        return BUFFER_SIZE;
    }


    /**
     * Returns a network connection for our RSS query.
     * 
     * Callers can use this to test the ability to connect to sf.net
     * before invoking getRSSFeedConnection(...).
     */
    public static HttpURLConnection getRSSFeedConnection()
    {
        try
        {
            URL url = new URL(SF_RSS_QUERY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Test if the connection really was made. Otherwise, callers may
            // get an UnknownHostException if the network is out, but they
            // won't get that exception until the time they actually try to
            // read from the connection. Let's get the exception now if we can.

            int code = connection.getResponseCode();
            if (code < 200 || code > 400) return null;

            // Set timeout parameters.
            connection.setConnectTimeout(NET_CONNECTION_TIMEOUT);
            connection.setReadTimeout(NET_READ_TIMEOUT);

            return connection;
        }
        catch (IOException ex)
        {
            // Probably have no network connection.
            return null;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Returns a NodeList of the contents at our RSS URL.
     */
    public static NodeList getRSSFeedContents()
    {
        return getRSSFeedContents(getRSSFeedConnection());
    }


    /**
     * Returns a NodeList of the contents at our RSS URL.
     * 
     * Callers can cache this value and use it in subsequent calls to other
     * methods like getCasesArchiveURLs(...), to reduce network accesses.
     * (Users on slow network links may appreciate that.)
     * 
     * For programming reference purposes, here is a sample of the RSS feed
     * contents returned by sf.net (in Nov. 2013):
     * 
     * <?xml version="1.0" encoding="utf-8"?>
     * <rss xmlns:content="http://purl.org/rss/1.0/modules/content/"
     * xmlns:files="http://sourceforge.net/api/files.rdf#"
     * xmlns:media="http://video.search.yahoo.com/mrss/"
     * xmlns:doap="http://usefulinc.com/ns/doap#"
     * xmlns:sf="http://sourceforge.net/api/sfelements.rdf#" version="2.0">
     * <channel xmlns:files="http://sourceforge.net/api/files.rdf#"
     * xmlns:media="http://video.search.yahoo.com/mrss/"
     * xmlns:doap="http://usefulinc.com/ns/doap#"
     * xmlns:sf="http://sourceforge.net/api/sfelements.rdf#">
     * <title><![CDATA[Systems Biology Markup Language (SBML)
     * downloads]]></title>
     * <link>http://sourceforge.net/api/file/index/project-id/71971/mtime/desc/
     * limit/100/path/test-suite/rss</link>
     * <description><![CDATA[Files from Systems Biology Markup Language (SBML).
     * The Systems Biology Markup Language (SBML) is an XML-based description
     * language for representing computational models in systems biology. Visit
     * the project web site to learn more.]]></description>
     * <pubDate>Sat, 23 Nov 2013 22:06:35 +0000</pubDate>
     * <managingEditor>noreply@sourceforge.net
     * (SourceForge.net)</managingEditor>
     * <generator>Zend_Feed</generator>
     * <docs>http://blogs.law.harvard.edu/tech/rss</docs>
     * <item>
     * <title><![CDATA[/test-suite/3.0.0/cases-archive]]></title>
     * <link>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-
     * archive/</link>
     * <guid>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-
     * archive/</guid>
     * <description><![CDATA[/test-suite/3.0.0/cases-archive]]></description>
     * <pubDate>Fri, 17 May 2013 19:38:31 +0000</pubDate>
     * <files:sf-file-id
     * xmlns:files="http://sourceforge.net/api/files.rdf#">8337375
     * </files:sf-file-id>
     * <media:content xmlns:media="http://video.search.yahoo.com/mrss/" type=""
     * url=
     * "http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-archive/download"
     * filesize=""><media:title>test-suite</media:title><media:hash
     * algo="md5"></media:hash></media:content>
     * </item>
     * <item>
     * <title><![CDATA[/test-suite/3.0.0/cases-archive/sbml-test-cases-2013-06-
     * 06.zip]]></title>
     * <link>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-
     * archive/sbml-test-cases-2013-06-06.zip/download</link>
     * <guid>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-
     * archive/sbml-test-cases-2013-06-06.zip/download</guid>
     * <description><![CDATA[/test-suite/3.0.0/cases-archive/sbml-test-cases-
     * 2013-06-06.zip]]></description>
     * <pubDate>Thu, 06 Jun 2013 19:45:48 +0000</pubDate>
     * <files:sf-file-id
     * xmlns:files="http://sourceforge.net/api/files.rdf#">8485257
     * </files:sf-file-id>
     * <files:extra-info
     * xmlns:files="http://sourceforge.net/api/files.rdf#">empty (Zip archive
     * data)</files:extra-info>
     * <media:content xmlns:media="http://video.search.yahoo.com/mrss/"
     * type="application/zip; charset=binary" url=
     * "http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/cases-archive/sbml-test-cases-2013-06-06.zip/download"
     * filesize="42819933"><media:title>test-suite</media:title><media:hash
     * algo="md5">93d19b51b33a6f0578db789fd25a146c</media:hash></media:content>
     * </item>
     * <item>
     * <title><![CDATA[/test-suite/3.0.0/test-runner/linux]]></title>
     * <link>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-
     * runner/linux/</link>
     * <guid>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-
     * runner/linux/</guid>
     * <description><![CDATA[/test-suite/3.0.0/test-runner/linux]]></description
     * >
     * <pubDate>Fri, 17 May 2013 19:38:49 +0000</pubDate>
     * <files:sf-file-id
     * xmlns:files="http://sourceforge.net/api/files.rdf#">8337381
     * </files:sf-file-id>
     * <media:content xmlns:media="http://video.search.yahoo.com/mrss/" type=""
     * url=
     * "http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-runner/linux/download"
     * filesize=""><media:title>test-suite</media:title><media:hash
     * algo="md5"></media:hash></media:content>
     * </item>
     * <item>
     * <title><![CDATA[/test-suite/3.0.0/test-runner/linux/SBMLTestRunner-3.0.0-
     * linux-x64-installer.run]]></title>
     * <link>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-
     * runner/linux/SBMLTestRunner-3.0.0-linux-x64-installer.run/download</link>
     * <guid>http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-
     * runner/linux/SBMLTestRunner-3.0.0-linux-x64-installer.run/download</guid>
     * <description><![CDATA[/test-suite/3.0.0/test-runner/linux/SBMLTestRunner-
     * 3.0.0-linux-x64-installer.run]]></description>
     * <pubDate>Thu, 06 Jun 2013 19:42:57 +0000</pubDate>
     * <files:sf-file-id
     * xmlns:files="http://sourceforge.net/api/files.rdf#">8485251
     * </files:sf-file-id>
     * <files:extra-info xmlns:files="http://sourceforge.net/api/files.rdf#">ELF
     * 64-bit LSB executable, x86-64 (GNU/Linux)</files:extra-info>
     * <media:content xmlns:media="http://video.search.yahoo.com/mrss/"
     * type="application/x-executable; charset=binary" url=
     * "http://sourceforge.net/projects/sbml/files/test-suite/3.0.0/test-runner/linux/SBMLTestRunner-3.0.0-linux-x64-installer.run/download"
     * filesize="49740935"><media:title>test-suite</media:title><media:hash
     * algo="md5">095f40dcd18829d01326f0bceadd4e1a</media:hash></media:content>
     * </item>
     * ...
     * <doap:Project xmlns:doap="http://usefulinc.com/ns/doap#"
     * name="Systems Biology Markup Language (SBML)" description=
     * "The Systems Biology Markup Language (SBML) is an XML-based description language for representing computational models in systems biology. Visit the project web site to learn more."
     * ><sf:id
     * xmlns:sf="http://sourceforge.net/api/sfelements.rdf#">71971</sf:id
     * ></doap:Project>
     * </channel>
     * </rss>
     * 
     */
    public static NodeList getRSSFeedContents(HttpURLConnection connection)
    {
        if (connection == null) return null;
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) return null;
            Document doc = db.parse(inputStream);
            return doc.getElementsByTagName("item");
        }
        catch (SAXParseException ex)
        {
            // This can happen when the network is screwed up and we get a
            // connection, but what we actually read isn't what we expect.
            return null;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Returns test case archives that can be found among the last 100
     * releases of the Test Suite.
     * 
     * @return list of archive urls
     */

    public static Vector<String> getCasesArchiveURLs()
    {
        return getCasesArchiveURLs(getRSSFeedContents(), null);
    }


    /**
     * Returns test case archives that can be found among the last 100
     * releases of the Test Suite.
     * 
     * @return list of archive urls
     */

    public static Vector<String> getCasesArchiveURLs(Date date)
    {
        return getCasesArchiveURLs(getRSSFeedContents(), date);
    }


    /**
     * Returns test case archives that can be found among the last 100
     * releases of the Test Suite and are newer than the given @p date.
     * 
     * @param date
     *            the cutoff day, only archives newer than this date will be
     *            included
     * 
     * @return a vector of URLs, or null if an error occurred while trying
     *         to contact the sf.net servers.
     */
    public static Vector<String>
            getCasesArchiveURLs(NodeList contents, Date date)
    {
        if (contents == null) return null;
        Vector<String> result = new Vector<String>();
        try
        {
            DateFormat df = new SimpleDateFormat(
                                                 "EEE, dd MMM yyyy kk:mm:ss zzz");
            for (int i = 0; i < contents.getLength(); ++i)
            {
                Element current = (Element) contents.item(i);
                if (current == null) continue;
                String title = getTextFromTag(current, "title");
                if (!title.contains("sbml-test-cases-")) continue;
                if (date != null)
                {
                    String pubDateString = getTextFromTag(current, "pubDate");
                    // deal with a bug at SF, that contains an invalid UTC date
                    if (pubDateString.endsWith("UT"))
                        pubDateString = pubDateString + "C";
                    Date published = df.parse(pubDateString);
                    // skip all releases before the date
                    if (published.compareTo(date) <= 0) continue;
                }
                result.add(getTextFromTag(current, "link"));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
        return result;
    }


    /**
     * Given a particular link URL, returns the size (if any) reported in the
     * RSS item from sf.net.
     * 
     * @return the size if found, or null if no size found.
     */
    public static int getCasesArchiveSize(String linkURL)
    {
        return getCasesArchiveSize(getRSSFeedContents(), linkURL);
    }


    /**
     * Given a particular link URL, returns the size (if any) reported in the
     * RSS item from sf.net.
     * 
     * @return the size if found, or null if no size found.
     */
    public static int getCasesArchiveSize(NodeList contents, String linkURL)
    {
        if (linkURL == null || contents == null) return -1;
        try
        {
            for (int i = 0; i < contents.getLength(); ++i)
            {
                Element current = (Element) contents.item(i);
                if (current == null) continue;
                if (linkURL.equals(getTextFromTag(current, "link")))
                {
                    NodeList mc = current.getElementsByTagName("media:content");
                    if (mc == null) return -1;
                    NamedNodeMap attributes = mc.item(0).getAttributes();
                    if (attributes == null) return -1;
                    Node filesizeAttrib = attributes.getNamedItem("filesize");
                    if (filesizeAttrib == null) return -1;
                    String value = filesizeAttrib.getNodeValue();
                    if (value == null || value.length() == 0) return -1;
                    return Integer.parseInt(value);
                }
            }
            return -1;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }


    /**
     * Return the inner text from the given tag
     * 
     * @param current
     *            the current element
     * @param tag
     *            the tag name
     * @return the text inside the tag, or ""
     */
    private static String getTextFromTag(Element current, String tag)
    {
        if (current == null) return "";
        String result = "";
        NodeList nodes = current.getElementsByTagName(tag);
        if (nodes.getLength() > 0)
            result = ((Element) nodes.item(0)).getTextContent();
        return result;
    }


    /**
     * Download the url, and return the content as byte array
     * 
     * @param url
     *            the url to download
     * @return the content as byte array, or null
     */
    public static byte[] readFileFromUrl(String url)
    {
        try
        {
            return readFileFromUrl(new URL(url));
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }


    /**
     * Download the url, and return the content as byte array
     * 
     * @param url
     *            the url to download
     * @return the content as byte array, or null
     */
    public static byte[] readFileFromUrl(URL url)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = new BufferedOutputStream(bos);
        downloadUrlToStream(url, out, null, null);
        return bos.toByteArray();
    }


    /**
     * Download the url to the local file
     * 
     * @param url
     *            the url to download
     * @param localFile
     *            the local file to save the url under
     * @return true if operation succeeded, false if it failed or was cancelled
     */
    public static boolean downloadFile(URL url, File localFile)
        throws FileNotFoundException
    {
        FileOutputStream fos = new FileOutputStream(localFile);
        OutputStream out = new BufferedOutputStream(fos);
        return downloadUrlToStream(url, out, null, null);
    }


    /**
     * Download the url to the local file
     * 
     * @param url
     *            the url to download
     * @param localFile
     *            the local file to save the url under
     * @return true if operation succeeded, false if it failed or was cancelled
     */
    public static boolean downloadFile(String url, File localFile)
        throws FileNotFoundException, MalformedURLException
    {
        return downloadFile(new URL(url), localFile);
    }


    /**
     * Download the test suite archive under the given URL, and initialize the
     * test suite from that archive
     * 
     * @param url
     *            the url to the test suite archive
     */
    public static void downloadReleaseArchiveAndInitialize(String url)
    {
        try
        {
            File temp = File.createTempFile("download", "zip");
            downloadFile(url, temp);
            unzipArchive(temp);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * Download the url and save the content to the given output stream.
     * 
     * @param url
     *            the url to download
     * @param out
     *            the stream to write to
     * @param cancelCallback
     *            a callback object to test for cancelling the download
     * @param updateCallback
     *            a callback object to invoke each time a multiple of the
     *            buffer size is read. Callers can use getStreamReadBufferSize()
     *            to find out the buffer size.
     * 
     * @return true if operation succeeded, false if it failed or was cancelled
     */
    public static boolean downloadUrlToStream(URL url, OutputStream out,
                                              CancelCallback cancelCallback,
                                              UpdateCallback updateCallback)
    {
        boolean result = true;
        InputStream in = null;
        try
        {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(NET_CONNECTION_TIMEOUT);
            conn.setReadTimeout(NET_READ_TIMEOUT);
            in = conn.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead;
            long bytesWritten = 0;
            long multiples = 0;

            while ((bytesRead = in.read(buffer, 0, BUFFER_SIZE)) != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;

                // We may not get the number of bytes we ask for in a read.
                // Thus, we need to track when we reach BUFFER_SIZE amounts.

                long current = bytesWritten / BUFFER_SIZE;
                if (current > multiples)
                {
                    multiples = current;
                    if (updateCallback != null) updateCallback.update();
                    if (cancelCallback != null && multiples % 8 == 0
                        && cancelCallback.cancellationRequested())
                    {
                        result = false;
                        break;
                    }
                }
            }
        }
        catch (SocketTimeoutException ex)
        {
            // Network timeout.
            result = false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ioe)
            {}
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException ioe)
            {}

        }
        return result;
    }


    /**
     * Utility function copying streams
     * 
     * @param in
     *            input stream
     * @param bufferedOutputStream
     *            output stream
     * @throws IOException
     *             io exceptions
     */
    public static final void
            copyInputStream(InputStream in,
                            BufferedOutputStream bufferedOutputStream)
                throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = in.read(buffer)) >= 0)
            bufferedOutputStream.write(buffer, 0, len);
        in.close();
        bufferedOutputStream.close();
    }


    /**
     * recursively deletes the given file
     * 
     * @param file
     *            the file or directory to delete
     * @throws IOException
     *             io exception
     */
    public static void delete(File file)
        throws IOException
    {
        if (file == null || !file.exists()) return;
        if (file.isDirectory())
        {
            for (File c : file.listFiles())
                delete(c);
        }
        if (!file.delete())
            throw new FileNotFoundException("Failed to delete file: " + file);
    }


    /**
     * @return the directory of the bundled test suite
     */
    public static File getInternalTestSuiteDir(boolean createIfMissing)
    {
        File destinationDir = new File(getUserDir() + File.separator
                                       + ".test-suite");
        // Don't blindly assume we can write in the destination directory.
        if (!destinationDir.exists() && createIfMissing)
            try
            {
                destinationDir.mkdirs();
            }
            catch (Exception e)
            {
            }
        return destinationDir;
    }


    /**
     * @return the directory of the bundled test suite
     */
    public static File getInternalTestSuiteDir()
    {
        return getInternalTestSuiteDir(false);
    }

    /**
     * Parses the given level / version string in the formats:
     * 
     * - level[.]version
     * - [l|L]level[v|V]version
     * 
     * and returns a two element integer array representing level and version
     * 
     * @param levelVersion
     *            the level and version string
     * @return int array with the level and version
     */
    public static int[] getLevelAndVersion(String levelVersion)
    {
        int[] result = new int[2];
        String temp[] = levelVersion.split("\\.|l|L|v|V| ");
        try
        {
            if (temp.length == 2)
            {
                result[0] = Integer.parseInt(temp[0]);
                result[1] = Integer.parseInt(temp[1]);
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error in getLevelAndVersion with: '"
                + levelVersion + "'");
            ex.printStackTrace();
        }
        /*
         * Vector<String> temp = (Vector<String>) split(levelVersion, new char[]
         * {
         * '.','l', 'L', 'v', 'V'});
         * if (temp.size() == 2)
         * {
         * result[0] = Integer.parseInt(temp.get(0));
         * result[1] = Integer.parseInt(temp.get(1));
         * }
         */
        return result;
    }


    /**
     * Returns the snippet of contents that starts with 'start' and ends with
     * 'end'
     * 
     * @param contents
     *            the contents
     * @param start
     *            the start element
     * @param end
     *            the end element
     * @return the snippet between start and end
     */
    public static String getSnippet(String contents, String start, String end)
    {
        try
        {
            int startIndex = contents.indexOf(start) + start.length();
            int endIndex = contents.indexOf(end, startIndex);

            String result = contents.substring(startIndex, endIndex).trim();

            Collection<String> lines = split(result, new char[] {'\n'}, true);
            StringBuilder builder = new StringBuilder();
            for (String item : lines)
            {
                builder.append(item.trim() + " ");
            }
            return builder.toString();
        }
        catch (Exception ex)
        {
            return "";
        }

    }


    /**
     * @return the user directory (user.home) on OSX / Linux and %APPDATA% on
     *         windows.
     */
    public static String getUserDir()
    {
        String userDir = System.getenv("APPDATA");
        if (userDir == null) userDir = System.getProperty("user.home");
        return userDir;
    }


    /**
     * Returns true, if the string is null or empty
     * 
     * @param string
     *            the string
     * @return boolean indicating whether the string is null or empty
     */
    public static boolean isNullOrEmpty(String string)
    {
        return string == null || string.length() == 0;
    }


    /**
     * Opens the given file with the associated application
     * 
     * @param file
     *            the file to open
     */
    public static void openFile(File file)
    {
        if (file == null || !file.exists()) return;
        try
        {
            Desktop.getDesktop().open(file);
        }
        catch (IOException e)
        {

        }
    }


    /**
     * Parses the given string and returns its double value (or 0.0 in case of
     * error)
     * 
     * @param value
     *            the string to parse
     * @return the strings value as double
     */
    public static double parseDouble(String value)
    {
        try
        {
            return Double.parseDouble(value.trim());
        }
        catch (Exception ex)
        {
            return 0.0;
        }
    }


    /**
     * Parses the given string and returns its int value (or 0 in case of error)
     * 
     * @param value
     *            the string to parse
     * @return the strings value as int
     */
    public static int parseInt(String value)
    {
        try
        {
            return Integer.parseInt(value.trim());
        }
        catch (Exception ex)
        {
            return 0;
        }
    }


    /**
     * Read the given file and return its contents as string
     * 
     * @param file
     *            the file to read
     * @return its contents as string
     */
    public static String readAllText(File file)
    {
        if (file == null || !file.exists()) return null;

        BufferedReader reader;
        StringBuilder builder = new StringBuilder();
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line + "\n");
            reader.close();
        }
        catch (Exception e)
        {}
        return builder.toString();

    }


    /**
     * Sleep for the number of milliseconds
     * 
     * @param millies
     *            the number of milliseconds to sleep
     */
    public static void sleep(int millies)
    {
        try
        {
            Thread.sleep(millies);
        }
        catch (Exception e)
        {}

    }


    /**
     * Splits the given string by whitespaces
     * 
     * @param snippet
     *            the snippet to split
     * @return the pieces of the snippet
     */
    public static Collection<String> split(String snippet)
    {
        return split(snippet, new char[] {',', ' ', '\n'});
    }


    /**
     * Splits the given snippet by all given characters
     * 
     * @param snippet
     *            the snippet to split
     * @param characters
     *            all characters to split
     * @return the pieces of the snippet
     */
    public static Collection<String> split(String snippet, char[] characters)
    {
        return split(snippet, characters, true);
    }


    /**
     * Splits the given snippet by all given characters, removing all empty
     * elements
     * 
     * @param snippet
     *            the snippet to split
     * @param characters
     *            the characters to split by
     * @param removeEmptyEntries
     *            boolean indicating whether empty elements ought to be removed
     * @return the pieces of the snippet
     */
    public static Collection<String> split(String snippet, char[] characters,
                                           boolean removeEmptyEntries)
    {
        Vector<String> result = new Vector<String>();
        if (characters == null || characters.length == 0)
        {
            result.add(snippet);
        }
        else
        {
            for (int i = 1; i < characters.length; i++)
                snippet = snippet.replace(characters[i], characters[0]);

            result.addAll(Arrays.asList(snippet.split("" + characters[0])));
        }

        if (removeEmptyEntries)
        {
            for (int i = result.size() - 1; i >= 0; i--)
            {
                String current = result.get(i);
                if (current == null || current.length() == 0
                    || current.trim().equalsIgnoreCase("")) result.remove(i);
            }
        }

        return result;
    }


    /**
     * Returns the items of the given vector as comma separated list
     * 
     * @param items
     *            the vector to flatten
     * @return string of comma separated items
     */
    public static String toString(Vector<String> items)
    {
        if (items == null || items.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.size(); i++)
        {
            builder.append(items.get(i));
            if (i + 1 < items.size()) builder.append(", ");
        }
        return builder.toString();
    }


    /**
     * Unzips the given file
     * 
     * @param file
     *            the file to unzip
     */
    public static boolean unzipArchive(File file)
    {
        return unzipArchive(file, null);
    }


    /**
     * Unzips the given file, adhering to cancallation callback
     * 
     * @param file
     *            the file to unzip
     * @param callback
     *            a cancellation callback
     */
    public static boolean unzipArchive(File file, CancelCallback callback)
    {
        if (!file.exists()) return false;

        File destinationDir = getInternalTestSuiteDir(false);
        try
        {
            delete(destinationDir);
        }
        catch (IOException e)
        {
            return false;
        }

        int ncount = 0;
        try
        {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                File currentFile = new File(destinationDir, entry.getName());
                if (entry.isDirectory())
                {
                    if (!currentFile.exists()) currentFile.mkdirs();
                    continue;
                }
                if (currentFile.exists()) currentFile.delete();

                copyInputStream(zipFile.getInputStream(entry),
                                new BufferedOutputStream(
                                                         new FileOutputStream(
                                                                              currentFile)));
                ncount++;
                if (ncount % 100 == 0 && callback != null)
                    if (callback.cancellationRequested()) return false;
            }
            zipFile.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Writes the given string into the given file
     * 
     * @param file
     *            the file to write to
     * @param content
     *            the content to write
     */
    public static void writeAllText(File file, String content)
    {
        BufferedWriter writer;
        try
        {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {}
    }


    /**
     * Return a date read from our special archive date file.
     * 
     * @return a date, read as YYYY-MM-DD.
     */
    public static Date readArchiveDateFile(File dateFile)
    {
        if (!dateFile.exists() || !dateFile.canRead()) return null;

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(dateFile));
            try
            {
                return readArchiveDateFile(br);
            }
            finally
            {
                br.close();
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * Return a date read from our special archive date file.
     * 
     * @return a date, read as YYYY-MM-DD.
     */
    public static Date readArchiveDateFile(File dir, String filename)
    {
        return readArchiveDateFile(new File(dir, filename));
    }


    /**
     * Return a date read from our special archive date file.
     * 
     * @return a date, read as YYYY-MM-DD.
     */
    public static Date readArchiveDateFileStream(InputStream is)
    {
        try
        {
            Reader reader = new InputStreamReader(is);
            try
            {
                return readArchiveDateFile(reader);
            }
            finally
            {
                reader.close();
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static Date readArchiveDateFile(Reader reader)
    {
        try
        {
            char[] buffer = new char[10];
            reader.read(buffer, 0, 10);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = formatter.parse(new String(buffer));

            // Problem: we store only a date in the file, without hh:mm:ss,
            // which causes the Date object to be created with 00:00:00.
            // This throws off date comparisons. Solution: we manually
            // set the time to 23:59:59.

            theDate.setHours(23);
            theDate.setMinutes(59);
            theDate.setSeconds(59);
            return theDate;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static String archiveDateToString(Date date)
    {
        return String.format("%4d-%02d-%02d", date.getYear() + 1900,
                             date.getMonth() + 1, date.getDate());
    }


    /**
     * Remove a file by putting it in the trash if possible, and deleting
     * it outright if no trash is available.
     */
    public static void deleteFile(File file)
    {
        try
        {
            FileUtils fileUtils = FileUtils.getInstance();
            if (fileUtils.hasTrash())
                fileUtils.moveToTrash( new File[] { file });
        }
        catch (IOException ioe)
        {
            file.delete();
        }
    }
}
