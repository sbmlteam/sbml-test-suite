//
// @file   MacStarter.java
// @brief  Wrapper application to start the real STS jar on OS X.
// @author Michael Hucka
// @date   Created 2017-06-09 <mhucka@caltech.edu>
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

import java.awt.SplashScreen;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * MacStarter is a small main class that is invoked first on OS X.
 *
 * BACKGROUND
 * ..........
 *
 * MacStarter does not (and MUST NEVER) use SWT like the rest of the SBML
 * Test Runner, so that the Java SplashScreen mechanism works.  The job of
 * this small main class is to (1) spawn another process to run the bundled
 * Java binary on the real SBML Test Runner main class (i.e.,
 * org.sbml.testsuite.ui.Program), and (2) close the splash screen after a
 * delay.
 *
 * You may think this is ridiculous, and I agree.  The reason this class
 * exists at all is because even on a fast OS X machine with an SSD, the Test
 * Runner takes an annoyingly long time before the first window appears --
 * and it's worse on slower machines.  (I *suspect* this has gotten worse on
 * more recent versions of OS X because we use the SWT widget set, but I
 * can't quantify this with any evidence.)  This long delay may confuse
 * people into thinking that something is wrong, so to help give the user a
 * clue that things are happening, the Test Runner displays a simple splash
 * screen.  Unfortunately, what should be a trivial task (showing a splash
 * screen) is ridiculously difficult for the following reasons:
 *
 *   1) The built-in Java splash screen mechanism does not work with SWT on
 *      OS X.  The following discussions and bug reports document this:
 *
 *     a) https://stackoverflow.com/q/44207024/743730
 *        archived copies: https://archive.is/WP0YX
 *                         https://perma.cc/KCQ3-VE6T
 *     b) https://stackoverflow.com/q/21022788/743730
 *        archived copies: https://archive.is/8B1aW
 *                         https://perma.cc/GP8N-MJBP
 *     c) https://bugs.openjdk.java.net/browse/JDK-8068815
 *        archived copies: https://archive.is/GTmKc
 *                         https://perma.cc/Q2M8-JKMB
 *
 *   2) If you create a splash screen in your application yourself, using the
 *      SWT widgets, it will appear at the same time as all the other windows
 *      are initialized.  This completely defeats the purpose of having the
 *      splash screen.
 *
 *   3) If you attempt to create a splash screen using AWT widgets in an
 *      application that also uses SWT widgets, you will run into thread
 *      management issues: the SWT widget calls have to be in a separate
 *      thread as the AWT calls.  But then the AWT code doesn't appear to
 *      work when you use the -XstartOnFirstThread JVM option that is
 *      necessary to use the SWT widgets.
 *
 * Separately, an advantage of using this wrapper is that it can invoke the
 * real sbmltestrunner.jar with the -XstartOnFirstThread option required by
 * SWT (for users who want to invoke the jar directly and not use the .app).
 * This may help users who may never have heard of the option and then
 * encounter the cryptic error that Java produces when an SWT-based application
 * is started without that obscure JVM flag.
 *
 * As far as I can tell, an installer-generator that creates a wrapper around
 * your Java application such as Intall4J just runs a small Java wrapper
 * program too, like this.  Rather than rely on a closed-source commercial
 * program, we may as well use our own wrapper.  (I did try to use Install4J,
 * but could not make the splash screen feature work anyway, and gave up
 * after a long time of trying.  Besides, Install4J is a powerful but complex
 * program, and for the sake of future maintainability of the SBML Test Suite
 * by other people, I think creating our own solution is better anyway.)
 *
 * Finally, the splash screen is currently shown for a set time period; it is
 * not closed based on when the underlying test runner program actually
 * starts up.  To change this, it would be necessary to find a good way of
 * detecting that the test runner has actually started up.  I decided that
 * looking for a semaphore file of some kind would be error-prone, and
 * anyway, the splash screen does not have to disappear exactly when the main
 * starts up -- just flashing it on the screen briefly is enough to
 * accomplish its purpose, which is to let the user know that the Test Runner
 * has indeed been started.
 *
 * INSTALLATION AND SETUP
 * ......................
 *
 * This relies on a number of other things being set up properly.
 *
 *   1) In order for Java to find the image for the splash screen, two copies
 *      of the image file need to be located in separate places to account
 *      for the different ways in which the test runner might be invoked:
 *
 *      a) The jar file itself needs to contain the image as a resource,
 *         and the manifest file has to have the SplashScreen-Image property
 *         set to the path to that file (relative to the jar file).  Then,
 *         if the jar file is invoked using "java -jar sbmltestrunner.jar",
 *         Java finds the image inside the jar file as expected.
 *
 *      b) A second copy has to be placed outside the jar file for when the
 *         Test Runner is invoked from the .app wrapper.  This is needed
 *         because the .app wrapper produced by javapackager will invoke the
 *         jar file with the -splash JVM option, but something about the .app
 *         startup approach causes Java to be unable to find the image file
 *         if it's inside the jar file.  (Or at least, I spent hours trying
 *         to make it work and failed.)  Putting the image file outside the
 *         jar file (but still inside the .app bundle) solves this problem.
 *
 *   2) The Info.plist file inside the .app bundle has to be modified from
 *      what javapackager produces.  The rules are in build.xml, but
 *      basically the modification is to add a JVM option for -splash to
 *      point to the image file for the splash screen.  (As noted above, this
 *      file is located outside the .jar file itself.)
 *
 *   3) Assuming the jar file manifest is set up properly (see build.xml),
 *      then the jar file could be invoked as
 *          java -jar sbmltestrunner.jar
 *      For this to work, the jar file manifest file has to set the
 *      Main-Class property to org.sbml.testsuite.ui.MacStarter.
 *
 *
 * CONFIGURATION
 * .............
 *
 * MacStarter understands a few configuration options, mainly to help with
 * debugging.  These can be set on the command line as JVM options:
 *
 *    -Dmacstarter.log=console
 *        Send logging output to the console instead of to a log file.
 *
 *    -Dmacstarter.log=/path/to/file
 *        Send logging output to the given file instead of to the default
 *        log file, which is /tmp/sbmltestrunner.log
 *
 *    -Dmacstarter.splash-duration=N
 *        Set the duration for displaying the splash screen to N seconds.
 *        (Default: 3 seconds.)
 */
