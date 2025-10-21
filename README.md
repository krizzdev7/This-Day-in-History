# 📜 This Day in History

A comprehensive Java Swing desktop application that displays historical events, famous births, and notable deaths for any selected date. Built with Java 24, Swing UI, and MySQL database using JDBC connectivity.

![Java](https://img.shields.io/badge/Java-24-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![Swing](https://img.shields.io/badge/UI-Swing-green)
![JDBC](https://img.shields.io/badge/DB-JDBC-red)

## 🎯 Features

- **Historical Events Browser**: View important events that occurred on any date in history
- **Famous Births & Deaths**: Discover notable people born or who died on specific dates
- **Interactive Timeline**: Visual representation of events chronologically
- **Search Functionality**: Find events by keywords
- **Category Filtering**: Filter events by type (Politics, Science, Technology, Arts, etc.)
- **Quote of the Day**: Inspirational quotes from historical figures
- **Date Navigation**: Easy date picker to explore any day in history
- **Vintage UI Design**: Clean, minimalistic interface with warm color palette
- **Smooth Animations**: Fade-in effects and interactive elements

## 🏗️ Project Structure

```
ThisDayInHistory/
├── lib/
│   └── mysql-connector-j.jar          # MySQL JDBC Driver
├── src/
│   ├── Main.java                      # Application entry point
│   ├── db/
│   │   └── DatabaseConnection.java     # Database connection manager
│   ├── model/
│   │   ├── Event.java                 # Event entity
│   │   ├── Person.java                # Person entity
│   │   └── Quote.java                 # Quote entity
│   ├── dao/
│   │   ├── EventDAO.java              # Event data access
│   │   ├── PersonDAO.java             # Person data access
│   │   └── QuoteDAO.java              # Quote data access
│   ├── service/
│   │   ├── HistoryService.java        # Business logic for history
│   │   └── QuoteService.java          # Business logic for quotes
│   ├── ui/
│   │   ├── MainFrame.java             # Main application window
│   │   ├── EventCard.java             # Event display card
│   │   ├── TimelinePanel.java         # Timeline visualization
│   │   ├── SidebarPanel.java          # Sidebar with filters
│   │   ├── HeaderPanel.java           # Header with navigation
│   │   └── FooterPanel.java           # Footer with quotes
│   └── util/
│       ├── DateUtil.java              # Date utility functions
│       ├── ImageLoader.java           # Image loading & caching
│       └── AnimationUtil.java         # UI animations
├── config.properties                   # Database configuration
├── history_schema.sql                  # Database schema & seed data
├── LICENSE                             # MIT License
└── README.md                           # This file
```

## 🛠️ Technology Stack

- **Language**: Java 24
- **GUI Framework**: javax.swing + java.awt
- **Database**: MySQL 8.x
- **Database Connectivity**: JDBC (MySQL Connector/J)
- **IDE**: Visual Studio Code
- **Build System**: Plain Java (no Maven/Gradle)

## 📋 Prerequisites

Before running this application, ensure you have:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version`

2. **MySQL Server 8.x**
   - Download from [MySQL Official Site](https://dev.mysql.com/downloads/mysql/)
   - Or install via package manager:
     - Windows: [MySQL Installer](https://dev.mysql.com/downloads/installer/)
     - Linux: `sudo apt install mysql-server`
     - macOS: `brew install mysql`

3. **MySQL Connector/J (JDBC Driver)**
   - Download from [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - Or use the included jar in the `lib/` folder

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/krizzdev7/This-Day-in-History.git
cd This-Day-in-History
```

### Step 2: Download MySQL Connector

Download the MySQL Connector/J jar file and place it in the `lib/` directory:

```bash
# If not already present, download from:
# https://dev.mysql.com/downloads/connector/j/
# Extract and copy mysql-connector-j-x.x.x.jar to lib/ folder
```

### Step 3: Setup MySQL Database

1. **Start MySQL Server**:
   ```bash
   # Windows (if using MySQL service)
   net start MySQL80
   
   # Linux/macOS
   sudo service mysql start
   # or
   sudo systemctl start mysql
   ```

2. **Run the SQL Schema**:
   ```bash
   # Login to MySQL
   mysql -u root -p
   
   # Run the schema file
   source history_schema.sql;
   
   # Or directly:
   mysql -u root -p < history_schema.sql
   ```

   This will:
   - Create the `history_db` database
   - Create tables: `events`, `people`, `quotes`
   - Insert 40+ historical events
   - Insert 20+ famous people
   - Insert 18+ inspirational quotes

### Step 4: Configure Database Connection

Edit `config.properties` with your MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/history_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=yourpassword
db.driver=com.mysql.cj.jdbc.Driver
```

**Note**: Replace `yourpassword` with your actual MySQL root password.

### Step 5: Compile the Application

#### On Windows (PowerShell/CMD):
```powershell
javac -cp ".;lib/mysql-connector-j.jar" -d bin src/**/*.java src/*.java
```

#### On Linux/macOS:
```bash
javac -cp ".:lib/mysql-connector-j.jar" -d bin src/**/*.java src/*.java
```

### Step 6: Run the Application

#### On Windows (PowerShell/CMD):
```powershell
java -cp "bin;lib/mysql-connector-j.jar" Main
```

#### On Linux/macOS:
```bash
java -cp "bin:lib/mysql-connector-j.jar" Main
```

## 🎮 Usage Guide

### Main Interface

1. **Header Section**:
   - Logo and title display
   - Date picker (Day, Month, Year dropdowns)
   - "Go" button to load selected date
   - "Today" button to return to current date
   - Search bar for keyword searches

2. **Sidebar**:
   - Category filters (Politics, Science, Technology, etc.)
   - "Did You Know?" random fact generator
   - Quick navigation options

3. **Main Content Area**:
   - **Historical Events Tab**: Scrollable cards showing events
   - **Famous Births Tab**: People born on selected date
   - **Notable Deaths Tab**: People who died on selected date
   - **Timeline Tab**: Visual timeline representation

4. **Footer**:
   - Quote of the Day with author
   - Refresh button for new quotes

### Features Usage

#### 1. Browse Events by Date
- Select month, day, and year from dropdowns
- Click "Go" to load events for that date
- Click "Today" to return to current date

#### 2. Search Events
- Enter keywords in search bar (e.g., "moon", "revolution")
- Press Enter or click search button
- View matching results

#### 3. Filter by Category
- Click any category button in sidebar
- View all events in that category

#### 4. View Event Details
- Click on any event card
- Dialog opens with full description
- Close button returns to main view

#### 5. Explore People
- Switch to "Famous Births" or "Notable Deaths" tabs
- View people associated with selected date
- See lifespan and brief biography

## 📊 Database Schema

### Events Table
```sql
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### People Table
```sql
CREATE TABLE people (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_year INT,
    death_year INT,
    birth_month INT,
    birth_day INT,
    death_month INT,
    death_day INT,
    category VARCHAR(100),
    description TEXT,
    image_url VARCHAR(255)
);
```

### Quotes Table
```sql
CREATE TABLE quotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🎨 UI Design

- **Color Palette**:
  - Background: Beige (#F5F5DC)
  - Accent: Dark Goldenrod (#B8860B)
  - Cards: Antique White (#FAEBD7)
  - Text: Dark Brown (#2C2416)

- **Fonts**:
  - Titles: Georgia (serif)
  - Body: Segoe UI (sans-serif)
  - Emojis: Segoe UI Emoji

## 🐛 Troubleshooting

### Database Connection Issues

**Problem**: "Failed to connect to database"

**Solutions**:
1. Verify MySQL server is running
2. Check `config.properties` credentials
3. Ensure database `history_db` exists
4. Verify MySQL Connector/J jar is in `lib/` folder

### Compilation Errors

**Problem**: "package does not exist" errors

**Solution**:
- Ensure you're compiling from the project root
- Check classpath includes MySQL connector jar
- Verify all source files are in correct directories

### Runtime Errors

**Problem**: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Solution**:
- Add MySQL Connector jar to classpath when running
- Verify jar file exists in `lib/` folder

## 📝 Sample Data

The application comes pre-loaded with:
- **40+ Historical Events** including:
  - Apollo 11 Moon Landing (1969)
  - Fall of Berlin Wall (1989)
  - Titanic Sinks (1912)
  - And many more...

- **20+ Famous People** including:
  - Albert Einstein
  - Marie Curie
  - Leonardo da Vinci
  - Martin Luther King Jr.
  - And more...

- **18+ Quotes** from:
  - Einstein, Gandhi, Mandela
  - Churchill, Shakespeare, Tesla
  - And other historical figures

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Team**: This Day in History Development Team
- **GitHub**: [@krizzdev7](https://github.com/krizzdev7)

## 🙏 Acknowledgments

- Historical data compiled from various public domain sources
- Icons and emojis from Unicode Standard
- MySQL Connector/J by Oracle
- Java Swing Framework

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on [GitHub](https://github.com/krizzdev7/This-Day-in-History/issues)
- Contact: [Your Contact Information]

## 🔮 Future Enhancements

- [ ] Export data to PDF/CSV
- [ ] Add more historical events
- [ ] Include images for events
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] Bookmark favorite events
- [ ] Share events on social media
- [ ] Mobile version

---

**Made with ❤️ and Java Swing**
This Day in History is an academic java project based on Java Swing UI and an active backend running on MySQL. 
