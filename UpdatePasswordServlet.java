package com.app.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.app.db.SpotifyDatabase;
import com.app.helpers.DataValidations;

@WebServlet("/UpdatePasswordServlet")
public class UpdatePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("LoginPage.html?error=session_expired");
            return;
        }

        String email = (String) session.getAttribute("userEmail");
        String currentPwd = request.getParameter("current_pwd");
        String newPwd1 = request.getParameter("new_pwd_1");
        String newPwd2 = request.getParameter("new_pwd_2");

        if (currentPwd == null || newPwd1 == null || newPwd2 == null ||
            currentPwd.isEmpty() || newPwd1.isEmpty() || newPwd2.isEmpty()) {
            response.sendRedirect("SettingsPage.jsp?error=missing_fields");
            return;
        }

        if (!DataValidations.passwordValidation(newPwd1, newPwd2)) {
            response.sendRedirect("SettingsPage.jsp?error=password_mismatch");
            return;
        }

        boolean changed = SpotifyDatabase.changePassword(email, currentPwd, newPwd1);

        if (changed) {
            response.sendRedirect("SettingsPage.jsp?update=success");
        } else {
            response.sendRedirect("SettingsPage.jsp?error=invalid_current_password");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("SettingsPage.jsp");
    }
}