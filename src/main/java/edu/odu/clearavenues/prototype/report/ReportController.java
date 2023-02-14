package edu.odu.clearavenues.prototype.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/reports")
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

    /* Ignore this for now
    @PostMapping("/reports")
    public ResponseEntity<Report> addUser(@RequestBody Report newReport){
        Report report = reportRepository.save(newReport);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    } */
}
