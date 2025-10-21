#!/bin/bash
# Build script for Linux/macOS

echo "Compiling This Day in History..."
javac -cp ".:lib/*" -d bin src/**/*.java src/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the application, execute: ./run.sh"
else
    echo "Compilation failed! Please check for errors."
    exit 1
fi
