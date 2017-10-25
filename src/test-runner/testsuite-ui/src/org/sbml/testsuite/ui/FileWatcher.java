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

public class FileWatcher
{
    // Global variables.
    // ........................................................................

    private WatchService watcher;
    private HashMap<Path, FileListener> listenersMap;
    private Thread watcherThread;


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

    public FileWatcher()
    {
        listenersMap = new HashMap<Path, FileListener>();
    }


    /**
     * Set up a watcher on the directory @p path.  Close any previous watchers.
     */
    public void watchPath(Path path)
    {
        if (path == null)
            return;

        try
        {
            if (watcher != null)
                watcher.close();

            if (watcherThread != null)
            {
                watcherThread.interrupt();
                watcherThread = null;
            }

            watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            WatcherRunnable runner = new WatcherRunnable();
            watcherThread = new Thread(runner);
            watcherThread.start();
        }
        catch (Exception e)
        {
        }
    }


    public void watchPath(File path)
    {
        if (path == null)
            return;
        watchPath(path.toPath());
    }


    public void watchPath(String path)
    {
        if (path == null)
            return;

        watchPath(new File(path).toPath());
    }


    public void addListener(Path path, FileListener listener)
    {
        if (path == null || listener == null)
            return;
        listenersMap.put(path, listener);
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


    /**
     * Stop the file monitor polling.
     */
    public void stop()
    {
        watcherThread = null;
    }
}
