<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Sign Up - Restaurant Order System</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f3f3f3;
    }

    .container {
      max-width: 400px;
      margin: 100px auto;
      padding: 30px;
      background-color: #ffffff;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    h2 {
      text-align: center;
    }

    .error {
      color: red;
      margin-bottom: 15px;
      text-align: center;
    }

    label {
      display: block;
      margin: 15px 0 5px;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      box-sizing: border-box;
    }

    input[type="submit"] {
      margin-top: 20px;
      width: 100%;
      padding: 10px;
      background-color: #4CAF50;
      border: none;
      color: white;
      font-weight: bold;
      cursor: pointer;
    }

    input[type="submit"]:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>Sign Up</h2>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
  <div class="error"><%= error %></div>
  <% } %>

  <form method="post" action="signup">
    <label for="fullName">Full Name:</label>
    <input type="text" name="fullName" id="fullName" required>

    <label for="email">Email:</label>
    <input type="email" name="email" id="email" required>

    <label for="password">Password:</label>
    <input type="password" name="password" id="password" required>

    <p>Already have an account? <a href="login">Login here</a></p>

    <input type="submit" value="Sign Up">
  </form>
</div>
</body>
</html>
