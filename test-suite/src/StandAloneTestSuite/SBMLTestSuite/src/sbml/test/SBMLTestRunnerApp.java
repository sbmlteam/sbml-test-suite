
package sbml.test;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class SBMLTestRunnerApp {

    static {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SBML Test Suite ");
            System.setProperty("apple.awt.showGrowBox", "true");
        } catch (Exception e) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        boolean guiFlag = true;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-nogui")) {
                guiFlag = false;
            } else if (args[i].equalsIgnoreCase("-help")) {
                showUsage();
                System.exit(1);
            }

        }


        if (guiFlag) {
            SBMLTestRunnerApp inst = new SBMLTestRunnerApp();
            inst.startGui(args);
        } else {
            startCLI(args);
        }


    }

    @SuppressWarnings("static-access")
    private void startGui(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        // handle exception
        } catch (ClassNotFoundException e) {
        // handle exception
        } catch (InstantiationException e) {
        // handle exception
        } catch (IllegalAccessException e) {
        // handle exception
        }


        TestRunnerView testRunnerView = new TestRunnerView();
        testRunnerView.showGui(args);
    }

    private static void showUsage() {
        System.out.println("Usage: java -jar SBMLTestRunner.jar [OPTIONS]");
        System.out.println("-nogui       Start without GUI (javavm needs to be in headless mode");
    }

    private static void startCLI(String[] args) {
        System.out.println("Starting without Gui...");
    }
}
