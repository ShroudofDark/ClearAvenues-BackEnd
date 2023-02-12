package edu.odu.clearavenues.prototype.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// A "Controller" is the layer where HTTP requests are handled
@Controller
public class UserController {
    @Autowired

    // Lets you access the user database. Read UserRepository.java for more info
    private UserRepository userRepository;


    // Go to http://127.0.0.1:8080/allUsers in your browser. It will return JSON data with a list of all Users
    @GetMapping(path="/allUsers")
    @ResponseBody
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }


    // Go to http://127.0.0.1:8080/nativeSQLAllUsers?type=standard in your browser. It will return all users with a
    // Standard account type
    @GetMapping(path="/nativeSQLAllUsers")
    @ResponseBody
    public  Iterable<User> nativeSQLAllUsers(@RequestParam String type){
        // You can also use userRepository.findByAccountType(type) which is the auto implemented one
        return userRepository.nativeSQLFindByAccountType(type);
    }
}
