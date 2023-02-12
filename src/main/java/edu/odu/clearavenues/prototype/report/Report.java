package edu.odu.clearavenues.prototype.report;

import jakarta.persistence.*;


@Entity
public class Report {
    public enum Type {
        SIGN,
        ACCIDENT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Type type;

}
