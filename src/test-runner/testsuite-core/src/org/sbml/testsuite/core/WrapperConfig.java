//
// @file WrapperConfig.java
// @brief WrapperConfig holds all settings for Simulator wrappers
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
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

package org.sbml.testsuite.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.sbml.testsuite.core.data.CompareResultSets;
import org.sbml.testsuite.core.data.Comparison;
import org.sbml.testsuite.core.data.ResultSet;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Persister;

/**
 * WrapperConfig holds all settings for Simulator wrappers.
 * 
 * Simulator Wrappers do not have to be simulatable, at the very least
 * it would be possible to just have a name and a directory with test files
 * to compare with the expected results.
 */
@Default
public class WrapperConfig
{
    private String                         name;
    @Element(required = false)
    private String                         program;
    @Element(required = false)
    private String                         outputPath;
    @Element(required = false)
    private String                         arguments;

    private Vector<String>                 unsupportedTags;

    @Element(required = false)
    private boolean                        viewOnly;

    @Element(required = false)
    private boolean                        supportsAllVersions;

    @Element(required = false)
    private boolean                        concurrentThreadsOK;

    @Transient
    private SortedMap<String, DelayedResult> resultCache
        = Collections.synchronizedSortedMap(new TreeMap<String, DelayedResult>());

    @Transient
    private ConcurrentHashMap<String, File> resultFiles;

    static ExecutorService                 executor = Executors.newFixedThreadPool(20);


    /**
     * Default Constructor
     */
    public WrapperConfig()
    {
        super();
        this.name = "";
        this.program = "";
        this.outputPath = "";
        this.arguments = "";
        this.viewOnly = false;
        this.concurrentThreadsOK = false;
        this.supportsAllVersions = false;
        this.unsupportedTags = new Vector<String>();
        this.resultFiles = new ConcurrentHashMap<String, File>(1200, (float) 0.75, 4);
    }


    /**
     * Copy constructor
     */
    public WrapperConfig(WrapperConfig config)
    {
        this();
        this.updateFrom(config);                                
    }


    /**
     * Constructs a new config with name
     * 
     * @param name
     *            the name for the configuration
     */
    public WrapperConfig(String name)
    {
        this();
        this.name = name;
    }


