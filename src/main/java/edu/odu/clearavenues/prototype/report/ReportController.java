package edu.odu.clearavenues.prototype.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/getReport")
    @ResponseBody
    public Optional<Report> getReportById(@RequestParam int id){
        return reportRepository.findById(id);
    }

    @GetMapping("/createReport")
    @ResponseBody
    public void createReport(@RequestParam("reportType") String reportType, @RequestParam("latitude") int latitude, @RequestParam("longitude") int longitude,
                             @RequestParam("submittedBy") String submittedBy, @RequestParam("comment") String comment, @RequestParam("locationId") int locationId) {

        Report.Type type = Report.Type.valueOf(reportType.toUpperCase());
        Report report = new Report(type, latitude, longitude, submittedBy, comment, locationId);
        reportRepository.save(report);
    }

    @GetMapping("/editComment")
    @ResponseBody
    public void editComment(@RequestParam("reportId") int reportId, @RequestParam("comment") String comment) {

        Report report = reportRepository.findByReportId(reportId);
        report.setReportComment(comment);
        reportRepository.save(report);
    }

    @GetMapping("/resolveReport")
    @ResponseBody
    public void resolveReport(@RequestParam("reportId") int reportId, @RequestParam("resolvedBy") String email) {

        Report report = reportRepository.findByReportId(reportId);
        report.setResolvedBy(email);
        LocalDateTime date = LocalDateTime.now();
        report.setResolutionDate(date);
        report.setReportStatus(Report.Status.closed);

        reportRepository.save(report);
    }

    @GetMapping("/reportVote")
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
