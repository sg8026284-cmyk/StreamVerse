package com.app.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.app.db.SpotifyDatabase;

@WebServlet("/OTPValidationServlet")
public class OTPValidationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String enteredOTP = request.getParameter("enteredOTP");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("LoginPage.html?error=session_expired");
            return;
        }

        String token = (String) session.getAttribute("sentOTPToken");
        String email = (String) session.getAttribute("pendingEmail");

        // Verify either via JWT or direct token string
        String validOTP = null;
        try {
            validOTP = com.app.services.JWTServices.verifyJWT(token);
        } catch (Throwable t) {
            validOTP = token;
        }

        if (validOTP == null) {
            validOTP = token;
        }

        String cleanEntered = (enteredOTP != null) ? enteredOTP.trim() : "";

        if (cleanEntered.equals(validOTP) && email != null) {
            SpotifyDatabase.verifyUser(email);
            session.removeAttribute("sentOTPToken");
            session.removeAttribute("pendingEmail");
            response.sendRedirect("LoginPage.html?signup=success");
        } else {
            response.sendRedirect("OTPEnterPage.html?error=invalid_otp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}