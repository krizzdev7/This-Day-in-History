# 🚀 Quick Start Guide - This Day in History

## Prerequisites Checklist

Before you begin, make sure you have:

- [ ] Java JDK 11 or higher installed
- [ ] MySQL Server 8.x installed and running
- [ ] MySQL Connector/J jar file in `lib/` folder
- [ ] Git (if cloning from repository)

## Step-by-Step Setup (5 Minutes)

### 1️⃣ Get the Project

```bash
# If from GitHub
git clone https://github.com/krizzdev7/This-Day-in-History.git
cd This-Day-in-History

# Or if you have the folder already
cd This-Day-in-History
```

### 2️⃣ Download MySQL Connector

1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Download the Platform Independent version
3. Extract and copy the jar file to `lib/` folder
4. The file should be named like: `mysql-connector-j-8.0.33.jar`

### 3️⃣ Setup Database

**Start MySQL Server:**
```bash
# Windows
net start MySQL80

# Linux/macOS
sudo service mysql start
```

**Create Database:**
```bash
# Login to MySQL
mysql -u root -p

# Run the schema file
source history_schema.sql;
# Or
exit
mysql -u root -p < history_schema.sql
```

### 4️⃣ Configure Database Connection

Edit `config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/history_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_PASSWORD_HERE
```

**⚠️ Important**: Replace `YOUR_PASSWORD_HERE` with your actual MySQL password!

### 5️⃣ Build and Run

**Windows:**
```batch
# Build
build.bat

# Run
run.bat
```

**Linux/macOS:**
```bash
# Make scripts executable
chmod +x build.sh run.sh

# Build
./build.sh

# Run
./run.sh
```

**Or Manually:**

Windows:
```powershell
javac -cp ".;lib/*" -d bin src/**/*.java src/*.java
java -cp "bin;lib/*" Main
```

Linux/macOS:
```bash
javac -cp ".:lib/*" -d bin src/**/*.java src/*.java
java -cp "bin:lib/*" Main
```

## ✅ Verify Installation

When you run the application, you should see:
1. A window titled "This Day in History"
2. Today's date displayed prominently
3. Historical events for today
4. A quote in the footer
5. Category filters in the sidebar

## 🎯 First Steps After Launch

1. **Browse Today's Events**: See what happened on this day in history
2. **Try the Date Picker**: Select a different date and click "Go"
3. **Search**: Type "moon" or "revolution" in the search bar
4. **Filter**: Click a category like "Science" or "Politics"
5. **Click an Event**: Get full details in a popup dialog
6. **Check Other Tabs**: View Famous Births, Notable Deaths, and Timeline

## 🐛 Common Issues

### "Failed to connect to database"
- ✅ MySQL server is running
- ✅ Database `history_db` exists (run `history_schema.sql`)
- ✅ Username/password correct in `config.properties`

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- ✅ MySQL connector jar is in `lib/` folder
- ✅ Jar is included in classpath when running

### Compilation errors
- ✅ Compiling from project root directory
- ✅ All source files present in `src/` folders
- ✅ Using correct command for your OS (`;` vs `:`)

## 📊 What's in the Database?

After running `history_schema.sql`, you'll have:
- **40+ Historical Events** (Moon landing, Berlin Wall, Titanic, etc.)
- **20+ Famous People** (Einstein, Gandhi, Shakespeare, etc.)
- **18+ Inspirational Quotes** (From various historical figures)

## 🎨 UI Features

- **Header**: Date picker, search bar, navigation
- **Sidebar**: Category filters, random facts
- **Main Area**: Tabbed interface with events, births, deaths, timeline
- **Footer**: Quote of the day with refresh button

## 📞 Need Help?

- Check the full README.md for detailed documentation
- Review database schema in history_schema.sql
- Ensure all config settings are correct
- Verify Java and MySQL versions

## 🎉 You're Ready!

Your application is now set up and running. Explore historical events, discover famous people, and enjoy learning about "This Day in History"!

---

**Built with Java Swing + MySQL + JDBC**
