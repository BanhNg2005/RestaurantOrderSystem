<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>About Us - MonAmour Restaurant</title>
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
        .about {
            padding: 40px 20px;
            max-width: 800px;
            margin: 20px auto;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        }

        .about h2 {
            text-align: center;
            font-size: 32px;
            color: #e44d26;
            margin-bottom: 20px;
        }

        .about p {
            font-size: 18px;
            color: #666;
            line-height: 1.6;
            text-align: justify;
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
<div class="about">
    <h2>About Us</h2>
    <p>Welcome to MonAmour Restaurant, a place where culinary artistry meets a warm and inviting atmosphere.
        Established in 2005, MonAmour has been serving exquisite dishes crafted with the finest ingredients,
        bringing joy to food lovers from all walks of life.</p>
    <p>Our mission is to provide an unforgettable dining experience, combining exceptional service with a menu
        that celebrates both tradition and innovation. Whether you're here for a romantic dinner, a family gathering,
        or a casual meal, MonAmour is the perfect destination.</p>
    <p>We take pride in our commitment to quality, sustainability, and community. Thank you for choosing MonAmour Restaurant.
        We look forward to serving you!</p>
</div>
<footer>
    <p>&copy; 2025 MonAmour Restaurant. All rights reserved.</p>
</footer>
</body>
</html>