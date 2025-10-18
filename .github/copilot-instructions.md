# Copilot Instructions for This Day in History

## Project Overview
Java 24 Swing desktop application for exploring historical facts with MySQL backend. Built with Maven, follows MVC architecture with strict layering: Model → DAO → Service → UI.

## Architecture & Key Patterns

### Layered Architecture (Strict Separation)
- **Model** (`com.thisdayhistory.model`): POJOs with validation in setters (e.g., `HistoricalFact.setMonth()` validates 1-12)
- **DAO** (`com.thisdayhistory.dao`): Database access only, uses PreparedStatements exclusively, throws RuntimeException wrapping SQLException
- **Service** (`com.thisdayhistory.service`): Business logic layer - handles session-level fact tracking (`shownFactIds`), validation, filtering
- **UI** (`com.thisdayhistory.ui`): Swing components - all DB operations via `SwingWorker` to keep EDT responsive

### Database Connection Management
`DatabaseManager` is a **Singleton** with connection pooling (default 10 connections, configurable via `config.properties`):
```java
// Always use try-with-resources, connection returns to pool automatically
try (Connection conn = dbManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
    // Your code
}
```
Implements retry logic (3 attempts, 1s delay) for connection failures.

### Non-Repeating Random Facts
`FactService` maintains session-level `Set<Integer> shownFactIds` to track displayed facts. When all facts for a date are shown, automatically clears and cycles again. This is core functionality - don't bypass it.

## Critical Developer Workflows

### Build & Run
```powershell
# Build (compiles to target/ with dependencies)
mvn clean package

# Run (mainClass defined in maven-jar-plugin)
java -jar target/thisdayhistory-1.0.0.jar

# If using local MySQL connector
java -cp "target/thisdayhistory-1.0.0.jar;lib/mysql-connector-j-X.X.X.jar" com.thisdayhistory.MainApp
```

### Database Setup
1. Create database: `CREATE DATABASE thisdayhistory CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;`
2. Run `schema.sql` for table creation and sample data (30+ historical facts)
3. Configure `src/main/resources/config.properties` with DB credentials

### Testing
JUnit 5 configured. Service/DAO methods should have unit tests. Run with: `mvn test`

## Swing UI Conventions

### Threading Rules (Strictly Enforced)
- **All UI updates** on Event Dispatch Thread (EDT): Use `SwingUtilities.invokeLater()`
- **All DB operations** in background: Use `SwingWorker<Result, Void>` pattern from `MainFrame`
- Show progress indicators (disable buttons) during background tasks

Example from `MainFrame`:
```java
new SwingWorker<List<HistoricalFact>, Void>() {
    @Override
    protected List<HistoricalFact> doInBackground() {
        return factService.getFactsForDate(month, day, category, favoritesOnly);
    }
    @Override
    protected void done() {
        // Update UI on EDT
    }
}.execute();
```

### UI Component Organization
- **MainFrame**: 1024x768 default, uses `BorderLayout` with toolbar (North), split pane (Center), status bar (South)
- **FactTableModel**: Custom `AbstractTableModel` for JTable with columns [ID, Date, Year, Event, Category, Favorite]
- **Dialogs**: Modal `JDialog` for Add/Edit operations (`AddEditFactDialog`)
- Keyboard shortcuts: Ctrl+N (new fact), Enter (search)

## Data Model Details

### HistoricalFact Schema
```sql
month TINYINT NOT NULL      -- 1-12, validated in model setter
day TINYINT NOT NULL        -- 1-31, validated by DateUtil
year SMALLINT NULL          -- Optional, can be null for unknown dates
event TEXT NOT NULL
category VARCHAR(64)
favorite BOOLEAN DEFAULT FALSE
UNIQUE (month, day, event(200))  -- Prevents duplicate events on same date
```

### Date Handling
`DateUtil` provides month/day validation considering leap years. All date validation flows through this utility.

## Configuration Management
`AppConfig` reads `config.properties` from classpath:
- DB connection: `db.host`, `db.port`, `db.name`, `db.user`, `db.password`
- Connection pool: `db.maxPoolSize` (default 10)
- Loaded once at startup, changes require restart

## Import/Export
- CSV format: month,day,year,event,category,source
- `ImportExportUtil` handles file operations with `JFileChooser`
- Validation on import: delegates to `FactService.saveFact()` for business rules

## Error Handling Patterns
- **DAO Layer**: Logs with `java.util.logging.Logger`, throws `RuntimeException` wrapping `SQLException`
- **UI Layer**: Catches exceptions, shows `JOptionPane.showMessageDialog()` with user-friendly messages
- **Service Layer**: Validates inputs, throws `IllegalArgumentException` for invalid parameters

## Dependencies (Java 24)
- MySQL Connector: `mysql-connector-java:8.0.33` (JDBC driver)
- Logging: `slf4j-simple:2.0.9` (backed by java.util.logging)
- Testing: JUnit 5 (`junit-jupiter-*:5.10.0`)
- Maven compiler: `release=24` (targets Java 24)

## Common Patterns to Follow
1. **Never** execute SQL directly - always use `PreparedStatement` (SQL injection prevention)
2. **Always** validate dates through `DateUtil` before DB operations
3. **Always** wrap long-running operations in `SwingWorker` when called from UI
4. Use `LOGGER.info()` for normal operations, `LOGGER.log(Level.SEVERE, msg, exception)` for errors
5. Modal dialogs should validate input before calling service methods

## File Reference Guide
- Entry point: `MainApp.java` (creates DAO → Service → MainFrame dependency chain)
- DB schema: `schema.sql` (includes sample data)
- Sample CSV: `sample_data.csv` (import format reference)
- Build config: `pom.xml` (Java 24, main class = `com.thisdayhistory.MainApp`)
