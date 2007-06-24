#!/usr/bin/awk -f
BEGIN {
    if (wrapper == "")
    {
        print "Set wrapper variable"
        halting = 1
        exit 1
    }
    
    category = ""
    total_passed = 0
    total_failed = 0
    grand_total = 0
    halting = 0
}
$1 ~ /TEST/ {
    testpath=$2  
    total++
    test = ("test.bsh " wrapper " " testpath)
    print "running", test 
    result = system(test)
    if (result == 0)
    {
        #print testpath, "Successful"
        #print " "
        passed++
    }
    else
    {
        print testpath, "Failed"
        print " "
        failed++ 
        
        if (haltOnFailure == "true")
        {
            halting = 1
            exit 1
        }
    }
}
$1 ~ /CATEGORY/ {
        if (category != "")
        {
            print " "
            print "SUMMARY", category, "passed", passed, "failed", failed, "-", passed*100/total, "%"
            print " "
            total_passed += passed
            total_failed += failed
            grand_total += total 
        }
          
        n = (NF-1); # number of words
        
        #print "number of words", n
        
        category = $2
        
        for (i=3; i != n+2; i++)
        {
            category = (category  " " $(i))    
        }
        passed = 0
        failed = 0 
        total = 0
}
END {
     if (halting == 0)
     {
        if (category != "")
        {
            print " "
            print "SUMMARY", category, "passed", passed, "failed", failed, "-", passed*100/total, "%" 
            print " "
            total_passed += passed
            total_failed += failed
            grand_total += total 
        }
        print "SUMMARY FINAL", "passed", total_passed, "failed", total_failed, "-", total_passed*100/grand_total, "%" 
     }
}
