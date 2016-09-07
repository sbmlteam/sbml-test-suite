@echo off
REM ADJUST THE FOLLOWING TO MEET YOUR ENVIRONMENT
SET JAVA_HOME=C:\Program Files (x86)\Java\jdk1.7.0_09
SET L4J=C:\Program Files (x86)\Launch4j\launch4jc.exe
SET ANT=C:\Program Files\apache-ant-1.8.4\bin\ant.bat
SET NSIS=C:\Program Files (x86)\NSIS\makensis.exe

REM SHOULD NOT NEED TO MODIFY ANYTHING BELOW ...
SET BASE_DIR=%~dp0
cd /D %BASE_DIR%
call "%ANT%" stage-win32
"%L4J%" testsuite-l4j.xml
"%NSIS%" NSIS_SBMLTestsuite.nsi
