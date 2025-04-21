package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationsDAO {
    Reservation findReservationByID(long reservationId);
    List<Reservation> findAllReservations();
    List<Reservation> findReservationsByUserId(int userId);
    List<Reservation> findReservationsByTableId(int tableId);
    boolean saveReservation(Reservation reservation);
    boolean deleteReservation(long reservationId);
    List<Reservation> findReservationsByTime(LocalDateTime from, LocalDateTime to);
}
