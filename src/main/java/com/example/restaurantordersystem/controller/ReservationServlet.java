package com.example.restaurantordersystem.controller;

import com.example.restaurantordersystem.dao.ReservationsDAO;
import com.example.restaurantordersystem.dao.TablesDAO;
import com.example.restaurantordersystem.dao.impl.ReservationsDAOImpl;
import com.example.restaurantordersystem.dao.impl.TablesDAOImpl;
import com.example.restaurantordersystem.model.Reservation;
import com.example.restaurantordersystem.model.Table;
import com.example.restaurantordersystem.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {
    private final ReservationsDAO reservationsDAO = new ReservationsDAOImpl();
    private final TablesDAO tablesDAO = new TablesDAOImpl();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            listUserReservations(request, response, user);
        } else {
            switch (action) {
                case "view":
                    showViewReservation(request, response, user);
                    break;
                case "edit":
                    showEditForm(request, response, user);
                    break;
                case "new":
                    showNewForm(request, response, user);
                    break;
                case "delete":
                    deleteReservation(request, response, user);
                    break;
                default:
                    listUserReservations(request, response, user);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addReservation(request, response, user);
        } else if ("update".equals(action)) {
            updateReservation(request, response, user);
        } else {
            response.sendRedirect(request.getContextPath() + "/reservations");
        }
    }

    private void listUserReservations(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Reservation> reservations = reservationsDAO.findReservationsByUserId(user.getUserId());
        List<Table> tables = tablesDAO.findAllTables();

        List<Table> availableTables = new ArrayList<>();
        for (Table table : tables) {
            if (table.isAvailable()) {
                availableTables.add(table);
            }
        }

        request.setAttribute("reservations", reservations);
        request.setAttribute("tables", availableTables);

        request.getRequestDispatcher("/reservation.jsp").forward(request, response);
    }

    private void showViewReservation(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        Reservation reservation = reservationsDAO.findReservationByID(id);

        if (reservation == null || reservation.getUser().getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/reservations");
            return;
        }

        List<Table> tables = tablesDAO.findAllTables();

        request.setAttribute("reservation", reservation);
        request.setAttribute("tables", tables);
        request.getRequestDispatcher("/reservation.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        Reservation reservation = reservationsDAO.findReservationByID(id);

        if (reservation == null || reservation.getUser().getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/reservations");
            return;
        }

        List<Table> tables = tablesDAO.findAllTables();

        // Format LocalDateTime for datetime-local input
        String reservationTimeFormatted = reservation.getReservationTime().format(DATE_TIME_FORMATTER);

        request.setAttribute("reservation", reservation);
        request.setAttribute("tables", tables);
        request.setAttribute("reservationTimeFormatted", reservationTimeFormatted);

        request.getRequestDispatcher("/reservation.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Table> tables = tablesDAO.findAllTables();
        List<Table> availableTables = new ArrayList<>();
        for (Table table : tables) {
            if (table.isAvailable()) {
                availableTables.add(table);
            }
        }
        request.setAttribute("tables", availableTables);
        request.getRequestDispatcher("/reservation.jsp").forward(request, response);
    }

    private void addReservation(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            long tableId = Long.parseLong(request.getParameter("tableId"));
            String reservationTimeStr = request.getParameter("reservationTime");
            int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));

            Table table = tablesDAO.findTableByID(tableId);
            if (table != null) {
                if (!table.isAvailable()) {
                    request.setAttribute("message", "Selected table is not available.");
                    listUserReservations(request, response, user);
                    return;
                }

                if (table.getCapacity() < numberOfGuests) {
                    request.setAttribute("message", "Selected table does not have enough seats for your guests.");
                    listUserReservations(request, response, user);
                    return;
                }

                LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeStr, DATE_TIME_FORMATTER);
                Reservation reservation = new Reservation(reservationTime, numberOfGuests, user, table);

                if (reservationsDAO.saveReservation(reservation)) {
                    table.setAvailable(false);
                    tablesDAO.saveTable(table);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/reservations");
    }


    private void updateReservation(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Reservation reservation = reservationsDAO.findReservationByID(id);

            if (reservation != null && reservation.getUser().getUserId() == user.getUserId()) {
                long tableId = Long.parseLong(request.getParameter("tableId"));
                String reservationTimeStr = request.getParameter("reservationTime");
                int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));

                LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeStr, DATE_TIME_FORMATTER);
                Table table = tablesDAO.findTableByID(tableId);
                if (table.getCapacity() < numberOfGuests) {
                    request.setAttribute("message", "Selected table does not have enough seats for your guests.");
                    listUserReservations(request, response, user);
                    return;
                }
                if (table != null) {
                    reservation.setReservationTime(reservationTime);
                    reservation.setNumberOfGuests(numberOfGuests);
                    reservation.setTable(table);

                    reservationsDAO.saveReservation(reservation);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/reservations");
    }

    private void deleteReservation(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Reservation reservation = reservationsDAO.findReservationByID(id);

            if (reservation != null && reservation.getUser().getUserId() == user.getUserId()) {
                Table table = reservation.getTable();
                table.setAvailable(true);
                tablesDAO.saveTable(table);

                reservationsDAO.deleteReservation(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/reservations");
    }
}
