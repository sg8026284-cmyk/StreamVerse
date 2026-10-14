package com.app.services;

import com.app.db.SpotifyDatabase;
import com.app.db.UserSchema;

public class UserServices {

    public static Boolean passwordReset(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String cleanEmail = email.trim().toLowerCase();
        UserSchema user = SpotifyDatabase.readUser(cleanEmail);

        if (user == null) {
            System.err.println("[USER SERVICES] Email not registered in database: " + cleanEmail);
            return false;
        }

        try {
            String secretcode = JWTServices.GeneratePasswordResetCode(cleanEmail);
            
            System.out.println("==================================================");
            System.out.println(">>> DIRECT CONSOLE RESET URL:");
            System.out.println("http://localhost:8080/Youtube/ResetPassword?code=" + secretcode);
            System.out.println("==================================================");

            EmailServices.sendPasswordResetMail(cleanEmail, secretcode);
            return true;
        } catch (Exception e) {
            System.err.println("[USER SERVICES] Error initiating password reset: " + e.getMessage());
            return false;
        }
    }
}