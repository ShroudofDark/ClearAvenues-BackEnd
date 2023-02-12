package edu.odu.clearavenues.prototype.report;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ReportController {
    private ReportRepository reportRepository;

    @GetMapping("/reports")
    @ResponseBody
    public Optional<Report> getReportById(@RequestParam int id){
        return reportRepository.findById(id);
    }

    /* Ignore this for now
    @PostMapping("/reports")
    public ResponseEntity<Report> addUser(@RequestBody Report newReport){
        Report report = reportRepository.save(newReport);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    } */
}
