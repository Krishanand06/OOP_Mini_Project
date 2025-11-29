-- =====================================================
-- Campus Café Management System - Sample Data
-- =====================================================
-- This file contains essential data for the application
-- Run this AFTER creating the schema with schema.sql
-- =====================================================

USE campus_cafe;

-- =====================================================
-- ADMIN ACCOUNT
-- =====================================================
INSERT INTO admins (admin_id, name, email, password_hash) VALUES
('admin', 'System Administrator', 'admin@campus.edu', 'admin123');

-- =====================================================
-- MESS PLANS
-- =====================================================
INSERT INTO mess_plans (plan_id, plan_name, plan_type, monthly_cost, description) VALUES
('PLAN-BASIC', 'Basic Plan', 'Basic', 350.00, 'Standard mess plan with simple meals.'),
('PLAN-PREMIUM', 'Premium Plan', 'Premium', 500.00, 'Premium mess plan with variety of meals.'),
('PLAN-WEEKEND', 'Weekend Plan', 'Weekend', 200.00, 'Plan for students who eat in mess only on weekends.');

-- =====================================================
-- MENU ITEMS - CANTEEN
-- =====================================================
INSERT INTO menu (name, price, category, available) VALUES
-- MAIN Category
('Veg Biryani', 45.00, 'MAIN', TRUE),
('Chicken Biryani', 65.00, 'MAIN', TRUE),
('Paneer Tikka Masala', 55.00, 'MAIN', TRUE),
('Dal Makhani with Rice', 35.00, 'MAIN', TRUE),
('Fried Rice', 40.00, 'MAIN', TRUE),
('Pasta Alfredo', 50.00, 'MAIN', TRUE),

-- SNACK Category
('Samosa (2 pcs)', 10.00, 'SNACK', TRUE),
('French Fries', 20.00, 'SNACK', TRUE),
('Veg Sandwich', 18.00, 'SNACK', TRUE),
('Burger', 28.00, 'SNACK', TRUE),
('Pav Bhaji', 25.00, 'SNACK', TRUE),
('Spring Rolls', 30.00, 'SNACK', TRUE),

-- DRINK Category
('Masala Chai', 8.00, 'DRINK', TRUE),
('Filter Coffee', 10.00, 'DRINK', TRUE),
('Cold Coffee', 22.00, 'DRINK', TRUE),
('Fresh Lime Soda', 15.00, 'DRINK', TRUE),
('Mango Lassi', 20.00, 'DRINK', TRUE),
('Bottled Water', 5.00, 'DRINK', TRUE),

-- DESSERT Category
('Gulab Jamun (2 pcs)', 15.00, 'DESSERT', TRUE),
('Ice Cream Cup', 18.00, 'DESSERT', TRUE),
('Brownie with Ice Cream', 35.00, 'DESSERT', TRUE),
('Fruit Salad', 22.00, 'DESSERT', TRUE),
('Chocolate Cake Slice', 28.00, 'DESSERT', TRUE),
('Rasgulla (3 pcs)', 20.00, 'DESSERT', TRUE);

-- =====================================================
-- CATERING ITEMS
-- =====================================================
INSERT INTO menu (name, price, category, available) VALUES
-- Catering packages for events
('Veg Sandwich Box (10 pcs)', 150.00, 'CATERING', TRUE),
('Chicken Burger Combo (10 pcs)', 280.00, 'CATERING', TRUE),
('Pizza Slice Box (20 slices)', 400.00, 'CATERING', TRUE),
('Soft Drink Cans (24 pcs)', 120.00, 'CATERING', TRUE),
('Juice Bottles (20 pcs)', 200.00, 'CATERING', TRUE),
('Samosa Pack (25 pcs)', 125.00, 'CATERING', TRUE);

-- =====================================================
-- Success Message
-- =====================================================
SELECT 'Sample data loaded successfully!' AS Status;
SELECT 'Admin login: admin / admin123' AS Info;
SELECT 'Students can register new accounts' AS Info;
SELECT 'Menu includes: MAIN, SNACK, DRINK, DESSERT, CATERING' AS Info;
