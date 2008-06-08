

package sbml.test;

import java.util.HashMap;


public class TestConfiguration {
    HashMap<String, Object> hashMap;
    
    
    public TestConfiguration(HashMap<String, Object> selections) {
        hashMap = selections;
    }    
    
    public Object get(String key) {
        return hashMap.get(key);
    }
    
    public void set(String key, Integer value) {
        hashMap.put(key, value);
    }
    
}
