// 
// @file    ResultDifference.java
// @brief   Capture differences in a single test case result.
// @author  Michael Hucka
// @date    Created 2011-03-24
//
// $Id$
// $URL$
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
// more information about SBML, and the latest version of the SBML Test Suite.
//
// Copyright 2008-2010 California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation.  A copy of the license agreement is provided
// in the file named "LICENSE.txt" included with this software distribution
// and also available at http://sbml.org/Software/SBML_Test_Suite/License
// ----------------------------------------------------------------------------

package sbml.test;

import java.math.*;

public class ResultDifference
{
    // 
    // ------------------------- Public constructors --------------------------
    // 

    public ResultDifference() { }
        
    public ResultDifference(BigDecimal num)
    {
        value = num;
    }

    public ResultDifference(double num)
    {
        value = new BigDecimal(num);
    }

    // 
    // --------------------------- Public methods ----------------------------
    // 

    public void    setNumerical(boolean v)  { numerical = v; }
    public boolean isNumerical()            { return numerical; }

    public void setValue(double num)
    {
        value = new BigDecimal(num);
        setNumerical(true);
    }

    public void setValue(BigDecimal num)
    {
        value = num;
        setNumerical(true);
    }

    public BigDecimal getValue() { return value; }

    // 
    // -------------------------- Private variables ---------------------------
    // 
    
    private BigDecimal value;
    private boolean numerical = true;
}
