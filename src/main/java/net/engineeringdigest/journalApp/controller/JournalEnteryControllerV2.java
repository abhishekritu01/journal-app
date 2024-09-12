package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryServices;
import net.engineeringdigest.journalApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEnteryControllerV2 {


    @Autowired
    private JournalEntryServices journalEntryServices;

    @Autowired
    private UserServices userServices;


    @GetMapping("{username}")
    private ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(@PathVariable String username) {

        // find user by username
        User user = userServices.findByUsername(username);

        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (journalEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    private ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryServices.findById(id);

        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    private ResponseEntity<JournalEntry> createEntery(@RequestBody JournalEntry myJournalEntry, @PathVariable String username) {
        try{
            journalEntryServices.saveEntry(myJournalEntry, username);
            return new ResponseEntity<>(myJournalEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("id/{username}/{id}")
    private ResponseEntity<?> updateEntery(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry, @PathVariable String username) {
        JournalEntry old= journalEntryServices.findById(id).orElse(null);
        if (old != null) {
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    @DeleteMapping("id/{username}/{id}")
    private ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String username) {
        journalEntryServices.deleteEntry(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
