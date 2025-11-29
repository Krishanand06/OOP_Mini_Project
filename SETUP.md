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


