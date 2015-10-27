@echo off
set JAVA_HOME=C:\Java\jdk1.8.0_45
cd /D %~dp0

%JAVA_HOME%\bin\java.exe -jar onyaec.jar %*