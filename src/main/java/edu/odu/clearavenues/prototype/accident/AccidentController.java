package edu.odu.clearavenues.prototype.accident;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class AccidentController {

    @Autowired
    private AccidentRepository accidentRepository;

    @GetMapping("/importAccident")
    @ResponseBody
    public void importAccident(@RequestParam("accidentType") String accidentType, @RequestParam("latitude") int latitude, @RequestParam("longitude") int longitude,
                               @RequestParam("datetime") LocalDateTime datetime, @RequestParam("numInjuries") int numInjuries, @RequestParam("locationId") int locationId,
                               @RequestParam("fatal") boolean fatal) {

        Accident.Type type = Accident.Type.valueOf(accidentType.toUpperCase());
        Accident accident = new Accident(type, latitude, longitude, datetime, numInjuries, locationId, fatal);
        accidentRepository.save(accident);




    }

}
