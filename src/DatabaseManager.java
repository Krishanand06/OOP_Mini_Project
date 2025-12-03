
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// Handles all database operations for the campus cafe system

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_cafe";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Password Here"; // Change this to your MySQL password

    // Load MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Authentication

    // Student login
    public static Student authenticateStudent(String rollNo, String password) {
        String sql = "SELECT student_id, roll_no, name, email, hostel_status " +
                "FROM students WHERE roll_no = ? AND password_hash = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rollNo);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String studentId = rs.getString("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String hostelStatus = rs.getString("hostel_status");

                // Update last login
                updateLastLogin(studentId);

                // Create appropriate student object
                if ("Hosteller".equals(hostelStatus)) {
                    MealPlan plan = loadStudentMessPlan(studentId);
                    return new Hosteller(studentId, name, email, rollNo, plan);
                } else {
                    return new DayScholar(studentId, name, email, rollNo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating student: " + e.getMessage());
        }
        return null;
    }

    // Admin login
    public static Admin authenticateAdmin(String username, String password) {
        String sql = "SELECT admin_id, name, email FROM admins " +
                "WHERE admin_id = ? AND password_hash = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String adminId = rs.getString("admin_id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                // Update last login
                updateAdminLastLogin(adminId);

                return new Admin(adminId, name, email);
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating admin: " + e.getMessage());
        }
        return null;
    }

    private static void updateLastLogin(String studentId) {
        String sql = "UPDATE students SET last_login = NOW() WHERE student_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Note: Could not update last login");
        }
    }

    private static void updateAdminLastLogin(String adminId) {
        String sql = "UPDATE admins SET last_login = NOW() WHERE admin_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adminId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Note: Could not update admin last login");
        }
    }

    // Student Registration

    // Register new student
    public static boolean registerStudent(Student student, String password) {
        String sql = "INSERT INTO students (student_id, roll_no, name, email, password_hash, hostel_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Insert student
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, student.getUserId());
                stmt.setString(2, student.getRollNo());
                stmt.setString(3, student.getName());
                stmt.setString(4, student.getEmail());
                stmt.setString(5, password);
                stmt.setString(6, student.getHostelStatus());
                stmt.executeUpdate();
            }

            // Create wallet
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO wallets (owner_id, balance) VALUES (?, 0.00)")) {
                stmt.setString(1, student.getUserId());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registering student: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    // Mess Plans

    // Subscribe to mess plan (hostellers only)
    public static boolean subscribeToMessPlan(String studentId, String planId) {
        // First cancel any active subscriptions
        String cancelSql = "UPDATE student_mess_subscriptions SET status = 'CANCELLED' " +
                "WHERE student_id = ? AND status = 'ACTIVE'";

        String subscribeSql = "INSERT INTO student_mess_subscriptions " +
                "(student_id, plan_id, start_date, end_date, status) " +
                "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'ACTIVE')";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Cancel existing
            try (PreparedStatement stmt = conn.prepareStatement(cancelSql)) {
                stmt.setString(1, studentId);
                stmt.executeUpdate();
            }

            // Add new subscription
            try (PreparedStatement stmt = conn.prepareStatement(subscribeSql)) {
                stmt.setString(1, studentId);
                stmt.setString(2, planId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error subscribing to mess plan: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    // Get active mess plan
    public static MealPlan loadStudentMessPlan(String studentId) {
        String sql = "SELECT mp.plan_type FROM student_mess_subscriptions sms " +
                "JOIN mess_plans mp ON sms.plan_id = mp.plan_id " +
                "WHERE sms.student_id = ? AND sms.status = 'ACTIVE' " +
                "ORDER BY sms.start_date DESC LIMIT 1";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String planType = rs.getString("plan_type");
                if ("Basic".equals(planType))
                    return new BasicPlan();
                if ("Premium".equals(planType))
                    return new PremiumPlan();
                if ("Weekend".equals(planType))
                    return new WeekendPlan();
            }
        } catch (SQLException e) {
            System.out.println("Error loading mess plan: " + e.getMessage());
        }
        return null;
    }

    // Get all available plans
    public static List<MealPlan> loadMessPlans() {
        List<MealPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM mess_plans";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String planType = rs.getString("plan_type");
                MealPlan plan = null;

                if ("Basic".equals(planType))
                    plan = new BasicPlan();
                else if ("Premium".equals(planType))
                    plan = new PremiumPlan();
                else if ("Weekend".equals(planType))
                    plan = new WeekendPlan();

                if (plan != null)
                    plans.add(plan);
            }
        } catch (SQLException e) {
            System.out.println("Error loading mess plans: " + e.getMessage());
        }
        return plans;
    }

    // Helpers

    // Get student name
    public static String getStudentName(String studentId) {
        String sql = "SELECT name FROM students WHERE student_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error getting student name: " + e.getMessage());
        }
        return "Unknown";
    }

    // Check hostel status
    public static boolean isHosteller(String studentId) {
        String sql = "SELECT hostel_status FROM students WHERE student_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return "Hosteller".equals(rs.getString("hostel_status"));
            }
        } catch (SQLException e) {
            System.out.println("Error checking hostel status: " + e.getMessage());
        }
        return false;
    }

    // Menu

    public static List<MenuItem> loadMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM menu WHERE available = TRUE";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error loading menu items: " + e.getMessage());
        }
        return items;
    }

    // Wallet

    public static double loadWalletBalance(String ownerId) {
        String sql = "SELECT balance FROM wallets WHERE owner_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Error loading wallet balance: " + e.getMessage());
        }
        return 0.0;
    }

    public static void saveWalletBalance(Wallet wallet) {
        String sql = "UPDATE wallets SET balance = ? WHERE owner_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, wallet.getBalance());
            stmt.setString(2, wallet.getOwnerId());
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                // Insert if doesn't exist
                String insertSql = "INSERT INTO wallets (owner_id, balance) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, wallet.getOwnerId());
                    insertStmt.setDouble(2, wallet.getBalance());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving wallet balance: " + e.getMessage());
        }
    }

    public static boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, owner_id, transaction_type, amount, description) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getTransactionId().split("-")[0]);
            stmt.setString(3, transaction.getType().name());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setString(5, transaction.getDescription());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            return false;
        }
    }

    // Feedback

    public static void insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (feedback_id, student_id, category, rating, comments) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feedback.getFeedbackId());
            stmt.setString(2, feedback.getStudentId());
            stmt.setString(3, feedback.getCategory().name());
            stmt.setInt(4, feedback.getRating());
            stmt.setString(5, feedback.getComments());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting feedback: " + e.getMessage());
        }
    }

    public static List<Feedback> loadStudentFeedback(String studentId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE student_id = ? ORDER BY created_at DESC";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String feedbackId = rs.getString("feedback_id");
                String categoryStr = rs.getString("category");
                int rating = rs.getInt("rating");
                String comments = rs.getString("comments");

                FeedbackCategory category;
                try {
                    category = FeedbackCategory.valueOf(categoryStr);
                } catch (IllegalArgumentException e) {
                    category = FeedbackCategory.CANTEEN;
                }

                feedbackList.add(new Feedback(feedbackId, studentId, category, rating, comments));
            }
        } catch (SQLException e) {
            System.out.println("Error loading student feedback: " + e.getMessage());
        }
        return feedbackList;
    }

    public static List<Feedback> loadAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.feedback_id, f.student_id, f.category, f.rating, f.comments " +
                "FROM feedback f ORDER BY f.created_at DESC";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String feedbackId = rs.getString("feedback_id");
                String studentId = rs.getString("student_id");
                String categoryStr = rs.getString("category");
                int rating = rs.getInt("rating");
                String comments = rs.getString("comments");

                FeedbackCategory category;
                try {
                    category = FeedbackCategory.valueOf(categoryStr);
                } catch (IllegalArgumentException e) {
                    category = FeedbackCategory.CANTEEN;
                }

                feedbackList.add(new Feedback(feedbackId, studentId, category, rating, comments));
            }
        } catch (SQLException e) {
            System.out.println("Error loading feedback: " + e.getMessage());
        }
        return feedbackList;
    }

    // Clubs

    public static List<Club> loadClubs() {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT * FROM clubs";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clubs.add(new Club(
                        rs.getString("club_id"),
                        rs.getString("club_name"),
                        rs.getString("description"),
                        rs.getString("president_id")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading clubs: " + e.getMessage());
        }
        return clubs;
    }

    // Orders

    public static boolean insertOrder(Order order) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Insert order
            String sqlOrder = "INSERT INTO orders (order_id, student_id, total_amount, status, payment_method) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder)) {
                stmt.setString(1, order.getOrderId());
                stmt.setString(2, order.getCustomer().getUserId());
                stmt.setDouble(3, order.calculateBillAmount());
                stmt.setString(4, order.getStatus().name());
                stmt.setString(5, "Wallet");
                stmt.executeUpdate();
            }

            // Insert order items
            String sqlItems = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlItems)) {
                List<MenuItem> items = order.getItems();
                List<Integer> quantities = order.getQuantities();

                for (int i = 0; i < items.size(); i++) {
                    MenuItem item = items.get(i);
                    int quantity = quantities.get(i);

                    stmt.setString(1, order.getOrderId());
                    stmt.setInt(2, item.getItemId());
                    stmt.setInt(3, quantity);
                    stmt.setDouble(4, item.getUnitPrice());
                    stmt.setDouble(5, item.getUnitPrice() * quantity);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public static boolean updateOrderStatus(String orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE order_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    // Admin Menu

    public static void updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu SET name = ?, price = ?, category = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setDouble(2, item.getUnitPrice());
            stmt.setString(3, item.getCategory());
            stmt.setInt(4, item.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating menu item: " + e.getMessage());
        }
    }

    public static void deleteMenuItem(int itemId) {
        String sql = "DELETE FROM menu WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
        }
    }

    // Order Details

    // Get order items as formatted strings
    public static java.util.List<String> loadOrderItems(String orderId) {
        java.util.List<String> items = new ArrayList<>();
        String sql = "SELECT oi.quantity, oi.unit_price, m.name " +
                "FROM order_items oi " +
                "JOIN menu m ON oi.menu_item_id = m.id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("unit_price");
                String name = rs.getString("name");

                items.add(name + " x" + qty + " @ " + price + " AED");
            }
        } catch (SQLException e) {
            System.out.println("Error loading order items: " + e.getMessage());
        }
        return items;
    }

    // Catering

    public static String getNextCateringRequestId() {
        String sql = "SELECT request_id FROM catering_requests";
        int maxId = 0;

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("request_id");
                if (id != null && id.startsWith("CAT-")) {
                    try {
                        int num = Integer.parseInt(id.substring(4));
                        if (num > maxId) {
                            maxId = num;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid formats
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating catering request ID: " + e.getMessage());
        }
        return "CAT-" + (maxId + 1);
    }

    public static void insertCateringRequest(String requestId, String eventName, String date, String time,
            String venue, int attendees, String items, String status) {
        String sql = "INSERT INTO catering_requests (request_id, event_name, event_date, event_time, venue, attendees, items, status) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, requestId);
            stmt.setString(2, eventName);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, venue);
            stmt.setInt(6, attendees);
            stmt.setString(7, items);
            stmt.setString(8, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting catering request: " + e.getMessage());
        }
    }

    public static List<String> loadCateringRequests() {
        List<String> requests = new ArrayList<>();
        String sql = "SELECT * FROM catering_requests ORDER BY created_at DESC";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("request_id");
                String event = rs.getString("event_name");
                String date = rs.getString("event_date");
                String time = rs.getString("event_time");
                String venue = rs.getString("venue");
                int attendees = rs.getInt("attendees");
                String items = rs.getString("items");
                String status = rs.getString("status");
                Timestamp created = rs.getTimestamp("created_at");

                // Format: ID|EventName|Date|Time|Venue|Attendees|Items|Status|Timestamp
                String line = id + "|" + event + "|" + date + "|" + time + "|" + venue + "|" +
                        attendees + "|" + items + "|" + status + "|" + created;
                requests.add(line);
            }
        } catch (SQLException e) {
            System.out.println("Error loading catering requests: " + e.getMessage());
        }
        return requests;
    }

    public static void updateCateringStatus(String requestId, String newStatus) {
        String sql = "UPDATE catering_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating catering status: " + e.getMessage());
        }
    }

    // Popularity Analysis

    // Get popular items from order history
    public static List<Object[]> getPopularMenuItems(int limit) {
        List<Object[]> popularItems = new ArrayList<>();
        String sql = "SELECT m.id, m.name, m.category, COALESCE(SUM(oi.quantity), 0) as total_ordered " +
                "FROM menu m " +
                "LEFT JOIN order_items oi ON m.id = oi.menu_item_id " +
                "LEFT JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.status IN ('COMPLETED', 'READY') OR o.status IS NULL " +
                "GROUP BY m.id, m.name, m.category " +
                "ORDER BY total_ordered DESC " +
                "LIMIT ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] item = new Object[4];
                item[0] = rs.getInt("id");
                item[1] = rs.getString("name");
                item[2] = rs.getString("category");
                item[3] = rs.getInt("total_ordered");
                popularItems.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error loading popular items: " + e.getMessage());
        }
        return popularItems;
    }
}
