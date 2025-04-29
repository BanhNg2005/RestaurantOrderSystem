<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Sign Up - MonAmour Restaurant</title>

  <link rel="preconnect" href="https://fonts.gstatic.com">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">

  <!-- Font Awesome for icons (optional if needed later) -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

  <style>
    * {
      padding: 0;
      margin: 0;
      box-sizing: border-box;
      font-family: 'Poppins', sans-serif;
    }
    body {
      background-color: #eaf0fb;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .background {
      position: absolute;
      width: 430px;
      height: 570px;
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

      background: linear-gradient(to right, #ff512f, #f09819);
      left: -80px;
      top: -80px;
    }
    .shape:last-child {
      background: linear-gradient(#aaaaaa, #eaf0fb);
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
      color: #080710;
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
      color: #080710;
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
      color: #080710;
    }
    button {
      margin-top: 30px;
      width: 100%;
      padding: 12px;
      background-color: #080710;
      color: #eaf0fb;
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
    .login-link {
      text-align: center;
      margin-top: 15px;
      color: #080710;
      font-size: 14px;
    }
    .login-link a {
      color: #23a2f6;
      text-decoration: none;
      font-weight: 600;
    }
    .login-link a:hover {
      text-decoration: underline;
    }
  </style>
</head>

<body>

<div class="background">
  <div class="shape"></div>
  <div class="shape"></div>
</div>

<form action="${pageContext.request.contextPath}/signup" method="post">
  <h3>Sign Up</h3>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
  <div class="error"><%= error %></div>
  <% } %>

  <label for="fullName">Full Name</label>
  <input type="text" id="fullName" name="fullName" placeholder="Enter your full name" required>

  <label for="email">Email</label>
  <input type="email" id="email" name="email" placeholder="Enter your email" required>

  <label for="password">Password</label>
  <input type="password" id="password" name="password" placeholder="Create a password" required>

  <button type="submit">Sign Up</button>

  <div class="login-link">
    Already have an account? <a href="login">Login here</a>
  </div>
</form>

</body>
</html>
