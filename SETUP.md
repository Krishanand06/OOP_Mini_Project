# Campus Café Management System - Setup Guide

Complete installation instructions for getting the application running.

---

## 📋 Prerequisites

Before you begin, install:

1. **Java JDK 8+** - [Download here](https://www.oracle.com/java/technologies/downloads/)
   - Verify: `java -version` and `javac -version`

2. **MySQL Server 8.0+** - [Download here](https://dev.mysql.com/downloads/mysql/)
   - Set a root password during installation
   - Verify: `mysql --version`

> **Note:** The MySQL Connector JAR is already included in the repository!

---

## 🚀 Quick Setup (3 Steps)

### Step 1: Set Up Database

**Windows:**
```bash
mysql -u root -p < data\schema.sql
mysql -u root -p campus_cafe < data\sample_data.sql
```

**Linux/Mac:**
```bash
mysql -u root -p < data/schema.sql
mysql -u root -p campus_cafe < data/sample_data.sql
```

Enter your MySQL password when prompted.

### Step 2: Configure Database Password

Edit `src/DatabaseManager.java` line 15:
```java
private static final String DB_PASSWORD = "your_mysql_password";
```

Replace `"your_mysql_password"` with your actual MySQL root password.

### Step 3: Run the Application

**Windows:** Double-click `run.bat`

**Linux/Mac:**
```bash
chmod +x run.sh
./run.sh
```

**Manual:**
```bash
# Windows
javac -cp ".;mysql-connector-j-8.0.33.jar" -d . src\*.java
java -cp ".;mysql-connector-j-8.0.33.jar" Main

# Linux/Mac
javac -cp ".:mysql-connector-j-8.0.33.jar" -d . src/*.java
java -cp ".:mysql-connector-j-8.0.33.jar" Main
```

---

## 🔐 Default Login

**Admin:**
- Username: `admin`
- Password: `admin123`

**Students:** Register new accounts using the registration screen.

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| "Driver not found" | Verify `mysql-connector-j-8.0.33.jar` exists in project root |
| "Access denied" | Update password in `DatabaseManager.java` line 15 |
| "Unknown database" | Run `data/schema.sql` first |
| "Table doesn't exist" | Run both `schema.sql` and `sample_data.sql` |
| MySQL not running | Start MySQL service |

**Check MySQL is running:**
```bash
# Windows
sc query MySQL80

# Linux/Mac
sudo systemctl status mysql
```

**Verify database:**
```sql
mysql -u root -p
USE campus_cafe;
SHOW TABLES;  -- Should show 14 tables
```

---

## 📦 What's Included

- ✅ All Java source code
- ✅ MySQL Connector JAR (no download needed!)
- ✅ Database schema
- ✅ Sample data (admin account, menu items, mess plans)
- ✅ Run scripts for easy execution

---

## 📚 Additional Info

**Project Structure:**
```
OOP_Mini_Project-main/
├── src/                          # Java source files
├── data/
│   ├── schema.sql                # Database structure
│   └── sample_data.sql           # Initial data
├── mysql-connector-j-8.0.33.jar  # JDBC driver (included)
├── run.bat                       # Windows launcher
└── README.md                     # Project overview
```

**License:** MySQL Connector is licensed under GPL v2.0 with FOSS Exception. See [LICENSE-THIRD-PARTY.md](LICENSE-THIRD-PARTY.md).

---

## ✅ Success Checklist

- [ ] Java JDK 8+ installed
- [ ] MySQL Server 8.0+ installed and running
- [ ] Database created (ran `schema.sql`)
- [ ] Sample data loaded (ran `sample_data.sql`)
- [ ] Password configured in `DatabaseManager.java`
- [ ] Application runs without errors
- [ ] Can login as admin

**Need help?** Check the troubleshooting section above or review error messages in the console.
