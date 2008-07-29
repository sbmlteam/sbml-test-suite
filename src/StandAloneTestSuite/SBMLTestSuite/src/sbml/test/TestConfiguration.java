// @file    TestConfiguration.java
// @brief   TestConfiguration class for SBML Standalone application
// @author  Kimberly Begley
// 

//
//<!---------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// 
// Copyright 2008      California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available at
// http://sbml.org/Software/SBML_Test_Suite/license.html
//------------------------------------------------------------------------- -->
// Generate Test configuration for the selections from the wizard.
//

package sbml.test;

import java.util.HashMap;

/**
 * TestConfiguration is a class that contains the user level, type, component tags, test tags and wrapper configuration from the wizard.
 * @author Kimberly Begley
 * @version 2.0
 */
public class TestConfiguration {
    HashMap<String, Object> hashMap;
    
    /**
     * Test configuration has one constructor that takes one argument 
     * @param selections is a hashmap that contains the values of the options available from the wizard.
     */
    public TestConfiguration(HashMap<String, Object> selections) {
        hashMap = selections;
    }    
    /**
     * get - gets a value from the selections hashmap 
     * @param key the key to retrieve from the hashmap
     * @return returns the object associated with the key in the hashmap
     */
    public Object get(String key) {
        return hashMap.get(key);
    }
    /**
     * set - sets a value in the hashmap
     * @param key - the key at which to set in the hashmap
     * @param value - the value to set with the associated key in the hashmap
     */
    public void set(String key, Integer value) {
        hashMap.put(key, value);
    }
    
}
