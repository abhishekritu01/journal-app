package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_test")
public class TestNote {

    private Map<Long, JournalEntry> journalEntryMap = new HashMap<>();


    @GetMapping()
    private List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntryMap.values());
    }

    @GetMapping("id/{id}")
    private JournalEntry getEntryById(@PathVariable long id){
        return journalEntryMap.get(id);
    }

//    @PostMapping()
//    private boolean createEntery(@RequestBody JournalEntry myJournalEntry){
//        journalEntryMap.put(myJournalEntry.getId(), myJournalEntry);
//        return true;
//    }
//
//    @PutMapping("id/{id}")
//    private boolean updateEntery(@RequestBody JournalEntry myJournalEntry){
//        journalEntryMap.put(myJournalEntry.getId(), myJournalEntry);
//        return true;
//    }

    @DeleteMapping("id/{id}")
    private boolean deleteEntry(@PathVariable long id){
        journalEntryMap.remove(id);
        return true;
    }

}
