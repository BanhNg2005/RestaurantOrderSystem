


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Reservations - MonAmour Restaurant</title>
    <style>
        /* General Reset and Body Styling */
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            line-height: 1.6;
        }

        /* Navigation Bar */
        nav {
            background-color: #fff;
            border-bottom: 1px solid #ddd;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        nav a {
            color: #e44d26; /* MonAmour brand color */
            text-decoration: none;
            font-size: 22px;
            font-weight: bold;
            transition: color 0.3s ease;
        }

        nav a:hover {
            color: #c73a1d;
        }

        nav ul {
            list-style: none;
            display: flex;
            padding: 0;
            margin: 0;
        }

        nav ul li {
            margin-left: 25px;
        }

        nav ul li a {
            color: #555;
            text-decoration: none;
            padding: 10px 15px;
            border-radius: 5px;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        nav ul li a:hover {
            background-color: #e44d26;
            color: #fff;
        }

        /* Main Content Area */
        .content {
            padding: 40px 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            margin: 20px auto;
            max-width: 900px;
        }

        h1 {
            font-size: 32px;
            color: #e44d26;
            margin-bottom: 20px;
            text-align: center;
        }

        /* Message styling */
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: center;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* Table Styling */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f8f8f8;
            color: #e44d26;
            font-weight: bold;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        /* Button Styling */
        .btn {
            display: inline-block;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            font-size: 16px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background-color: #e44d26;
            color: white;
        }

        .btn-primary:hover {
            background-color: #c73a1d;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background-color: #bd2130;
        }

        .btn-sm {
            padding: 5px 10px;
            font-size: 14px;
        }

        /* Action buttons in table */
        .action-btn {
            margin-right: 5px;
        }

        /* Form Styling */
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }

        .form-actions {
            margin-top: 30px;
            text-align: center;
        }

        /* Footer */
        footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 20px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
            font-size: 14px;
        }

        /* Add some bottom padding to body to prevent content from being hidden by footer */
        body {
            padding-bottom: 60px;
        }

        .empty-state {
            text-align: center;
            padding: 40px 20px;
        }

        .empty-state p {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<nav>
    <a href="${pageContext.request.contextPath}/home">MonAmour</a>
    <ul>
        <li><a href="about.jsp">About</a></li>
        <li><a href="${pageContext.request.contextPath}/reservations">Reservation</a></li>
        <li><a href="${pageContext.request.contextPath}/menu">Menu</a></li>
        <li><a href="order.jsp">Order</a></li>
        <li><a href="${pageContext.request.contextPath}/login">Logout</a></li>
    </ul>
</nav>

<div class="content">
    <%-- Message display if needed --%>
    <c:if test="${not empty param.message || not empty requestScope.message}">
        <div class="message ${param.message.startsWith('Failed') || param.message.startsWith('Invalid') ? 'error' : 'success'}">
                ${not empty requestScope.message ? requestScope.message : param.message}
        </div>
    </c:if>

    <c:choose>

        <%-- List of Reservations --%>
        <c:when test="${param.action == null || param.action == 'list'}">
            <h1>My Reservations</h1>

            <div style="text-align: right; margin-bottom: 20px;">
                <a href="${pageContext.request.contextPath}/reservations?action=new" class="btn btn-primary">Make New Reservation</a>
            </div>

            <c:choose>
                <c:when test="${empty reservations}">
                    <div class="empty-state">
                        <p>You don't have any reservations yet.</p>
                        <a href="${pageContext.request.contextPath}/reservations?action=new" class="btn btn-primary">Make Your First Reservation</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                        <tr>
                            <th>Date & Time</th>
                            <th>Table</th>
                            <th>Guests</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="reservation" items="${reservations}">
                            <tr>
                                <td>
                                    <c:if test="${not empty reservation.reservationTimeAsDate}">
                                        <fmt:formatDate value="${reservation.reservationTimeAsDate}" pattern="MMM dd, yyyy - HH:mm" />
                                    </c:if>
                                </td>
                                <td>Table ${reservation.table.tableId}</td>
                                <td>${reservation.numberOfGuests}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/reservations?action=view&id=${reservation.reservationId}" class="btn btn-secondary btn-sm action-btn">View</a>
                                    <a href="${pageContext.request.contextPath}/reservations?action=edit&id=${reservation.reservationId}" class="btn btn-primary btn-sm action-btn">Edit</a>
                                    <a href="#" onclick="confirmDelete(${reservation.reservationId})" class="btn btn-danger btn-sm action-btn">Cancel</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </c:when>

        <%-- View a Reservation --%>
        <c:when test="${param.action == 'view' && not empty reservation}">
            <h1>Reservation Details</h1>

            <div class="form-container">
                <div class="form-group">
                    <label>Reservation Date & Time:</label>
                    <p>
                        <c:if test="${not empty reservation.reservationTimeAsDate}">
                            <fmt:formatDate value="${reservation.reservationTimeAsDate}" pattern="MMMM dd, yyyy - HH:mm" />
                        </c:if>
                    </p>
                </div>

                <div class="form-group">
                    <label>Table:</label>
                    <p>Table ${reservation.table.tableId}</p>
                </div>

                <div class="form-group">
                    <label>Number of Guests:</label>
                    <p>${reservation.numberOfGuests}</p>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">Back to List</a>
                    <a href="${pageContext.request.contextPath}/reservations?action=edit&id=${reservation.reservationId}" class="btn btn-primary">Edit</a>
                    <a href="#" onclick="confirmDelete(${reservation.reservationId})" class="btn btn-danger">Cancel Reservation</a>
                </div>
            </div>
        </c:when>

        <%-- New / Edit Form --%>
        <c:when test="${param.action == 'new' || param.action == 'edit'}">
            <h1>${param.action == 'new' ? 'Make a New Reservation' : 'Edit Reservation'}</h1>

            <div class="form-container">
                <form action="${pageContext.request.contextPath}/reservations" method="post">
                    <input type="hidden" name="action" value="${param.action == 'new' ? 'add' : 'update'}" />

                    <c:if test="${param.action == 'edit'}">
                        <input type="hidden" name="id" value="${reservation.reservationId}" />
                    </c:if>

                    <div class="form-group">
                        <label for="reservationTime">Reservation Date & Time:</label>
                        <input type="datetime-local" id="reservationTime" name="reservationTime" class="form-control"
                               value="<c:out value='${param.action == "edit" ? reservationTimeFormatted : ""}'/>" required />
                    </div>

                    <div class="form-group">
                        <label for="tableId">Select Table:</label>
                        <select id="tableId" name="tableId" class="form-control" required>
                            <option value="">-- Select a Table --</option>
                            <c:forEach var="table" items="${tables}">
                                <option value="${table.tableId}" <c:if test="${param.action == 'edit' && reservation.table.tableId == table.tableId}">selected</c:if>>
                                    Table ${table.tableId} (${table.capacity} seats)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="numberOfGuests">Number of Guests:</label>
                        <input type="number" id="numberOfGuests" name="numberOfGuests" class="form-control"
                               value="${param.action == 'edit' ? reservation.numberOfGuests : '2'}"
                               min="1" max="12" required />
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">Cancel</a>
                        <button type="submit" class="btn btn-primary">${param.action == 'new' ? 'Make Reservation' : 'Update Reservation'}</button>
                    </div>
                </form>
            </div>
        </c:when>

    </c:choose>
</div>

<footer>
    &copy; 2024 MonAmour Restaurant. All Rights Reserved.
</footer>

<script>
    function confirmDelete(id) {
        if (confirm('Are you sure you want to cancel this reservation?')) {
            window.location.href = '${pageContext.request.contextPath}/reservations?action=delete&id=' + id;
        }
    }
</script>

</body>
</html>