    /**
     * Constructs a new config with name, program, outputPath and arguments
     * 
     * @param name
     *            the name of the simulator
     * @param program
     *            the wrapper executable
     * @param outputPath
     *            the output path of the wrapper
     * @param arguments
     *            additional arguments
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments)
    {
        this(name);
        this.program = program;
        this.outputPath = outputPath;
        this.arguments = arguments;
    }


    /**
     * Constructs a new config with name, program, outputPath, arguments as well
     * as a list
     * of unsupported test flags and a boolean indicating whether the wrapper
     * can run all levels / versions of SBML
     * 
     * @param name
     *            the name of the simulator
     * @param program
     *            the wrapper executable
     * @param outputPath
     *            the output path of the wrapper
     * @param arguments
     *            additional arguments
     * @param unsupportedTags
     *            a comma separated list of test / component tags not supported
     *            by this simulator
     * @param supportsAllVersions
     *            boolean indicating whether this simulator supports *all*
     *            levels / versions of SBML
     * @param concurrentThreadsOK
     *            boolean indicating whether this wrapper can be executed
     *            in parallel threads.
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, String unsupportedTags,
                         boolean supportsAllVersions, boolean concurrentThreadsOK)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = new Vector<String>(Util.split(unsupportedTags));
        this.supportsAllVersions = supportsAllVersions;
        this.concurrentThreadsOK = concurrentThreadsOK;
    }


    /**
     * Constructs a new config with name, program, outputPath, arguments as well
     * as a list
     * of unsupported test flags and a boolean indicating whether the wrapper
     * can run all levels / versions of SBML
     * 
     * @param name
     *            the name of the simulator
     * @param program
     *            the wrapper executable
     * @param outputPath
     *            the output path of the wrapper
     * @param arguments
     *            additional arguments
     * @param unsupportedTags
     *            a comma separated list of test / component tags not supported
     *            by this simulator
     * @param supportsAllVersions
     *            boolean indicating whether this simulator supports *all*
     *            levels / versions of SBML
     * @param concurrentThreadsOK
     *            boolean indicating whether this wrapper can be executed
     *            in parallel threads.
     * @param viewOnly
     *            boolean indicating whether this wrapper is a view-only
     *            pseudo-wrapper.
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, String unsupportedTags,
                         boolean supportsAllVersions, boolean concurrentThreadsOK,
                         boolean viewOnly)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = new Vector<String>(Util.split(unsupportedTags));
        this.supportsAllVersions = supportsAllVersions;
        this.concurrentThreadsOK = concurrentThreadsOK;
        this.viewOnly = viewOnly;
    }


    /**
     * Constructs a new config with name, program, outputPath, arguments as well
     * as a list
     * of unsupported test flags and a boolean indicating whether the wrapper
     * can run all levels / versions of SBML
     * 
     * @param name
     *            the name of the simulator
     * @param program
     *            the wrapper executable
     * @param outputPath
     *            the output path of the wrapper
     * @param arguments
     *            additional arguments
     * @param unsupportedTags
     *            a string vector of test / component tags not supported by this
     *            simulator
     * @param supportsAllVersions
     *            boolean indicating whether this simulator supports *all*
     *            levels / versions of SBML
     * @param concurrentThreadsOK
     *            boolean indicating whether this wrapper can be executed
     *            in parallel threads.
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, Vector<String> unsupportedTags,
                         boolean supportsAllVersions, boolean concurrentThreadsOK)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = unsupportedTags;
        this.supportsAllVersions = supportsAllVersions;
        this.concurrentThreadsOK = concurrentThreadsOK;
    }


    /**
     * Constructs a new config with name, program, outputPath, arguments as well
     * as a list
     * of unsupported test flags and a boolean indicating whether the wrapper
     * can run all levels / versions of SBML
     * 
     * @param name
     *            the name of the simulator
     * @param program
     *            the wrapper executable
     * @param outputPath
     *            the output path of the wrapper
     * @param arguments
     *            additional arguments
     * @param unsupportedTags
     *            a string vector of test / component tags not supported by this
     *            simulator
     * @param supportsAllVersions
     *            boolean indicating whether this simulator supports *all*
     *            levels / versions of SBML
     * @param concurrentThreadsOK
     *            boolean indicating whether this wrapper can be executed
     *            in parallel threads.
     * @param viewOnly
     *            boolean indicating whether this wrapper is a view-only
     *            pseudo-wrapper.
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, Vector<String> unsupportedTags,
                         boolean supportsAllVersions, boolean concurrentThreadsOK,
                         boolean viewOnly)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = unsupportedTags;
        this.supportsAllVersions = supportsAllVersions;
        this.concurrentThreadsOK = concurrentThreadsOK;
        this.viewOnly = viewOnly;
    }


    /**
     * Load a wrapper configuration from file
     * 
     * @param file
     *            the filename
     * @return the wrapper object
     * @throws Exception
     *             IO / Deserialization exceptions
     */
    public static WrapperConfig fromFile(File file)
        throws Exception
    {
        Serializer serializer = new Persister();
        WrapperConfig config = serializer.read(WrapperConfig.class, file);
        if (config.unsupportedTags == null)
            config.unsupportedTags = new Vector<String>();
        return config;
    }


    /**
     * Starts the calculation of all results.
     * 
     * @param suite
     *            the tests to compare against
     */
    public void beginUpdate(TestSuite suite, LevelVersion lv)
    {
        for (final TestCase test : suite.getCases())
        {
            resultCache.put(test.getId(), new DelayedResult(this, test, lv));
        }
    }


