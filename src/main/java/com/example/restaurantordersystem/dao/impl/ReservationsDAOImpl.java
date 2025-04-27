package com.example.restaurantordersystem.dao.impl;

import com.example.restaurantordersystem.dao.ReservationsDAO;
import com.example.restaurantordersystem.db.DbUtil;
import com.example.restaurantordersystem.model.Reservation;
import com.example.restaurantordersystem.model.Table;
import com.example.restaurantordersystem.model.User;
import com.example.restaurantordersystem.dao.TablesDAO;
import com.example.restaurantordersystem.dao.UsersDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationsDAOImpl implements ReservationsDAO {

    private final TablesDAO tablesDAO = new TablesDAOImpl();
    private final UsersDAO usersDAO = new UsersDAOImpl();

    @Override
    public boolean saveReservation(Reservation reservation) {
        String sql;
        boolean isNew = (reservation.getReservationId() == null);

        if (isNew) {
            sql = "INSERT INTO reservations (reservation_time, number_of_guests, user_id, table_id) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE reservations SET reservation_time = ?, number_of_guests = ?, user_id = ?, table_id = ? WHERE reservation_id = ?";
        }

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(reservation.getReservationTime()));
            pstmt.setInt(2, reservation.getNumberOfGuests());
            pstmt.setLong(3, reservation.getUser().getUserId());
            pstmt.setLong(4, reservation.getTable().getTableId());

            if (!isNew) {
                pstmt.setLong(5, reservation.getReservationId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (isNew && affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setReservationId(generatedKeys.getLong(1));
                    }
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reservation findReservationByID(long reservationId){
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractReservationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public List<Reservation> findReservationsByUserId(int userId){
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ? ORDER BY reservation_time DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public boolean deleteReservation(long reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, reservationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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

        Timestamp timestamp = rs.getTimestamp("reservation_time");
        if (timestamp != null) {
            reservation.setReservationTime(timestamp.toLocalDateTime());
        }

        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));

        User user = usersDAO.findUserByID(rs.getInt("user_id"));
        reservation.setUser(user);

        Table table = tablesDAO.findTableByID(rs.getLong("table_id"));
        reservation.setTable(table);

        return reservation;
    }
}
