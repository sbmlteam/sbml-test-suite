#!/usr/bin/awk -f
$1 ~ /URL/ {
        URL=""
        for (i=2; i != NF+1; i++)
        {
            URL = (URL $(i) " ")
        }
}

$1 ~ /REM/ {
        message = ""
        for (i=2; i != NF+1; i++)
        {
            message = (message $(i) " ")
        }
        print message
}
END {
  print "see http://www.sbml.org/wiki/" URL
}
