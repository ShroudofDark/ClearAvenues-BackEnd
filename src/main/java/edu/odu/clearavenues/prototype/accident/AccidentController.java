package edu.odu.clearavenues.prototype.accident;

import edu.odu.clearavenues.prototype.location.Location;
import edu.odu.clearavenues.prototype.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/accidents")
public class AccidentController {

    @Autowired
    private AccidentRepository accidentRepository;

    @PostMapping("new")
    @ResponseBody
    public void importAccident(@RequestParam("accidentType") String accidentType, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude,
                               @RequestParam("datetime") LocalDateTime datetime, @RequestParam("numInjuries") int numInjuries, @RequestParam("locationId") int locationId,
                               @RequestParam("fatal") boolean fatal) {

        Accident.Type type = Accident.Type.valueOf(accidentType.toUpperCase());
        Accident accident = new Accident(type, latitude, longitude, datetime, numInjuries, locationId, fatal);
        accidentRepository.save(accident);
    }

    @GetMapping("")
    @ResponseBody
    public Iterable<Accident> getAllAccidents() {return accidentRepository.findAll();}

    @GetMapping("/bylocation/{locationId}")
    @ResponseBody
    public List<Accident> getAccidentsByLocationId(@PathVariable("locationId") int locationId) {
        List<Accident> accidents;
        accidents = accidentRepository.findByLocationId(locationId);
        return accidents;
    }
}
