package com.app.db;

public class UserSchema {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isVerified;

    public UserSchema() {}

    public UserSchema(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isVerified = false;
    }

    public String getFirstName() { return (firstName != null) ? firstName : ""; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return (lastName != null) ? lastName : ""; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return (email != null) ? email : ""; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return (password != null) ? password : ""; }
    public void setPassword(String password) { this.password = password; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}