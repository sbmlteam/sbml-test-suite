#!/usr/bin/awk -f
{
    # process everything but the first line
    if (FNR != 1)
    {
        n = (NF-3)/2; # number of species
        
        #print "number of species", n
        
        if ( $2 < 0.999*$(n+3) || $(n+3) < 0.999*$2 )
        {
            print "time fields out of sync on line", FNR, "in file", FILENAME;
            exit 1
        }
        
        j=n+4;
        
        for (i=3; i != n+3; i++)
        {
            # goto zero if small - filters out bizare numbers
            num1 = $(i) < 1e-24 ? 0 : $(i);
            num2 = $(j) < 1e-24 ? 0 : $(j);
            
            if ( num1 < 0.999*num2 || num2 < 0.999*num1 )
            {
                print "species", i-2, "values", num1, "and", num2, "don't match at time", $2
                print "  on line", FNR, "in file", FILENAME
                exit 1
            }
            
            j++;
        }
    }
}
