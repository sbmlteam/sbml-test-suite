//
// @file   TaskExecutor.java
// @brief  Helper class for executing processes
// @author Michael Hucka
// @date   Created 2013-02-20 <mhucka@caltech.edu>
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.swt.widgets.Display;


/**
 * Helper class for executing processes.
 */
class TaskExecutor
{
    private ExecutorService ex;


    public TaskExecutor()
    {
        init(false, defaultNumThreads());
    }


    public TaskExecutor(int numThreads)
    {
        init(false, numThreads);
    }


    public void init(boolean concurrencyOK, int numThreads)
    {
        if (ex != null && ! ex.isTerminated())
            ex.shutdownNow();

        if (concurrencyOK)
        {
            ex = Executors.newFixedThreadPool(numThreads);
        }
        else
        {
            ex = Executors.newSingleThreadExecutor();
        }
    }


    public void execute(Runnable command)
    {
        if (ex != null)
            ex.execute(command);
    }


    public void waitForProcesses(Display display)
    {
        if (ex == null) return;
        ex.shutdown();                  // Finish the threads we started.
        while (!ex.isTerminated())
        {
            if (display == null || display.isDisposed()) return;
            display.readAndDispatch();
        }
    }


    public static int defaultNumThreads()
    {
        int numProcs = Runtime.getRuntime().availableProcessors();
        return Math.max(1, numProcs - 1);
    }


    public void shutdownNow()
    {
        if (ex != null)
            ex.shutdownNow();
    }
}
