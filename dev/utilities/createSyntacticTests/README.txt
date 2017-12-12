This directory is designed to be copied to the 'examples/c++' directory in the libsbml source code.  If you then modify

libsbml/examples/c++/CMakeLists.txt

to include the line

add_subdirectory(createSyntacticTests)

and build libsbml with WITH_EXAMPLES turned on, the program will be compiled and can be run.  All packages must be enabled, as well.

The program takes two arguments, the first of which should be the libsbml source directory ('path/to/libsbml/'), and the second of which should be the directory in which to create the syntactic tests, typically 'path/to/test-suite/cases/':

  createSyntacticTests svn/libsbml/ svn/test-suite/cases/

It will then search the libsbml source code for models created to test the validation rules, analyze them, and copy them to the 'syntactic/' subdirectory of the output directory, in an appropriate folder, and renamed so as to have more information about the file in the name.  It will also create a text file companion for each model, with libsbml's validation error and warning messages.
