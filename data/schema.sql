-- =====================================================
-- Campus Café Management System - SIMPLIFIED Schema
-- =====================================================
-- Option B: Consolidated students table + separate wallets
-- - Single students table (user info + auth)
-- - Separate admin table
-- - Wallets table (for transaction history)
-- - Mess plan chosen AFTER registration
-- =====================================================

DROP DATABASE IF EXISTS campus_cafe;
CREATE DATABASE campus_cafe;
USE campus_cafe;

-- ===========================================
-- STUDENTS TABLE (All-in-one)
-- ===========================================
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    hostel_status ENUM('Hosteller', 'DayScholar') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_roll_no (roll_no),
    INDEX idx_email (email)
);

-- ===========================================
-- ADMIN TABLE
-- ===========================================
CREATE TABLE admins (
    admin_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- ===========================================
-- WALLETS TABLE
-- ===========================================
CREATE TABLE wallets (
    owner_id VARCHAR(20) PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- ===========================================
-- TRANSACTIONS TABLE
-- ===========================================
CREATE TABLE transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    owner_id VARCHAR(20) NOT NULL,
    transaction_type ENUM('CREDIT', 'DEBIT') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES wallets(owner_id) ON DELETE CASCADE,
    INDEX idx_owner (owner_id)
);

-- ===========================================
-- MENU TABLE
-- ===========================================
CREATE TABLE menu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category)
);

-- ===========================================
-- MESS PLANS TABLE
-- ===========================================
CREATE TABLE mess_plans (
    plan_id VARCHAR(20) PRIMARY KEY,
    plan_name VARCHAR(50) NOT NULL,
    plan_type ENUM('Basic', 'Premium', 'Weekend') NOT NULL UNIQUE,
    monthly_cost DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- STUDENT MESS SUBSCRIPTIONS
-- ===========================================
CREATE TABLE student_mess_subscriptions (
    subscription_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    plan_id VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES mess_plans(plan_id),
    INDEX idx_student_status (student_id, status)
);

-- ===========================================
-- CLUBS TABLE
-- ===========================================
CREATE TABLE clubs (
    club_id VARCHAR(20) PRIMARY KEY,
    club_name VARCHAR(100) NOT NULL,
    description TEXT,
    president_id VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (president_id) REFERENCES students(student_id) ON DELETE SET NULL
);

-- ===========================================
-- ORDERS TABLE
-- ===========================================
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    payment_method VARCHAR(50) DEFAULT 'Wallet',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student (student_id),
    INDEX idx_status (status)
);

-- ===========================================
-- ORDER ITEMS TABLE
-- ===========================================
CREATE TABLE order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu(id) ON DELETE RESTRICT
);

-- ===========================================
-- CATERING EVENTS TABLE
-- ===========================================
CREATE TABLE catering_events (
    event_id VARCHAR(50) PRIMARY KEY,
    organizer_id VARCHAR(20) NOT NULL,
    club_id VARCHAR(20),
    event_name VARCHAR(200) NOT NULL,
    event_date DATE NOT NULL,
    guest_count INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE SET NULL,
    INDEX idx_organizer (organizer_id)
);

-- ===========================================
-- CATERING EVENT ITEMS TABLE
-- ===========================================
CREATE TABLE catering_event_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id VARCHAR(50) NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES catering_events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu(id) ON DELETE RESTRICT
);

-- ===========================================
-- FEEDBACK TABLE
-- ===========================================
CREATE TABLE feedback (
    feedback_id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    category ENUM('CANTEEN', 'MESS', 'EVENT_CATERING') NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student (student_id),
    INDEX idx_category (category)
);

-- ===========================================
-- CATERING REQUESTS TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS catering_requests (
    request_id VARCHAR(20) PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_date VARCHAR(20) NOT NULL,
    event_time VARCHAR(20) NOT NULL,
    venue VARCHAR(50) NOT NULL,
    attendees INT NOT NULL,
    items TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- Success message
-- ===========================================
SELECT 'Simplified database schema created successfully!' AS Status;
SELECT '- Students table: ALL student info in one place' AS Info;
SELECT '- Wallets table: Separate for transaction history' AS Info;
SELECT '- Mess plan: Choose AFTER registration' AS Info;
