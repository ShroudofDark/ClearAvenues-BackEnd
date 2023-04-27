package edu.odu.clearavenues.prototype.accident;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "accidents")
public class Accident {

    public enum Type {
        single_vehicle,
        multi_vehicle,
        vehicle_pedestrian
    }

    public Accident() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accidentId;

    @Enumerated(EnumType.STRING)
    private Type accidentType;

    private double accidentLocationLat;

    private double accidentLocationLong;

    @CreationTimestamp
    private LocalDateTime accidentTime;

    private int numInjuries;

    /* Commented out for same reason as what is detailed in the comment for locationId in reports class */
    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    // Name of the foreign key column
    @JoinColumn(name = "location_id")
    @JsonIgnore */
    private int locationId;

    private boolean fatal;

    // format for LocalDateTime is YYYY-MM-DDTHH:MM:SS with the time being 24-hour format
    public Accident(Type type, double latitude, double longitude, LocalDateTime datetime, int numInjuries, int locationId, boolean fatal) {
        this.accidentType = type;
        this.accidentLocationLat = latitude;
        this.accidentLocationLong = longitude;
        this.accidentTime = datetime;
        this.numInjuries = numInjuries;
        this.locationId = locationId;
        this.fatal = fatal;
    }

    public String getAccidentType() { return String.valueOf(accidentType); }

    public double getAccidentLocationLat() { return accidentLocationLat; }

    public double getAccidentLocationLong() { return accidentLocationLong; }

    public LocalDateTime getAccidentTime() { return accidentTime;}

    public int getNumInjuries() { return numInjuries; }

    public int getLocationId() { return locationId; }

    public int getAccidentId() { return accidentId; }
}