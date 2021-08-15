@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  codegen startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and CODEGEN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%/lib/guava-18.0.jar;%APP_HOME%/lib/commons-io-2.4.jar;%APP_HOME%/lib/commons-lang3-3.4.jar;%APP_HOME%/lib/jalopy-1.5rc3.jar;%APP_HOME%/lib/log4j-1.2.8.jar;%APP_HOME%/lib/lombok-1.16.6.jar;%APP_HOME%/lib/mybatis-generator-core-1.3.2.jar;%APP_HOME%/lib/mysql-connector-java-5.1.18.jar;%APP_HOME%/lib/org.eclipse.core.commands-3.6.0.jar;%APP_HOME%/lib/org.eclipse.core.contenttype-3.4.100.jar;%APP_HOME%/lib/org.eclipse.core.expressions-3.4.300.jar;%APP_HOME%/lib/org.eclipse.core.filesystem-1.3.100.jar;%APP_HOME%/lib/org.eclipse.core.jobs-3.5.100.jar;%APP_HOME%/lib/org.eclipse.core.resources-3.7.100.jar;%APP_HOME%/lib/org.eclipse.core.runtime-3.7.0.jar;%APP_HOME%/lib/org.eclipse.equinox.app-1.3.100.jar;%APP_HOME%/lib/org.eclipse.equinox.common-3.6.0.jar;%APP_HOME%/lib/org.eclipse.equinox.preferences-3.4.1.jar;%APP_HOME%/lib/org.eclipse.equinox.registry-3.5.101.jar;%APP_HOME%/lib/org.eclipse.jdt.core-3.10.0.jar;%APP_HOME%/lib/org.eclipse.osgi-3.7.1.jar;%APP_HOME%/lib/org.eclipse.text-3.5.101.jar;%APP_HOME%/lib/virjarjcd-0.0.1-SNAPSHOT.jar


@rem Execute codegen
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CODEGEN_OPTS%  -classpath "%CLASSPATH%" cn.edu.scu.virjarjcd.codegertator.Generator %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable CODEGEN_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%CODEGEN_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
