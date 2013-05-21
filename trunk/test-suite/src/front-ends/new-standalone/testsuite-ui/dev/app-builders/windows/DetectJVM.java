//
// @file   DetectJVM.java
// @brief  Detect the word size of the JVM invoked by installer on Windows
// @author Michael Hucka
// @date   Created 2013-05-21 <mhucka@caltech.edu>
//
// The original code contained here was taken from the following StackOverflow
// answer by user "Gene": http://stackoverflow.com/a/4137875/743730
// No copyright or license information was given; consequently, I assume that
// we are free to copy and use the program.
//
// ----------------------------------------------------------------------------
// This file is part of the SBML Testsuite. Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// ----------------------------------------------------------------------------

/**
 * Class to detect whether a JVM is 32-bit or 64-bit.  Usage:
 *
 * Approach 1: java -jar DetectJVM.jar
 *   
 *     In this case, it only sets the exit code.  The code will be the number
 *     64 or 32, depending on the word size of the JVM.
 *
 * Approach 2: java -jar DetectJVM.jar -print
 *   
 *     In this case, it not only sets the exit code; it also prints information
 *     to the standard output stream.
 */
public class DetectJVM
{
    private static final String keys [] =
        {
        "sun.arch.data.model",
        "com.ibm.vm.bitmode",
        "os.arch",
        };

    public static void main (String [] args)
    {
        boolean print = args.length > 0 && "-print".equals(args[0]);
        for (String key : keys )
        {
            String property = System.getProperty(key);
            if (print) System.out.println(key + "=" + property);
            if (property != null)
            {
                int errCode = (property.indexOf("64") >= 0) ? 64 : 32;
                if (print) System.out.println("err code=" + errCode);
                System.exit(errCode);
            }
        }
    }
}
