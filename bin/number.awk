#!/usr/bin/awk -f
BEGIN{l=1;FS=","}
{ print l, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12; l++ }