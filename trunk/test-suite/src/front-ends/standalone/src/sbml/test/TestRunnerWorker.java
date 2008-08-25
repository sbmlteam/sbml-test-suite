// @file    TestRunnerWorker.java
// @brief   TestRunnerWorker class for SBML Standalone application
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
// Main testing class for the application - all logic for testing is here.
//
package sbml.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import javax.swing.SwingUtilities;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipFile;

/**
 * TestRunnerWorker class contains the testing logic of the Stand Alone SBML Test Suite.
 * @author Kimberly Begley
 * @version 2.0
 */
public class TestRunnerWorker extends SwingWorker {

    private static Logger logger = Logger.getLogger(TestRunnerWorker.class.getName());
    private static Logger ziplogger = Logger.getLogger(TestRunnerWorker.class.getCanonicalName());
    private static FileHandler fh,zfh;
    volatile boolean stopper = false;
    TestRunnerView testRunnerView;
    TestConfiguration testConfiguration;
    TestCaseListModel testCaseListModel;
    FailedTestCaseListModel failedTestCaseListModel;
    PassedTestCaseListModel passedTestCaseListModel;
    SkippedTestCaseListModel skippedTestCaseListModel;
    //PreferencesDialog preferencesDialog;
    final int BUFFER = 2048;
    String tempdir;
    /**
     * TestRunnerWorker has one constructor 
     * @param testRunnerView instance variable - contains the main view of the application
     * @param testConfiguration instance variable - contains the configuration settings from the wizard
     */
    public TestRunnerWorker(TestRunnerView testRunnerView, TestConfiguration testConfiguration) {
        super();
        this.testRunnerView = testRunnerView;
        this.testConfiguration = testConfiguration;
        this.testCaseListModel = TestRunnerView.testCaseListModel;
        this.failedTestCaseListModel = TestRunnerView.failedTestCaseListModel;
        this.passedTestCaseListModel = TestRunnerView.passedTestCaseListModel;
        this.skippedTestCaseListModel = TestRunnerView.skippedTestCaseListModel;


    }
    /**
     * construct method basically does all the testing of the selected tests.
     * @return and object or null
     */
    public Object construct() {
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        int totals = 0;
        int finish = 0;
        Vector<TestResultDetails> output = new Vector<TestResultDetails>();
        SBMLTestCase t2 = new SBMLTestCase();



        // Look for a test case directory - if it does not exist unzip the zipped test case files
        File test = null;
        String testdir = null;
        String dirpath = null;
        String userpath = System.getProperty("user.home");
        //File tests = new File(userpath + "/.sbmltestrunner");
        File tests = new File(userpath + File.separator + ".sbmltestrunner");

        //  File tests = new File("sbmltestrunner");
        if (!tests.exists()) {
            tests.mkdir();
            dirpath = tests.getAbsolutePath();
        }
        File localtimestampcases = new File(userpath + File.separator + ".sbmltestrunner" + File.separator +  "timestamp");
        if(!localtimestampcases.exists()){
            
            String ts = "";
            if (getClass().getResource("resources/sbml-test-suite.zip") == null) {
                System.out.println("Zipped Test cases are not found with application - unable to run.");
               // ziplogger.warning("Zipped test cases are not found with application - unable ro tun.");
            } else {
               // ZipFile zf;
               // ZipEntry ze;
                try {
                    
                    ZipInputStream zis = new ZipInputStream(getClass().getResourceAsStream("resources/sbml-test-suite.zip"));
                    ZipEntry zise;
                    String s=null;
                    while((zise = zis.getNextEntry()) != null){
                        if(zise.getName().equals("sbml-test-suite/.cases-archive-date")){
                            BufferedReader bzr = new BufferedReader(new InputStreamReader(zis));
                            
                            while((s=bzr.readLine()) != null){
                                
                                s.trim();
                                break;
                            }
                        }
                    }
                    
              //      BufferedReader bzr = new BufferedReader(zis);
              //      ts = bzr.readLine();
                    
                    ts = s;
                    String destinationDirectory = tests + File.separator + ts;
                    File ddir = new File(destinationDirectory);
                    
                    if(!ddir.exists()){
                    //System.out.println("test dir does not exist");

                        ZipInputStream zipFile = new ZipInputStream(getClass().getResourceAsStream("resources/sbml-test-suite.zip"));
                        ZipEntry entry;
                    
                        while ((entry = zipFile.getNextEntry()) != null) {
                            int count;
                            byte data[] = new byte[2048];
                            BufferedOutputStream dest;
                            File destFile = new File(destinationDirectory, entry.getName());
                            File destinationParent = destFile.getParentFile();
                            destinationParent.mkdirs();
                            if (!entry.isDirectory()) {

                                FileOutputStream fout = new FileOutputStream(destFile);
                                dest = new BufferedOutputStream(fout, 2048);
                                while ((count = zipFile.read(data, 0, 2048)) != -1) {
                                    dest.write(data, 0, count);
                                }
                                dest.flush();
                                dest.close();
                            }
                        zipFile.closeEntry();
                    }   
                     } // end of if dest dir does not exist
                // write the timestamp file for these cases 
                    try {                  
                        //Create file if it does not exist
                        BufferedWriter timestampout = new BufferedWriter(new FileWriter(localtimestampcases));
                        timestampout.write(ts);
                        timestampout.close();
                    } catch (IOException e) {
                        System.out.println("Caught IO Exception while trying to create timestamp file in user directory");
                    }   
               
                } catch (IOException io) {
                    Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, io);
                }
            
            test = new File(tests + File.separator + ts);
            //System.out.println("test dir is " + test);
    }
        } else {
            // the timestamp file already exists - set the test directory
            //System.out.println("timestamp dir does exist");
            BufferedReader btsr = null;
            String localtimestamp = "";
            dirpath = tests.getAbsolutePath();
            try {
                btsr = new BufferedReader(new FileReader(localtimestampcases));
                localtimestamp = btsr.readLine();
                //System.out.println("timestamp is " + localtimestamp);
                
                //t2.deleteDirectory()
            } catch (FileNotFoundException fnf) {
                Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, fnf);
            } catch (IOException ioe){
                Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ioe);
            } 
            finally {
                try {
                    btsr.close();
                } catch (IOException ex) {
                    Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String testsdir  = tests + File.separator + localtimestamp;
           
            File tdir = new File(testsdir);
            // if the timestamp file contains a timestamp for which there is no directory
            if(!tdir.exists()){
                
                String ts = "";
                if (getClass().getResource("resources/sbml-test-suite.zip") == null) {
                    System.out.println("Zipped Test cases are not found with application - unable to run.");
                // ziplogger.warning("Zipped test cases are not found with application - unable ro tun.");
                } else {
                    
                        // unzip the file
                  //  ZipFile zf;
                  //  ZipEntry ze;
                    try {
                    
                        ZipInputStream zis = new ZipInputStream(getClass().getResourceAsStream("resources/sbml-test-suite.zip"));
                        ZipEntry zise;
                        String s=null;
                        while((zise = zis.getNextEntry()) != null){
                            if(zise.getName().equals("sbml-test-suite/.cases-archive-date")){
                                BufferedReader bzr = new BufferedReader(new InputStreamReader(zis));
                            
                                while((s=bzr.readLine()) != null){
                                
                                    s.trim();
                                    break;
                                }
                            }
                        }
                    

                        ts = s;
                        String destinationDirectory = tests + File.separator + ts;
                        File ddir = new File(destinationDirectory);
                       
                        if (!ddir.exists()) {


                            ZipInputStream zipFile = new ZipInputStream(getClass().getResourceAsStream("resources/sbml-test-suite.zip"));
                            ZipEntry entry;

                            while ((entry = zipFile.getNextEntry()) != null) {
                                int count;
                                byte data[] = new byte[2048];
                                BufferedOutputStream dest;
                                File destFile = new File(destinationDirectory, entry.getName());
                                File destinationParent = destFile.getParentFile();
                                destinationParent.mkdirs();
                                if (!entry.isDirectory()) {

                                    FileOutputStream fout = new FileOutputStream(destFile);
                                    dest = new BufferedOutputStream(fout, 2048);
                                    while ((count = zipFile.read(data, 0, 2048)) != -1) {
                                        dest.write(data, 0, count);
                                    }
                                    dest.flush();
                                    dest.close();
                                }
                                zipFile.closeEntry();
                            }
                            // write the timestamp file for these cases 
                            try {
                                //Create file if it does not exist
                                BufferedWriter timestampout = new BufferedWriter(new FileWriter(localtimestampcases));
                                timestampout.write(ts);
                                timestampout.close();
                            } catch (IOException e) {
                                System.out.println("Caught IO Exception while trying to create timestamp file in user directory");
                            }
                        } // end of if dest dir does not exist
                    } catch (IOException io) {
                        Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, io);
                    }

            }
            }
            test = new File(dirpath + File.separator + localtimestamp);
        
        }
        HashMap<String, Object> logginginfo = new HashMap<String, Object>();
        logginginfo = testRunnerView.getLoggingInfo();
        Boolean log = (Boolean) logginginfo.get("logging");
        String conf = (String) logginginfo.get("configpath");

        //System.out.println("the logging is set to " + log);
        //System.out.println("the path is set to " + conf);

        // String logfile = new String(userpath+"/.sbmltestrunner/sbml-test-suite.log");
        String logfile = new String(conf);
        //int log = (Integer)this.testConfiguration.hashMap.get("logging");
        boolean append = true;

        try {

            fh = new FileHandler(logfile, append);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh); // send logger output to FileHandler


        /*   DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        logger.info(dateformat.format(date));
         */
        } catch (IOException ex) {
            Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (log == true) {
            logger.setLevel(Level.ALL); // request that every detail gets logged
        } else {
           // System.out.println("the log equals false");
            logger.setLevel(Level.WARNING);
        }

        // Get the selected level
        int[] levels = new int[4];
        levels[0] = (Integer) this.testConfiguration.hashMap.get("L1V2radiobutton");
        levels[1] = (Integer) this.testConfiguration.hashMap.get("L2V1radiobutton");
        levels[2] = (Integer) this.testConfiguration.hashMap.get("L2V2radiobutton");
        levels[3] = (Integer) this.testConfiguration.hashMap.get("L2V3radiobutton");

        String level = new String();
        for (int i = 0; i < 4; i++) {

            if (levels[i] == 1) {
                if (i == 0) {
                    level = "1.2";
                }
                if (i == 1) {
                    level = "2.1";
                }
                if (i == 2) {
                    level = "2.2";
                }
                if (i == 3) {
                    level = "2.3";
                }

            }
        }
        // Get the selected testtype
        int[] testtype = new int[1];
        String ttype = new String();
        testtype[0] = (Integer) this.testConfiguration.hashMap.get("timecourse");

        if (testtype[0] == 1) {
            ttype = "TimeCourse";
        }


        // Get the excluded component tags
        Vector<String> ctags = new Vector<String>();
        int functiondefinition = (Integer) this.testConfiguration.hashMap.get("FunctionDefinition");
        if (functiondefinition == 1) {
            ctags.addElement("FunctionDefinition");
        }
        /*      int unitdefinition = (Integer)this.testConfiguration.hashMap.get("UnitDefinition");
        if (unitdefinition == 1) {
        ctags.addElement("UnitDefinition");
        } */
        int initialassignment = (Integer) this.testConfiguration.hashMap.get("InitialAssignment");
        if (initialassignment == 1) {
            ctags.addElement("InitialAssignment");
        }
        int assignmentrule = (Integer) this.testConfiguration.hashMap.get("AssignmentRule");
        if (assignmentrule == 1) {
            ctags.addElement("AssignmentRule");
        }
        int raterule = (Integer) this.testConfiguration.hashMap.get("RateRule");
        if (raterule == 1) {
            ctags.addElement("RateRule");
        }
        int algebraicrule = (Integer) this.testConfiguration.hashMap.get("AlgebraicRule");
        if (algebraicrule == 1) {
            ctags.addElement("AlgebraicRule");
        }
        /*         int constraint = (Integer)this.testConfiguration.hashMap.get("Constraint");
        if (constraint == 1) {
        ctags.addElement("Constraint");
        } */
        int eventwithdelay = (Integer) this.testConfiguration.hashMap.get("EventWithDelay");
        if (eventwithdelay == 1) {
            ctags.addElement("EventWithDelay");
        }
        int eventnodelay = (Integer) this.testConfiguration.hashMap.get("EventNoDelay");
        if (eventnodelay == 1) {
            ctags.addElement("EventNoDelay");
        }
        int compartment = (Integer) this.testConfiguration.hashMap.get("Compartment");
        if (compartment == 1) {
            ctags.addElement("Compartment");
        }
        int species = (Integer) this.testConfiguration.hashMap.get("Species");
        if (species == 1) {
            ctags.addElement("Species");
        }
        int reaction = (Integer) this.testConfiguration.hashMap.get("Reaction");
        if (reaction == 1) {
            ctags.addElement("Reaction");
        }
        int parameter = (Integer) this.testConfiguration.hashMap.get("Parameter");
        if (parameter == 1) {
            ctags.addElement("Parameter");
        }
        // Get the excluded test tags
        Vector<String> ttags = new Vector<String>();
        int tdcompartment = (Integer) this.testConfiguration.hashMap.get("2D-Compartment");
        if (tdcompartment == 1) {
            ttags.addElement("2D-Compartment");
        }
        int odcompartment = (Integer) this.testConfiguration.hashMap.get("1D-Compartment");
        if (odcompartment == 1) {
            ttags.addElement("1D-Compartment");
        }
        int zdcompartment = (Integer) this.testConfiguration.hashMap.get("0D-Compartment");
        if (zdcompartment == 1) {
            ttags.addElement("0D-Compartment");
        }
        int nonconstantcompartment = (Integer) this.testConfiguration.hashMap.get("NonConstantCompartment");
        if (nonconstantcompartment == 1) {
            ttags.addElement("NonConstantCompartment");
        }
        int multicompartment = (Integer) this.testConfiguration.hashMap.get("MultiCompartment");
        if (multicompartment == 1) {
            ttags.addElement("MultiCompartment");
        }
        int initialamount = (Integer) this.testConfiguration.hashMap.get("InitialAmount");
        if (initialamount == 1) {
            ttags.addElement("InitialAmount");
        }
        int initialconcentration = (Integer) this.testConfiguration.hashMap.get("InitialConcentration");
        if (initialconcentration == 1) {
            ttags.addElement("InitialConcentration");
        }
        int hasonlysubstanceunits = (Integer) this.testConfiguration.hashMap.get("HasOnlySubstanceUnits");
        if (hasonlysubstanceunits == 1) {
            ttags.addElement("HasOnlySubstanceUnits");
        }
        int boundarycondition = (Integer) this.testConfiguration.hashMap.get("BoundaryCondition");
        if (boundarycondition == 1) {
            ttags.addElement("BoundaryCondition");
        }
        int constantspecies = (Integer) this.testConfiguration.hashMap.get("ConstantSpecies");
        if (constantspecies == 1) {
            ttags.addElement("ConstantSpecies");
        }
        int nonconstantparameter = (Integer) this.testConfiguration.hashMap.get("NonConstantParameter");
        if (nonconstantparameter == 1) {
            ttags.addElement("NonConstantParameter");
        }
        int fastreaction = (Integer) this.testConfiguration.hashMap.get("FastReaction");
        if (fastreaction == 1) {
            ttags.addElement("FastReaction");
        }
        int reversiblereaction = (Integer) this.testConfiguration.hashMap.get("ReversibleReaction");
        if (reversiblereaction == 1) {
            ttags.addElement("ReversibleReaction");
        }
        /*         int zerorate = (Integer)this.testConfiguration.hashMap.get("ZeroRate");
        if (zerorate == 1) {
        ttags.addElement("ZeroRate");
        } */
        int nonunitystoichiometry = (Integer) this.testConfiguration.hashMap.get("NonUnityStoichiometry");
        if (nonunitystoichiometry == 1) {
            ttags.addElement("NonUnityStoichiometry");
        }
        int stoichiometrymath = (Integer) this.testConfiguration.hashMap.get("StoichiometryMath");
        if (stoichiometrymath == 1) {
            ttags.addElement("StoichiometryMath");
        }
        int localparameters = (Integer) this.testConfiguration.hashMap.get("LocalParameters");
        if (localparameters == 1) {
            ttags.addElement("LocalParameters");
        }
        /*        int csymboldelay = (Integer)this.testConfiguration.hashMap.get("CSymbolDelay");
        if (csymboldelay == 1) {
        ttags.addElement("CSymbolDelay");
        } */
        int csymboltime = (Integer) this.testConfiguration.hashMap.get("CSymbolTime");
        if (csymboltime == 1) {
            ttags.addElement("CSymbolTime");
        }
        /*        int massunits = (Integer)this.testConfiguration.hashMap.get("MassUnits");
        if (massunits == 1) {
        ttags.addElement("MassUnits");
        }
        int units = (Integer)this.testConfiguration.hashMap.get("Units");
        if (units == 1) {
        ttags.addElement("Units");
        } */
        /*        int mathml = (Integer)this.testConfiguration.hashMap.get("MathML");
        if (mathml == 1) {
        ttags.addElement("MathML");
        }
        int discontinuity = (Integer)this.testConfiguration.hashMap.get("Discontinuity");
        if (discontinuity == 1) {
        ttags.addElement("Discontinuity");
        } */
        
        
         String osName = System.getProperty("os.name").toLowerCase();
       // String windowsuserpath = userpath.replaceAll("\\\\", "\\\\\\");
       //  logger.info("windowsuserpath is : " +windowsuserpath);
        //  String testdir = (String)this.testConfiguration.hashMap.get("InputPath");
        testdir = test + File.separator + "sbml-test-suite" + File.separator + "cases" + File.separator + "semantic";
       // String windowstestdir = testdir.replaceAll("\\", "\\\\");
       // logger.info("testdir for windows is : " + windowstestdir);
        String command = (String) this.testConfiguration.hashMap.get("WrapperPath");
        //String outdir = (String)this.testConfiguration.hashMap.get("OutputPath");
        String outdir = test + File.separator + "wrapperOutput";
        //String windowsoutdir = outdir.replaceAll("\\", "\\\\");
        //logger.info("outdir for windows is : " + windowsoutdir);
        File o = new File(outdir);
        if (!o.exists()) {
            o.mkdir();
            logger.info("Created wrapper output file directory: " + outdir);

        } else {
            t2.deleteDirectory(o);
            o.mkdir();
            logger.info("Removed old output directory and created new one: " + outdir);

        }
        String wrappercommand="";
        if(osName.indexOf("windows") != -1){
            wrappercommand = command.replaceAll("(?:\\%d)+","\""+testdir.replaceAll("\\\\","\\\\\\\\")+"\"");
            wrappercommand = wrappercommand.replaceAll("(?:\\%o)+", "\""+outdir.replaceAll("\\\\","\\\\\\\\")+"\"");
        }
        else {
            wrappercommand = command.replaceAll("(?:\\%d)+", testdir);
            wrappercommand = wrappercommand.replaceAll("(?:\\%o)+", outdir);
        }
        logger.info("wrappercommand is: " + wrappercommand);
        
        
        Enumeration enumerator1 = ctags.elements();
        Enumeration enumerator2 = ttags.elements();

        SBMLTestSelection t1 = new SBMLTestSelection();

        int header = 1;
        File f = new File(testdir);
        String[] testdir_listing = f.list();
        String tcase = new String();
        String tmodelfile = new String();
        Vector<String> selected_cases = new Vector<String>();

        try {

            for (int i = 0; i < testdir_listing.length; i++) {
                // while(stopper == false){
                // System.out.println("thread is " + Thread.currentThread().getName());
                if (i == testdir_listing.length - 1) {
                    finish = 1;
                }
                tcase = testdir_listing[i];
                Pattern p = Pattern.compile("\\d{5}$");
                Matcher matcher = p.matcher(tcase);
                if (matcher.find()) {

                    tmodelfile = t1.getModelFile(tcase, testdir);

                    t1.readModelFile(tmodelfile);
                    int itsout = 0;
                    if (t1.containsTesttype(ttype) && t1.containsLevel(level)) {
                        while (enumerator1.hasMoreElements()) {
                            String c = (String) enumerator1.nextElement();
                            c.trim();

                            if (t1.containsComponent(c)) {
                                itsout++;

                            }
                        }
                        while (enumerator2.hasMoreElements()) {
                            if (t1.containsTag((String) enumerator2.nextElement())) {
                                itsout++;

                            }
                        }
                        if (itsout == 0) {
                            // add the case to the testingcaselist
                            selected_cases.addElement(tcase);
                        }
                    }
                }
            }
            // end check for tests here and add to a vector then loop through the vector
            Iterator it = selected_cases.iterator();
            totals = selected_cases.size();
            while (it.hasNext()) {
                String testcase = (String) it.next();

                String wrapperwcase = wrappercommand.replaceAll("(?:\\%n)+", testcase);
                logger.info("the wrapper with case number is : " + wrapperwcase);
                // logger.info("Wrapper command to run is: " + wrapperwcase);
                //selected_cases.addElement(testcase);
               
                String[] cmd = new String[3];
                Runtime rt = Runtime.getRuntime();
                Process proc = null;
                try {
                    if (osName.equals("windows nt") || osName.equals("windows xp") || osName.equals("windows 2000") || osName.equals("windows vista")) {
                        cmd[0] = "cmd.exe";
                        cmd[1] = "/C";
                        cmd[2] = wrapperwcase;
                    } else if (osName.equals("windows 95")) {
                        cmd[0] = "command.exe";
                        cmd[1] = "/C";
                        cmd[2] = wrapperwcase;
                    } else if (osName.equals("mac os x") || osName.indexOf("linux") != -1) {
                        cmd[0] = "/bin/bash";
                        cmd[1] = "-c";
                        cmd[2] = wrapperwcase;
                    }

                    //proc = rt.exec(wrapperwcase);
                    proc = rt.exec(cmd);
                } catch (IOException ex) {
                    // Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, "Attempt to run wrapper caused an IOException", ex);
               

                }
                InputStream stdout = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdout);
                BufferedReader br = new BufferedReader(isr);
                try {
                    String line = br.readLine();
                    int exitVal = proc.waitFor();
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    if (exitVal == 0) {
                        // compare output results

                        String user_results = outdir + File.separator + "results" + testcase + ".csv";
                        int totalpoints = 0;
                        //  SBMLTestCase t2 = new SBMLTestCase();

                        String control_results = t2.getControlResults(testcase, testdir);
                        String control_settings = t2.getSettingsFile(testcase, testdir);
                        String plot_file = t2.getPlotFile(testcase, testdir);
                        String html_file = t2.getHtmlFile(testcase, testdir);


                        try {
                            t2.readSettingsFile(control_settings);
                            totalpoints = t2.getVariables_count() * t2.getSteps();
                        } catch (FileNotFoundException e) {
                            String msg = "Control settings file was not found for case " + testcase + " " + e;
                            logger.severe(msg);
                        } catch (IOException e) {
                            String msg = "IOException while reading control settings file for case " + testcase + " " + e;
                            logger.severe(msg);
                        }
                        int steps = t2.getSteps();
                        int vars = t2.getVariables_count();
                        BigDecimal absd = t2.getAbs();
                        BigDecimal reld = t2.getRel();
                        BigDecimal[][] results = new BigDecimal[steps + header][];
                        try {
                            results = t2.readResults(control_results, header, steps, vars);
                        } catch (FileNotFoundException e) {
                            String msg = "Control results file was not found for case " + testcase + " " + e;
                            logger.severe(msg);
                        } catch (IOException e) {
                            String msg = "IOException while reading control results file for case " + testcase + " " + e;
                            logger.severe(msg);
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            String msg = "Control file has inconsistent number of variables for test when compared to settings file for case " + testcase;
                            logger.warning(msg);
                            String mfile = t1.getModelFile(testcase, testdir);
                            t1.readModelFile(mfile);
                            String desc = t1.getSynopsis();
                            Vector<String> cvector = t1.getComponenttags();
                            Vector<String> tvector = t1.getTesttags();
                            TestResultDetails t3 = new TestResultDetails(-1, testcase, desc, "Control file has inconsistent number of variables for test", plot_file, html_file, cvector, tvector, totalpoints);

                            skipped++;
                            //totals++;
                            updateDetails(totals, failed, passed, skipped, t3, finish);

                            continue;
                        }
                        BigDecimal[][] userresults = new BigDecimal[steps + header][];
                        try {
                            userresults = t2.readResults(user_results, header, steps, vars);
                        } catch (FileNotFoundException e) {
                            String msg = "User results file was not found for case " + testcase + " " + e;
                            logger.severe(msg);
                        } catch (IOException e) {
                            String msg = "IOException while reading user results file for case " + testcase + " " + e;
                            logger.severe(msg);
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            String message = e.getMessage() + "for case" + testcase;
                            logger.info(message);

                            String mfile = t1.getModelFile(testcase, testdir);
                            t1.readModelFile(mfile);
                            String desc = t1.getSynopsis();
                            Vector<String> cvector = t1.getComponenttags();
                            Vector<String> tvector = t1.getTesttags();
                            TestResultDetails t3 = new TestResultDetails(-1, testcase, desc, message, plot_file, html_file, cvector, tvector, totalpoints);

                            skipped++;
                            // totals++;
                            updateDetails(totals, failed, passed, skipped, t3, finish);

                            continue;
                        }
                        try {
                            t2.validateResults(results, userresults);
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            String message = e.getMessage() + "for case" + testcase;
                            logger.info(message);

                            String mfile = t1.getModelFile(testcase, testdir);
                            t1.readModelFile(mfile);
                            String desc = t1.getSynopsis();
                            Vector<String> cvector = t1.getComponenttags();
                            Vector<String> tvector = t1.getTesttags();
                            TestResultDetails t3 = new TestResultDetails(-1, testcase, desc, message, plot_file, html_file, cvector, tvector, totalpoints);
                            skipped++;
                            // totals++;
                            updateDetails(totals, failed, passed, skipped, t3, finish);

                            continue;
                        }
                        BigDecimal[][] comp_results = new BigDecimal[steps + 1][vars + 1];
                        try {
                            comp_results = t2.compareResults(results, userresults, steps, vars);
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            String message = e.getMessage() + "for case" + testcase;
                            logger.info(message);
                            String mfile = t1.getModelFile(testcase, testdir);
                            t1.readModelFile(mfile);
                            String desc = t1.getSynopsis();
                            Vector<String> cvector = t1.getComponenttags();
                            Vector<String> tvector = t1.getTesttags();
                            TestResultDetails t3 = new TestResultDetails(-1, testcase, desc, message, plot_file, html_file, cvector, tvector, totalpoints);
                            skipped++;
                            // totals++;
                            updateDetails(totals, failed, passed, skipped, t3, finish);

                            continue;
                        }
                        int pass_fail = t2.analyzeResults(results, comp_results, vars, absd, reld);
                        String mfile = t1.getModelFile(testcase, testdir);
                        t1.readModelFile(mfile);
                        String desc = t1.getSynopsis();
                        Vector<String> cvector = t1.getComponenttags();
                        Vector<String> tvector = t1.getTesttags();
                        TestResultDetails t3 = new TestResultDetails(pass_fail, testcase, desc, "", plot_file, html_file, cvector, tvector, totalpoints);
                        // totals++;
                        if (pass_fail == 0) {
                            passed++;
                        } else {
                            failed++;
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw e;
                        }
                        updateDetails(totals, failed, passed, skipped, t3, finish);
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                    } else {
                        String msg = "Wrapper executed with error - exit value was " + exitVal + " and error message was " + line;
                        logger.severe(msg);
                        // display a wrapper execution error and exit

                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {

                                testRunnerView.displayWrapperError();
                            }
                        });
                        Thread.currentThread().interrupt();

                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Reading wrapper error message caused and IOException", ex);
                    return null;
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
            //  } // end of while
            } // end of for loop
            finish = 1;

            testRunnerView.updateProgress(totals, failed, skipped, passed, finish);
            testRunnerView.disableStartButton();
            testRunnerView.disableStopButton();
            logger.info("Completed testing");

        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, "Wrapper execution caused an InterupptedException");
            return null;
        } finally {    
            return output;
        }
    // }

    }
    /**
     * updateDetils calls the testrunner view method to update the viewer and progress bar details
     * @param totals total number of tests so far
     * @param failed total number of failed tests so far
     * @param passed total number of passed tests so far
     * @param skipped total number of skipped tests so far
     * @param t3 the TestResultDetails oject of the currently calculated result
     * @param finish a flag indicating the finish point of the loop
     */
    public void updateDetails(int totals, int failed, int passed, int skipped, TestResultDetails t3, int finish) {
        final int total = totals;
        final int skip = skipped;
        final int fail = failed;
        final int pass = passed;
        final int finished = finish;
        final TestResultDetails t = t3;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // update totals
                testRunnerView.updateProgress(total, fail, skip, pass, finished);
                if (finished == 1) {
                    testRunnerView.disableStartButton();
                }
                if (t != null) {
                    testCaseListModel.addElement(t);
                    if (t.getResult() > 0) {
                        failedTestCaseListModel.addElement(t);
                    } else if (t.getResult() == 0) {
                        passedTestCaseListModel.addElement(t);
                    } else if (t.getResult() == -1) {
                        skippedTestCaseListModel.addElement(t);
                    }
                }
            }
        });
    }

    @Override
    public void finished() {
//        TestResultDetails[] newdata = (TestResultDetails[]) get();
    }
}
