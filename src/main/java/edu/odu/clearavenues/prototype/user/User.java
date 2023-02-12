package edu.odu.clearavenues.prototype.user;

import jakarta.persistence.*;


// This is where you construct create the User object in accordance with the database structure
@Entity
@Table(name = "users")
public class User {

    // Default constructor
    protected User() {}
    public enum TYPE {
        standard, insurance, maintainer,
    }

    // Each private variable represents a column in the users table
    // Optionally, you can use the @Column annotation, but it's unneeded

    // Since the email_address is used as the identifying factor, we use @Id annotation
    @Id
    private String emailAddress;

    private String displayName;
    private String passwordHash;

    // Account type is an enum, so it requires the @Enumerated annotation
    @Enumerated(EnumType.STRING)
    private TYPE accountType;

    // This is the User constructor
    public User(String email, String name, String pwHash, User.TYPE accountType){
        this.emailAddress = email;
        this.displayName = name;
        this.passwordHash = pwHash;
        this.accountType = accountType;
    }

    // And these are all just setters and getters for each variable. Typical OOP things
    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
