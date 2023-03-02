package edu.odu.clearavenues.prototype.report;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "reports")
public class Report {
    public enum Type {
        debris,
        vehicle_accident,
        vandalism,
        missing_signage,
        pothole,
        other
    }

    public enum Status {
        submitted,
        in_progress,
        closed
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;

    @Enumerated(EnumType.STRING)
    private Type reportType;

    private int reportLocationLat;

    private int reportLocationLong;

    private String submittedBy;

    private String reportComment;

    @CreationTimestamp
    private LocalDateTime reportDate;

    private int reportScore;

    private LocalDateTime resolutionDate;

    private String resolvedBy;

    private int locationId;

    @Enumerated(EnumType.STRING)
    private Status reportStatus;

    // Default constructor required. Was receiving error about it missing when using one of the functions.
    public Report() {}

    public Report(Type reportType, int latitude, int longitude, String email, String comment, int locationId){
        this.reportType = reportType;
        this.reportLocationLat = latitude;
        this.reportLocationLong = longitude;
        this.submittedBy = email;
        this.reportComment = comment;
        this.locationId = locationId;
        this.reportStatus = Status.submitted;
    }

    public int getReportId() {return reportId;}

    public int getReportLocationLatitude() {return reportLocationLat;}

    public int getReportLocationLongitude() {return reportLocationLong;}

    public String getSubmittedBy() {return submittedBy;}

    public String getReportComment() {return reportComment;}

    public LocalDateTime getReportDate() {return reportDate;}

    public int getReportScore() {return reportScore;}

    public LocalDateTime getResolutionDate() {return resolutionDate;}

    public String getResolvedBy() {return resolvedBy;}

    public int getLocationId() {return locationId;}

    public void setReportComment(String comment) {this.reportComment = comment;}

    public void setResolutionDate(LocalDateTime date) {this.resolutionDate = date;}

    public void setResolvedBy(String email) {this.resolvedBy = email;}

    public void setReportStatus(Status status) {this.reportStatus = status;}

    public void voteOnReport(boolean vote) {

        if (vote) {
            reportScore++;
        }

        else {
            reportScore--;
        }
    }
}
