package edu.odu.clearavenues.prototype.locations;
import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    private int intensityScore;

    public Location() {}

    public Location(int id) {

        locationId = id;
        // Intensity score defaults to null in DB if nothing is specified
    }

    public Location(int id, int score) {

        locationId = Id;
        intensityScore = score;
    }

    public int getLocationId() {return locationId;}

    public int getIntensityScore() {return intensityScore;}

    public void setIntensityScore(int score) {intensityScore = score;}
}
