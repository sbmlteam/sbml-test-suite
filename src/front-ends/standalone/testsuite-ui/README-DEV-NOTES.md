Developer's notes for the standalone SBML Test Runner
=====================================================

The following are miscellaneous notes about things done or not done, and tried or not tried, with the SBML Test Runner.

Creating an application wrapper for OS X
----------------------------------------

Creating an executable application (known as an _app bundle_ on the Mac) from a Java JAR file has become more challenging on recent versions of the Mac operating system.  One problem is that Apple continues to demote the importance of Java support.  It stopped shipping Java as of OS&nbsp;X&nbsp;10.7, and the Java that is available from Apple is an old version (1.6).  You have to get more recent versions from Oracle.  This by itself a hindrance and discourages users from dealing with Java-based applications.  For this reason, as of version 3.3.0 of the SBML Test Runner, we bundle the Java run-time (JRE) inside the app bundle on OS&nbsp;X.

The changes in Mac support for Java over the years have also led to challenges in creating the executable application.  Apple's process is documented but [relatively complex](https://developer.apple.com/library/content/documentation/Java/Conceptual/Java14Development/03-JavaDeployment/JavaDeployment.html).  Originally, we used a third-party tool called [JarBundler](http://informagen.com/JarBundler/) to create the SBML Test Runner.  This tool stopped working for newer combinations of OS&nbsp;X and Java.  For version 3.3.0 of the SBML Test Runner, we examined a number of other software tools that can produce applications from Java JAR files, including installer-generators (see the separate section below) as well as some more focused tools:

* [javapackager](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javapackager.html)
* [pakr](https://github.com/libgdx/packr)
* [jar2app](https://github.com/Jorl17/jar2app)

The SBML Test Runner build process currently uses [javapackager](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javapackager.html), a tool provided by Oracle.  With all due respects to javapackager's authors, it is not a great tool: the documentation is unclear and insufficient, the program's behavior is sometimes baffling, it insists on creating some things that we don't need, and some Java run-time arguments can't be controlled via javapackager's command line arguments.  On the other hand, javapackager has more capabilities than the other tools listed above, and hopefully it will be supported by Oracle for the forseable future, so we use it.

For building the SBML Test Runner, the process is controlled by Ant tasks in [build.xml](build.xml), specifically the `app-mac-normal-common` target.  The process involves replacing the `Info.plist` file produced by javapackager with [our own version](dev/app-builders/macos/javapackager-config/Info.plist) to work around some undesirable javapackager behaviors.  (One problem is it proved impossible to use javapackager's `-B` flag to set a JVM option that did _not_ use an equal sign, such as the `-splash` option.  Javapackager seems to complain if the value given via `-B` lacks a `=` character as part of it.)


Creating a splash screen for OS X
---------------------------------

We have sought to make the SBML Test Runner's GUI interface look and feel as much as possible as native applications on each of the major operating systems (Mac OS&nbsp;X, Windows, Linux).  When we first developed the GUI interface for the SBML Test Runner, the best choice for this goal was to use the [SWT](https://www.eclipse.org/swt/) (Standard Widget Toolkit) interface library.  Unfortunately, SWT-based Java applications (or perhaps only the SBML Test Runner) seem to have become slower to start in recent versions of Java on OS&nbsp;X.  To help prevent users from thinking that something is wrong when nothing happens for several seconds after they start the application, we added a splash screen in version 3.3.0.

This turned out to be a frustrating exercise.

1) The built-in Java splash screen mechanism does not work with SWT on OS&nbsp;X.

2) If you create a splash screen in your application yourself, using the SWT widgets, it will not appear onscreen until the same time as all the other windows.  This completely defeats the purpose of having the splash screen.

3) If you attempt to create a splash screen using AWT widgets in an application that also uses SWT widgets, you will run into thread management issues: the SWT widget calls have to be in a separate thread as the AWT calls.  But the AWT code doesn't appear to work when you use the `-XstartOnFirstThread` JVM option that is necessary to use the SWT widgets.

After considerable time trying different alternatives, we finally hit on the idea of creating a small AWT-based Java wrapper application that then invokes Java on the real SBML Test Runner.  This is implemented in [MacStarter](src/org/sbml/testsuite/ui/MacStarter.java).

MacStarter a small, purely AWT-based Java application that can be started with the now-standard Java [SplashScreen facility](https://docs.oracle.com/javase/tutorial/uiswing/misc/splashscreen.html) so that the splash screen is shown almost immediately.  The splash screen is shown by the Java JVM; the job of MacStarter is then to (1) spawn another process to run the bundled Java binary on the real SBML Test Runner main class (`org.sbml.testsuite.ui.Program`), and (2) close the splash screen after a delay.  MacStarter is bundled in the same jar file as the SBML Test Runner (`sbmltestrunner.jar`), and to complete the seamless user experience, the `Manifest.mf` file in `sbmltestrunner.jar` is configured so that MacStarter is the main class and a splash screen is defined.  The same behavior thus results whether the SBML Test Runner is started by double-clicking the app bundle (see section above) or by running it on the command line with `java -jar sbmltestrunner.jar`.  As a bonus, the user does not have to remember to provide the JVM argument `-XstartOnFirstThread` on OS&nbsp;X.

As far as I can tell, an installer-generator that creates a wrapper around your Java application such as [Intall4J](https://www.ej-technologies.com/products/install4j/overview.html) just runs a small Java wrapper program too, like our own MacStarter.  Rather than rely on a closed-source commercial program, we may as well use our own wrapper.  (I did try to use Install4J, but could not make the splash screen feature work anyway, and gave up after a long time of trying.  Besides, Install4J is a powerful but complex program, and for the sake of future maintainability of the SBML Test Suite by other people, I think creating our own solution is better anyway.)


Creating installers for OS X
----------------------------

In addition to the production of executable applications (_app bundles_), one more step is needed to produce a good application experience for users on OS&nbsp;X: the creation of an installer.  Originally, we produced `.dmg` disk images for the SBML Test Runner using basic OS&nbsp;X commands such as `hdiutil`.  However, disk images are not proper installers.  In OS&nbsp;X, installers typically take the form of an executable `.pkg` package file; when started by the user, these perform the usual steps that installers on other platforms perform, such as displaying recent news about the software version, informing users of the licensing terms, and so on.

Oracle's javapackager (discussed above) can generate `.pkg` files too.  However, it is difficult to control how it does so, and it appears to be impossible to do things such as change the icon used in the installer or add features such as displaying a recent-news file during the installation process.  We gave up on using javapackager and found a free installer-generated from WhiteBox called [Packages](http://s.sudre.free.fr/Software/Packages/about.html).  This is an easy-to-use software tool that provides enough control over the installer to suit the needs of the SBML Test Runner.
