package edu.odu.clearavenues.prototype.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

// A "Controller" is the layer where HTTP requests are handled
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired

    // Lets you access the user database. Read UserRepository.java for more info
    private UserRepository userRepository;


    // Go to http://127.0.0.1:8080/allUsers in your browser. It will return JSON data with a list of all Users
    @GetMapping
    @ResponseBody
    public Iterable<User> getAllUsers(@RequestParam Optional<String> account_type){
        if (account_type.isPresent()){
            return userRepository.findByAccountType("admin");
        }
        else {
            return userRepository.findAll();
        }
    }
    @GetMapping("{email}")
    @ResponseBody
    public User getUser(@PathVariable("email") String email){
        return userRepository.findByEmailAddress(email);
    }


    // Go to http://127.0.0.1:8080/nativeSQLAllUsers?type=standard in your browser. It will return all users with a
    // Standard account type
    @GetMapping(path="/nativeSQLAllUsers")
    @ResponseBody
    public  Iterable<User> nativeSQLAllUsers(@RequestParam String type){
        // You can also use userRepository.findByAccountType(type) which is the auto implemented one
        return userRepository.nativeSQLFindByAccountType(type);
    }

    /* Reminder for later to consider defaulting account_type to standard for all new users and
       creating a separate function to change a user's account type which will require an existing administrator's
       username and password to do
     */

    @PostMapping(path = "new")
    @ResponseBody
    public void createUser(@RequestParam("email_address") String email_address,@RequestParam("display_name") String display_name,
                           @RequestParam("password") String password, @RequestParam("account_type") String account_type){

        try {
            User.TYPE type = User.TYPE.valueOf(account_type);
            String hashedPw = hashString(password);
            User user = new User(email_address, display_name, hashedPw, type);
            userRepository.save(user);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/resetPassword")
    @ResponseBody
    public void resetPassword(@RequestParam("email_address") String email_address, @RequestParam("oldPassword") String oldPassword,
                              @RequestParam("newPassword") String newPassword) {

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

    @GetMapping(value = "/userLogin")
    @ResponseBody
    public boolean userLogin(@RequestParam("email_address") String email_address, @RequestParam("password") String password) {

        try {
            User user = userRepository.findByEmailAddress(email_address);
            return user.getPasswordHash().equals(hashString(password));
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
