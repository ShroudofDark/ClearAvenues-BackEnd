package edu.odu.clearavenues.prototype.report;

import edu.odu.clearavenues.prototype.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Entity
@Table(name = "reports")
public class Report {
    public enum Type {
        debris,
        vehicle_accident,
        vandalism,
        missing_signage,
        obstructed_sign,
        pothole,
        flooding,
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

    private double reportLocationLat;

    private double reportLocationLong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submitted_by")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User submittedBy;

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

    public Report(Type reportType, double latitude, double longitude, User email, String comment, int locationId){
        this.reportType = reportType;
        this.reportLocationLat = latitude;
        this.reportLocationLong = longitude;
        this.submittedBy = email;
        this.reportComment = comment;
        this.locationId = locationId;
        this.reportStatus = Status.submitted;
    }

    public int getReportId() {return reportId;}

    public double getReportLocationLatitude() {return reportLocationLat;}

    public double getReportLocationLongitude() {return reportLocationLong;}


    public User getSubmittedBy() {return submittedBy;}

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

    public void setReportLocationLat(double reportLocationLat) {
        this.reportLocationLat = reportLocationLat;
    }

    public void setReportLocationLong(double reportLocationLong) {
        this.reportLocationLong = reportLocationLong;
    }

    public void setReportType(Type reportType) {
        this.reportType = reportType;
    }

    public void voteOnReport(boolean vote) {

        if (vote) {
            reportScore++;
        }

        else {
            reportScore--;
        }
    }
}
