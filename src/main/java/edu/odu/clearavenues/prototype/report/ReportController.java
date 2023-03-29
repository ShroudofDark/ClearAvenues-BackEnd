package edu.odu.clearavenues.prototype.report;

import edu.odu.clearavenues.prototype.user.User;
import edu.odu.clearavenues.prototype.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;



    @GetMapping()
    @ResponseBody
    public Iterable<Report> getAllReports(){
        return reportRepository.findAll();
    }

    @GetMapping("{id}")
    @ResponseBody
    public Optional<Report> getReportById(@PathVariable("id") int id){
        return reportRepository.findById(id);
    }

    @GetMapping(path = "/users/{email}/reports")
    @ResponseBody
    public List<Report> getReportsByUser(@PathVariable("email") String email){
        List<Report> reports;
        reports = reportRepository.findBySubmittedBy(email);
        return reports;
    }

    @PostMapping("/users/{email}/reports")
    @ResponseBody
    public void createReport(HttpServletRequest request, @PathVariable("email") String email, @RequestParam("reportType") String reportType, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude,
                             @RequestParam("comment") String comment, @RequestParam("locationId") int locationId) {
        Report report;
        Report.Type type = Report.Type.valueOf(reportType);
        User user =  userRepository.findByEmailAddress(email);
        report = new Report(type, latitude, longitude, user, comment, locationId);
        reportRepository.save(report);
    }

    @PostMapping("/users/{email}/reports/{id}")
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
    @GetMapping("editComment")
    @ResponseBody
    public void editComment(@RequestParam("reportId") int reportId, @RequestParam("comment") String comment) {

        Report report = reportRepository.findByReportId(reportId);
        report.setReportComment(comment);
        reportRepository.save(report);
    }

    @GetMapping("resolve")
    @ResponseBody
    public void resolveReport(@RequestParam("reportId") int reportId, @RequestParam("resolvedBy") String email) {

        Report report = reportRepository.findByReportId(reportId);
        report.setResolvedBy(email);
        LocalDateTime date = LocalDateTime.now();
        report.setResolutionDate(date);
        report.setReportStatus(Report.Status.closed);

        reportRepository.save(report);
    }

    @GetMapping("vote")
    @ResponseBody
    public void reportVote(@RequestParam("reportId") int reportId, @RequestParam("vote") boolean vote) {

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
