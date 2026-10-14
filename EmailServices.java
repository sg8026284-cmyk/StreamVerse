package com.app.services;

import java.util.Locale;
import java.util.Properties;

import com.app.config.SecretsReader;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailServices {
    
    public static Boolean sendOTP(String to, String userName, int OTP) {
        Locale locale = new Locale("en", "US");
        String FROM = SecretsReader.readData("secrets", locale, "ID");
        String PASSWORD = SecretsReader.readData("secrets", locale, "PASS"); 
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Registration OTP");
            message.setText("Hi " + userName + ",\n\nYour OTP is: " + OTP);
            
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("[EMAIL SERVICES] Error sending OTP email:");
            e.printStackTrace();
            return false; 
        }
    }
    
    public static Boolean sendPasswordResetMail(String to, String secretcode) {
        Locale locale = new Locale("en", "US");
        String FROM = SecretsReader.readData("secrets", locale, "ID");
        String PASSWORD = SecretsReader.readData("secrets", locale, "PASS"); 

        System.out.println("[EMAIL SERVICES] FROM: " + FROM);
        System.out.println("[EMAIL SERVICES] TO: " + to);

        if (FROM == null || PASSWORD == null) {
            System.err.println("[EMAIL SERVICES] Error: ID or PASS in secrets reader is null!");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
       
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        // Logs full SMTP handshake in console
        session.setDebug(true);

        try {
            String resetUrl = "http://localhost:8080/Youtube/ResetPassword?code=" + secretcode;

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Password Reset Request");
            message.setText("Hi,\n\nPlease click the link below to reset your password:\n" 
                    + resetUrl + "\n\nThis link is valid for 60 minutes.");
            
            Transport.send(message);
            System.out.println("[EMAIL SERVICES] Reset email successfully dispatched to " + to);
            return true;
        } catch (Exception e) {
            System.err.println("[EMAIL SERVICES] SMTP exception encountered:");
            e.printStackTrace();
            return false; 
        }
    }
}