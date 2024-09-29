package net.engineeringdigest.journalApp.services;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryServices {
    //business logic

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserServices userServices;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
       try{
           User user = userServices.findByUsername(userName);
           journalEntry.setDate(LocalDateTime.now());
           journalEntryRepository.save(journalEntry);
           JournalEntry saved = journalEntryRepository.save(journalEntry);
           user.getJournalEntries().add(saved);
           userServices.saveUser(user);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }


    public void deleteEntry(ObjectId id, String username) {
        User user = userServices.findByUsername(username);
        user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userServices.saveNewUser(user);
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry updateEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);

    }



}
