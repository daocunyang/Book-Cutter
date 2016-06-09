@echo off
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_91
set classpath=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;
set path=%JAVA_HOME%\bin
javac Main.java
java Main
echo Success...
pause