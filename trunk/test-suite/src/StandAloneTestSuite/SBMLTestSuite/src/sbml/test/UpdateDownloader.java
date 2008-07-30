// @file    UpdateDownloader.java
// @brief   UpdateDownloader class for SBML Standalone application
// @author  Kimberly Begley
// 

//
//<!---------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// 
// Copyright 2008      California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available at
// http://sbml.org/Software/SBML_Test_Suite/license.html
//------------------------------------------------------------------------- -->
// UpdateDownloader will download new SBML test case files from SourceForge.net
//
package sbml.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.SwingUtilities;

/**
 * UpdateDownloader will download new SBML test case files from SourceForge.net
 * 
 * 
 * @author Kimberly Begley
 * @version 2.0
 */
public class UpdateDownloader extends SwingWorker {

    private FileOutputStream fOut;
    private DataOutputStream out;
    private DataInputStream in;
    private static String timestampURL = "http://sbml.svn.sf.net/viewvc/*checkout*/sbml/trunk/test-suite/.cases-archive-date";
    private TestCaseUpdater tcu;
    /**
     * UpdateDownloader has one constructor
     * @param tcu takes a TestCaseUpdater as input
     */
    public UpdateDownloader(TestCaseUpdater tcu) {
        super();
        this.tcu = tcu;
    }