public class MacStarter
{
    public final static void main(String[] args)
    {
        Logger log;

        // I could not get the command "java -jar our.jar SomeMainClass" to
        // start a specific main class: it always started the main class that
        // is defined in the jar manifest file.  Our manifest file has to set
        // the main class as this (MacStarter), which means that trying to
        // execute "java -jar our.jar SomeMainClass" from within MacStarter
        // leads to infinite loops.  To avoid this, MacStarter sets a property
        // when it runs the jar file, and if that property is set, it invokes
        // the real main class instead of running the jar file again.

        if (System.getProperty("macstarter.startmain", "notset").equals("notset"))
        {
            log = getLog(true);
            log.info("Running jar");
            runJar(log);
        }
        else
        {
            log = getLog(false);
            log.info("Running main");
            runMain(log);
        }
    }


    private static void runJar(final Logger log)
    {
        String java = System.getProperty("java.home") + "/bin/java";
        String jarPath = getLocation(MacStarter.class);
        String iconPath = new File(jarPath).getParent() + "/sbmltestrunner-icon.png";
        int splashDuration = getSplashDuration();

        log.info("Java binary: " + java);
        log.info("Jar path: " + jarPath);
        log.info("Icon path: " + iconPath);
        log.info("Splash duration: " + splashDuration);

        try
        {
            // Call on Java to start the real jar file.
            // Note the use of -splash:none.  This is because we need to set
            // the SplashScreen-Image property in the jar file manifest so
            // that if the user invokes the jar file manually (and not via
            // the app we create), they'll get the splash screen.  However,
            // unless we take precautions, that property will also cause the
            // splash screen to be shown when we invoke the code below,
            // leading to two splash screens.  The -splash:none is done to
            // block the splash screen when we run the jar from inside here.
            ProcessBuilder pb = new ProcessBuilder(java,
                                                   "-XstartOnFirstThread",
                                                   "-Xdock:icon=" + iconPath,
                                                   "-Xdock:name=SBML Test Runner",
                                                   "-Dapple.laf.useScreenMenuBar=true",
                                                   "-Dapple.awt.application.name=SBML Test Runner",
                                                   "-Dcom.apple.macos.use-file-dialog-packages=true",
                                                   "-Dcom.apple.macos.useScreenMenuBar=true",
                                                   "-Dcom.apple.mrj.application.apple.menu.about.name=SBML Test Runner",
                                                   "-Dcom.apple.smallTabs=true",
                                                   "-Dmacstarter.startmain=true",
                                                   "-splash:none",
                                                   "-jar", jarPath);

            Map<String, String> env = pb.environment();
            if (log != null) {
                log.info("Command: " + pb.command());
                for (Map.Entry<String, String> entry : env.entrySet()) {
                    log.info("Env var " + entry.getKey() + " = " + entry.getValue());
                }
            }
            pb.inheritIO().start();

            // Close the splash screen and exit this thread after a delay.
            TimerTask closerTask = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        closeSplashScreen(log);
                        System.exit(0);
                        Runtime.getRuntime().halt(0);
                    }
                };
            new java.util.Timer().schedule(closerTask, splashDuration * 1000);
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Trying to start subprocess", e);
        }
    }


    private static void runMain(final Logger log)
    {
        final Class<?> entryPoint = org.sbml.testsuite.ui.Program.class;
        final String[] args = new String[0];
        try
        {
            entryPoint.getMethod("main", String[].class).invoke(null, (Object) args);
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Unable to invoke main", e);
        }
    }


    private static void closeSplashScreen(final Logger log)
    {
        try
        {
            final SplashScreen splash = SplashScreen.getSplashScreen();
            if (splash != null)
                splash.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Unable to get SplashScreen", e);
        }
    }


    private static Logger getLog(boolean clearLog)
    {
        Logger log = Logger.getLogger(LOG_PREFIX);
        if (log == null)
        {
            // This shouldn't happen. Something is really wrong.
            System.err.println("MacStarter: failed to create Logger object.");
            return null;
        }

        // Check if we were told to log on the console.
        // Look for the following possibilities:
        //   macstarter.log=console
        //   macstarter.log=/path/to/file

        String logOption = System.getProperty("macstarter.log");
        boolean useConsole = false;
        File logFile = null;

        if (logOption != null)
        {
            if (logOption.equalsIgnoreCase("console"))
                useConsole = true;
            else
                logFile = new File(logOption);
        }

        if (! useConsole && logFile == null)
        {
            // We were not told to use the console, but weren't given a log
            // file.  We fall back to using our default log file.
            String path = "/tmp/" + DEFAULT_LOG_FILE_NAME;
            logFile = new File(path);
        }

        // Set up our logger and formatter.

        if (useConsole)
        {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.INFO);
            log.addHandler(ch);
        }
        else if (logFile != null)
        {
            // Clear old log file content if there is any.
            if (logFile.exists() && clearLog)
                try
                {
                    PrintWriter writer = new PrintWriter(logFile);
                    writer.print("");
                    writer.close();
                }
                catch (Exception e)
                {}

            // Set up the handler.
            try
            {
                FileHandler fh = new FileHandler(logFile.toString());
                fh.setLevel(Level.INFO);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                log.addHandler(fh);
            }
            catch (Exception e)
            {
                System.err.println("Unable to set up log file handler");
            }
        }
        log.setUseParentHandlers(false);
        return log;
    }


    private static int getSplashDuration()
    {
        String durationOption = System.getProperty("macstarter.splash-duration");
        if (durationOption != null)
            return Integer.parseInt(durationOption);
        else
            return DEFAULT_SPLASH_DURATION;
    }


    /**
     * Gets the base location of the given class.
     * <p>
     * If the class is directly on the file system (e.g.,
     * "/path/to/my/package/MyClass.class") then it will return the base
     * directory (e.g., "/path/to").
     * </p>
     * <p>
     * If the class is within a JAR file (e.g.,
     * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
     * path to the JAR (e.g., "/path/to/my-jar.jar").
     * </p>
     *
     * @param c The class whose location is desired.
     *
     * Originally from https://stackoverflow.com/a/12733172/743730
     */
    private static String getLocation(final Class<?> c)
    {
        if (c == null)
            return null; // could not load the class

        // Try the easy way first.
        try
        {
            final URL codeSourceLocation =
                c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null)
                return new File(codeSourceLocation.toURI()).getPath();
        }
        catch (Exception e)
        {
            // Fall through.
        }

        // NB: The easy way failed, so we try the hard way. We ask for the class
        // itself as a resource, then strip the class's path from the URL string,
        // leaving the base path.

        // Get the class's raw resource path.
        final URL classResource = c.getResource(c.getSimpleName() + ".class");
        if (classResource == null)
            return null; // cannot find class resource

        final String url = classResource.toString();
        final String suffix = c.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix))
            return null; // weird URL

        // Strip the class's path from the URL string.
        final String base = url.substring(0, url.length() - suffix.length());

        String path = base;

        // Remove the "jar:" prefix and "!/" suffix, if present.
        if (path.startsWith("jar:"))
            path = path.substring(4, path.length() - 2);

        try
        {
            URL pathAsURL = new URL(path);
            return new File(pathAsURL.toURI()).getPath();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /** Various constants. **/
    private static final String LOG_PREFIX              = Thread.currentThread().getStackTrace()[0].getClassName();
    private static final String DEFAULT_LOG_FILE_NAME   = "sbmltestsuite.log";
    private static final int    DEFAULT_SPLASH_DURATION = 2; // sec.
}
