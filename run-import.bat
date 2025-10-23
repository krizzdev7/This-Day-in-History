@echo off
setlocal EnableExtensions

REM Run CSV importer for events
REM Usage: run-import.bat [path-to-csv]

set CP=bin;lib\mysql-connector-j-9.4.0.jar
if "%~1"=="" (
  echo Running importer with default CSV: history_events.csv or historical_events.csv
  java -cp "%CP%" util.CsvEventImporter
) else (
  echo Running importer with CSV: %~1
  java -cp "%CP%" util.CsvEventImporter "%~1"
)

endlocal
