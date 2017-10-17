/*
 * Explanation from mhucka@caltech.edu:
 *
 * This file came from http://geosoft.no/software/, obtained on 2017-10-09.
 * Unfortunately, there is no author information on the site, but a whois
 * lookup on geosoft.no returns "GEOTECHNICAL SOFTWARE SERVICES Jacob Dreyer"
 * as the owner of the domain.  I could find no other information, other than
 * some job listings that imply geosoft.no is, indeed, located in Norway.
 *
 * I made a minor enhancement to this file to add the clearFiles() and
 * clearListeners() methods, and to change the Java package declaration.
 *
 * What follows is the original file header of this file:
 * ----------------------------------------------------------------------------
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA  02111-1307, USA.
 */

package org.sbml.testsuite.ui;

import java.util.*;
import java.io.File;
import java.lang.ref.WeakReference;


/**
 * Class for monitoring changes in disk files.
 * Usage:
 *
 *    1. Implement the FileListener interface.
 *    2. Create a FileMonitor instance.
 *    3. Add the file(s)/directory(ies) to listen for.
 *
 * fileChanged() will be called when a monitored file is created,
 * deleted or its modified time changes.
 *
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class FileMonitor
{
    private Timer timer_;
    private HashMap<File, Long> files_;
    private Collection<WeakReference<FileListener>> listeners_;


    /**
     * Create a file monitor instance with specified polling interval.
     *
     * @param pollingInterval  Polling interval in milli seconds.
     */
    public FileMonitor (long pollingInterval)
    {
        files_     = new HashMap<File, Long>();
        listeners_ = new ArrayList<WeakReference<FileListener>>();

        timer_ = new Timer (true);
        timer_.schedule (new FileMonitorNotifier(), 0, pollingInterval);
    }



    /**
     * Stop the file monitor polling.
     */
    public void stop()
    {
        timer_.cancel();
    }


    /**
     * Add file to listen for. File may be any java.io.File (including a
     * directory) and may well be a non-existing file in the case where the
     * creating of the file is to be trepped.
     * <p>
     * More than one file can be listened for. When the specified file is
     * created, modified or deleted, listeners are notified.
     *
     * @param file  File to listen for.
     */
    public void addFile (File file)
    {
        if (!files_.containsKey (file)) {
            long modifiedTime = file.exists() ? file.lastModified() : -1;
            files_.put (file, new Long (modifiedTime));
        }
    }



    /**
     * Remove specified file for listening.
     *
     * @param file  File to remove.
     */
    public void removeFile (File file)
    {
        files_.remove (file);
    }


    /**
     * Remove all files from the list being monitored.
     */
    public void clearFiles()
    {
        files_.clear();
    }


    /**
     * Add listener to this file monitor.
     *
     * @param fileListener  Listener to add.
     */
    public void addListener (FileListener fileListener)
    {
        // Don't add if its already there
        for (Iterator i = listeners_.iterator(); i.hasNext(); ) {
            WeakReference reference = (WeakReference) i.next();
            FileListener listener = (FileListener) reference.get();
            if (listener == fileListener)
                return;
        }

        // Use WeakReference to avoid memory leak if this becomes the
        // sole reference to the object.
        listeners_.add (new WeakReference<FileListener> (fileListener));
    }



    /**
     * Remove listener from this file monitor.
     *
     * @param fileListener  Listener to remove.
     */
    public void removeListener (FileListener fileListener)
    {
        for (Iterator i = listeners_.iterator(); i.hasNext(); ) {
            WeakReference reference = (WeakReference) i.next();
            FileListener listener = (FileListener) reference.get();
            if (listener == fileListener) {
                i.remove();
                break;
            }
        }
    }


    public void clearListeners()
    {
        listeners_.clear();
    }


    /**
     * This is the timer thread which is executed every n milliseconds
     * according to the setting of the file monitor. It investigates the
     * file in question and notify listeners if changed.
     */
    private class FileMonitorNotifier extends TimerTask
    {
        public void run()
        {
            // Loop over the registered files and see which have changed.
            // Use a copy of the list in case listener wants to alter the
            // list within its fileChanged method.
            Collection<File> files = new ArrayList<File> (files_.keySet());

            for (Iterator i = files.iterator(); i.hasNext(); ) {
                File file = (File) i.next();
                long lastModifiedTime = ((Long) files_.get (file)).longValue();
                long newModifiedTime  = file.exists() ? file.lastModified() : -1;

                // Chek if file has changed
                if (newModifiedTime != lastModifiedTime) {

                    // Register new modified time
                    files_.put (file, new Long (newModifiedTime));

                    // Notify listeners
                    for (Iterator j = listeners_.iterator(); j.hasNext(); ) {
                        WeakReference reference = (WeakReference) j.next();
                        FileListener listener = (FileListener) reference.get();

                        // Remove from list if the back-end object has been GC'd
                        if (listener == null)
                            j.remove();
                        else
                            listener.fileChanged (file);
                    }
                }
            }
        }
    }


    /**
     * Test this class.
     *
     * @param args  Not used.
     */
    public static void main (String args[])
    {
        // Create the monitor
        FileMonitor monitor = new FileMonitor (1000);

        // Add some files to listen for
        monitor.addFile (new File ("/home/jacob/test1.txt"));
        monitor.addFile (new File ("/home/jacob/test2.txt"));
        monitor.addFile (new File ("/home/jacob/"));

        // Add a dummy listener
        monitor.addListener (monitor.new TestListener());

        // Avoid program exit
        while (!false) ;
    }


    private class TestListener
        implements FileListener
    {
        public void fileChanged (File file)
        {
            System.out.println ("File changed: " + file);
        }
    }
}
