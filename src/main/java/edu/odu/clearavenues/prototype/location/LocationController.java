
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

    @GetMapping("{id}/latitude")
    @ResponseBody
    public double getHotspotLatitude(@PathVariable("id") int locationId) {

        Location location = locationRepository.findByLocationId(locationId);
        return location.getHotspotLat();
    }

    @GetMapping("{id}/longitude")
    @ResponseBody
    public double getHotspotLongitude(@PathVariable("id") int locationId) {

        Location location = locationRepository.findByLocationId(locationId);
        return location.getHotspotLong();
    }

    @GetMapping("")
    @ResponseBody
    public Iterable<Location> getAllLocations() {return locationRepository.findAll();}
    @PostMapping("update/{locationId}")
    @ResponseBody
    public void updateIntensityScore(@PathVariable("locationId") int locationId) {

        Location location = locationRepository.findByLocationId(locationId);

        //Changed to use the smallest value here to simulate "pairing" off an accident and report.
        //Aka in order to increase this count by one there needs to be at least one report and at least one accident.
        final int combinedAccidentAndReportsLast7Days = Math.min(reportRepository.getLast7DayReportCount(locationId),
                accidentRepository.getLast7DayAccidentCount(locationId));

        /*
        if (combinedAccidentAndReportsLast7Days == 0)
            location.setIntensityScore(0);

        else if(combinedAccidentAndReportsLast7Days > 0 && combinedAccidentAndReportsLast7Days < 5)
            location.setIntensityScore(1);

        else if(combinedAccidentAndReportsLast7Days > 4 && combinedAccidentAndReportsLast7Days < 10)
            location.setIntensityScore(2);

        else
            location.setIntensityScore(3);
        */
        location.setIntensityScore(combinedAccidentAndReportsLast7Days);
        locationRepository.save(location);
    }
}