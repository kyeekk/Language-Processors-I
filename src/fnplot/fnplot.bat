@echo off

set BASE="C:\Users\kayla\Documents\UWI YEAR 3 SEM 1\COMP3652 - Language Processors"

set CUP_VERSION=11b
set CUP_ALIAS=cup
set CUP_HOME=C:\Users\kayla\Desktop\cup

set PROJECT_HOME=%BASE%\src

call jflex syntax/FnPlotLexer
call %CUP_ALIAS% -parser FnPlotParser -destdir syntax syntax\FnPlotParser.cup

REM compile the code
javac -classpath %CUP_HOME%\lib\java-cup-%CUP_VERSION%-runtime.jar;%PROJECT_HOME%\fnplot\cs34q.gfx.jar;%PROJECT_HOME%; gui/*.java syntax/*.java semantics/*.java sys/*.java values/*.java

REM run the repl
java -classpath %CUP_HOME%\lib\java-cup-%CUP_VERSION%-runtime.jar;%PROJECT_HOME%\fnplot\cs34q.gfx.jar;%PROJECT_HOME%; fnplot.gui.FnPlotFrame