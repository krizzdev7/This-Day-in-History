# Build and Run Scripts

## Windows (build.bat)

@echo off
echo Compiling This Day in History...
javac -cp ".;lib/*" -d bin src/**/*.java src/*.java
if %errorlevel% == 0 (
    echo Compilation successful!
    echo.
    echo To run the application, execute: run.bat
) else (
    echo Compilation failed! Please check for errors.
)
pause
