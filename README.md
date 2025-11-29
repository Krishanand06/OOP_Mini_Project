# Campus Café Management System

A comprehensive Java Swing desktop application for managing campus café operations, including student orders, mess plans, catering requests, and feedback management.

---

## 🚀 Quick Start

**First time setup?** See **[SETUP.md](SETUP.md)** for complete installation instructions.

**Already set up?** Just run:
- Windows: Double-click `run.bat`
- Linux/Mac: `./run.sh`

**Default login:** `admin` / `admin123`

---

## Features

- **Student Portal**
  - Login/Registration
  - Canteen ordering with real-time menu
  - Digital wallet management
  - Mess plan subscription
  - Catering requests for events
  - Feedback submission

- **Admin Dashboard**
  - Menu management
  - Order tracking
  - Catering request approval
  - Feedback analysis
  - Mess plan administration

- **Staff Interface**
  - Order status updates
  - Kitchen management

## Tech Stack

- **Frontend**: Java Swing (GUI)
- **Backend**: Java
- **Database**: MySQL
- **JDBC Driver**: MySQL Connector/J 8.0.33


## Default Login Credentials

After loading sample data:

**Student**:
- Roll No: `2023A7PS0001G`
- Password: `password123`

**Admin**:
- Username: `admin`
- Password: `admin123`


## Key Classes

- **DatabaseManager.java** - Handles all database operations
- **Main.java** - Application entry point
- **AppConstants.java** - Enums, interfaces, and exceptions
- **UI Screens** - `*Screen.java` files for different interfaces

## Features in Detail

### Wallet System
- Add money to digital wallet
- Pay for orders using wallet balance
- Transaction history tracking

### Mess Plans
- Basic Plan: Breakfast + Lunch
- Premium Plan: All meals
- Weekend Plan: Weekend-only meals

### Catering
- Event catering requests
- Admin approval workflow
- Custom menu selection

### Feedback
- Category-based feedback (Canteen, Mess, Events)
- Rating system (1-5 stars)
- Admin feedback dashboard

## Troubleshooting

### "MySQL JDBC driver not found"
- Ensure `mysql-connector-j-8.0.33.jar` is in the project root
- Check the classpath in compile/run commands

### "Access denied for user"
- Verify MySQL credentials in `DatabaseManager.java`
- Ensure MySQL server is running

### "Table doesn't exist"
- Run `data/schema.sql` to create tables
- Check database name is `campus_cafe`

