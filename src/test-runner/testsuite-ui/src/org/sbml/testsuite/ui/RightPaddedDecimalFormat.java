//
// @file   RightPaddedDecimalFormat.java
// @brief  Return a string representation of a number, with a space added
// @author Michael Hucka
// @@date  Created 2013-01-14 <mhucka@caltech.edu>
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

import java.text.AttributedCharacterIterator;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;


public class RightPaddedDecimalFormat
    extends Format
{
    private DecimalFormat formatter;
    private StringBuffer  padding;
    private String        paddingString;


    public RightPaddedDecimalFormat(String pattern)
    {
        this(pattern, 1);
    }

    public RightPaddedDecimalFormat(String pattern, int numPaddingChars)
    {
        formatter = new DecimalFormat(pattern);
        padding = new StringBuffer();
        for (int i = 0; i < numPaddingChars; i++)
            padding.append(' ');
    }


    public String format(Number number)
    {
        return formatter.format(number) + paddingString;
    }


    public String format(double number)
    {
        return formatter.format(number) + paddingString;
    }


    public String format(long number)
    {
        return formatter.format(number) + paddingString;
    }

    
    public StringBuffer format(Object number, StringBuffer sb, FieldPosition pos)
    {
        StringBuffer output = formatter.format(number, sb, pos);
        output.append(padding);
        return output;
    }

    public StringBuffer format(double number, StringBuffer sb, FieldPosition pos)
    {
        StringBuffer output = formatter.format(number, sb, pos);
        output.append(padding);
        return output;
    }

    public StringBuffer format(long number, StringBuffer sb, FieldPosition pos)
    {
        StringBuffer output = formatter.format(number, sb, pos);
        output.append(padding);
        return output;
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj)
    {
        return formatter.formatToCharacterIterator(obj);
    }


    public Object parseObject(String source, ParsePosition pos)
    {
        return formatter.parseObject(source, pos);
    }
}
