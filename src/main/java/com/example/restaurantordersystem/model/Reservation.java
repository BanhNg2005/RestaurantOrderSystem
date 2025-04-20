package com.example.restaurantordersystem.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long reservationId;
    private LocalDateTime reservationTime;
    private int numberOfGuests;
    private User user;
    private Table table;

    // Constructors
    public Reservation() {}

    public Reservation(LocalDateTime reservationTime, int numberOfGuests, User user, Table table) {
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
        this.user = user;
        this.table = table;
    }

    // Getters and Setters

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Table getTable() {
        return table;
    }


    public void setTable(Table table) {
        this.table = table;
    }


}