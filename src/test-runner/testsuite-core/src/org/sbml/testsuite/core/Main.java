//
// @file Main.java
// @brief Command line interface to the core api (unused)
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

import java.util.Vector;
import org.sbml.testsuite.core.RunOutcome.Code;
import org.sbml.testsuite.core.commandline.TestSuiteArguments;

/**
 * Unused command line interface for the core api.
 */
public class Main
{

    /**
     * the test suite, a specific
     * test case, modify cached test suite parameters or even run test cases.
     * 
     * @param rawArgs
     *            the command line arguments
     */
    public static void main(String[] rawArgs)
    {
        TestSuiteArguments args = new TestSuiteArguments(rawArgs);
        if (!args.isValid())
        {
            args.printArguments(System.err);
            System.exit(1);
        }

        if (args.isShouldRun())
        {
            runWrapper(args);
        }
        else if (args.isShouldListReleases())
        {
            Vector<String> archives = Util.getCasesArchiveURLs(args.getPublishDate());
            if (archives == null)
                System.out.println("Unable to retrieve list of archives from sf.net");
            else
            {
                System.out.println("The following release archives are available from sf.net: ");
                for (String url : archives)
                    System.out.println("\t" + url);
                System.out.println();
            }
        }
        else if (args.isShouldDownload())
        {
            System.out.println("Download release archive from: ");
            System.out.println("\t" + args.getUrl());
            Util.downloadReleaseArchiveAndInitialize(args.getUrl());
        }

    }


    /**
     * Runs the specified wrapper for over the given range.
     * 
     * @param args
     *            the parsed arguments
     */
    public static void runWrapper(TestSuiteArguments args)
    {
        TestSuiteSettings settings = TestSuiteSettings.loadDefault();
        WrapperConfig wrapper = settings.getWrapper(args.getWrapperName());
        if (wrapper == null)
        {
            System.out.println(String.format("The wrapper '%s' does not exist.",
                                             args.getWrapperName()));
            System.exit(1);
        }

        if (!wrapper.canRun())
        {
            System.out.println(String.format("The wrapper '%s' cannot be executed.",
                                             args.getWrapperName()));
            System.exit(1);
        }

        TestCase test = settings.getSuite().get(args.getTestOrTestRange());
        if (test != null)
        {
            System.out.println(String.format("Starting Wrapper %s for case %s",
                                             wrapper.getName(), test.getId()));
            RunOutcome result = wrapper.run(test, settings.getSuite()
                                                          .getCasesDirectory()
                                                          .getAbsolutePath());

            if (result.getCode() == Code.success)
            {
                System.out.println(String.format("... run succeeded, result is: "
                    + wrapper.getResultType(test).toString()));
            }
            else
            {
                System.out.println(result.getMessage());
            }

        }
        else
        {
            String[] range = args.getTestOrTestRange().split("-");
            if (range == null || range.length != 2)
            {
                System.out.println(String.format("Invalid test range '%s' specified",
                                                 args.getTestOrTestRange()));
                System.exit(1);
            }
            try
            {

                int lower = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);

                for (int i = lower; i >= 0 && i <= end
                    && i < settings.getSuite().getNumCases(); i++)
                {
                    test = settings.getSuite().get(i - 1);
                    System.out.println(String.format("Starting Wrapper %s for case %s",
                                                     wrapper.getName(),
                                                     test.getId()));
                    RunOutcome result = wrapper.run(test,
                                                    settings.getSuite()
                                                            .getCasesDirectory()
                                                            .getAbsolutePath());
                    if (result.getCode() == Code.success)
                    {
                        System.out.println(String.format("... run succeeded, result is: "
                            + wrapper.getResultType(test).toString()));
                    }
                    else
                    {
                        System.out.println(result.getMessage());
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                System.out.println(String.format("Invalid test range '%s' specified",
                                                 args.getTestOrTestRange()));
                System.exit(1);
            }

        }
        System.out.println();
        System.out.println("done ...");
        System.exit(0);
    }
}
