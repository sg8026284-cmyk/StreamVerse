package com.app.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.app.db.SpotifyDatabase;
import com.app.db.UserSchema;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String fname = request.getParameter("f_name");
        String lname = request.getParameter("l_name");
        String email = request.getParameter("user_email");
        String pwd1 = request.getParameter("pwd_1");
        String pwd2 = request.getParameter("pwd_2");

        System.out.println("========== [REGISTER REQUEST] ==========");
        System.out.println("Email: " + email + ", Name: " + fname + " " + lname);

        // 1. Basic validation
        if (fname == null || lname == null || email == null || pwd1 == null || pwd2 == null ||
            fname.trim().isEmpty() || lname.trim().isEmpty() || email.trim().isEmpty() || pwd1.isEmpty()) {
            response.sendRedirect("RegisterPage.html?error=missing_inputs");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            response.sendRedirect("RegisterPage.html?error=password_mismatch");
            return;
        }

        // 2. Insert user into database
        UserSchema user = new UserSchema(fname.trim(), lname.trim(), email.trim(), pwd1);
        boolean created = SpotifyDatabase.createUser(user);

        if (!created) {
            System.err.println("[REGISTER] User already exists or DB down.");
            response.sendRedirect("RegisterPage.html?error=email_exists");
            return;
        }

        // 3. Generate 6-digit OTP
        int otp = (int) (Math.random() * 900000) + 100000;
        String rawOtp = String.valueOf(otp);

        System.out.println("==================================================");
        System.out.println(">>> GENERATED OTP: " + rawOtp);
        System.out.println("==================================================");

        // 4. Try generating JWT token (fallback to raw OTP string if JWT fails)
        String token = rawOtp;
        try {
            token = com.app.services.JWTServices.generateJWT(otp);
        } catch (Throwable t) {
            System.err.println("[REGISTER] JWTServices warning: " + t.getMessage());
            token = rawOtp;
        }

        // 5. Try sending Email safely (Never stops redirect if SMTP times out/fails)
        try {
            com.app.services.EmailServices.sendOTP(email, fname, otp);
            System.out.println("[REGISTER] Email dispatched successfully.");
        } catch (Throwable t) {
            System.err.println("[REGISTER] Email sending skipped/failed: " + t.getMessage());
            System.out.println(">>> Use Console OTP: " + rawOtp);
        }

        // 6. Store in Session
        HttpSession session = request.getSession(true);
        session.setAttribute("sentOTPToken", token);
        session.setAttribute("pendingEmail", email.trim().toLowerCase());

        // 7. Guarantee Redirect
        System.out.println("[REGISTER] Redirecting to OTPEnterPage.html");
        response.sendRedirect("OTPEnterPage.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}