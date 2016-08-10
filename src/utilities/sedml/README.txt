Collection of tools for generating SED-ML and validating the tests. Usage: 

<mono> GenerateSedML 
		[--dir | -d <test cases directory>]
		[--case | -c <test case directory>]
		[--all-levels-and-versions | -a]
		[--specific-versions | -s <list of  versions i.e l2v1>]


<mono> VerifyTests    
		[--dir | -d <test cases directory>]
		[--case | -c <test case directory>]
		[--all-levels-and-versions | -a]
		[--specific-versions | -s <list of  versions i.e l2v1>]
				
For example: 

Generate sed-ml for all l1v2 models: 

GenerateSedML.exe -d c:\Development\test-suite\cases\semantic -s l1v2

Generate SED-ML files for: c:\Development\test-suite\cases\semantic
... reading suite
... suite has: 980 cases
......................N(00023, l1v2).N(00024, l1v2).N(00025, l1v2).N(00026, l1v2).N(00027, l1v2).N(00028, l1v2)......N(0
0034, l1v2).N(00035, l1v2).N(00036, .

Done



One of the options -c | --case or -d | --dir has to be used. 

In the output above N(00023) notes that no l1v2 model is available for. 

Before use libsbmlcsP.dll should be copied into the path and the accompanying libsbmlcs native library has to be in the DYLD_LIBRARY_PATH/LD_LIBRARY_PATH/PATH environment. 

------------------------------
2011-05-25 Frank T. Bergmann