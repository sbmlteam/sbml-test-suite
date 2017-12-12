//
// @file   UIUtils.java
// @brief  Utilities for the package org.sbml.testsuite.ui
// @author Michael Hucka
// @date   2013-01-07 <mhucka@caltech.edu>
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

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarFile;
import java.util.prefs.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * Utility class for org.sbml.testsuite.ui
 */
public class UIUtils
{
    /**
     * Return a resource we have embedded in our application.
     */
    public static Image getImageResource(String fileName)
    {
        return SWTResourceManager.getImage(UIUtils.class, 
            "/org/sbml/testsuite/ui/resources/" + fileName);
    }


    /**
     * Return a resource we have embedded in our application.
     */
    public static InputStream getFileResourceStream(String fileName)
    {
        return UIUtils.class.getResourceAsStream(
            "/org/sbml/testsuite/ui/resources/" + fileName);
    }


    /**
     * Return a file resource on the file system relative to our
     * JAR file.
     */
    public static File getFileResource(String file)
    {
        URL url = UIUtils.class.getResource(file);
        if (url != null)
        {
            try
            {
                return new File(url.toURI());
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }


    /**
     * Return a file object pointing to our jar file.
     */
    public static JarFile getJarFile()
    {
        try
        {
            URL url = Program.class.getProtectionDomain().getCodeSource().getLocation();
            String decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
            return new JarFile(decodedPath);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static KeyListener createCloseKeyListener(final Shell shell)
    {
        return new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) { return; }
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (isModifierKey(e) && e.keyCode == 'w'
                    && shell != null && !shell.isDisposed())
                    shell.close();      // Will invoke shell close() listener.
            }
        };
    }


    public static Listener createEscapeKeyListener(final Shell shell)
    {
        return new Listener() {
            @Override
            public void handleEvent (final Event event)
            {
                if (event.detail == SWT.TRAVERSE_ESCAPE
                    && shell != null && !shell.isDisposed())
                    shell.close();
            }
        };
    }


    public static Listener createShellCloseListener(final Shell shell)
    {
        return new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                if (shell != null && !shell.isDisposed())
                    shell.dispose();
            }
        };
    }


    /**
     * Add a MouseListener to all children of the given control.
     * Code based on http://stackoverflow.com/a/7226876/743730
     */
    public static void addMouseListenerRecursively(Control control,
                                                   MouseListener listener)
    {
        control.addMouseListener(listener);
        if (control instanceof Composite)
        {
            for (final Control c : ((Composite) control).getChildren())
                addMouseListenerRecursively(c, listener);
        }
    }


    public static void addKeyListenerRecursively(Control control,
                                                 Listener listener)
    {
        control.addListener(SWT.KeyDown, listener);
        if (control instanceof Composite)
        {
            for (final Control c : ((Composite) control).getChildren())
                addKeyListenerRecursively(c, listener);
        }
    }


    public static void addCancelKeyListenerRecursively(final Shell shell)
    {
        Listener listener = new Listener() {
            @Override
            public void handleEvent (final Event e)
            {
                if (isMacOSX())
                {
                    if (isModifier(e) && e.keyCode == '.'
                        && shell != null && !shell.isDisposed())
                        shell.close();
                }
            }
        };
        addKeyListenerRecursively(shell, listener);
    }


    /**
     * Returns a font object based on the default button font.
     *
     * I don't understand why, but on Mac OS X, the label font size is not
     * the same as the button font size.  This looks wrong, as if it's our
     * mistake and not the defaults.  To compensate, we find out the button
     * font size and then set the labels to the same size.  This method
     * is a convenience function to get the font object.
     */
    public static final Font getDefaultLabelFont()
    {
        Shell tmpShell = new Shell();
        Button tmpButton = new Button(tmpShell, SWT.NONE);
        FontData[] tmpFontData = tmpButton.getFont().getFontData();
        tmpButton.dispose();
        tmpShell.dispose();
        final Display display = Display.findDisplay(Thread.currentThread());
        final Font newFont = new Font(display, tmpFontData);
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (newFont != null && !newFont.isDisposed())
                    newFont.dispose();
            }
        });
        return newFont;
    }


    public static final Color getDefaultLabelForegroundColor()
    {
        Shell tmpShell = new Shell();
        Label tmpLabel = new Label(tmpShell, SWT.NONE);
        Color color = tmpLabel.getForeground();
        tmpLabel.dispose();
        tmpShell.dispose();
        return color;
    }


    public static final Color getInactiveTextColor()
    {
        return SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
    }


    public static final Color createColor(int r, int g, int b)
    {
        final Display display = Display.findDisplay(Thread.currentThread());
        final Color color = new Color(display, r, g, b);
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (color != null && !color.isDisposed())
                    color.dispose();
            }
        });
        return color;
    }


    public static final int getDefaultFontHeight()
    {
        Shell tmpShell = new Shell();
        Label tmpLabel = new Label(tmpShell, SWT.NONE);
        FontData[] tmpFontData = tmpLabel.getFont().getFontData();
        tmpLabel.dispose();
        tmpShell.dispose();
        return tmpFontData[0].getHeight();
    }


    /**
     * Takes a point size and returns an adjusted value based on the DPI of
     * the current device.  The numbers were empirically determined using two
     * systems with 72 and 96 dpi values.  The adjustments are not done on
     * Macs because something in the system (SWT? underlying Mac OS X font 
     * libraries? don't know) adjusts the sizes even for Retina displays.
     */
    public static final int scaledFontSize(int pt)
    {
        if (isMacOSX())
            return pt;
        
        final Display display = Display.findDisplay(Thread.currentThread());
        final Point dpi       = display.getDPI();
        final Double scaled   = pt * 72.0/dpi.y * (0.0027777778 * dpi.y + 0.8);
        return scaled.intValue();
    }


    /**
     * Returns a font with a rescaled size based on the DPI of the current
     * device.
     */
    public static Font getFont(String name, int size, int style)
    {
        return SWTResourceManager.getFont(name, scaledFontSize(size), style);
    }


    public static final Font createStyledFont(Font font, int style)
    {
        if (font == null) return null;
        FontData[] fontData = font.getFontData();
        for (int i = 0; i < fontData.length; ++i)
            fontData[i].setStyle(style);
        final Display display = Display.findDisplay(Thread.currentThread());
        final Font newFont = new Font(display, fontData);
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (newFont != null && !newFont.isDisposed())
                    newFont.dispose();
            }
        });
        return newFont;
    }


    public static Font createResizedFont(Font font, int sizeDifference)
    {
        if (font == null) return null;
        FontData[] fontData = font.getFontData();
        for (int i = 0; i < fontData.length; ++i)
            fontData[i].setHeight(fontData[i].getHeight() + sizeDifference);
        final Display display = Display.findDisplay(Thread.currentThread());
        final Font newFont = new Font(display, fontData);
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (newFont != null && !newFont.isDisposed())
                    newFont.dispose();
            }
        });
        return newFont;
    }


    public static Font createResizedFont(String name, int style, 
                                         int sizeDifference)
    {
        Shell tmpShell = new Shell();
        Label tmpLabel = new Label(tmpShell, SWT.NONE);
        FontData[] tmpFontData = tmpLabel.getFont().getFontData();
        int defaultHeight = tmpFontData[0].getHeight();
        Font font = SWTResourceManager.getFont(name, defaultHeight, style);
        if (font == null) return null;

        // Bogus, but I don't know what else to do about huge fonts on Ubuntu:
        if (isLinux() && defaultHeight >= 11) sizeDifference -= 2;

        FontData[] fontData = font.getFontData();
        for (int i = 0; i < fontData.length; ++i)
        {
            fontData[i].setHeight(fontData[i].getHeight() + sizeDifference);
            fontData[i].setStyle(style);
        }

        tmpLabel.dispose();
        tmpShell.dispose();
        final Font newFont = new Font(font.getDevice(), fontData);
        Display.getCurrent().addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (newFont != null && !newFont.isDisposed())
                    newFont.dispose();
            }
        });
        return newFont;
    }


    /**
     * Create a cursor object, and hook up a dispose event to delete it
     * when the application exits.
     */
    public static final Cursor createCursor(int type)
    {
        final Display display = Display.findDisplay(Thread.currentThread());
        final Cursor cursor = new Cursor(display, type);
        display.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event)
            {
                if (cursor != null && !cursor.isDisposed())
                    cursor.dispose();
            }
        });
        return cursor;
    }


    /**
     * @return true if running on OS X
     */
    public final static boolean isMacOSX()
    {
        final String osName = System.getProperty("os.name");
        return osName.startsWith("Mac OS X");
    }


    /**
     * @return true if running on Linux
     */
    public final static boolean isLinux()
    {
        final String osName = System.getProperty("os.name");
        return osName.startsWith("Linux");
    }


    /**
     * @return true if running on Windows
     */
    public final static boolean isWindows()
    {
        final String osName = System.getProperty("os.name");
        return osName.startsWith("Windows");
    }


    /**
     * Returns true if the key press involved a modifier key.
     * 
     * This handles platform-specific combination, such as using control
     * on Windows vs Command on Mac OS X.
     *
     * @return true if the key had a modifier set, false otherwise.
     */
    public static boolean isModifierKey(KeyEvent e)
    {
        // SWT.MOD1 is supposed to be set to SWT.CONTROL on Windows and to
        // SWT.COMMAND on the Macintosh.
        return (e.stateMask & SWT.MOD1) == SWT.MOD1;
    }


    /**
     * Returns true if the key press involved a shift key.
     *
     * @return true if the key had shift set, false otherwise.
     */
    public static boolean isShiftKey(KeyEvent e)
    {
        return (e.stateMask & SWT.SHIFT) == SWT.SHIFT;
    }


    /**
     * Returns true if the key press involved a modifier key.
     * 
     * This handles platform-specific combination, such as using control on
     * Windows vs Command on Mac OS X.  This is different from
     * isModifierKey(...) because SWT's KeyEvent does not inherit from Event
     * and KeyEvent cannot be cast to Event.
     *
     * @return true if the key had a modifier set, false otherwise.
     */
    public static boolean isModifier(Event e)
    {
        // SWT.MOD1 is supposed to be set to SWT.CONTROL on Windows and to
        // SWT.COMMAND on the Macintosh.
        return (e.stateMask & SWT.MOD1) == SWT.MOD1;
    }


    /**
     * Returns true if the key press involved a shift key.
     *
     * @return true if the key had a modifier set, false otherwise.
     */
    public static boolean isShift(Event e)
    {
        return (e.stateMask & SWT.SHIFT) == SWT.SHIFT;
    }


    public static void saveWindow(Shell shell, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        String name = obj.getClass().getName();

        Point size = shell.getSize();
        prefs.putInt(name + ".width", size.x);
        prefs.putInt(name + ".height", size.y);

        Point location = shell.getLocation();
        prefs.putInt(name + ".x", location.x);
        prefs.putInt(name + ".y", location.y);
    }


    public static void restoreWindow(Shell shell, Object obj)
    {
        restoreWindow(shell, obj, null);
    }


    public static void restoreWindow(Shell shell, Object obj, Rectangle preferred)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        String name = obj.getClass().getName();

        int x = prefs.getInt(name + ".width", SWT.DEFAULT);
        int y = prefs.getInt(name + ".height", SWT.DEFAULT);
        if (x != SWT.DEFAULT || y != SWT.DEFAULT)
            shell.setSize(x, y);
        else if (preferred != null)
            shell.setSize(preferred.width, preferred.height);

        x = prefs.getInt(name + ".x", SWT.DEFAULT);
        y = prefs.getInt(name + ".y", SWT.DEFAULT);
        if (x != SWT.DEFAULT || y != SWT.DEFAULT)
            shell.setLocation(new Point(x, y));
        else if (preferred != null)
            shell.setLocation(preferred.x, preferred.y);
        else // Fall-back.
        {
            Monitor primary = shell.getDisplay().getPrimaryMonitor();
            Rectangle bounds = primary.getBounds();
            Rectangle rect = shell.getBounds();
            x = bounds.x + (bounds.width - rect.width) / 2;
            y = bounds.y + (bounds.height - rect.height) / 2;
            shell.setLocation(x, y);
        }
    }


    public static void saveBooleanPref(String prefName, boolean value, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        prefs.putBoolean(prefName, value);
    }


    public static boolean getBooleanPref(String prefName, boolean defaultValue,
                                         Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        return prefs.getBoolean(prefName, defaultValue);
    }


    public static void saveIntPref(String prefName, int value, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        prefs.putInt(prefName, value);
    }


    public static int getIntPref(String prefName, int defaultValue, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        return prefs.getInt(prefName, defaultValue);
    }


    public static void saveStringPref(String prefName, String value, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        prefs.put(prefName, value);
    }


    public static String getStringPref(String prefName, String defaultValue, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        return prefs.get(prefName, defaultValue);
    }


    public static void removePref(String prefName, Object obj)
    {
        Preferences prefs = Preferences.userNodeForPackage(obj.getClass());
        prefs.remove(prefName);
    }


    public static String stackTraceToString(Exception e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