    /**
     * @return boolean indicating whether this wrapepr is executable. This is
     *         the case when the wrapper executable exists as well as teh output
     *         tag.
     */
    public boolean canRun()
    {
        if (viewOnly) return true;
        if (program == null || program.length() == 0) return false;
        if (outputPath == null || outputPath.length() == 0) return false;

        File outPath = new File(outputPath);
        File fileProg = new File(program);
        if (fileProg.exists() && fileProg.isFile()
            && fileProg.canRead() && fileProg.canExecute()
            && outPath.exists() && outPath.isDirectory() && outPath.canWrite())
            return true;

        return false;
    }


    public WrapperProblem wrapperProblem()
    {
        if (program == null || program.length() == 0)
            return WrapperProblem.noWrapperGiven;
        if (outputPath == null || outputPath.length() == 0)
            return WrapperProblem.noSuchDirectory;

        File outPath = new File(outputPath);
        if (! outPath.exists() || ! outPath.isDirectory())
            return WrapperProblem.noSuchDirectory;
        if (! outPath.canWrite())
            return WrapperProblem.cannotWriteDirectory;

        File fileProg = new File(program);
        if (! fileProg.exists() || ! fileProg.isFile())
            return WrapperProblem.noSuchFile;
        if (! fileProg.canRead())
            return WrapperProblem.cannotReadWrapper;
        if (! fileProg.canExecute())
            return WrapperProblem.cannotExecuteWrapper;

        return WrapperProblem.noProblem;
    }


    /**
     * Deletes the result for the given test
     * 
     * @param test
     *            the test
     */
    public void deleteResult(TestCase test)
    {
        File testFile = getResultFile(test);
        if (testFile == null) return;
        Util.deleteFile(testFile);
        resultFiles.remove(testFile);
        resultCache.put(test.getId(), new DelayedResult(ResultType.Unknown));
    }


    /**
     * @return the wrapper arguments
     */
    public String getArguments()
    {
        return arguments;
    }


    /**
     * @return the cache of all computed results
     */
    public SortedMap<String, DelayedResult> getCache()
    {
        return resultCache;
    }


    /**
     * Invalidates the cache for a given case.
     */
    public void invalidateCache(String id)
    {
        if (resultCache != null && resultCache.get(id) != null)
            resultCache.put(id, null);
    }


    /**
     * returns the cached result for the test with given id
     * 
     * @param id
     *            the test id
     * @return the result type
     */
    public ResultType getCachedResult(String id)
    {
        DelayedResult cachedResult = resultCache.get(id);
        if (cachedResult != null)
            return cachedResult.getResult();
        return null;
    }


    /**
     * Returns the cached result for the test with given id
     * and level/version, if it exists.
     * 
     * @param id
     *            the test id
     * @return the result type
     */
    public ResultType getCachedResult(String id, LevelVersion lv)
    {
        DelayedResult cachedResult = resultCache.get(id);
        if (cachedResult != null && cachedResult.getLevelVersion().equals(lv))
            return cachedResult.getResult();
        return null;
    }


    /**
     * Expands variables within the argument string
     * 
     * @param test
     *            the test case
     * @param testSuiteDir
     *            the test suite directory
     * @return expanded argument string
     */
    private String getExpandedArguments(TestCase test, int level, int version,
                                        String testSuiteDir)
    {
        String arguments = getArguments();
        arguments = arguments.replace("%d", testSuiteDir);
        arguments = arguments.replace("%o", getOutputPath());
        arguments = arguments.replace("%n", test.getId());
        arguments = arguments.replace("%l", Integer.toString(level));
        arguments = arguments.replace("%v", Integer.toString(version));
        return arguments;
    }


    /**
     * Expands variables within the argument string.
     * 
     * @param test
     *            the test case
     * @param testSuiteDir
     *            the test suite directory
     * @return expanded argument string
     */
    private String getExpandedArguments(TestCase test, LevelVersion lv,
                                        String testSuiteDir)
    {
        int level;
        int version;
        if (lv != null && lv.getLevel() != 0)
        {
            level = lv.getLevel();
            version = lv.getVersion();
        }
        else
        {
            level = test.getHighestSupportedLevel();
            version = test.getHighestSupportedVersion();
        }
        return getExpandedArguments(test, level, version, testSuiteDir);
    }


