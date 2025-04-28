<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Our Menu - MonAmour Restaurant</title>
    <style>
        /* General Reset */
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            color: #333;
        }

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

        .content {
            padding: 40px 20px;
            max-width: 1200px;
            margin: 20px auto;
            text-align: center;
        }

        h1 {
            font-size: 36px;
            color: #e44d26;
            margin-bottom: 30px;
        }

        /* Category Filter */
        .filter-buttons {
            margin-bottom: 40px;
        }

        .filter-buttons button {
            background-color: #e44d26;
            color: white;
            border: none;
            padding: 10px 20px;
            margin: 5px;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .filter-buttons button:hover {
            background-color: #c73a1d;
        }

        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
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

        .menu-item h2 {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
        }

        .menu-item p {
            font-size: 16px;
            color: #666;
            margin-bottom: 15px;
        }

        .menu-item .price {
            font-size: 20px;
            color: #e44d26;
            font-weight: bold;
            margin-top: auto;
        }

        footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 20px 0;
            margin-top: 40px;
            font-size: 14px;
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
    <h1>Our Menu</h1>

    <div class="filter-buttons">
        <button onclick="filterMenu('ALL')">All</button>
        <button onclick="filterMenu('DRINKS')">Drinks</button>
        <button onclick="filterMenu('APPETIZERS')">Appetizers</button>
        <button onclick="filterMenu('MAIN_COURSE')">Main Courses</button>
        <button onclick="filterMenu('DESSERTS')">Desserts</button>
    </div>

    <div class="menu-grid" id="menuGrid">
        <c:forEach var="item" items="${menuItems}">
            <div class="menu-item" data-category="${item.category}">
                <h2>${item.name}</h2>
                <p>${item.description}</p>
                <div class="price">$${item.price}</div>
            </div>
        </c:forEach>
    </div>
</div>

<footer>
    &copy; 2024 MonAmour Restaurant. All rights reserved.
</footer>

<script>
    function filterMenu(category) {
        const items = document.querySelectorAll('.menu-item');
        items.forEach(item => {
            const itemCategory = item.getAttribute('data-category');
            if (category === 'ALL' || itemCategory === category) {
                item.style.display = 'flex';
            } else {
                item.style.display = 'none';
            }
        });
    }
</script>

</body>
</html>
