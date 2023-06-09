package edu.odu.clearavenues.prototype.user;

import edu.odu.clearavenues.prototype.accident.Accident;
import edu.odu.clearavenues.prototype.organization.Organization;
import edu.odu.clearavenues.prototype.organization.OrganizationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.mysql.cj.MysqlType.NULL;

// A "Controller" is the layer where HTTP requests are handled
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired

    // Lets you access the user database. Read UserRepository.java for more info
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;


    // Go to http://127.0.0.1:8080/allUsers in your browser. It will return JSON data with a list of all Users
    @GetMapping
    @ResponseBody
    public Iterable<User> getAllUsers(@RequestParam Optional<String> account_type){
        if (account_type.isPresent()){
            User.TYPE accountType = User.TYPE.valueOf(account_type.get());
            return userRepository.findByAccountType(accountType);
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
    @GetMapping("/nativeSQLAllUsers")
    @ResponseBody
    public  Iterable<User> nativeSQLAllUsers(@RequestParam String type){
        // You can also use userRepository.findByAccountType(type) which is the auto implemented one
        return userRepository.nativeSQLFindByAccountType(type);
    }

    /* Reminder for later to consider defaulting account_type to standard for all new users and
       creating a separate function to change a user's account type which will require an existing administrator's
       username and password to do
     */

    @PostMapping("new")
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

    @PostMapping("resetPassword")
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

    @GetMapping("login")
    @ResponseBody
    public boolean userLogin(@RequestParam("email_address") String email_address, @RequestParam("password") String password) {

        try {
            User user = userRepository.findByEmailAddress(email_address);
            return user.getPasswordHash().equals(hashString(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Need functions to add user to organization, remove user from organization, etc?

    @PostMapping("{user}/addtoOrg/{organization}")
    @ResponseBody
    public boolean addUserToOrg(@PathVariable("organization") String orgName, @PathVariable("user") String email) {

        User user = userRepository.findByEmailAddress(email);
        if(user != null) {
            Organization organization = organizationRepository.findByOrgName(orgName);
            if(organization != null) {
                user.setOrganization(organization);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    @PostMapping("{user}/removeOrg")
    @ResponseBody
    public boolean removeUserFromOrg(@PathVariable("user") String email) {

        User user = userRepository.findByEmailAddress(email);
        if (user != null) {
            User update = new User(user.getEmailAddress(), user.getDisplayName(), user.getPasswordHash(), User.TYPE.valueOf(user.getAccountType()));
            userRepository.save(update);
            return true;
        }
        return false;
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
