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
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE,
                    role VARCHAR(20) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create menu_items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS menu_items (
                    item_id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    description TEXT,
                    price DECIMAL(8,2) NOT NULL,
                    category VARCHAR(50),
                    image_path VARCHAR(255),
                    is_available BOOLEAN DEFAULT TRUE
                )
            """);

            // Create categories table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(50) UNIQUE NOT NULL,
                    description TEXT
                )
            """);

            // Create tables table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tables (
                    table_id INT PRIMARY KEY AUTO_INCREMENT,
                    table_number INT UNIQUE NOT NULL,
                    capacity INT NOT NULL,
                    status VARCHAR(20) DEFAULT 'AVAILABLE'
                )
            """);

            // Create orders table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT,
                    table_id INT,
                    status VARCHAR(20) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id),
                    FOREIGN KEY (table_id) REFERENCES tables(table_id)
                )
            """);

            // Create order_items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT,
                    item_id INT,
                    quantity INT NOT NULL,
                    price DECIMAL(8,2) NOT NULL,
                    notes TEXT,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                    FOREIGN KEY (item_id) REFERENCES menu_items(item_id)
                )
            """);

            // Create payments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payments (
                    payment_id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT,
                    amount DECIMAL(10,2) NOT NULL,
                    payment_method VARCHAR(50) NOT NULL,
                    status VARCHAR(20) DEFAULT 'PENDING',
                    transaction_id VARCHAR(100),
                    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id)
                )
            """);

            // Create reservations table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT,
                    table_id INT,
                    party_size INT NOT NULL,
                    reservation_date DATE NOT NULL,
                    reservation_time TIME NOT NULL,
                    status VARCHAR(20) DEFAULT 'CONFIRMED',
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

            // Insert sample categories
            stmt.execute("""
                INSERT INTO categories (name, description) VALUES
                ('Appetizers', 'Starters and small plates'),
                ('Main Courses', 'Hearty entrees and main dishes'),
                ('Desserts', 'Sweet treats to finish your meal'),
                ('Beverages', 'Drinks and refreshments'),
                ('Specials', 'Chef''s special dishes of the day')
                ON DUPLICATE KEY UPDATE name=name
            """);

            // Insert sample menu items
            stmt.execute("""
                INSERT INTO menu_items (name, description, price, category) VALUES
                ('Garlic Bread', 'Toasted bread with garlic butter', 5.99, 'Appetizers'),
                ('Caesar Salad', 'Fresh romaine lettuce with Caesar dressing', 8.99, 'Appetizers'),
                ('Spaghetti Bolognese', 'Classic pasta with meat sauce', 14.99, 'Main Courses'),
                ('Grilled Salmon', 'Fresh salmon with lemon butter sauce', 22.99, 'Main Courses'),
                ('Chocolate Cake', 'Rich chocolate cake with ganache', 7.99, 'Desserts'),
                ('Tiramisu', 'Classic Italian coffee-flavored dessert', 8.99, 'Desserts'),
                ('Soda', 'Choice of cola, lemon-lime, or root beer', 2.99, 'Beverages'),
                ('Chef''s Special Risotto', 'Creamy risotto with seasonal ingredients', 18.99, 'Specials')
                ON DUPLICATE KEY UPDATE name=name
            """);

            // Insert sample tables
            stmt.execute("""
                INSERT INTO tables (table_number, capacity, status) VALUES
                (1, 2, 'AVAILABLE'),
                (2, 2, 'AVAILABLE'),
                (3, 4, 'AVAILABLE'),
                (4, 4, 'AVAILABLE'),
                (5, 6, 'AVAILABLE'),
                (6, 8, 'AVAILABLE')
                ON DUPLICATE KEY UPDATE table_number=table_number
            """);

            // Insert admin user
            stmt.execute("""
                INSERT INTO users (username, password_hash, email, role) VALUES
                ('admin', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'admin@restaurant.com', 'ADMIN')
                ON DUPLICATE KEY UPDATE username=username
            """);

            System.out.println("Sample data inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}