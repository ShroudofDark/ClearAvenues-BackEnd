package edu.odu.clearavenues.prototype.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    @ResponseBody
    public void createUser(@RequestParam("email_address") String email_address,@RequestParam("display_name") String display_name,@RequestParam("password") String password, @RequestParam("account_type") User.TYPE account_type){

        try {
            String hashedPw = hashString(password);
            User user = new User(email_address, display_name, hashedPw, account_type);
            userRepository.save(user);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    @ResponseBody
    public void resetPassword(@RequestParam("email_address") String email_address, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {

        //oldHashedPassword = hashString(oldPassword);
        User user = userRepository.findByEmailAddress(email_address);
        try {
            if(user.getPasswordHash().equals(hashString(oldPassword)))
            {
                user.setPasswordHash(hashString(newPassword));
                userRepository.save(user);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }

    private @NotNull String hashString(final @NotNull String toHash) throws NoSuchAlgorithmException {

        // Hash text string

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toHash.getBytes());
        final byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();

    }

}
