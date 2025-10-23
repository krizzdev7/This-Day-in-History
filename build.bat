@echo off
setlocal EnableExtensions EnableDelayedExpansion

echo Compiling This Day in History...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Collect all Java source files recursively
set SOURCES=
for /R src %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)

if "!SOURCES!"=="" (
    echo No Java source files found under src\
    exit /b 1
)

javac -cp ".;lib/*" -d bin !SOURCES!
if %errorlevel% == 0 (
    echo Compilation successful!
    echo.
    echo To run the application, execute: run.bat
) else (
    echo Compilation failed! Please check for errors.
    exit /b 1
)

endlocal
