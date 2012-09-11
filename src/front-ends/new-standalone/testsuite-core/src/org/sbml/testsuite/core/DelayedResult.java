//
// @file DelayedResult.java
// @brief Asynchronous determination of the result type for a given wrapper and
// test
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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Asynchronous determination of the result type for a given wrapper and test
 * 
 */
public class DelayedResult
{
    /**
     * boolean indicating whether the operation completed
     */
    private boolean             isDone;

    /**
     * computed result type
     */
    ResultType                  result;

    /**
     * asynchronous worker computing the result
     */
    FutureTask<ResultType>      worker;

    /**
     * wrapper configuration
     */
    private final WrapperConfig wrapperConfig;


    /**
     * Initialize already with a result, this means that this result will be
     * completed
     * instantaneously. It is meant to be used whenever the result is known
     * beforehand.
     * 
     * @param result
     *            the result
     */
    public DelayedResult(ResultType result)
    {
        this.result = result;
        wrapperConfig = null;
        isDone = true;

    }


    /**
     * Constructs a new DelayedResult with a wrapper configuration and a test
     * case.
     * This will immediately schedule the computation for the result.
     * 
     * @param wrapperConfig
     *            the configuration of the wrapper
     * @param test
     *            the test to get the result for
     */
    public DelayedResult(WrapperConfig wrapperConfig, final TestCase test)
    {
        this.wrapperConfig = wrapperConfig;
        result = ResultType.Unknown;
        isDone = false;

        worker = new FutureTask<ResultType>(new Callable<ResultType>() {

            public ResultType call()
                throws Exception
            {
                result = DelayedResult.this.wrapperConfig.getResultTypeInternal(test);
                isDone = true;
                return result;
            }

        });
        WrapperConfig.executor.execute(worker);

    }


    /**
     * Return the result type computed. If the operation did not yet
     * complete it will wait for the result to be computed first.
     * 
     * @return
     */
    public ResultType getResult()
    {
        if (isDone) return result;

        try
        {
            result = worker.get();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        isDone = true;
        worker = null;
        return result;

    }


    /**
     * 
     * @return boolean indicating whether the current task is complete
     */
    public boolean isDone()
    {
        return isDone;
    }

}
