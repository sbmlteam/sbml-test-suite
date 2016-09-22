This directory is designed to be copied to the 'examples/c++' directory in the libsbml source code.  If you then modify

libsbml/examples/c++/CMakeLists.txt

to include the line

add_subdirectory(generateTestsFrom)

and build libsbml with WITH_EXAMPLES turned on, the program will be compiled and can be run.
