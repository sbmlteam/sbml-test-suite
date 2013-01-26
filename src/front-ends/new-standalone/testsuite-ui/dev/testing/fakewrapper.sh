#!/bin/csh -q
echo `date` =========
echo $* >>& /tmp/wrapper-log.txt

cp $1/$2/$2-results.csv $3/$2.csv
