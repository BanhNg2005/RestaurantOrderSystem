package com.example.restaurantordersystem.dao;

import com.example.restaurantordersystem.model.Reservation;

import java.util.List;

public interface ReservationsDAO {
    Reservation findReservationByID(long reservationID);

    List<Reservation> findAllReservations();

    boolean saveReservation(Reservation reservation);

    boolean deleteReservation(int id);
}
