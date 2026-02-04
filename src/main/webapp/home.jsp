<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String user = (String) session.getAttribute("user");
%>

<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Trang chủ</h2>

    <p>Xin chào, <b><%= user %></b></p>

    <a href="logout">Logout</a>
</body>
</html>
