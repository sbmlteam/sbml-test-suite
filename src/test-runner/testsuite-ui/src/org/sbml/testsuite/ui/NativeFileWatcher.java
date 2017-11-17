//
// @file   NativeFileWatcher.java
// @brief  File watcher that uses Java NIO WatchService API
// @author Michael Hucka
// @date   Created 2017-11-16 <mhucka@caltech.edu>
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

package org.sbml.testsuite.ui;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

public class NativeFileWatcher implements FileWatcher
{
    // Global variables.
    // ........................................................................

    private WatchService watcher;
    private HashMap<Path, FileListener> listenersMap;
    private Thread watcherThread;
    private Path watchedPath;


    // Supporting classes.
    // ........................................................................

    private class WatcherRunnable implements Runnable
    {
        public void run()
        {
            if (watcher == null)
                return;

            Thread thisThread = Thread.currentThread();
            while (watcherThread == thisThread)
            {
                WatchKey key;
                try
                {
                    // Retrieves and removes the next watch key, waiting if
                    // none are yet present.
                    key = watcher.take();
                }
                catch (InterruptedException ex)
                {
                    break;
                }
                catch (ClosedWatchServiceException ex)
                {
                    // Happens if this watch service is closed, or it is
                    // closed while waiting for the next key.
                    break;
                }

                for (WatchEvent<?> event : key.pollEvents())
                {
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path pathModified = ev.context();
                    if (listenersMap.containsKey(pathModified))
                    {
                        WatchEvent.Kind<?> kind = event.kind();
                        FileListener listener = listenersMap.get(pathModified);
                        if (kind == ENTRY_CREATE)
                            listener.fileCreated();
                        else if (kind == ENTRY_MODIFY)
                            listener.fileModified();
                        else if (kind == ENTRY_DELETE)
                            listener.fileDeleted();
                    }
                }

                // Keys must be reset after being processed, or the key will
                // not receive additional events.
                boolean valid = key.reset();

                // If key.reset() returns false, the directory is inaccessible
                // perhaps due to being deleted.
                if (!valid)
                    break;
            }
        }

    }


    // Main code.
    // ........................................................................

    public NativeFileWatcher()
    {
        listenersMap = new HashMap<Path, FileListener>();
    }


    /**
     * Set up a watcher on the directory @p path.  Close any previous watchers.
     */
    public void initWatch(Path path)
    {
        if (path == null)
            return;

        try
        {
            if (watcher != null)
                watcher.close();
            watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            watchedPath = path;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void initWatch(File path)
    {
        if (path == null)
            return;
        initWatch(path.toPath());
    }


    public void initWatch(String path)
    {
        if (path == null)
            return;
        initWatch(new File(path).toPath());
    }


    public void startWatcherThread()
    {
        if (watcherThread == null)
        {
            WatcherRunnable runner = new WatcherRunnable();
            watcherThread = new Thread(runner);
            watcherThread.start();
        }
    }


    public void stopWatcherThread()
    {
        if (watcherThread != null)
        {
            watcherThread.interrupt();
            watcherThread = null;
        }
    }


    public void addListener(Path path, FileListener listener)
    {
        if (path == null || listener == null)
            return;
        // Need the path to be relative to the directory being watched.
        String relative = new File(watchedPath.toString()).toURI()
            .relativize(path.toFile().toURI()).getPath();
        listenersMap.put(new File(relative).toPath(), listener);
        startWatcherThread();
    }


    public void addListener(File path, FileListener listener)
    {
        if (path == null || listener == null)
            return;
        addListener(path.toPath(), listener);
    }


    public void addListener(String path, FileListener listener)
    {
        if (path == null || listener == null)
            return;
        addListener(new File(path).toPath(), listener);
    }


    public void removeListener(Path path)
    {
        if (path == null)
            return;
        listenersMap.remove(path);
        if (listenersMap.isEmpty())
            stopWatcherThread();
    }


    public void removeListener(File path)
    {
        if (path == null)
            return;
        removeListener(path.toPath());
    }


    public void removeListener(String path)
    {
        if (path == null)
            return;
        removeListener(new File(path).toPath());
    }


    public void clearPaths()
    {
        listenersMap.clear();
    }
}
