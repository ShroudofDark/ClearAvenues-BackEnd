
package edu.odu.clearavenues.prototype.location;

import edu.odu.clearavenues.prototype.accident.AccidentRepository;
import edu.odu.clearavenues.prototype.report.Report;
import edu.odu.clearavenues.prototype.report.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AccidentRepository accidentRepository;

    @GetMapping("")
    @ResponseBody
    public Iterable<Location> getAllLocations() {return locationRepository.findAll();}
    @PostMapping("{locationId}")
    @ResponseBody
    public void updateIntensityScore(@PathVariable("locationId") int locationId) {

        Location location = locationRepository.findByLocationId(locationId);

        final int combinedAccidentAndReportsLast7Days = reportRepository.getLast7DayReportCount(locationId)
                + accidentRepository.getLast7DayAccidentCount(locationId);

        if (combinedAccidentAndReportsLast7Days == 0)
            location.setIntensityScore(0);

        else if(combinedAccidentAndReportsLast7Days > 0 && combinedAccidentAndReportsLast7Days < 11)
            location.setIntensityScore(1);

        else if(combinedAccidentAndReportsLast7Days > 10 && combinedAccidentAndReportsLast7Days < 25)
            location.setIntensityScore(2);

        else
            location.setIntensityScore(3);

        locationRepository.save(location);
        }
}