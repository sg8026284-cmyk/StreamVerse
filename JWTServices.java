package com.app.services;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTServices {
    private static final String TOP_SECRET = "this_is_top_secret";
    private static final Algorithm algo = Algorithm.HMAC256(TOP_SECRET);

    public static String generateJWT(int OTP) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + (5 * 60 * 1000));

        return JWT.create()
                .withClaim("OTP", OTP)
                .withIssuer("SEC-B")
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algo);
    }

    public static String verifyJWT(String token) {
        try {
            DecodedJWT decrypted = JWT.require(algo)
                    .withIssuer("SEC-B")
                    .build()
                    .verify(token);

            int otpValue = decrypted.getClaim("OTP").asInt();
            return String.valueOf(otpValue);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String GeneratePasswordResetCode(String email) {
        Date now = new Date();
        // 60 minutes expiry window for testing
        Date expiresAt = new Date(now.getTime() + (60 * 60 * 1000));

        return JWT.create()
                .withClaim("UserEmail", email.trim().toLowerCase())
                .withIssuer("SEC-B")
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algo);
    }

    public static String verifyPasswordResetCode(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        try {
            DecodedJWT decrypted = JWT.require(algo)
                    .withIssuer("SEC-B")
                    .build()
                    .verify(token.trim());

            return decrypted.getClaim("UserEmail").asString();
        } catch (Exception e) {
            System.err.println("[JWT VERIFY EXCEPTION] " + e.getMessage());
            return null;
        }
    }
}