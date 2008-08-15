// FormBean.java
//
// Created by Kimberly Begley
// Servlet to get selections from user test case selection web page.
//


package sbml.test;
import java.io.*;
import java.util.*;

public class FormBean {
   
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    
    private String[] levels;
    private String[] testtype;
    private String[] ctags;
    private String[] ttags;
    private HashMap<String, String>  errors;

    public boolean validate() {
        boolean allOk = true;
        return allOk;
    }

    public String getErrorMsg(String s) {
        String errorMsg = (String) errors.get(s.trim());
        return (errorMsg == null) ? "" : errorMsg;
    }

    public FormBean() {
        levels = new String[]{"1"};
        testtype = new String[]{"1"};
        ctags = new String[]{"1"};
        ttags = new String[]{"1"};
        errors = new HashMap<String, String>();
    }
    
    public String[] getLevels() {
        return levels;
    }

    public String[] getTesttype() {
        return testtype;
    }

    public String[] getCtags() {
        return ctags;
    }

    public String[] getTtags() {
        return ttags;
    }

    public void setLevels(String[] level) {
        levels = level;
    }

    public void setTesttype(String[] type) {
        testtype = type;
    }

    public void setCtags(String[] ctag) {
        ctags = ctag;
    }

    public void setTtags(String[] ttag) {
        ttags = ttag;
    }

    public void setErrors(String key, String msg) {
        errors.put(key, msg);
    }

}
