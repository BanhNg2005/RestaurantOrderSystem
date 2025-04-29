<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Place Your Order - MonAmour Restaurant</title>
    <style>
        /* General Reset */
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
            color: #e44d26;
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
        /* Main content */
        .content {
            padding: 40px 20px;
            max-width: 1200px;
            margin: 20px auto;
        }
        h1 {
            font-size: 36px;
            color: #e44d26;
            margin-bottom: 30px;
            text-align: center;
        }
        /* Order layout */
        .order-container {
            display: flex;
            gap: 30px;
        }
        /* Menu categories sidebar */
        .menu-categories {
            flex: 0 0 250px;
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            height: fit-content;
        }
        .menu-categories h2 {
            color: #e44d26;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .menu-categories ul {
            list-style: none;
            padding: 0;
        }
        .menu-categories ul li {
            margin-bottom: 15px;
        }
        .menu-categories ul li a {
            display: block;
            padding: 10px 15px;
            color: #555;
            text-decoration: none;
            border-radius: 5px;
            transition: all 0.3s ease;
        }
        .menu-categories ul li a:hover, .menu-categories ul li a.active {
            background-color: #e44d26;
            color: #fff;
        }
        /* Menu items grid */
        .menu-items {
            flex: 1;
        }
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .menu-item {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .menu-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }
        .menu-item h3 {
            font-size: 20px;
            color: #333;
            margin-bottom: 10px;
        }
        .menu-item p {
            font-size: 15px;
            color: #666;
            margin-bottom: 15px;
        }
        .menu-item .price {
            font-size: 18px;
            color: #e44d26;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .menu-item button {
            background-color: #e44d26;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 100%;
        }
        .menu-item button:hover {
            background-color: #c73a1d;
        }
        /* Order summary */
        .order-summary {
            flex: 0 0 300px;
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            height: fit-content;
            position: sticky;
            top: 20px;
        }
        .order-summary h2 {
            color: #e44d26;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .cart-items {
            margin-bottom: 20px;
        }
        .cart-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .cart-item-details {
            flex: 1;
        }
        .cart-item-name {
            font-weight: bold;
        }
        .cart-item-price {
            color: #e44d26;
        }
        .cart-item-actions {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .cart-item-quantity {
            width: 40px;
            text-align: center;
            border: 1px solid #ddd;
            border-radius: 3px;
            padding: 5px;
        }
        .cart-item-remove {
            background-color: #d9534f;
            color: white;
            border: none;
            border-radius: 3px;
            padding: 5px 10px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .cart-item-remove:hover {
            background-color: #c9302c;
        }
        .cart-total {
            display: flex;
            justify-content: space-between;
            font-weight: bold;
            font-size: 18px;
            margin-top: 20px;
            padding-top: 10px;
            border-top: 2px solid #eee;
        }
        .place-order-btn {
            background-color: #e44d26;
            color: white;
            border: none;
            padding: 12px;
            border-radius: 5px;
            font-size: 18px;
            width: 100%;
            margin-top: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .place-order-btn:hover {
            background-color: #c73a1d;
        }
        .place-order-btn:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }
        /* Table Selection Styling */
        .table-selection {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .table-selection h3 {
            color: #e44d26;
            margin-top: 0;
            margin-bottom: 15px;
            font-size: 20px;
        }
        .table-selection select {
            width: 300px;
            max-width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            color: #333;
            background-color: #f9f9f9;
            appearance: none;
            -webkit-appearance: none;
            background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%23e44d26' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right 10px center;
            background-size: 16px;
            cursor: pointer;
            transition: border-color 0.3s, box-shadow 0.3s;
            margin: 0 auto;
            display: block;
        }
        /* Footer */
        footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 20px 0;
            margin-top: 40px;
            font-size: 14px;
        }
        .empty-cart {
            text-align: center;
            padding: 20px;
            color: #777;
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
        <li><a href="${pageContext.request.contextPath}/order">Order</a></li>
        <li><a href="${pageContext.request.contextPath}/login">Logout</a></li>
    </ul>
</nav>

<div class="content">
    <h1>Place Your Order</h1>

    <!-- Table Selection -->
    <div class="table-selection">
        <h2>Select a Table</h2>
        <select id="tableNumber" name="tableNumber">
            <option value="">-- Select a Table --</option>
            <c:forEach var="table" items="${availableTables}">
                <option value="${table.tableNumber}">Table ${table.tableNumber} (Seats ${table.capacity})</option>
            </c:forEach>
        </select>
    </div>

    <!-- Menu Items -->
    <div class="menu-section">
        <h2>Menu Items</h2>
        <div class="menu-grid">
            <c:forEach var="item" items="${menuItems}">
                <div class="menu-item" data-category="${item.category}">
                    <h3>${item.name}</h3>
                    <p>${item.description}</p>
                    <div class="price">$${item.price}</div>
                    <button type="button" onclick="addToCart(${item.id}, '${item.name}', ${item.price})">Add to Order</button>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Cart Section -->
    <div class="cart-section">
        <h2>Your Order</h2>
        <div id="cartItems" class="cart-items">
            <!-- Cart items will be displayed here -->
        </div>
        <div class="cart-total">
            <span>Total:</span>
            <span id="cartTotal">$0.00</span>
        </div>

        <!-- Order Form -->
        <form id="orderForm" method="post" action="${pageContext.request.contextPath}/payment">
            <input type="hidden" id="tableNumberHidden" name="tableNumber" value="">
            <input type="hidden" id="orderDetails" name="orderDetails" value="">
            <input type="hidden" id="orderTotal" name="orderTotal" value="0.00">
            <button type="submit" class="place-order-btn" disabled>Select a Table First</button>
        </form>
    </div>
</div>

<footer>
    &copy; 2025 MonAmour Restaurant. All Rights Reserved.
</footer>

<script>
    let cart = [];

    function addToCart(id, name, price) {
        const existingItem = cart.find(item => item.id === id);
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            cart.push({ id, name, price, quantity: 1 });
        }
        updateCartDisplay();
    }

    function removeFromCart(id) {
        cart = cart.filter(item => item.id !== id);
        updateCartDisplay();
    }

    function updateQuantity(id, newQuantity) {
        if (newQuantity < 1) {
            removeFromCart(id);
            return;
        }
        const item = cart.find(item => item.id === id);
        if (item) {
            item.quantity = newQuantity;
            updateCartDisplay();
        }
    }

    function updateCartDisplay() {
        const cartItemsDiv = document.getElementById('cartItems');
        const cartTotalSpan = document.getElementById('cartTotal');
        const orderDetailsIn = document.getElementById('orderDetails');
        const orderTotalIn = document.getElementById('orderTotal');
        const tableSelect = document.getElementById('tableNumber');
        const tableHiddenIn = document.getElementById('tableNumberHidden');
        const placeOrderBtn = document.querySelector('.place-order-btn');

        cartItemsDiv.innerHTML = '';
        if (cart.length === 0) {
            cartItemsDiv.innerHTML = '<div class="empty-cart">Your cart is empty</div>';
        }

        let total = 0;
        cart.forEach(item => {
            const itemTotal = item.price * item.quantity;
            total += itemTotal;

            // wrapper
            const cartItemDiv = document.createElement('div');
            cartItemDiv.className = 'cart-item';

            // details
            const detailsDiv = document.createElement('div');
            detailsDiv.className = 'cart-item-details';
            const nameDiv  = document.createElement('div');
            nameDiv.className = 'cart-item-name';
            nameDiv.textContent = item.name;
            const priceDiv = document.createElement('div');
            priceDiv.className = 'cart-item-price';
            priceDiv.textContent = '$' + itemTotal.toFixed(2);
            detailsDiv.append(nameDiv, priceDiv);

            // actions
            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'cart-item-actions';

            const qtyInput = document.createElement('input');
            qtyInput.type = 'number';
            qtyInput.min = 1;
            qtyInput.value = item.quantity;
            qtyInput.className = 'cart-item-quantity';
            qtyInput.addEventListener('change', () => {
                updateQuantity(item.id, parseInt(qtyInput.value, 10));
            });

            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'cart-item-remove';
            removeBtn.textContent = '×';
            removeBtn.addEventListener('click', () => removeFromCart(item.id));

            actionsDiv.append(qtyInput, removeBtn);
            cartItemDiv.append(detailsDiv, actionsDiv);
            cartItemsDiv.appendChild(cartItemDiv);
        });

        cartTotalSpan.textContent = '$' + total.toFixed(2);
        orderDetailsIn.value = JSON.stringify(cart);
        orderTotalIn.value = total.toFixed(2);
        tableHiddenIn.value = tableSelect.value;

        if (tableSelect.value && cart.length > 0) {
            placeOrderBtn.disabled = false;
            placeOrderBtn.textContent = 'Proceed to Payment';
        } else {
            placeOrderBtn.disabled      = true;
            placeOrderBtn.textContent   = tableSelect.value
                ? 'Add Items to Order'
                : 'Select a Table First';
        }
    }


    function handleTableSelection() {
        updateCartDisplay();
    }

    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('tableNumber').addEventListener('change', handleTableSelection);
        document.getElementById('orderForm').addEventListener('submit', function(e) {
            console.log("Table:", document.getElementById('tableNumberHidden').value);
            console.log("Order total:", document.getElementById('orderTotal').value);
            console.log("Order details:", document.getElementById('orderDetails').value);
        });
    });
</script>
</body>
</html>