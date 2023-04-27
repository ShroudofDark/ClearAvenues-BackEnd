package edu.odu.clearavenues.prototype.report;

import edu.odu.clearavenues.prototype.location.Location;
import edu.odu.clearavenues.prototype.location.LocationRepository;
import edu.odu.clearavenues.prototype.user.User;
import edu.odu.clearavenues.prototype.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;



    @GetMapping("/reports")
    @ResponseBody
    public Iterable<Report> getAllReports(){
        return reportRepository.findAll();
    }


    // Return a report given its id
    @GetMapping("/reports/{id}")
    @ResponseBody
    public Optional<Report> getReportById(@PathVariable("id") int id){
        return reportRepository.findById(id);
    }

    @DeleteMapping("/reports/{id}")
    @ResponseBody
    public void deleteReportById(@PathVariable("id") int id){
        reportRepository.deleteById(id);
    }

    // Return all reports made by a specific user
    @GetMapping(path = "/users/{email}/reports")
    @ResponseBody
    public List<Report> getReportsByUser(@PathVariable("email") String email){
        List<Report> reports;
        reports = reportRepository.findBySubmitter(email);
        return reports;
    }

    @GetMapping(path = "/users/{email}/reports/{status}")
    @ResponseBody
    public List<Report> getReportsByUserAndStatus(@PathVariable("email") String email, @PathVariable String status){

        List<Report> reports;

        reports = reportRepository.getReportsBySubmitterAndStatus(email, status);
        return reports;
    }

    @GetMapping("/reports/bylocation/{locationId}")
    @ResponseBody
    public List<Report> getReportsByLocationId(@PathVariable("locationId") int locationId) {
        List<Report> reports;
        reports = reportRepository.findByLocationId(locationId);
        return reports;
    }

    @PostMapping("/users/{email}/reports")
    @ResponseBody
    public void createReport(HttpServletRequest request, @PathVariable("email") String email, @RequestParam("reportType") String reportType, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude,
                             @RequestParam("comment") String comment, @RequestParam("locationId") int locationId, @RequestParam("imageString") Optional<String> image, @RequestParam("date")Optional<String> date) {
        Report report;
        Report.Type type = Report.Type.valueOf(reportType);
        User user =  userRepository.findByEmailAddress(email);
        Location location = locationRepository.findByLocationId(locationId);

        // The report generator on frontend doesn't submit with images so we can include it in this check
        if (image.isEmpty()) {
            if (date.isEmpty()) {
                report = new Report(type, latitude, longitude, user, comment, location);
            } else {
                LocalDateTime parsedDate = LocalDateTime.parse(date.get().replaceAll("Z", ""));
                report = new Report(type, latitude, longitude, user, comment, location, parsedDate);
            }
        }
        else {
            report = new Report(type, latitude, longitude, user, comment, location, image.get());
        }
        reportRepository.save(report);
    }




    // Allows a user to edit an existing report (incomplete, haven't tested yet)
    // Probably don't need all these fields to be editable though
    @PutMapping("/users/{email}/reports/{id}")
    @ResponseBody
    public ResponseEntity<String> editReport(@PathVariable("email") String email, @RequestParam("reportType") String reportType, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude,
                           @RequestParam("comment") String comment, @RequestParam("locationId") int locationId, @PathVariable("id") int reportId) {
        Report report;
        Optional<Report> specifiedReport = reportRepository.findById(reportId);
        if (specifiedReport.isPresent()) {
            report = specifiedReport.get();
            report.setReportComment(comment);
            report.setReportLocationLat(latitude);
            report.setReportLocationLong(longitude);
            report.setReportType(Report.Type.valueOf(reportType));
            reportRepository.save(report);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/reports/{id}/editComment")
    @ResponseBody
    public void editComment(@PathVariable("id") int reportId, @RequestParam("comment") String comment) {

        Report report = reportRepository.findByReportId(reportId);
        report.setReportComment(comment);
        reportRepository.save(report);
    }

    @GetMapping("/reports/{id}/resolve")
    @ResponseBody
    public void resolveReport(@PathVariable("id") int reportId, @RequestParam("resolvedBy") String email) {

        Report report = reportRepository.findByReportId(reportId);
        //report.setResolvedBy(email);
        User user =  userRepository.findByEmailAddress(email);
        report.setResolvedBy(user);
        LocalDateTime date = LocalDateTime.now();
        report.setResolutionDate(date);
        report.setStatus(Report.Status.closed);

        reportRepository.save(report);
    }

    @GetMapping("/reports/{id}/vote")
    @ResponseBody
    public void reportVote(@PathVariable("id") int reportId, @RequestParam("vote") boolean vote) {

        Report report = reportRepository.findByReportId(reportId);
        report.voteOnReport(vote);
        reportRepository.save(report);
    }



    /* Ignore this for now
    @PostMapping("/reports")
    public ResponseEntity<Report> addUser(@RequestBody Report newReport){
        Report report = reportRepository.save(newReport);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    } */
}
