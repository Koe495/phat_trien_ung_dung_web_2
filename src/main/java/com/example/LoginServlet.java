package com.example;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Hard-coded authentication
        if ("admin".equals(username) && "123".equals(password)) {

            HttpSession session = request.getSession();
            session.setAttribute("user", username);

            response.sendRedirect("home");

        } else {
            request.setAttribute("error", "Sai username hoặc password!");
            request.getRequestDispatcher("login.jsp")
                   .forward(request, response);
        }
    }
}