    @Override
    public Object construct() {
        String localtimestamp = "";
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                tcu.setProgressBarIndeterminate(true);
                tcu.appendToLog("Starting Update\n");
            }
        });

        try {
            // Looks for the timestamp file in the .sbmltestrunner directory - this should have the timestamp for the latest installed version of cases
            File localtimestampfile = new File(((String) System.getProperty("user.home")) + File.separator + ".sbmltestrunner" + File.separator + "sbml-test-suite" + File.separator + "timestamp");
            BufferedReader br = new BufferedReader(new FileReader(localtimestampfile));
            localtimestamp = br.readLine();
            final String ts = localtimestamp;
            if (localtimestamp.length() > 0) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.appendToLog("Local Timestamp: " + ts + "\n");
                        tcu.setLocalTimestamp(ts);
                    }
                });
            }
        } catch (FileNotFoundException e) {
            reportToDialog("Cannot read local timestamp"); 
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            reportToDialog("Cannot read local timestamp");
            e.printStackTrace();
            return false;
        }

        try {

            String timestamp = "";
            String servertimestamp = "";
            //read lines from .cases-archive-date

            BufferedReader buff = getReaderOnURL(new URL(timestampURL));

            servertimestamp = buff.readLine();
            if (servertimestamp != null) {
                timestamp = servertimestamp;
            }
            // now check if the current version is of a previous timestamp - if so proceed

            final String logLine = servertimestamp;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (logLine != null) {
                        tcu.appendToLog("Server Timestamp: " + logLine);
                    } else {
                        tcu.appendToLog("Unable to fetch timestamp, check log file for errors");
                    }
                    tcu.setNewTimestamp(logLine);
                }
            });

            boolean updaterequired;
            try {
                updaterequired = checkIfUpdateNecessary(servertimestamp, localtimestamp);
            } catch (NumberFormatException e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.setFinished("\nCould not parse timestamps\nUpdate failed");
                    }
                });

                return false;

            }
            if (updaterequired) {

                   URL cases_base = new URL("http://downloads.sf.net/sbml/");
                   String relativeURL = "sbml-test-cases-" + timestamp +".zip";
                   URL cf = new URL(cases_base, relativeURL);

               

                System.out.println(cf.toString());

                HttpURLConnection httpuc = getHttpURLConnectionFromURL(cf);

                final int downloadfilesize = httpuc.getContentLength();

                in = new DataInputStream(httpuc.getInputStream());


                fOut = new FileOutputStream(((String) System.getProperty("java.io.tmpdir")) + File.separator + "testoutputfile.zip");
                out = new DataOutputStream(fOut);

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.setProgressBarMax(downloadfilesize);
                    }
                });


                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.appendToLog("\nStarting download of " + downloadfilesize / (1024 * 1024) + "MB");
                        tcu.setProgressBarIndeterminate(false);

                    }
                });


                int progress = 0;

                int BUF_SIZE = 1024;
                byte[] buffer = new byte[BUF_SIZE];

                int len;

                while ((len = in.read(buffer, 0, BUF_SIZE)) > 0) {
                    fOut.write(buffer, 0, len);
                    progress += len;


                    final int pg = progress;
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            tcu.setProgressBarProgress(pg);
                        }
                    });
                }

                in.close();
                fOut.flush();
                fOut.close();

                
                try {
                    // unzip the files
                    FileInputStream newcasesfile = new FileInputStream(((String) System.getProperty("java.io.tmpdir")) + File.separator + "testoutputfile.zip");
                    ZipInputStream zipFile = new ZipInputStream(new BufferedInputStream(newcasesfile));
                    ZipEntry entry;
                    String userpath = System.getProperty("user.home");
                    File tests = new File(userpath + File.separator + ".sbmltestrunner");
                    String destinationDirectory = tests.getAbsolutePath();


                    while ((entry = zipFile.getNextEntry()) != null) {
                        int count;
                        byte data[] = new byte[2048];
                        BufferedOutputStream dest;
                        File destFile = new File(destinationDirectory, entry.getName());
                        File destinationParent = destFile.getParentFile();
                        destinationParent.mkdirs();
                        if (!entry.isDirectory()) {

                            FileOutputStream fout = new FileOutputStream(destFile);
                            dest = new BufferedOutputStream(fout, 2048);
                            while ((count = zipFile.read(data, 0, 2048)) != -1) {
                                dest.write(data, 0, count);
                            }
                            dest.flush();
                            dest.close();
                        }


                    }
                    // write the timestamp file for these cases - hardcoded right now but can edit to look up later
                    try {
                        File localtimestampcases = new File(userpath + File.separator + ".sbmltestrunner" + File.separator + "sbml-test-suite" + File.separator + "timestamp");
                        localtimestampcases.delete();
                        //Create file if it does not exist
                        BufferedWriter timestampout = new BufferedWriter(new FileWriter(localtimestampcases));
                        timestampout.write(timestamp);
                        timestampout.close();
                    } catch (IOException e) {
                        System.out.println("Caught IO Exception while trying to create timestamp file in user directory");
                    }

                } catch (IOException ex) {
                    System.out.println("Caught IOException while trying to unzip caes.");
                } 
                
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.appendToLog("\nDownload complete");
                        tcu.setProgressBarIndeterminate(false);
                        tcu.appendToLog("\nCases Unzipped");


                    }
                });

            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tcu.setFinished("\nNo Update required.");

                    }
                });

            }




        } catch (MalformedURLException e) {
            reportToDialog("\nPlease check the URL:\n" + e.toString());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            reportToDialog("\nCan't read from the Internet:\n" + e.toString());
            return false;
        }


        return true;

    }
    /** 
     * reportToDialog - appends the string to the dialog
     * @param message the string to append
     */
    private void reportToDialog(String message) {
        final String m = message;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tcu.appendToLog(m);
            }
        });
    }
    /**
     * checkIfUpdateNecessary
     * @param servertimestamp
     * @param localtimestamp
     * @return
     * @throws java.lang.NumberFormatException
     */
    private boolean checkIfUpdateNecessary(String servertimestamp, String localtimestamp) throws NumberFormatException {

        Calendar localdate = new GregorianCalendar();
        localdate.set(Integer.parseInt(localtimestamp.substring(0, 4)), Integer.parseInt(localtimestamp.substring(5, 7)), Integer.parseInt(localtimestamp.substring(8, 10)));
        Calendar serverdate = new GregorianCalendar();
        serverdate.set(Integer.parseInt(servertimestamp.substring(0, 4)), Integer.parseInt(servertimestamp.substring(5, 7)), Integer.parseInt(servertimestamp.substring(8, 10)));

        return serverdate.after(localdate);
    }
    /**
     * getReaderOnURL
     * @param inputurl
     * @return
     * @throws java.io.IOException
     */
    private BufferedReader getReaderOnURL(URL inputurl) throws IOException {
        HttpURLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        URL url = inputurl;

        urlConn = (HttpURLConnection) url.openConnection();

        if (!HttpURLConnection.getFollowRedirects()) {
            urlConn.setInstanceFollowRedirects(true);
        }
        inStream = new InputStreamReader(urlConn.getInputStream());
        buff = new BufferedReader(inStream);


        return buff;
    }
    /**
     * getHttpURLConnectionFromURL
     * @param inputurl
     * @return
     * @throws java.io.IOException
     */
    private HttpURLConnection getHttpURLConnectionFromURL(URL inputurl) throws IOException {
        HttpURLConnection urlConn = null;
        URL url = inputurl;

        urlConn = (HttpURLConnection) url.openConnection();

        if (!HttpURLConnection.getFollowRedirects()) {
            urlConn.setInstanceFollowRedirects(true);
        }
        return urlConn;
    }
}
