package com.app.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.app.services.JWTServices;

@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String code = request.getParameter("code");
        System.out.println("[RESET SERVLET] Incoming request with code: " + code);

        if (code == null || code.trim().isEmpty()) {
            System.err.println("[RESET SERVLET] 'code' parameter missing from request URL.");
            response.sendRedirect("LoginPage.html?error=expired_or_invalid_token");
            return;
        }

        String userEmail = JWTServices.verifyPasswordResetCode(code);

        if (userEmail != null && !userEmail.trim().isEmpty()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("resetUserEmail", userEmail);
            System.out.println("[RESET SERVLET] Verified email " + userEmail + ". Forwarding to ResetPassword.html");
            response.sendRedirect("ResetPassword.html");
        } else {
            System.err.println("[RESET SERVLET] Token verification returned null.");
            response.sendRedirect("LoginPage.html?error=expired_or_invalid_token");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}