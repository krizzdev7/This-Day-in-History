#!/usr/bin/env bash
set -euo pipefail

# Run CSV importer for events
# Usage: ./run-import.sh [path-to-csv]

CP="bin:lib/mysql-connector-j-9.4.0.jar"
if [[ $# -eq 0 ]]; then
  echo "Running importer with default CSV (history_events.csv or historical_events.csv)"
  java -cp "$CP" util.CsvEventImporter
else
  echo "Running importer with CSV: $1"
  java -cp "$CP" util.CsvEventImporter "$1"
fi
