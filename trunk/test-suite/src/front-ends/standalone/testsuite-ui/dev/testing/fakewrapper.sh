#!/bin/csh -q

/bin/rm -f $3/$2.csv
cp $1/$2/$2-results.csv $3/$2.csv
