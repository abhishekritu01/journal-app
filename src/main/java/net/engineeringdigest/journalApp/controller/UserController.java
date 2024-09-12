package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    public List<User> getAllUsers() {
        return userServices.getAllEntries();
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userServices.saveEntry(user);
    }


    @PutMapping("/{username}")
    public ResponseEntity<?> userUpdate(@RequestBody User user,@PathVariable String username) {
        User userInDb = userServices.findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userServices.saveEntry(userInDb);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable ObjectId id) {
        userServices.deleteById(id);

    }


}
