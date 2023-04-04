package edu.odu.clearavenues.prototype.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.odu.clearavenues.prototype.organization.Organization;
import jakarta.persistence.*;


// This is where you construct create the User object in accordance with the database structure
@Entity
@Table(name = "users")
public class User {

    // Default constructor
    protected User() {}
    public enum TYPE {
        standard, institute, municipality, admin
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    // Name of the foreign key column
    @JoinColumn(name = "organization")
    @JsonIgnore
    private Organization organization;

    private boolean orgApproved;


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

    public Organization getOrganization() { return organization; }

    public boolean isOrgApproved() { return orgApproved; }

    public String getAccountType() { return String.valueOf(accountType);}

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAccountType(String accountType) { this.accountType = TYPE.valueOf(accountType); }

    public void setOrganization(Organization orgName) { this.organization = orgName; }

    public void setOrgApproved() { this.orgApproved = true; }
}