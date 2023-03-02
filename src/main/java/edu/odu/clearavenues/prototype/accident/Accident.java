package edu.odu.clearavenues.prototype.accident;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "accidents")
public class Accident {

    public enum Type {
        SINGLE_VEHICLE,
        MULTI_VEHICLE,
        VEHICLE_PEDESTRIAN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accidentId;

    @Enumerated(EnumType.STRING)
    private Type accidentType;

    private int accidentLocationLat;

    private int accidentLocationLong;

    @CreationTimestamp
    private LocalDateTime accidentTime;

    private int numInjuries;

    private int locationId;

    private boolean fatal;

    // format for LocalDateTime is YYYY-MM-DDTHH:MM:SS with the time being 24-hour format
    public Accident(Type type, int latitude, int longitude, LocalDateTime datetime, int numInjuries, int locationId, boolean fatal) {
        this.accidentType = type;
        this.accidentLocationLat = latitude;
        this.accidentLocationLong = longitude;
        this.accidentTime = datetime;
        this.numInjuries = numInjuries;
        this.locationId = locationId;
        this.fatal = fatal;
    }

    public String getAccidentType() { return String.valueOf(accidentType); }

    public int getAccidentLocationLat() { return accidentLocationLat; }

    public int getAccidentLocationLong() { return accidentLocationLong; }

    public LocalDateTime getAccidentTime() { return accidentTime;}

    public int getNumInjuries() { return numInjuries; }

    public int getLocationId() { return locationId; }

    public int getAccidentId() { return accidentId; }
}


