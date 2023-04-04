package edu.odu.clearavenues.prototype.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.odu.clearavenues.prototype.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {

    // Organization name
    @Id
    private String orgName;

    // Organization's state of jurisdiction. Limited to 2 character abbreviation; eg. VA, MD, DC, CA, etc.
    private String state;

    public Organization(String name, String location) {

        orgName = name;
        state = location;
    }
}
