# Campus Café Management System

A comprehensive Java Swing desktop application for managing campus café operations, including student orders, mess plans, catering requests, and feedback management.

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

