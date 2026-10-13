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

@WebServlet("/NewPassword")
public class NewPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pass1 = request.getParameter("pass1");
        String pass2 = request.getParameter("pass2");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("resetUserEmail") == null) {
            response.sendRedirect("LoginPage.html?error=session_expired");
            return;
        }

        String email = (String) session.getAttribute("resetUserEmail");

        if (!DataValidations.passwordValidation(pass1, pass2) || pass1.trim().isEmpty()) {
            response.sendRedirect("ResetPassword.html?error=password_mismatch");
            return;
        }

        boolean updated = SpotifyDatabase.updatePassword(email, pass1);

        if (updated) {
            session.removeAttribute("resetUserEmail");
            response.sendRedirect("LoginPage.html?reset=success");
        } else {
            response.sendRedirect("ResetPassword.html?error=update_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("ResetPassword.html");
    }
}