    /**
     * @return the name of this configuration
     */
    public String getName()
    {
        return name;
    }


    /**
     * @return true if this is a view-only wrapper
     */
    public boolean getViewOnly()
    {
        return viewOnly;
    }


    /**
     * @return the wrapper output path / the folder containing the simulator
     *         results
     */
    public String getOutputPath()
    {
        return outputPath;
    }


    /**
     * @return the wrapper executable
     */
    public String getProgram()
    {
        return program;
    }


    /**
     * Returns the file for the simulator result file for the given test
     * 
     * @param test
     *            the test
     * @return simulator result file
     */
    public File getResultFile(final TestCase test)
    {
        final String id = test.getId();

        // Speediest case: we already know where to look.

        File cachedFile = resultFiles.get(id);
        if (cachedFile != null)
            return cachedFile;

        // OK, we haven't tried to read this file yet.  Next speediest case:
        // the file is named "NNNNN.csv".

        File hopeful = new File(getOutputPath() + File.separator + id + ".csv");
        if (hopeful.exists() && hopeful.isFile())
        {
            resultFiles.put(id, hopeful);
            return hopeful;
        }

        // OK, it's not named "NNNNN.csv" or else it doesn't exist (yet).
        // Now we have to look for a file name that contains the case id
        // number and ends in .csv.  This corresponds to what's allowed by
        // the online SBML Test Suite as of 2013.  If we ever change what's
        // allowed to be uploaded in the online system, we need to make
        // corresponding changes here.  The algorithm below is suboptimal,
        // but it is not clear how else this can be done in Java.  There's no
        // direct way to test for the existence of a file name pattern.

        final File dir = new File(getOutputPath());
        final File[] matchingFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name)
            {
                return name.toLowerCase().endsWith(".csv")
                    && (name.indexOf(id) != -1);
            }
        });

        // There should only be one match.  If there's more than one, we're
        // in a quandary.  We should flag that up as an error, but with the
        // current code architecture, it's difficult.  So, right now, this
        // simply takes the first match that's a file (and not, e.g., a dir).
        // Also, we cache the result in resultFiles.

        if (matchingFiles != null && matchingFiles.length > 0)
        {
            for (final File file : matchingFiles)
                if (file.isFile())
                {
                    resultFiles.put(id, file);
                    return file;
                }
        }

        // If we don't find it, return null to signal the caller.

        return null;
    }


    /**
     * Get the result set for the given test
     * 
     * @param test
     *            the test to get the result for
     * @return the simulator result for the test
     */
    public ResultSet getResultSet(TestCase test)
    {
        File testFile = getResultFile(test);
        if (testFile == null) return null;
        return ResultSet.fromFile(testFile);
    }


    /**
     * Gets the result type for the given test.  Uses the cached result
     * if it exists, or computes it if a cached result doesn't exist.
     * 
     * @param test
     *            the test to get the result type for
     * @return the result type
     */
    public ResultType getResultType(TestCase test)
    {
        ResultType result = getCachedResult(test.getId());
        if (result == null)
        {
            result = getResultTypeInternal(test, new LevelVersion());
            resultCache.put(test.getId(), new DelayedResult(result));
        }
        return result;
    }


    /**
     * Gets the result type for the given test.  Uses the cached result
     * if it exists, or computes it if a cached result doesn't exist.
     * 
     * @param test
     *            the test to get the result type for
     * @param lv
     *            the SBML Level & Version of the test case to use
     *
     * @return the result type
     */
    public ResultType getResultType(TestCase test, LevelVersion lv)
    {
        ResultType result = getCachedResult(test.getId(), lv);
        if (result == null)
        {
            if (lv == null) lv = new LevelVersion(); // Defaults to highest.
            result = getResultTypeInternal(test, lv);
            resultCache.put(test.getId(), new DelayedResult(result, lv));
        }
        return result;
    }


    /**
     * Gets the result type for the given test.  Uses the cached result
     * if it exists, or computes it if a cached result doesn't exist.
     * 
     * @param test
     *            the test to get the result type for
     * @param level
     *            the SBML Level of the test case to use (0 = highest)
     * @param version
     *            the Version within the SBML Level to use (0 = highest)
     *
     * @return the result type
     */
    public ResultType getResultType(TestCase test, int level, int version)
    {
        return getResultType(test, new LevelVersion(level, version));
    }


    /**
     * Computes the result type for the given test
     * 
     * @param test
     *            the test
     * @return the result type
     */
    public ResultType getResultTypeInternal(TestCase test, int level, int version)
    {
        return getResultTypeInternal(test, new LevelVersion(level, version));
    }


    /**
     * Computes the result type for the given test
     * 
     * @param test
     *            the test
     * @return the result type
     */
    public ResultType getResultTypeInternal(TestCase test, LevelVersion lv)
    {
        if (lv == null) lv = test.getHighestSupportedLevelVersion();

        if (lv.getLevel() != 0 && !test.supportsLevelVersion(lv))
            return ResultType.Unavailable;

        ResultSet delivered = getResultSet(test);
        if (delivered == null)                 // It didn't produce a result.
        {
            if (test.matches(getUnsupportedTags()))
                return ResultType.Unsupported; // We know why it didn't.
            else
                return ResultType.Unknown;     // We don't know why it didn't.
        }
        if (!delivered.parseable())            // Something's wrong.
        {
            return ResultType.Error;
        }

        // If we get here, the wrapper produced a result.

        if (test.matches(getUnsupportedTags()))
            return ResultType.CannotSolve;     // We ignore it anyway.

        // How did the numerical values compare to what is expected?

        ResultSet expected = test.getExpectedResult();
        CompareResultSets crs = new CompareResultSets(expected, delivered);
        Comparison outcome = crs.compare(test.getSettings(true));
        if (outcome.isMatch())
            return ResultType.Match;
        else
            return ResultType.NoMatch;
    }


    /**
     * @return the list of unsupported test / component tags
     */
    public Vector<String> getUnsupportedTags()
    {
        return unsupportedTags;
    }


    /**
     * @return a string containing a comma separated list of unsupported test /
     *         component tags
     */
    public String getUnsupportedTagsString()
    {
        return Util.toString(unsupportedTags);
    }


    /**
     * @return boolean indicating whether this wrapper supports all levels /
     *         versions of SBML
     */
    public boolean isSupportsAllVersions()
    {
        if (viewOnly) return true;
        return supportsAllVersions;
    }


    /**
     * @return boolean indicating whether this wrapper is a view-only
     *         pseudo-wrapper
     */
    public boolean isViewOnly()
    {
        return viewOnly;
    }


    /**
     * @return boolean indicating whether this wrapper can be executed in
     * parallel threads.
     */
    public boolean isConcurrencyAllowed()
    {
        if (viewOnly) return true;
        return concurrentThreadsOK;
    }


    /**
     * Executed the given test using the highest Level+Version of the SBML
     * file for this test case.
     * 
     * @param test
     *            the test to execute
     * @param testSuiteDir
     *            the test cases directory
     */
    public RunOutcome run(final TestCase test, final String testSuiteDir)
    {
        return run(test, 0, 0, testSuiteDir, null, true);
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param level
     *            the SBML Level of the test case to use (0 = highest)
     * @param version
     *            the Version within the SBML Level to use (0 = highest)
     * @param testSuiteDir
     *            the test cases directory
     * @param callback
     *            a cancellation callback allowing to interrupt the execution
     */
    public RunOutcome run(final TestCase test, int level, int version,
                          final String testSuiteDir, 
                          final CancelCallback callback,
                          final boolean deleteFirst)
    {
        return run(test, new LevelVersion(level, version),
                   testSuiteDir, 250, callback, deleteFirst);
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param level
     *            the SBML Level of the test case to use (0 = highest)
     * @param version
     *            the Version within the SBML Level to use (0 = highest)
     * @param testSuiteDir
     *            the test cases directory
     * @param milli
     *            milliseconds to wait between calling the callback
     * @param callback
     *            a cancellation callback allowing to interrupt the execution
     */
    public RunOutcome run(final TestCase test, final String testSuiteDir,
                          final int milli, final CancelCallback callback,
                          final boolean deleteFirst)
    {
        int level   = test.getHighestSupportedLevel();
        int version = test.getHighestSupportedVersion();
        return run(test, new LevelVersion(level, version),
                   testSuiteDir, milli, callback, deleteFirst);
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param level
     *            the SBML Level of the test case to use (0 = highest)
     * @param version
     *            the Version within the SBML Level to use (0 = highest)
     * @param testSuiteDir
     *            the test cases directory
     * @param milli
     *            milliseconds to wait between calling the callback
     * @param callback
     *            a cancellation callback allowing to interrupt the execution
     */
    public RunOutcome run(final TestCase test, LevelVersion lv,
                          final String testSuiteDir, final int milli,
                          final CancelCallback callback,
                          final boolean deleteFirst)
    {
        if (viewOnly)
            return outcomeWithInfo(RunOutcome.Code.success, 
                                   "This is a view-only wrapper.",
                                   "", null, null);

        if (lv == null)
            lv = test.getHighestSupportedLevelVersion();

        String cmd = getProgram() + " " + getExpandedArguments(test, lv, testSuiteDir);

        if (!test.supportsLevelVersion(lv))
        {
            addUnavailableToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.success, 
                                   "Case not available in a version for "
                                   + "SBML Level " + lv.getLevel()
                                   + " Version " + lv.getVersion() + ".",
                                   cmd, null, null);
        }

        File expectedFile = getResultFile(test);
        if (deleteFirst && expectedFile != null && expectedFile.exists()
            && !expectedFile.delete())
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.ioError, 
                                   "Unable to delete output file '"
                                   + expectedFile.getPath() + "' prior to "
                                   + "invocation of wrapper. Aborted "
                                   + "execution of wrapper for case "
                                   + test.getId() + ".",
                                   cmd, null, null);
        }

        Runtime rt              = Runtime.getRuntime();
        Process process         = null;
        StreamEater errorEater  = null;
        StreamEater outputEater = null;
        try
        {
            process = rt.exec(cmd);
            errorEater = new StreamEater(process.getErrorStream());
            outputEater = new StreamEater(process.getInputStream());
            errorEater.start();
            outputEater.start();

            if (callback == null || milli < 0)
                process.waitFor();
            else
            {
                while (isRunning(process))
                {
                    if (callback != null && callback.cancellationRequested())
                        return outcomeWithInfo(RunOutcome.Code.interrupted,
                                               "Interrupted",
                                               cmd, outputEater, errorEater);
                    Thread.sleep(milli);
                }
            }

            // Did the process exit abnormally?

            if (process.exitValue() != 0)
            {
                // If this is a case that's unsupported by the tool, we
                // don't call this a true error.
                if (test.matches(getUnsupportedTags()))
                    addUnsupportedToCache(test, lv);
                else
                    addErrorToCache(test, lv);

                // We still return the error info, because that's separate
                // from the interpretation of the result and we should still
                // communicate this to the user.
                return outcomeWithInfo(RunOutcome.Code.unknownError,
                                       "The wrapper exited with an error",
                                       cmd, outputEater, errorEater);
            }

            // It is possible that after the process exits, its output file
            // may not instantly show up in the file system.  Unfortunately,
            // sometimes a wrapper never writes the file at all.  We don't
            // want to fail if we don't *immediately* see the file after the
            // process exits, but we also don't know if the file will *ever*
            // show up.  We can't block waiting for it, so the following is a
            // compromise: we test for a time period and give up after ~1 sec.

            expectedFile = getResultFile(test);
            if (expectedFile == null || ! expectedFile.exists())
            {
                for (int count = 11; count > 0; count--)
                {
                    Thread.sleep(100);
                    expectedFile = getResultFile(test);
                    if (expectedFile != null && expectedFile.exists()) break;
                }
            }
        }
        catch (IOException ex)
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.ioError, 
                                   "IO exception occurred when running the wrapper",
                                   cmd, outputEater, errorEater);
        }
        catch (SecurityException ex)
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.securityError,
                                   "Security exception occurred when attempting to run the wrapper",
                                   cmd, outputEater, errorEater);
        }
        catch (IllegalArgumentException ex)
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.argumentError,
                                   "Badly formed wrapper command line",
                                   cmd, outputEater, errorEater);
        }
        catch (InterruptedException ex)
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.interrupted,
                                   "The wrapper process was interrupted unexpectedly",
                                   cmd, outputEater, errorEater);
        }
        catch (Exception e)
        {
            addErrorToCache(test, lv);
            return outcomeWithInfo(RunOutcome.Code.unknownError,
                                   "An unexpected error upon running the wrapper",
                                   cmd, outputEater, errorEater);
        }
        finally
        {
            if (process != null)
                process.destroy();
        }

        addResultToCache(test, lv);
        return outcomeWithInfo(RunOutcome.Code.success, 
                               "Wrapper completed normally",
                               cmd, outputEater, errorEater);
    }


    public RunOutcome outcomeWithInfo(RunOutcome.Code code, String msg,
                                      String cmd, StreamEater outputEater,
                                      StreamEater errorEater)
    {
        String outputText = "";
        String errorText  = "";
        
        if (outputEater != null)
            outputText = outputEater.getOutput();
        if (errorEater != null)
            errorText = errorEater.getOutput();

        msg += ".\n\nCommand line executed:\n"
            + cmd + "\n\n"
            + "Output produced on standard output stream:\n"
            + outputText + "\n\n"
            + "Output produced on standard error stream:\n"
            + errorText;

        return new RunOutcome(code, msg);
    }


    public void addResultToCache(final TestCase test, LevelVersion lv)
    {
        resultCache.put(test.getId(),
                        new DelayedResult(getResultTypeInternal(test, lv), lv));
    }


    public void addErrorToCache(final TestCase test, LevelVersion lv)
    {
        resultCache.put(test.getId(), new DelayedResult(ResultType.Error, lv));
    }


    public void addUnsupportedToCache(final TestCase test, LevelVersion lv)
    {
        resultCache.put(test.getId(), new DelayedResult(ResultType.Unsupported, lv));
    }


    public void addUnavailableToCache(final TestCase test, LevelVersion lv)
    {
        resultCache.put(test.getId(), new DelayedResult(ResultType.Unavailable, lv));
    }


    /**
     * Sets the wrapper arguments
     * 
     * @param arguments
     *            the wrapper arguments
     */
    public void setArguments(String arguments)
    {
        this.arguments = arguments;
    }


    /**
     * sets the name of this configuration
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Sets whether this wrapper is a view-only pseudo-wrapper.
     * 
     * @param yesNo
     *            whether it is, or not
     */
    public void setViewOnly(boolean yesNo)
    {
        this.viewOnly = yesNo;
    }


    /**
     * the wrapper output path / the folder containing the simulator results
     * 
     * @param outputPath
     *            the wrapper output path / the folder containing the simulator
     *            results
     */
    public void setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
    }


    /**
     * Sets the wrapper executable
     * 
     * @param program
     *            the wrapper executable
     */
    public void setProgram(String program)
    {
        this.program = program;
    }


    /**
     * Sets boolean indicating whether this wrapper supports all levels /
     * versions of SBML
     * 
     * @param supportsAllVersions
     *            boolean indicating whether this wrapper supports all levels /
     *            versions of SBML
     */
    public void setSupportsAllVersions(boolean supportsAllVersions)
    {
        this.supportsAllVersions = supportsAllVersions;
    }


    /**
     * @return boolean indicating whether this wrapper can be executed in
     * parallel threads.
     */
    public void setConcurrencyAllowed(boolean concurrentThreadsOK)
    {
        this.concurrentThreadsOK = concurrentThreadsOK;
    }


    /**
     * Sets the string containing a comma separated list of unsupported test /
     * component tags
     * 
     * @param unsupportedTags
     *            a string containing a comma separated list of unsupported test
     *            / component tags
     */
    public void setUnsupportedTags(String unsupportedTags)
    {
        this.unsupportedTags = new Vector<String>(Util.split(unsupportedTags));
    }


    /**
     * Sets the list of unsupported test / component tags
     * 
     * @param unsupportedTags
     *            the list of unsupported test / component tags
     */
    public void setUnsupportedTags(Vector<String> unsupportedTags)
    {
        this.unsupportedTags = unsupportedTags;
    }


    /**
     * @return a string representation of this wrapper
     */
    @Override
    public String toString()
    {
        return "WrapperConfig [name=" + name + ", program=" + program + "]";
    }


    /**
     * Start the calculation of all results.
     * 
     * @param suite
     *            the test suite to compare against
     * @return a map with test ids / delayed result objects
     */
    public SortedMap<String, DelayedResult> updateCache(TestSuite suite,
                                                        LevelVersion lv)
    {
        beginUpdate(suite, lv);
        return resultCache;
    }


    /**
     * Initializes this wrapper from another one
     * 
     * @param other
     *            other wrapper
     */
    public void updateFrom(WrapperConfig other)
    {
        if (other == null) return;

        this.arguments = other.arguments;
        this.name = other.name;
        this.outputPath = other.outputPath;
        this.program = other.program;
        this.viewOnly = other.viewOnly;
        this.supportsAllVersions = other.supportsAllVersions;
        this.unsupportedTags = new Vector<String>(other.unsupportedTags);
        this.concurrentThreadsOK = other.concurrentThreadsOK;
        this.resultFiles.putAll(other.resultFiles);
    }


    /**
     * Writes this config to file
     * 
     * @param file
     *            the filename to write to
     * @throws Exception
     *             IO / Serialization exceptions
     */
    public void writeToFile(File file)
        throws Exception
    {
        Serializer serializer = new Persister();
        serializer.write(this, file);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + (concurrentThreadsOK ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
            + ((outputPath == null) ? 0 : outputPath.hashCode());
        result = prime * result + ((program == null) ? 0 : program.hashCode());
        result = prime * result
            + ((resultCache == null) ? 0 : resultCache.hashCode());
        result = prime * result
            + ((resultFiles == null) ? 0 : resultFiles.hashCode());
        result = prime * result + (supportsAllVersions ? 1231 : 1237);
        result = prime * result
            + ((unsupportedTags == null) ? 0 : unsupportedTags.hashCode());
        result = prime * result + (viewOnly ? 1231 : 1237);
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof WrapperConfig)) return false;
        WrapperConfig other = (WrapperConfig) obj;
        if (arguments == null)
        {
            if (other.arguments != null) return false;
        }
        else if (!arguments.equals(other.arguments)) return false;
        if (concurrentThreadsOK != other.concurrentThreadsOK) return false;
        if (name == null)
        {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        if (outputPath == null)
        {
            if (other.outputPath != null) return false;
        }
        else if (!outputPath.equals(other.outputPath)) return false;
        if (program == null)
        {
            if (other.program != null) return false;
        }
        else if (!program.equals(other.program)) return false;
        if (resultCache == null)
        {
            if (other.resultCache != null) return false;
        }
        if (resultFiles == null)
        {
            if (other.resultFiles != null) return false;
        }
        if (supportsAllVersions != other.supportsAllVersions) return false;
        if (unsupportedTags == null)
        {
            if (other.unsupportedTags != null) return false;
        }
        else if (!unsupportedTags.equals(other.unsupportedTags)) return false;
        if (viewOnly != other.viewOnly) return false;
        return true;
    }


    /**
     * Simple test that checks whether a given process is running
     * 
     * @param p
     *            the process
     * 
     * @return true, if process is running, false otherwise.
     */
    private final static boolean isRunning(final Process p)
    {
        if (p == null) return false;

        try
        {
            p.exitValue();
        }
        catch (IllegalThreadStateException e)
        {
            return true;
        }
        return false;
    }
}
