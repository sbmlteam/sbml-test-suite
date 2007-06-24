#!/usr/bin/awk -f
$1 ~ /TIME/ { time=$2 }
$1 ~ /STEPS/ { steps=$2 }
$1 ~ /SPECIES/ {  
        n = (NF-1); # number of species
        
        #print "number of species", n
        
        for (i=2; i != n+2; i++)
        {
            species = (species  " " $(i))    
        }
}
$1 ~ /REM/ {}
END {
    print time, steps, "testout.CSV", "temp", species
}
