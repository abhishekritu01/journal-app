package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryServices;
import net.engineeringdigest.journalApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journal")
public class JournalEnteryControllerV2 {


    @Autowired
    private JournalEntryServices journalEntryServices;

    @Autowired
    private UserServices userServices;


    @GetMapping
    private ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userServices.findByUsername(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    private ResponseEntity<JournalEntry> createEntery(@RequestBody JournalEntry myJournalEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryServices.saveEntry(myJournalEntry, userName);
            return new ResponseEntity<>(myJournalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("id/{id}")
    private ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(id)).collect(Collectors.toList());
        if (collect != null && !collect.isEmpty()) {
            Optional<JournalEntry> entry = journalEntryServices.findById(id);
            if (entry.isPresent()) {
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("id/{id}")
    private ResponseEntity<JournalEntry> deleteEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(id)).collect(Collectors.toList());
        if (collect != null && !collect.isEmpty()) {
            journalEntryServices.deleteEntry(id, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("id/{id}")
    private ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(journalEntry1 -> journalEntry1.getId().equals(id)).collect(Collectors.toList());
        if (collect != null && !collect.isEmpty()) {
            journalEntry.setId(id);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry updated = journalEntryServices.updateEntry(journalEntry);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
