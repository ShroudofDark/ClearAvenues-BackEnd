package edu.odu.clearavenues.prototype.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// A "Controller" is the layer where HTTP requests are handled
@Controller
public class GeneralController {
    @GetMapping("/")
    @ResponseBody
    public String rootPage(){
        return "Clear Avenues Backend Server";
    }

}
