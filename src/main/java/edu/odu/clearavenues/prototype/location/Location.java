package edu.odu.clearavenues.prototype.location;
import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    private int intensityScore;

    private double hotspotLat;

    private double hotspotLong;

    public Location() {}

    public Location(int id) {

        locationId = id;
        intensityScore = 0;
        // Intensity score defaults to null in DB if nothing is specified

    }

    public Location(int id, int score, double latitude, double longitude) {

        locationId = id;
        intensityScore = score;
        hotspotLat = latitude;
        hotspotLong = longitude;
    }

    public int getLocationId() {return locationId;}

    public int getIntensityScore() {return intensityScore;}

    public double getHotspotLat() {return hotspotLat;}

    public double getHotspotLong() {return hotspotLong;}

    public void setIntensityScore(int score) {intensityScore = score;}
}
