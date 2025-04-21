package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.ReservationsDAO;
import com.example.restaurantordersystem.db.DbUtil;
import com.example.restaurantordersystem.model.Reservation;
import com.example.restaurantordersystem.model.Table;
import com.example.restaurantordersystem.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationsDAOImpl implements ReservationsDAO {

    @Override
    public Reservation findReservationByID(long reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractReservationFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservation: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all reservations: " + e.getMessage());
        }
        return reservations;
    }

    @Override
    public List<Reservation> findReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations by user: " + e.getMessage());
        }
        return reservations;
    }

    @Override
    public List<Reservation> findReservationsByTableId(int tableId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE table_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations by table: " + e.getMessage());
        }
        return reservations;
    }

    @Override
    public boolean saveReservation(Reservation reservation) {
        String checkSql = "SELECT COUNT(*) FROM reservations WHERE reservation_id = ?";
        String insertSql = "INSERT INTO reservations (user_id, table_id, reservation_time, number_of_guests) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE reservations SET user_id = ?, table_id = ?, reservation_time = ?, number_of_guests = ? WHERE reservation_id = ?";
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setLong(1, reservation.getReservationId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count == 0) {
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, reservation.getUser().getUserId());
                stmt.setLong(2, reservation.getTable().getTableId());
                stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationTime()));
                stmt.setInt(4, reservation.getNumberOfGuests());
            } else {
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, reservation.getUser().getUserId());
                stmt.setLong(2, reservation.getTable().getTableId());
                stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationTime()));
                stmt.setInt(4, reservation.getNumberOfGuests());
                stmt.setLong(5, reservation.getReservationId());
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saving reservation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteReservation(long reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reservation> findReservationsByTime(LocalDateTime from, LocalDateTime to) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE reservation_time BETWEEN ? AND ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations by time: " + e.getMessage());
        }
        return reservations;
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getLong("reservation_id"));
        reservation.setReservationTime(rs.getTimestamp("reservation_time").toLocalDateTime());
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        UsersDAOImpl user = new UsersDAOImpl();
        TablesDAOImpl table = new TablesDAOImpl();
        reservation.setUser(user.findUserByID(rs.getInt("user_id")));
        reservation.setTable( table.findTableByID(rs.getLong("table_id")));

        return reservation;
    }
}
