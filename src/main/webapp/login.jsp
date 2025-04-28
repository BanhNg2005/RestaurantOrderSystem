<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login - MonAmour Restaurant</title>

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <style>
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }
        body {
            background-color: #e44d26;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .background {
            position: absolute;
            width: 430px;
            height: 520px;
            transform: translate(-50%, -50%);
            left: 50%;
            top: 50%;
        }
        .background .shape {
            position: absolute;
            height: 200px;
            width: 200px;
            border-radius: 50%;
        }
        .shape:first-child {
            background: linear-gradient(#aaaaaa, #eaf0fb);
            left: -80px;
            top: -80px;
        }
        .shape:last-child {
            background: linear-gradient(to right, #ff512f, #f09819);
            right: -30px;
            bottom: -80px;
        }
        form {
            width: 400px;
            padding: 50px 35px;
            background: rgba(255, 255, 255, 0.13);
            border-radius: 10px;
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255, 255, 255, 0.1);
            box-shadow: 0 0 40px rgba(8,7,16,0.6);
            position: relative;
            z-index: 1;
        }
        form h3 {
            text-align: center;
            font-size: 32px;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 20px;
        }
        .error {
            color: #ff4d4d;
            text-align: center;
            margin-bottom: 15px;
            font-size: 14px;
        }
        label {
            display: block;
            margin-top: 20px;
            color: #ffffff;
            font-weight: 500;
            font-size: 16px;
        }
        input {
            display: block;
            width: 100%;
            margin-top: 8px;
            padding: 12px;
            background: rgba(255, 255, 255, 0.07);
            border-radius: 5px;
            font-size: 14px;
            color: #ffffff;
            border: none;
            outline: none;
        }
        input::placeholder {
            color: #e5e5e5;
        }
        button {
            margin-top: 30px;
            width: 100%;
            padding: 12px;
            background-color: #ffffff;
            color: #080710;
            font-size: 18px;
            font-weight: 600;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #ddd;
        }
        .social {
            margin-top: 20px;
            display: flex;
            justify-content: space-between;
        }
        .social div {
            width: 48%;
            background: rgba(255,255,255,0.27);
            color: #eaf0fb;
            padding: 10px 0;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .social div:hover {
            background: rgba(255,255,255,0.47);
        }
        .register-link {
            text-align: center;
            margin-top: 15px;
            color: #ffffff;
            font-size: 14px;
        }
        .register-link a {
            color: #23a2f6;
            text-decoration: none;
            font-weight: 600;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="background">
    <div class="shape"></div>
    <div class="shape"></div>
</div>

<form action="${pageContext.request.contextPath}/login" method="post">
    <h3>Login Here</h3>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="Enter your email" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="password" placeholder="Enter your password" required>

    <button type="submit">Log In</button>

    <div class="register-link">
        Don't have an account? <a href="signup">Register here</a>
    </div>

    <div class="social">
        <div class="go"><i class="fab fa-google"></i> Google</div>
        <div class="fb"><i class="fab fa-facebook"></i> Facebook</div>
    </div>
</form>

</body>
</html>
