<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MonAmour Restaurant</title>
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

        .content {
            padding: 40px 20px;
            text-align: center;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            margin: 20px auto;
            max-width: 1000px;
        }

        h1 {
            font-size: 36px;
            color: #e44d26;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            color: #ffffff;
            margin-bottom: 30px;
        }
        .welcome-box {
            position: relative;
            background-image: url('images/homepage.jpeg');
            background-size: cover;
            background-position: center;
            width: 100%;
            height: 700px;
            border-radius: 20px;
            overflow: hidden; /* important for overlay */
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
        }

        .welcome-box::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.4);
            z-index: 1;
        }

        .welcome-box h1,
        .welcome-box p,
        .welcome-box .btn-menu {
            position: relative;
            z-index: 2;
            color: white;
            text-align: center;
        }

        .welcome-box h1 {
            font-size: 32px;
            margin-bottom: 15px;
        }

        .welcome-box p {
            font-size: 18px;
            margin-bottom: 30px;
            max-width: 80%;
        }

        .welcome-box .btn-menu {
            background-color: #e44d26;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            text-decoration: none;
        }

        .welcome-box .btn-menu:hover {
            background-color: #c73a1d;

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
    <div class="welcome-box">
        <h1>Welcome to MonAmour Restaurant</h1>
        <p>Experience the finest dining with exquisite dishes and a romantic ambiance.</p>
        <a href="${pageContext.request.contextPath}/menu" class="btn-menu">View Menu</a>
    </div>
</div>
<footer>
    <p>&copy; 2025 MonAmour Restaurant. All rights reserved.</p>
</footer>
</body>
</html>