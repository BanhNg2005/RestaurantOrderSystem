package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.TablesDAO;
import com.example.restaurantordersystem.model.Table;
import com.example.restaurantordersystem.db.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TablesDAOImpl implements TablesDAO {
    @Override
    public Table findTableByID(long tableId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tables WHERE table_id = ?");
            stmt.setLong(1, tableId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int tableNumber = rs.getInt("table_number");
                int capacity = rs.getInt("capacity");
                boolean isAvailable = rs.getBoolean("is_available");

                Table table = new Table(tableNumber, capacity, isAvailable);
                table.setTableId(tableId);
                return table;
            }
        } catch (SQLException e) {
            System.err.println("Error finding table: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return null;
    }

    @Override
    public List<Table> findAllTables() {
        System.out.println("DEBUG: Entering findAllTables()");
        List<Table> tables = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            System.out.println("DEBUG: Connection established: " + (conn != null));

            Statement stmt = conn.createStatement();
            System.out.println("DEBUG: Executing query: SELECT * FROM tables");

            ResultSet rs = stmt.executeQuery("SELECT * FROM tables");
            System.out.println("DEBUG: Query executed, processing results...");

            while (rs.next()) {
                long tableId = rs.getLong("table_id");
                int tableNumber = rs.getInt("table_number");
                int capacity = rs.getInt("capacity");
                boolean isAvailable = rs.getBoolean("is_available");

                System.out.printf("DEBUG: Found table - ID: %d, Number: %d, Capacity: %d, Available: %b%n",
                        tableId, tableNumber, capacity, isAvailable);

                Table table = new Table(tableNumber, capacity, isAvailable);
                table.setTableId(tableId);
                tables.add(table);
            }
            System.out.println("DEBUG: Total tables found: " + tables.size());
        } catch (SQLException e) {
            System.err.println("ERROR in findAllTables(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return tables;
    }

    @Override
    public boolean saveTable(Table table) {
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();

            // Check if table already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM tables WHERE table_id = ?");
            checkStmt.setLong(1, table.getTableId() != null ? table.getTableId() : -1);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count == 0) {
                // Insert
                stmt = conn.prepareStatement("INSERT INTO tables (table_number, capacity, is_available) VALUES (?, ?, ?)");
                stmt.setInt(1, table.getTableNumber());
                stmt.setInt(2, table.getCapacity());
                stmt.setBoolean(3, table.isAvailable());
            } else {
                // Update
                stmt = conn.prepareStatement("UPDATE tables SET table_number = ?, capacity = ?, is_available = ? WHERE table_id = ?");
                stmt.setInt(1, table.getTableNumber());
                stmt.setInt(2, table.getCapacity());
                stmt.setBoolean(3, table.isAvailable());
                stmt.setLong(4, table.getTableId());
            }

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving table: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean deleteTable(int id) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM tables WHERE table_id = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting table: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
