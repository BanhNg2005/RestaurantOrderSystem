<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>MonAmour Admin Dashboard</title>
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
            background-color: #333;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        nav a.brand {
            color: #fff;
            text-decoration: none;
            font-size: 22px;
            font-weight: bold;
            transition: color 0.3s ease;
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
            color: #fff;
            text-decoration: none;
            padding: 10px 15px;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        nav ul li a:hover {
            background-color: #e44d26;
        }

        /* Admin Panel */
        .admin-panel {
            display: flex;
            margin: 20px;
        }

        /* Sidebar */
        .sidebar {
            width: 250px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            padding: 20px;
            margin-right: 20px;
            height: fit-content;
        }

        .sidebar h3 {
            color: #e44d26;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
        }

        .sidebar ul li {
            margin-bottom: 15px;
        }

        .sidebar ul li a {
            display: block;
            padding: 10px 15px;
            color: #555;
            text-decoration: none;
            border-radius: 5px;
            transition: all 0.3s ease;
        }

        .sidebar ul li a:hover, .sidebar ul li a.active {
            background-color: #e44d26;
            color: #fff;
        }

        /* Main content */
        .content {
            flex: 1;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            padding: 20px;
        }

        .content h1 {
            color: #e44d26;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        /* Section styling */
        .section {
            display: none;
        }

        .section.active {
            display: block;
        }

        /* Tables */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .data-table th, .data-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }

        .data-table th {
            background-color: #f8f8f8;
            color: #333;
        }

        .data-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .data-table tr:hover {
            background-color: #f1f1f1;
        }

        /* Forms */
        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group input[type="password"],
        .form-group input[type="email"],
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .form-group input[type="checkbox"] {
            margin-right: 5px;
        }

        .form-actions {
            margin-top: 20px;
        }

        .btn {
            display: inline-block;
            background-color: #e44d26;
            color: #fff;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s;
            text-decoration: none;
        }

        .btn:hover {
            background-color: #c73a1d;
        }

        .btn-danger {
            background-color: #d9534f;
        }

        .btn-danger:hover {
            background-color: #c9302c;
        }

        .btn-edit {
            background-color: #5bc0de;
        }

        .btn-edit:hover {
            background-color: #46b8da;
        }

        /* Modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            width: 50%;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #ddd;
            margin-bottom: 15px;
            padding-bottom: 10px;
        }

        .modal-header h2 {
            margin: 0;
            color: #e44d26;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover {
            color: #555;
        }

        .hidden {
            display: none;
        }
    </style>
</head>
<body>
<nav>
    <a class="brand" href="${pageContext.request.contextPath}/admin/home">MonAmour Admin</a>
    <ul>
        <li><a href="${pageContext.request.contextPath}/home">Main Site</a></li>
        <li><a href="${pageContext.request.contextPath}/login.jsp">Logout</a></li>
    </ul>
</nav>

<div class="admin-panel">
    <div class="sidebar">
        <h3>Admin Menu</h3>
        <ul>
            <li><a href="#" class="section-link active" data-section="dashboard">Dashboard</a></li>
            <li><a href="#" class="section-link" data-section="menu-management">Menu Items</a></li>
            <li><a href="#" class="section-link" data-section="table-management">Tables</a></li>
            <li><a href="#" class="section-link" data-section="user-management">Users</a></li>
            <li><a href="#" class="section-link" data-section="order-management">Orders</a></li>
            <li><a href="#" class="section-link" data-section="reservation-management">Reservations</a></li>
        </ul>
    </div>

    <div class="content">
        <!-- Dashboard Section -->
        <div id="dashboard" class="section active">
            <h1>Dashboard</h1>
            <div class="dashboard-stats">
                <div class="stat-box">
                    <h3>Menu Items</h3>
                    <p>${menuItems.size()} items</p>
                </div>
                <div class="stat-box">
                    <h3>Tables</h3>
                    <p>${tables.size()} tables</p>
                </div>
                <div class="stat-box">
                    <h3>Users</h3>
                    <p>${users.size()} users</p>
                </div>
                <div class="stat-box">
                    <h3>Orders</h3>
                    <p>${orders.size()} orders</p>
                </div>
                <div class="stat-box">
                    <h3>Reservations</h3>
                    <p>${reservations.size()} reservations</p>
                </div>
            </div>
            <p>Welcome to the MonAmour Restaurant Admin Panel. Use the sidebar to navigate between different management sections.</p>
        </div>

        <!-- Menu Management Section -->
        <div id="menu-management" class="section">
            <h1>Menu Management</h1>
            <button class="btn" onclick="openModal('add-menu-modal')">Add New Menu Item</button>

            <h3>Current Menu Items</h3>
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Category</th>
                    <th>Available</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${menuItems}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.description}</td>
                        <td>$${item.price}</td>
                        <td>${item.category}</td>
                        <td>${item.available ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn btn-edit" onclick="editMenuItem(${item.id}, '${item.name}', '${item.description}', ${item.price}, '${item.category}', ${item.available})">Edit</button>
                            <form method="post" action="${pageContext.request.contextPath}/admin/home" style="display:inline;">
                                <input type="hidden" name="action" value="deleteMenuItem">
                                <input type="hidden" name="id" value="${item.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Table Management Section -->
        <div id="table-management" class="section">
            <h1>Table Management</h1>
            <button class="btn" onclick="openModal('add-table-modal')">Add New Table</button>

            <h3>Current Tables</h3>
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Table Number</th>
                    <th>Capacity</th>
                    <th>Available</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${tables}" var="table">
                    <tr>
                        <td>${table.tableId}</td>
                        <td>${table.tableNumber}</td>
                        <td>${table.capacity}</td>
                        <td>${table.available ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn btn-edit" onclick="editTable(${table.tableId}, ${table.tableNumber}, ${table.capacity}, ${table.available})">Edit</button>
                            <form method="post" action="${pageContext.request.contextPath}/admin/home" style="display:inline;">
                                <input type="hidden" name="action" value="deleteTable">
                                <input type="hidden" name="id" value="${table.tableId}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this table?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- User Management Section -->
        <div id="user-management" class="section">
            <h1>User Management</h1>

            <h3>Registered Users</h3>
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Order Management Section -->
        <div id="order-management" class="section">
            <h1>Order Management</h1>

            <h3>Current Orders</h3>
            <table class="data-table">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Table</th>
                    <th>Status</th>
                    <th>Order Time</th>
                    <th>Total</th>
                    <th>Paid</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>${order.orderId}</td>
                        <td>${order.tableNumber}</td>
                        <td>${order.status}</td>
                        <td>${order.orderTime}</td>
                        <td>$${order.totalAmount}</td>
                        <td>${order.paid ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn btn-edit" onclick="updateOrderStatus(${order.orderId}, '${order.status}')">Update Status</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Reservation Management Section -->
        <div id="reservation-management" class="section">
            <h1>Reservation Management</h1>

            <h3>Current Reservations</h3>
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Table</th>
                    <th>Time</th>
                    <th>Guests</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${reservations}" var="reservation">
                    <tr>
                        <td>${reservation.reservationId}</td>
                        <td>${reservation.user.fullName}</td>
                        <td>${reservation.table.tableNumber}</td>
                        <td>${reservation.reservationTime}</td>
                        <td>${reservation.numberOfGuests}</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/admin/home" style="display:inline;">
                                <input type="hidden" name="action" value="deleteReservation">
                                <input type="hidden" name="id" value="${reservation.reservationId}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this reservation?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add Menu Item Modal -->
<div id="add-menu-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Add New Menu Item</h2>
            <span class="close" onclick="closeModal('add-menu-modal')">&times;</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/admin/home">
            <input type="hidden" name="action" value="addMenuItem">
            <div class="form-group">
                <label for="name">Item Name:</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="price">Price ($):</label>
                <input type="number" id="price" name="price" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="category">Category:</label>
                <select id="category" name="category" required>
                    <option value="DRINKS">Drinks</option>
                    <option value="APPETIZERS">Appetizers</option>
                    <option value="MAIN_COURSE">Main Course</option>
                    <option value="DESSERTS">Desserts</option>
                </select>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" name="isAvailable" checked>
                    Available
                </label>
            </div>
            <button type="submit" class="btn">Add Item</button>
        </form>
    </div>
</div>

<!-- Edit Menu Item Modal -->
<div id="edit-menu-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Edit Menu Item</h2>
            <span class="close" onclick="closeModal('edit-menu-modal')">&times;</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/admin/home">
            <input type="hidden" name="action" value="editMenuItem">
            <input type="hidden" id="edit-menu-id" name="id">
            <div class="form-group">
                <label for="edit-menu-name">Item Name:</label>
                <input type="text" id="edit-menu-name" name="name" required>
            </div>
            <div class="form-group">
                <label for="edit-menu-description">Description:</label>
                <textarea id="edit-menu-description" name="description" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="edit-menu-price">Price ($):</label>
                <input type="number" id="edit-menu-price" name="price" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="edit-menu-category">Category:</label>
                <select id="edit-menu-category" name="category" required>
                    <option value="DRINKS">Drinks</option>
                    <option value="APPETIZERS">Appetizers</option>
                    <option value="MAIN_COURSE">Main Course</option>
                    <option value="DESSERTS">Desserts</option>
                </select>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" id="edit-menu-available" name="isAvailable">
                    Available
                </label>
            </div>
            <button type="submit" class="btn">Update Item</button>
        </form>
    </div>
</div>

<!-- Add Table Modal -->
<div id="add-table-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Add New Table</h2>
            <span class="close" onclick="closeModal('add-table-modal')">&times;</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/admin/home">
            <input type="hidden" name="action" value="addTable">
            <div class="form-group">
                <label for="tableNumber">Table Number:</label>
                <input type="number" id="tableNumber" name="tableNumber" required>
            </div>
            <div class="form-group">
                <label for="capacity">Capacity:</label>
                <input type="number" id="capacity" name="capacity" required>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" name="isAvailable" checked>
                    Available
                </label>
            </div>
            <button type="submit" class="btn">Add Table</button>
        </form>
    </div>
</div>

<!-- Edit Table Modal -->
<div id="edit-table-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Edit Table</h2>
            <span class="close" onclick="closeModal('edit-table-modal')">&times;</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/admin/home">
            <input type="hidden" name="action" value="editTable">
            <input type="hidden" id="edit-table-id" name="id">
            <div class="form-group">
                <label for="edit-table-number">Table Number:</label>
                <input type="number" id="edit-table-number" name="tableNumber" required>
            </div>
            <div class="form-group">
                <label for="edit-table-capacity">Capacity:</label>
                <input type="number" id="edit-table-capacity" name="capacity" required>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" id="edit-table-available" name="isAvailable">
                    Available
                </label>
            </div>
            <button type="submit" class="btn">Update Table</button>
        </form>
    </div>
</div>

<!-- Update Order Status Modal -->
<div id="update-order-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Update Order Status</h2>
            <span class="close" onclick="closeModal('update-order-modal')">&times;</span>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/admin/home">
            <input type="hidden" name="action" value="updateOrder">
            <input type="hidden" id="update-order-id" name="id">
            <div class="form-group">
                <label for="update-order-status">Status:</label>
                <select id="update-order-status" name="status" required>
                    <option value="PLACED">Placed</option>
                    <option value="PREPARING">Preparing</option>
                    <option value="READY">Ready</option>
                    <option value="SERVED">Served</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>
            <button type="submit" class="btn">Update Status</button>
        </form>
    </div>
</div>

<script>
    // Switch between sections
    document.querySelectorAll('.section-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            // Remove active class from all links and sections
            document.querySelectorAll('.section-link').forEach(item => item.classList.remove('active'));
            document.querySelectorAll('.section').forEach(item => item.classList.remove('active'));

            // Add active class to clicked link
            this.classList.add('active');

            // Show the corresponding section
            const sectionId = this.getAttribute('data-section');
            document.getElementById(sectionId).classList.add('active');
        });
    });

    // Modal functions
    function openModal(modalId) {
        document.getElementById(modalId).style.display = 'block';
    }

    function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        document.querySelectorAll('.modal').forEach(modal => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        });
    }

    // Edit menu item
    function editMenuItem(id, name, description, price, category, available) {
        document.getElementById('edit-menu-id').value = id;
        document.getElementById('edit-menu-name').value = name;
        document.getElementById('edit-menu-description').value = description;
        document.getElementById('edit-menu-price').value = price;
        document.getElementById('edit-menu-category').value = category;
        document.getElementById('edit-menu-available').checked = available;

        openModal('edit-menu-modal');
    }

    // Edit table
    function editTable(id, tableNumber, capacity, available) {
        document.getElementById('edit-table-id').value = id;
        document.getElementById('edit-table-number').value = tableNumber;
        document.getElementById('edit-table-capacity').value = capacity;
        document.getElementById('edit-table-available').checked = available;

        openModal('edit-table-modal');
    }

    // Update order status
    function updateOrderStatus(id, currentStatus) {
        document.getElementById('update-order-id').value = id;
        document.getElementById('update-order-status').value = currentStatus;

        openModal('update-order-modal');
    }
</script>
</body>
</html>