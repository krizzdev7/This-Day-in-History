```GOAL
-----
Generate a complete, production-quality Java desktop application called **ThisDayHistory**:
- Java 24
- Swing UI
- Backend: MySQL via JDBC (mysql-connector-j)
- Build: Maven
- Development environment: VSCode (assume developer will run there)
- No external AI APIs or network calls from application
- Output must be a runnable jar and full source tree with Maven pom.xml and SQL script

CONSTRAINTS & NON-FUNCTIONAL REQUIREMENTS
----------------------------------------
1. Use Java 24 language features only where appropriate; target compatibility set in Maven (release 24).
2. Use Swing (not JavaFX). Follow Swing best practices: update UI on Event Dispatch Thread (EDT), use SwingWorker for long-running DB tasks.
3. Use MVC separation: model classes, DAO layer (JDBC), service layer (business logic), and Swing controllers/views.
4. Use Singleton pattern for DB connection manager. Use try-with-resources for all JDBC resources.
5. Use PreparedStatement for all SQL to avoid injection.
6. Provide robust exception handling and user-friendly error dialogs (JOptionPane).
7. Responsive UI: use progress indicators / disabled buttons while background tasks run.
8. Provide unit tests where reasonable (JUnit 5) for critical service/DAO methods (can mock DB or use an in-memory test DB).
9. Provide logging using java.util.logging (or slf4j-simple via Maven); do not require remote logging.
10. The app must auto-create DB schema if missing (optionally using a provided SQL script). Also provide a separate SQL file that can be run manually.
11. Provide sample seed data (at least 30 facts across many dates).
12. Provide README with exact setup instructions for MySQL, connector jar, Maven commands, and how to run the jar including classpath steps if using a local connector jar.

PROJECT SCOPE / FEATURES
------------------------
Implement the following functionality in the desktop app:

Core features:
1. **Date selection**: UI component (JSpinner or combo boxes) to pick a month & day or "Use Today".
2. **Get Random Fact**: For selected date, show a random historical fact (non-repeating in the current session until cycle completes). If no fact for date, show fallback content.
3. **List Facts for Date**: Show a table of all facts for chosen date (with ID, event, category).
4. **Fact Management (CRUD)**: Add, Edit, Delete facts (modal dialogs).
   - Add dialog must validate fields and persist into DB.
   - Edit loads selected record; Delete asks for confirmation.
5. **Search**: Search facts by keyword and category.
6. **Import CSV**: Function to import a CSV of facts into database (provide CSV format spec and sample).
7. **Export**: Export displayed facts to CSV.
8. **Favorite/Bookmark**: Mark facts as favorite; separate view for favorites.
9. **History / Non-repetition**: Keep session-level record of shown facts and ensure randomization doesn’t repeat until all facts for that date are shown.
10. **Settings**: Persistence for DB connection properties (host, port, DB name, username). Save settings to a local properties file (config.properties) using simple encryption or obfuscation is optional but not required.
11. **About & Help**: Simple About dialog and a built-in user manual accessible from Help menu.

UI / UX Requirements
-------------------
1. Main window layout:
   - Top toolbar: Date selector, "Use Today" toggle, Get Random Fact button, Search box, Import, Export buttons.
   - Center: Split pane — left side: table (facts list), right side: fact detail panel with full text, category, date, buttons: Favorite, Edit, Delete.
   - Bottom: Status bar showing DB connection status and last action/time.
   - Menu bar with File (Exit, Import CSV, Export CSV), Edit (Add Fact), View (Favorites), Help (User Manual, About).
2. Use JTables for lists, modal JDialog for add/edit, JFileChooser for import/export.
3. Ensure keyboard accessibility (Enter triggers search, Ctrl+N = New Fact).
4. Use icons where appropriate (optional small embedded icons—no external downloads).

DATA MODEL (SQL)
----------------
Create table `historical_facts` with these columns:

CREATE TABLE IF NOT EXISTS historical_facts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  month TINYINT NOT NULL,         -- 1-12
  day TINYINT NOT NULL,           -- 1-31 (validate by month on insert)
  year SMALLINT NULL,             -- optional year of event, allow NULL
  event TEXT NOT NULL,
  category VARCHAR(64),
  source VARCHAR(255),
  favorite BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (month, day, event(200)) -- allow multiple same-day different events but dedup common duplicates
);

Also provide an SQL file `schema.sql` that:
- Creates database schema for database named `thisdayhistory` (if not exists).
- Creates the `historical_facts` table.
- Inserts at least 30 sample records across different dates and categories.

DETAILED PACKAGE & FILE STRUCTURE
---------------------------------
Use Maven standard layout.

thisdayhistory/
├─ pom.xml
├─ README.md
├─ schema.sql
├─ sample_data.csv
├─ src/main/java/com/thisdayhistory/
│  ├─ MainApp.java
│  ├─ config/
│  │  └─ AppConfig.java     // reads/writes config.properties
│  ├─ model/
│  │  └─ HistoricalFact.java
│  ├─ dao/
│  │  └─ DatabaseManager.java
│  │  └─ HistoricalFactDAO.java
│  ├─ service/
│  │  └─ FactService.java
│  ├─ ui/
│  │  └─ MainFrame.java
│  │  └─ FactTableModel.java
│  │  └─ AddEditFactDialog.java
│  │  └─ ImportExportUtil.java
│  │  └─ AboutDialog.java
│  └─ util/
│     └─ CsvUtil.java
│     └─ DateUtil.java
├─ src/main/resources/
│  └─ config.properties (template)
├─ src/test/java/... (unit tests)
└─ lib/ (optional for local mysql-connector-j.jar if user insists)

DEVELOPMENT & BUILD INSTRUCTIONS (explicit)
------------------------------------------
1. **pom.xml**: Include `mysql-connector-j` dependency (latest version compatible with Java 24). Also include JUnit 5 and any small library like commons-csv (optional).
   - If the environment requires a local .jar only, provide instructions in README to put `mysql-connector-j.jar` into `lib/` and add to classpath: `java -cp target/ThisDayHistory.jar:lib/mysql-connector-j.jar com.thisdayhistory.MainApp`.
2. **Compiler plugin**: set `<maven-compiler-plugin>` to release 24.
3. **Build commands**:
   - `mvn clean package` — builds jar (use maven-shade-plugin to create fat jar excluding connector if local jar approach used)
   - How to run after build:
     - If using Maven-managed dependency: `java -jar target/thisdayhistory-1.0.jar`
     - If using local connector jar: `java -cp target/thisdayhistory-1.0.jar:lib/mysql-connector-j.jar com.thisdayhistory.MainApp`
4. **MySQL Setup**:
   - Provide SQL commands to create DB:
     - `CREATE DATABASE thisdayhistory CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;`
     - Run the provided `schema.sql` on that DB.
   - Provide default DB credentials in `config.properties` (example: host=localhost, port=3306, dbname=thisdayhistory, user=root, password=changeme). Warn user to change password.
5. **Auto-init on first run**:
   - If DB tables absent, application attempts to create them automatically. If connection fails, show clear error dialog with instructions and link to README.

IMPLEMENTATION DETAILS (must follow)
------------------------------------
1. **DatabaseManager (Singleton)**:
   - Read DB config from `config.properties`.
   - Provide `getConnection()` method that returns a `Connection`.
   - Implement `testConnection()` method that returns boolean and check on startup.
2. **HistoricalFactDAO**:
   - Methods: `List<HistoricalFact> findByDate(int month, int day)`, `List<HistoricalFact> search(String query)`, `HistoricalFact findById(int id)`, `int create(HistoricalFact fact)`, `boolean update(HistoricalFact)`, `boolean delete(int id)`, `List<HistoricalFact> findFavorites()`.
   - Use SQL pagination for large result sets (limit offset).
3. **FactService**:
   - Use DAO. Maintain session-level Set<Integer> shownFactIds per date to avoid repetition.
   - Provide `getRandomFact(int month, int day)`, `markFavorite(int id, boolean)`, CSV import/export wrappers.
4. **UI**:
   - MainFrame should be initialized in `SwingUtilities.invokeLater`.
   - Use `FactTableModel` extending `AbstractTableModel`.
   - For heavy DB calls (search, import, export, random fetch), use `SwingWorker` and show a modal progress dialog or status bar message.
5. **Validation**:
   - When adding/editing, validate month/day pair (e.g., Feb 29 only allowed if year provided and divisible by 4; otherwise accept but store raw — include simple validation).
6. **CSV Format**:
   - Columns: month,day,year,event,category,source
   - `CsvUtil` should handle escaping and simple validation. Import must skip duplicates (based on month+day+event text) and report counts: inserted/skipped/failed.
7. **Thread-safety**:
   - Database operations may be invoked concurrently; ensure the session-level shownFactIds map is accessed in a thread-safe manner or only on EDT.
8. **User Messages**:
   - Localize strings in a simple way (just constants in a class); English-only is fine.
9. **Packaging**:
   - Use `maven-shade-plugin` to bundle dependencies except mysql connector if user chooses local jar. Document both options.

DELIVERABLES (exact)
--------------------
Copilot must produce:
1. Full Maven project tree and all Java source files listed above with complete implementations.
2. `pom.xml` configured for Java 24, dependencies, shade plugin.
3. `schema.sql` with create DB + create table + seed data (30 rows).
4. `sample_data.csv` with sample facts in the described format.
5. `README.md` with:
   - Project overview
   - Step-by-step MySQL setup commands
   - How to configure `config.properties`
   - How to run import sample CSV
   - How to build and run the jar (both dependency-managed and local-jar approaches)
   - Troubleshooting common errors (driver ClassNotFoundException, Access denied, port in use)
6. Unit test examples for DAO & FactService (using JUnit 5).
7. A runnable Main class `MainApp.java` that opens the Swing UI and shows DB connection status on startup.

QUALITY & REVIEW RULES FOR THE AGENT
------------------------------------
- All code must compile with `mvn -DskipTests=false clean package`.
- No TODOs left in the final output — every method must be implemented.
- Use clear comments for major classes and methods.
- Keep methods reasonably sized (< 200 lines).
- Use explicit imports (no wildcard imports).
- Use descriptive variable and method names.
- Ensure no hardcoded DB passwords in code. Use `config.properties` and document environment variable alternative.
- Provide sample run logs in README showing starting app, connecting to DB, and fetching a random fact.

FINAL NOTE TO AGENT
-------------------
Produce the **complete project** (full source code and resource files). After finishing, output a short checklist showing that the following were generated and where they are located:
- `pom.xml`
- `src/main/java/...` classes (list)
- `schema.sql` and `sample_data.csv`
- `README.md`
- `target/thisdayhistory-1.0.jar` (explain build command to generate it)
- instructions for running with local `mysql-connector-j.jar` if the environment does not permit Maven dependency resolution.

Do NOT attempt to call external services, do not include AI/online API calls, and do not include any secrets. Use only standard Java libraries and the specified JDBC connector.

Begin now and generate file-by-file source code and resource contents. End output with the exact shell commands a developer must run (in order) to build, configure, initialize DB, seed data, and launch the application on a typical Linux/Windows dev machine.
```*