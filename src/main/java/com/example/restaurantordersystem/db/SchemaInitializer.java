package com.example.restaurantordersystem.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    public static void initializeSchema() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            // Create users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT PRIMARY KEY AUTO_INCREMENT,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(20) DEFAULT 'Customer' NOT NULL
                )
            """);
            // Create tables table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tables (
                    table_id INT PRIMARY KEY AUTO_INCREMENT,
                    table_number INT UNIQUE NOT NULL,
                    capacity INT NOT NULL,
                    is_available BOOLEAN DEFAULT TRUE
                )
            """);

            // Create menu_items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS menu_items (
                    item_id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    description TEXT,
                    price DECIMAL(8,2) NOT NULL,
                    category ENUM('DRINKS', 'APPETIZERS', 'MAIN_COURSE', 'DESSERTS') NOT NULL,
                    is_available BOOLEAN DEFAULT TRUE
                )
            """);

            // Create orders table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INT PRIMARY KEY AUTO_INCREMENT,
                    table_number INT NOT NULL,
                    status ENUM('PLACED', 'PREPARING', 'READY', 'SERVED', 'COMPLETED', 'CANCELLED') NOT NULL,
                    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    completion_time TIMESTAMP NULL,
                    is_paid BOOLEAN DEFAULT FALSE,
                    total_amount DECIMAL(10,2) NOT NULL
                )
            """);

            // Create order_items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT NOT NULL,
                    menu_item_id INT NOT NULL,
                    quantity INT NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                    FOREIGN KEY (menu_item_id) REFERENCES menu_items(item_id)
                )
            """);

            // Create payments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payments (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT NOT NULL,
                    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED', 'CANCELLED') NOT NULL,
                    payment_method ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'GIFT_CARD', 'OTHER') NOT NULL,
                    amount_paid DECIMAL(10,2) NOT NULL,
                    payment_time TIMESTAMP NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id)
                )
            """);

            // Create reservations table

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    table_id INT NOT NULL,
                    reservation_time TIMESTAMP NOT NULL,
                    number_of_guests INT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id),
                    FOREIGN KEY (table_id) REFERENCES tables(table_id)
                )
            """);

            System.out.println("Schema initialization completed successfully.");

        } catch (SQLException e) {
            System.err.println("Schema initialization error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    public static void insertSampleData() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();

            // Insert sample users
            stmt.execute("""
            MERGE INTO users (full_name, email, password, role) 
            KEY(email) VALUES
            ('Admin User', 'admin@restaurant.com', 'admin123', 'Admin'),
            ('John Doe', 'john@example.com', 'password123', 'Customer'),
            ('Jane Smith', 'jane@example.com', 'password123', 'Customer')
        """);

            // Insert sample tables
            stmt.execute("""
            MERGE INTO tables (table_number, capacity, is_available)
            KEY(table_number) VALUES
            (1, 2, true),
            (2, 4, true),
            (3, 6, true),
            (4, 8, true),
            (5, 2, true)
        """);

            // Insert sample menu items
            stmt.execute("""
            MERGE INTO menu_items (name, description, price, category, is_available)
            KEY(name) VALUES
            ('Espresso', 'Strong coffee shot', 2.99, 'DRINKS', true),
            ('Caesar Salad', 'Fresh romaine lettuce with Caesar dressing', 8.99, 'APPETIZERS', true),
            ('Chicken Alfredo', 'Fettuccine pasta with creamy sauce', 15.99, 'MAIN_COURSE', true),
            ('Steak', 'Grilled ribeye with vegetables', 24.99, 'MAIN_COURSE', true),
            ('Tiramisu', 'Coffee-flavored Italian dessert', 7.99, 'DESSERTS', true),
            ('Cheesecake', 'New York style cheesecake', 6.99, 'DESSERTS', true)
        """);

            System.out.println("Sample data inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
    public static void dropTables() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();

            // Drop tables in reverse order due to foreign key constraints
            System.out.println("Dropping existing tables...");

            // First drop tables with foreign keys
            stmt.execute("DROP TABLE IF EXISTS reservations");
            stmt.execute("DROP TABLE IF EXISTS payments");
            stmt.execute("DROP TABLE IF EXISTS order_items");

            // Then drop tables referenced by foreign keys
            stmt.execute("DROP TABLE IF EXISTS orders");
            stmt.execute("DROP TABLE IF EXISTS menu_items");
            stmt.execute("DROP TABLE IF EXISTS tables");
            stmt.execute("DROP TABLE IF EXISTS users");

            System.out.println("All tables dropped successfully.");

        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    public static void main(String[] args) {
        // Initialize schema and add sample data
        dropTables();
        initializeSchema();
        insertSampleData();
    }
}