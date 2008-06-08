
package sbml.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import java.net.URL;
import java.net.URLDecoder;


public class TestRunnerWorker extends SwingWorker {
    
    TestRunnerView testRunnerView;
    TestConfiguration testConfiguration;
    TestCaseListModel testCaseListModel;
    FailedTestCaseListModel failedTestCaseListModel;
    PassedTestCaseListModel passedTestCaseListModel;
    SkippedTestCaseListModel skippedTestCaseListModel;
    final int BUFFER = 2048;
    String tempdir;
    String tdir = new String("No test directory available before running a test.");
    String odir = new String("No output directory available before running a test.");
    
    public TestRunnerWorker(TestRunnerView testRunnerView, TestConfiguration testConfiguration) {
        super();
            this.testRunnerView = testRunnerView;
            this.testConfiguration = testConfiguration;
            this.testCaseListModel = TestRunnerView.testCaseListModel;
            this.failedTestCaseListModel = TestRunnerView.failedTestCaseListModel;
            this.passedTestCaseListModel = TestRunnerView.passedTestCaseListModel;
            this.skippedTestCaseListModel = TestRunnerView.skippedTestCaseListModel;

 }
    
    
    
    public Object construct() {
            int passed=0;
            int failed=0;
            int skipped=0;
            int totals = 0;
            Vector<TestResultDetails> output = new Vector<TestResultDetails>();
            SBMLTestCase t2 = new SBMLTestCase();
            
           
        
            // Look for a test case directory - if it does not exist unzip the zipped test case files
            String dirpath = null;
            File tests = new File("sbmltestrunner");
            if(!tests.exists()){
                tests.mkdir();
                dirpath = tests.getAbsolutePath();
                tdir = tests.getAbsolutePath();
          //  if(getClass().getResource("testcases") == null) {
                System.out.println("I couldn't find the test directory...");
                if(getClass().getResource("resources" + File.separator + "sbml-test-suite.zip") == null) {
                    System.out.println("I couldn't find the zip file either");
                }
                else {
                try {
                    // unzip the file
                    ZipInputStream zipFile = new ZipInputStream(getClass().getResourceAsStream("resources" +  File.separator + "sbml-test-suite.zip"));
                    ZipEntry entry;
                    String destinationDirectory = "sbmltestrunner";
                    
                    
                    while ((entry = zipFile.getNextEntry()) != null) {
                        int count;
                        byte data[] = new byte[2048];
                        BufferedOutputStream dest;
                        File destFile = new File(destinationDirectory, entry.getName());
                        File destinationParent = destFile.getParentFile();
                        destinationParent.mkdirs();
                        if(!entry.isDirectory()){
                            
                            FileOutputStream fout = new FileOutputStream(destFile);
                            dest = new BufferedOutputStream(fout, 2048);
                            while((count = zipFile.read(data,0,2048)) != -1){
                                dest.write(data,0,count);
                            }
                            dest.flush();
                            dest.close();
                        }
                        
                        
                    }
            //        String[] cmd = new String[3];
                 //   URL makedirloc = getClass().getResource("sbmltestrunner" + File.separator + "sbml-test-suite");
                  //  String makedir = URLDecoder.decode(makedirloc.getPath(), "UTF-8");
                 //   System.out.println("the directory to cd into is " + makedir);
            /*        File makedirloc = new File("sbmltestrunner" + File.separator + "sbml-test-suite");
                    String makedir = null;
                    if(makedirloc.exists()){
                         makedir = makedirloc.getAbsolutePath();
                         System.out.println("the abs path to dir is " + makedir);
                    }
                    try {
                        if(osName.equals("windows nt") || osName.equals("windows xp") || osName.equals("windows 2000") || osName.equals("windows vista")) {
                            cmd[0] = "cmd.exe";
                            cmd[1] = "/C";
                            cmd[2] = "make html";
                        }
                        else if(osName.equals("windows 95")){
                            cmd[0] = "command.exe";
                            cmd[1] = "/C";
                            cmd[2] = "make html";
                        }
                        else if(osName.equals("mac os x") || osName.indexOf("linux") != -1){
                            String newmakedir = makedir.replaceAll("[ ]", "\\\\ ");
                            System.out.println(newmakedir);
                            String makedirinquotes = "'" + newmakedir + "'";
                            System.out.println("makedirinquotes = " + makedirinquotes);
                            cmd[0] = "/bin/bash";
                            cmd[1] = "-c";
                            cmd[2] = newmakedir + File.separator + "makescript "  + makedirinquotes;
                            //cmd[3] = "make html";
                        }
                        
                        Runtime runtime = Runtime.getRuntime();
                        Process process = runtime.exec(cmd);
                        // any error message?
                        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");            
            
                        // any output?
                        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
                
                        // kick them off
                        errorGobbler.start();
                        outputGobbler.start();
                                    
                        // any error???
                        int exitVal = process.waitFor();
                        System.out.println("ExitValue: " + exitVal);      
                            
                    }
                    catch(Throwable t){
                        t.printStackTrace();
                    } 
             */
                    // run the make file to creat the html 
                   //     URL make = getClass().getResource("sbmltestrunner" + File.separator + "sbml-test-suite" + File.separator +  "Makefile");
                    //    String makefile = URLDecoder.decode(make.getPath(),"UTF-8");
                     //   System.out.println("the makefile path is " + makefile);
              
                    //String makefile = "sbmltestrunner" + File.separator + "sbml-test-suite" + File.separator +  "Makefile";
      /*              String makefile = "sbmltestrunner" + File.separator + "sbml-test-suite" + File.separator + "make html"; 
                    //String makefile = "make html";   
                    Runtime runtime = Runtime.getRuntime();
                          
                            Process process = null;
                            try {    
                            process = runtime.exec(makefile);
                        } catch (IOException ex) {
                            Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("threw an exception");
                        }
                            InputStream stdout = process.getInputStream();
                            InputStreamReader inputsr = new InputStreamReader(stdout);
                            BufferedReader breader = new BufferedReader(inputsr);
                            try {
                            String l = breader.readLine();
                            int exit = process.waitFor();
                            if(exit != 0){
                                System.out.println("error running makefile");
                            }
                            }
                            catch (InterruptedException ex) {
                            Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("got an interruptedexception");
                        }  
                            
                       */
                        
                } catch (IOException ex) {
                    Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
                }      
                
            }
            else {
                System.out.println("The test directory already exists");
                dirpath = tests.getAbsolutePath();
                tdir = tests.getAbsolutePath();
               //t2.deleteDirectory()
            }
        
        // Get the selected level
            int[] levels = new int[4];
            levels[0] = (Integer)this.testConfiguration.hashMap.get("L1V2radiobutton");
            levels[1] = (Integer)this.testConfiguration.hashMap.get("L2V1radiobutton");
            levels[2] = (Integer)this.testConfiguration.hashMap.get("L2V2radiobutton");
            levels[3] = (Integer)this.testConfiguration.hashMap.get("L2V3radiobutton");

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
            testtype[0] = (Integer)this.testConfiguration.hashMap.get("timecourse");

            if (testtype[0] == 1) {
                ttype = "TimeCourse";
            }
           

            // Get the excluded component tags
            Vector<String> ctags = new Vector<String>();
            int functiondefinition = (Integer)this.testConfiguration.hashMap.get("FunctionDefinition");
            if (functiondefinition == 1) {
                ctags.addElement("FunctionDefinition");
            }
            int unitdefinition = (Integer)this.testConfiguration.hashMap.get("UnitDefinition");
            if (unitdefinition == 1) {
                ctags.addElement("UnitDefinition");
            }
            int initialassignment = (Integer)this.testConfiguration.hashMap.get("InitialAssignment");
            if (initialassignment == 1) {
                ctags.addElement("InitialAssignment");
            }
            int assignmentrule = (Integer)this.testConfiguration.hashMap.get("AssignmentRule");
            if (assignmentrule == 1) {
                ctags.addElement("AssignmentRule");
            }
            int raterule = (Integer)this.testConfiguration.hashMap.get("RateRule");
            if (raterule == 1) {
                ctags.addElement("RateRule");
            }
            int algebraicrule = (Integer)this.testConfiguration.hashMap.get("AlgebraicRule");
            if (algebraicrule == 1) {
                ctags.addElement("AlgebraicRule");
            }
            int constraint = (Integer)this.testConfiguration.hashMap.get("Constraint");
            if (constraint == 1) {
                ctags.addElement("Constraint");
            }
            int eventwithdelay = (Integer)this.testConfiguration.hashMap.get("EventWithDelay");
            if (eventwithdelay == 1) {
                ctags.addElement("EventWithDelay");
            }
            int eventnodelay = (Integer)this.testConfiguration.hashMap.get("EventNoDelay");
            if (eventnodelay == 1) {
                ctags.addElement("EventNoDelay");
            }
            int compartment = (Integer)this.testConfiguration.hashMap.get("Compartment");
            if (compartment == 1) {
                ctags.addElement("Compartment");
            }
            int species = (Integer)this.testConfiguration.hashMap.get("Species");
            if (species == 1) {
                ctags.addElement("Species");
            }
            int reaction = (Integer)this.testConfiguration.hashMap.get("Reaction");
            if (reaction == 1) {
                ctags.addElement("Reaction");
            }
            int parameter = (Integer)this.testConfiguration.hashMap.get("Parameter");
            if (parameter == 1) {
                ctags.addElement("Parameter");
            }
            // Get the excluded test tags
            Vector<String> ttags = new Vector<String>();
            int tdcompartment = (Integer)this.testConfiguration.hashMap.get("2D-Compartment");
            if (tdcompartment == 1) {
                ttags.addElement("2D-Compartment");
            }
            int odcompartment = (Integer)this.testConfiguration.hashMap.get("1D-Compartment");
            if (odcompartment == 1) {
                ttags.addElement("1D-Compartment");
            }
            int zdcompartment = (Integer)this.testConfiguration.hashMap.get("0D-Compartment");
            if (zdcompartment == 1) {
                ttags.addElement("0D-Compartment");
            }
            int nonconstantcompartment = (Integer)this.testConfiguration.hashMap.get("NonConstantCompartment");
            if (nonconstantcompartment == 1) {
                ttags.addElement("NonConstantCompartment");
            }
            int multicompartment = (Integer)this.testConfiguration.hashMap.get("MultiCompartment");
            if (multicompartment == 1) {
                ttags.addElement("MultiCompartment");
            }
            int initialamount = (Integer)this.testConfiguration.hashMap.get("InitialAmount");
            if (initialamount == 1) {
                ttags.addElement("InitialAmount");
            }
            int initialconcentration = (Integer)this.testConfiguration.hashMap.get("InitialConcentration");
            if (initialconcentration == 1) {
                ttags.addElement("InitialConcentration");
            }
            int hasonlysubstanceunits = (Integer)this.testConfiguration.hashMap.get("HasOnlySubstanceUnits");
            if (hasonlysubstanceunits == 1) {
                ttags.addElement("HasOnlySubstanceUnits");
            }
            int boundarycondition = (Integer)this.testConfiguration.hashMap.get("BoundaryCondition");
            if (boundarycondition == 1) {
                ttags.addElement("BoundaryCondition");
            }
            int constantspecies = (Integer)this.testConfiguration.hashMap.get("ConstantSpecies");
            if (constantspecies == 1) {
                ttags.addElement("ConstantSpecies");
            }
            int nonconstantparameter = (Integer)this.testConfiguration.hashMap.get("NonConstantParameter");
            if (nonconstantparameter == 1) {
                ttags.addElement("NonConstantParameter");
            }
            int fastreaction = (Integer)this.testConfiguration.hashMap.get("FastReaction");
            if (fastreaction == 1) {
                ttags.addElement("FastReaction");
            }
            int reversiblereaction = (Integer)this.testConfiguration.hashMap.get("ReversibleReaction");
            if (reversiblereaction == 1) {
                ttags.addElement("ReversibleReaction");
            }
            int zerorate = (Integer)this.testConfiguration.hashMap.get("ZeroRate");
            if (zerorate == 1) {
                ttags.addElement("ZeroRate");
            }
            int nonunitystoichiometry = (Integer)this.testConfiguration.hashMap.get("NonUnityStoichiometry");
            if (nonunitystoichiometry == 1) {
                ttags.addElement("NonUnityStoichiometry");
            }
            int stoichiometrymath = (Integer)this.testConfiguration.hashMap.get("StoichiometryMath");
            if (stoichiometrymath == 1) {
                ttags.addElement("StoichiometryMath");
            }
            int localparameters = (Integer)this.testConfiguration.hashMap.get("LocalParameters");
            if (localparameters == 1) {
                ttags.addElement("LocalParameters");
            }
            int csymboldelay = (Integer)this.testConfiguration.hashMap.get("CSymbolDelay");
            if (csymboldelay == 1) {
                ttags.addElement("CSymbolDelay");
            }
            int csymboltime = (Integer)this.testConfiguration.hashMap.get("CSymbolTime");
            if (csymboltime == 1) {
                ttags.addElement("CSymbolTime");
            }
            int massunits = (Integer)this.testConfiguration.hashMap.get("MassUnits");
            if (massunits == 1) {
                ttags.addElement("MassUnits");
            }
            int units = (Integer)this.testConfiguration.hashMap.get("Units");
            if (units == 1) {
                ttags.addElement("Units");
            }
            int mathml = (Integer)this.testConfiguration.hashMap.get("MathML");
            if (mathml == 1) {
                ttags.addElement("MathML");
            }
            int discontinuity = (Integer)this.testConfiguration.hashMap.get("Discontinuity");
            if (discontinuity == 1) {
                ttags.addElement("Discontinuity");
            }
            
          //  String testdir = (String)this.testConfiguration.hashMap.get("InputPath");
            String testdir = tests + File.separator + "sbml-test-suite" + File.separator + "cases" + File.separator + "semantic";
            String command = (String)this.testConfiguration.hashMap.get("WrapperPath");
            //String outdir = (String)this.testConfiguration.hashMap.get("OutputPath");
            String outdir = tests + File.separator + "wrapperOutput";
            File o = new File(outdir);
            if(!o.exists()){
                o.mkdir();
                odir = o.getAbsolutePath();
            }
            else {
                t2.deleteDirectory(o);
                o.mkdir();
                odir = o.getAbsolutePath();
            }
            
            String wrappercommand = command.replaceAll("(?:\\%d)+", testdir);
            wrappercommand = wrappercommand.replaceAll("(?:\\%o)+", outdir);
            
            
            Enumeration enumerator1 = ctags.elements();
            Enumeration enumerator2 = ttags.elements();
           
            SBMLTestSelection t1 = new SBMLTestSelection();
            
            int header = 1;
            File f = new File(testdir);
            String[] testdir_listing = f.list();
            String tcase = new String();
            String tmodelfile = new String();
            Vector<String> selected_cases = new Vector<String>();

            for (int i = 0; i < testdir_listing.length; i++) {
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
                            String wrapperwcase = wrappercommand.replaceAll("(?:\\%n)+", tcase);
                          
                            selected_cases.addElement(tcase);
                            String osName = System.getProperty("os.name").toLowerCase();                      
                            String[] cmd = new String[3];
                            Runtime rt = Runtime.getRuntime();
                            Process proc = null;
                            try {   
                                if(osName.equals("windows nt") || osName.equals("windows xp") || osName.equals("windows 2000") || osName.equals("windows vista")) {
                                    cmd[0] = "cmd.exe";
                                    cmd[1] = "/C";
                                    cmd[2] = wrapperwcase;
                                }
                                 else if(osName.equals("windows 95")){
                                    cmd[0] = "command.exe";
                                    cmd[1] = "/C";
                                    cmd[2] = wrapperwcase;
                                }
                                else if(osName.equals("mac os x") || osName.indexOf("linux") != -1){                                   
                                    cmd[0] = "/bin/bash";
                                    cmd[1] = "-c";
                                    cmd[2] = wrapperwcase;                           
                                }
                                
                                //proc = rt.exec(wrapperwcase);
                                proc = rt.exec(cmd);
                            } catch (IOException ex) {
                                Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println("threw an exception");
                            }
                            InputStream stdout = proc.getInputStream();
                            InputStreamReader isr = new InputStreamReader(stdout);
                            BufferedReader br = new BufferedReader(isr);
                            try {
                            String line = br.readLine();
                            int exitVal = proc.waitFor();
                           
                            if(exitVal == 0){
                                // compare output results
                               
                                String user_results = outdir + File.separator + "results" + tcase + ".csv";
                                int totalpoints = 0;
                              //  SBMLTestCase t2 = new SBMLTestCase();
                                
                                String control_results = t2.getControlResults(tcase, testdir); 
                                String control_settings = t2.getSettingsFile(tcase, testdir); 
                                String plot_file = t2.getPlotFile(tcase, testdir);
                                String html_file = t2.getHtmlFile(tcase, testdir);
                                
                             
                                try {
                                    t2.readSettingsFile(control_settings);
                                    totalpoints = t2.getVariables_count() * t2.getSteps();
                                }
                                catch(FileNotFoundException e) {
                                    System.err.println("FileNotFound when reading control settings file");
                                }
                                catch(IOException e){
                                    System.err.println("IOException error while reading control settings file");
                                }
                               int steps = t2.getSteps(); 
                                int vars = t2.getVariables_count();
                               BigDecimal absd = t2.getAbs();
                               BigDecimal reld = t2.getRel();
                               BigDecimal [][] results = new BigDecimal[steps + header][];
                               try{
                                       results = t2.readResults(control_results, header, steps, vars);
                               }
                               catch(FileNotFoundException e){
                                   System.err.println("File not found when reading control results");
                               }
                               catch(IOException e) {
                                   System.err.println("IOException error while reading control results");
                               }
                               catch(Exception e){
                                   System.err.println("Control file has inconsistent number of variables for test");
                                   String mfile = t1.getModelFile(tcase , testdir);
                                   t1.readModelFile(mfile);
                                   String desc = t1.getSynopsis();
                                   Vector<String> cvector = t1.getComponenttags();
                                   Vector<String> tvector = t1.getTesttags();
                                   TestResultDetails t3 = new TestResultDetails(-1,tcase,desc,"Control file has inconsistent number of variables for test",plot_file,html_file,cvector, tvector,totalpoints);
                                   
                                   skipped++;
                                   totals++;
                                   updateDetails(totals,failed,passed,skipped,t3);

                                   continue;
                               }
                               BigDecimal [][] userresults = new BigDecimal[steps + header][];
                               try{
                                       userresults = t2.readResults(user_results, header, steps, vars);
                               }
                               catch(FileNotFoundException e){
                                   System.err.println("File not found when reading user results");
                               }
                               catch(IOException e) {
                                   System.err.println("IOException error while reading user results");
                               }
                               catch(Exception e){
                                   String message = e.getMessage();
                                   System.err.println(message);
                                   String mfile = t1.getModelFile(tcase , testdir);
                                   t1.readModelFile(mfile);
                                   String desc = t1.getSynopsis();
                                   Vector<String> cvector = t1.getComponenttags();
                                   Vector<String> tvector = t1.getTesttags();
                                   TestResultDetails t3 = new TestResultDetails(-1,tcase,desc,message,plot_file,html_file,cvector, tvector,totalpoints);
                                 
                                   skipped++;
                                   totals++;
                                   updateDetails(totals,failed,passed,skipped,t3);

                                   continue;
                               }
                               try {
                                   t2.validateResults(results,userresults);
                               }
                               catch(Exception e){
                                   String message = e.getMessage();
                                   System.err.println(message);
                                   String mfile = t1.getModelFile(tcase,testdir);
                                   t1.readModelFile(mfile);
                                   String desc = t1.getSynopsis();
                                   Vector<String> cvector = t1.getComponenttags();
                                   Vector<String> tvector = t1.getTesttags();
                                   TestResultDetails t3 = new TestResultDetails(-1,tcase,desc,message,plot_file,html_file,cvector, tvector,totalpoints);
                                   skipped++;
                                   totals++;
                                   updateDetails(totals,failed,passed,skipped,t3);

                                   continue;
                               }
                               BigDecimal [][] comp_results = new BigDecimal [steps+1][vars+1];
                               try{
                                   comp_results = t2.compareResults(results,userresults,steps,vars);
                               }
                               catch(Exception e){
                                   String message = e.getMessage();
                                   System.err.println(message);
                                   String mfile = t1.getModelFile(tcase,testdir);
                                   t1.readModelFile(mfile);
                                   String desc = t1.getSynopsis();
                                   Vector<String> cvector = t1.getComponenttags();
                                   Vector<String> tvector = t1.getTesttags();
                                   TestResultDetails t3 = new TestResultDetails(-1,tcase,desc,message,plot_file,html_file,cvector, tvector,totalpoints);
                                   skipped++;
                                   totals++;
                                   updateDetails(totals,failed,passed,skipped,t3);

                                   continue;
                               }
                               int pass_fail = t2.analyzeResults(results, comp_results, vars, absd, reld);
                               String mfile = t1.getModelFile(tcase,testdir);
                               t1.readModelFile(mfile);
                               String desc = t1.getSynopsis();
                               Vector<String> cvector = t1.getComponenttags();
                               Vector<String> tvector = t1.getTesttags();
                               TestResultDetails t3 = new TestResultDetails(pass_fail,tcase,desc,"",plot_file,html_file, cvector, tvector,totalpoints);
                               totals++;
                               if(pass_fail == 0) passed++;
                               else failed++;
                               
                               try {
                                    Thread.sleep(150);
                                } catch (InterruptedException e) {
                                    return null;
                                }
                               updateDetails(totals,failed,passed,skipped,t3);
                               
                            }
                            }
                        catch (InterruptedException ex) {
                            Logger.getLogger(TestRunnerWorker.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("got an interruptedexception");
                        }                            
                        catch(IOException ex){
                                System.out.println("got an io exception");
                            }
                        }
                        
                    } else {
                        
                    }
                }
            } // end of for loop
          

        if (Thread.currentThread().isInterrupted()) {
            return null;
        } else {
           // return new TestResultDetails[2];
             return output;
        }
   // }
    
    }
     public void updateDetails(int totals, int failed, int passed, int skipped, TestResultDetails t3) {
        final int total = totals;
        final int skip = skipped;
        final int fail = failed;
        final int pass = passed;
        final TestResultDetails t = t3;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // update totals
                testRunnerView.updateProgress(total, fail, skip, pass);
                testCaseListModel.addElement(t);
                if(t.getResult() > 0){
                    failedTestCaseListModel.addElement(t);
                }
                else if(t.getResult() == 0){
                    passedTestCaseListModel.addElement(t);
                }
                else if(t.getResult() == -1){
                    skippedTestCaseListModel.addElement(t);
                }
            }
        });
    }
     
    public String getOdir() {
        return odir;
    }
    
    public String getTdir() {
        return tdir;
    }

    @Override
    public void finished() {
//        TestResultDetails[] newdata = (TestResultDetails[]) get();

    }
    
    class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
        @Override
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(type + ">" + line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}
}
