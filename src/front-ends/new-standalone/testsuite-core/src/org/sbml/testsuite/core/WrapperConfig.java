//
// @file WrapperConfig.java
// @brief WrapperConfig holds all settings for Simulator wrappers
// @author Frank T. Bergmann
// @date Created 2012-06-06 <fbergman@caltech.edu>
//
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Testsuite. Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright (C) 2009-2012 jointly by the following organizations:
// 1. California Institute of Technology, Pasadena, CA, USA
// 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
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

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.sbml.testsuite.core.data.CompareResultSet;
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
     * Simple test that checks whether a given process is running
     * 
     * @param p
     *            the process
     * 
     * @return true, if process is running, false otherwise.
     */
    public static boolean isRunning(Process p)
    {
        if (p == null) return false;

        boolean isRunning = false;

        try
        {
            p.exitValue();
        }
        catch (IllegalThreadStateException e)
        {
            isRunning = true;
        }
        return isRunning;
    }

    private String                         name;
    @Element(required = false)
    private String                         program;
    private String                         outputPath;
    @Element(required = false)
    private String                         arguments;

    private Vector<String>                 unsupportedTags;

    @Element(required = false)
    private boolean                        supportsAllVersions;

    @Transient
    private TreeMap<String, DelayedResult> resultCache;

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
        supportsAllVersions = false;
        unsupportedTags = new Vector<String>();
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
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, String unsupportedTags,
                         boolean supportsAllVersions)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = new Vector<String>(Util.split(unsupportedTags));
        this.supportsAllVersions = supportsAllVersions;
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
     */
    public WrapperConfig(String name, String program, String outputPath,
                         String arguments, Vector<String> unsupportedTags,
                         boolean supportsAllVersions)
    {
        this(name, program, outputPath, arguments);
        this.unsupportedTags = unsupportedTags;
        this.supportsAllVersions = supportsAllVersions;
    }


    /**
     * Starts the calculation of all results.
     * 
     * @param suite
     *            the tests to compare against
     */
    public void beginUpdate(TestSuite suite)
    {
        resultCache = new TreeMap<String, DelayedResult>();
        for (final TestCase test : suite.getCases())
        {

            resultCache.put(test.getId(), new DelayedResult(WrapperConfig.this,
                                                            test));

        }
    }


    /**
     * @return boolean indicating whether this wrapepr is executable. This is
     *         the case when the wrapper executable exists as well as teh output
     *         tag.
     */
    public boolean canRun()
    {
        if (program == null || program.length() == 0) return false;
        if (outputPath == null || outputPath.length() == 0) return false;

        File outPath = new File(outputPath);
        File fileProg = new File(program);
        if (fileProg.exists() && fileProg.isFile() && outPath.exists()
            && outPath.isDirectory()) return true;

        return false;
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
        if (testFile.exists()) testFile.delete();
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
    public TreeMap<String, DelayedResult> getCache()
    {
        return resultCache;
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
        TreeMap<String, DelayedResult> cache = resultCache;

        return cache.get(id).getResult();

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
    private String getExpandedArguments(TestCase test, String testSuiteDir)
    {
        String arguments = getArguments();
        arguments = arguments.replace("%d", testSuiteDir);
        arguments = arguments.replace("%o", getOutputPath());
        arguments = arguments.replace("%n", test.getId());
        arguments = arguments.replace("%l", test.getHighestSupportedLevel()
            + "");
        arguments = arguments.replace("%v", test.getHighestSupportedVersion()
            + "");
        return arguments;
    }


    /**
     * @return the name of this configuration
     */
    public String getName()
    {
        return name;
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
    public File getResultFile(TestCase test)
    {
        return new File(getOutputPath() + File.separator + test.getId()
            + ".csv");

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
        if (!testFile.exists()) return null;
        return ResultSet.fromFile(testFile);

    }


    /**
     * Gets the result type for the given test (computes it immidiately)
     * 
     * @param test
     *            the test to get the result type for
     * @return the result type
     */
    public ResultType getResultType(TestCase test)
    {
        ResultType result = getResultTypeInternal(test);
        if (resultCache != null)
        {
            resultCache.put(test.getId(), new DelayedResult(result));
        }
        return result;

    }


    /**
     * Computes the result type for the given test
     * 
     * @param test
     *            the test
     * @return the result type
     */
    public ResultType getResultTypeInternal(TestCase test)
    {
        if (test.matches(getUnsupportedTags())) return ResultType.CannotSolve;

        ResultSet deliveredResult = getResultSet(test);
        if (deliveredResult == null) return ResultType.Unknown;

        ResultSet expectedResult = test.getExpectedResult();

        CompareResultSet set = new CompareResultSet(expectedResult,
                                                    deliveredResult);
        if (set.compareUsingTestSuite(test.getSettings().getAbsoluteError(),
                                      test.getSettings().getRelativeError()))
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
        return supportsAllVersions;
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param testSuiteDir
     *            the test cases directory
     */
    public RunOutcome run(TestCase test, String testSuiteDir)
    {
        return run(test, testSuiteDir, null);
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param testSuiteDir
     *            the test cases directory
     * @param callback
     *            a cancellation callback allowing to interrupt the execution
     */
    public RunOutcome run(TestCase test, String testSuiteDir,
                          CancelCallback callback)
    {
        return run(test, testSuiteDir, 250, callback);
    }


    /**
     * Executed the given test
     * 
     * @param test
     *            the test to execute
     * @param testSuiteDir
     *            the test cases directory
     * @param milli
     *            milliseconds to wait between calling the callback
     * @param callback
     *            a cancellation callback allowing to interrupt the execution
     */
    public RunOutcome run(TestCase test, String testSuiteDir, int milli,
                          CancelCallback callback)
    {
        Runtime rt = Runtime.getRuntime();
        String cmd = getProgram() + " "
            + getExpandedArguments(test, testSuiteDir);
        Process process;

        // ProcessBuilder builder = new ProcessBuilder(getProgram(),
        // getExpandedArguments(test, testSuiteDir));
        try
        {
            // Process p = builder.start();
            process = rt.exec(cmd);
            if (callback == null || milli < 0)
                process.waitFor();
            else
            {
                if (isRunning(process))
                {
                    if (callback != null && callback.cancellationRequested())
                        return new RunOutcome(RunOutcome.Code.success, cmd);
                    Thread.sleep(milli);
                }
            }

            process.destroy();
        }
        catch (IOException ex)
        {
            resultCache.put(test.getId(), new DelayedResult(ResultType.Error));
            return new RunOutcome(RunOutcome.Code.ioError,
                                  "IO exception running command: " + cmd);
        }
        catch (SecurityException ex)
        {
            resultCache.put(test.getId(), new DelayedResult(ResultType.Error));
            return new RunOutcome(RunOutcome.Code.securityError,
                                  "Security exception running command: " + cmd);
        }
        catch (IllegalArgumentException ex)
        {
            resultCache.put(test.getId(), new DelayedResult(ResultType.Error));
            return new RunOutcome(RunOutcome.Code.argumentError,
                                  "Badly formed command line: " + cmd);
        }
        catch (InterruptedException ex)
        {
            resultCache.put(test.getId(), new DelayedResult(ResultType.Error));
            return new RunOutcome(RunOutcome.Code.interrupted,
                                  "Process interrupted: " + cmd);
        }
        catch (Exception e)
        {
            resultCache.put(test.getId(), new DelayedResult(ResultType.Error));
            return new RunOutcome(RunOutcome.Code.unknownError,
                                  "Unexpected error running: " + cmd);
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        resultCache.put(test.getId(),
                        new DelayedResult(getResultTypeInternal(test)));
        return new RunOutcome(RunOutcome.Code.success, cmd);
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
    public TreeMap<String, DelayedResult> updateCache(TestSuite suite)
    {
        beginUpdate(suite);
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
        this.supportsAllVersions = other.supportsAllVersions;
        this.unsupportedTags = new Vector<String>(other.unsupportedTags);
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


    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
            + ((outputPath == null) ? 0 : outputPath.hashCode());
        result = prime * result + ((program == null) ? 0 : program.hashCode());
        result = prime * result
            + ((resultCache == null) ? 0 : resultCache.hashCode());
        result = prime * result + (supportsAllVersions ? 1231 : 1237);
        result = prime * result
            + ((unsupportedTags == null) ? 0 : unsupportedTags.hashCode());
        return result;
    }


    /*
     * (non-Javadoc)
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
        else if (!resultCache.equals(other.resultCache)) return false;
        if (supportsAllVersions != other.supportsAllVersions) return false;
        if (unsupportedTags == null)
        {
            if (other.unsupportedTags != null) return false;
        }
        else if (!unsupportedTags.equals(other.unsupportedTags)) return false;
        return true;
    }
}
