package com.Pahana_Edu_Backend.Help.controller;

import com.Pahana_Edu_Backend.Help.entity.Help;
import com.Pahana_Edu_Backend.Help.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
public class HelpController {

    @Autowired
    private HelpService helpService;

    @PostMapping("/auth/submit")
    public ResponseEntity<Help> submitHelpMessage(@RequestBody Help help) {
        help.setSubmittedAt(LocalDateTime.now());
        Help savedHelp = helpService.submitHelpMessage(help);
        return ResponseEntity.ok(savedHelp);
    }

    @PostMapping("/help/{id}")
    public ResponseEntity<?> replyToHelpMessage(
            @PathVariable("id") String id,
            @RequestParam("reply") String reply) {
        try {
            Help updatedHelp = helpService.replyToHelpMessage(id, reply);
            return ResponseEntity.ok(updatedHelp);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Help message with ID " + id + " not found");
        }
    }

    @GetMapping("/auth/getall")
    public ResponseEntity<List<Help>> getAllHelpMessages() {
        List<Help> helpMessages = helpService.getAllHelpMessages();
        return ResponseEntity.ok(helpMessages);
    }

    @GetMapping("/auth/{id}")
    public ResponseEntity<Help> getHelpMessageById(@PathVariable("id") String id) {
        Help help = helpService.getHelpMessageById(id);
        if (help != null) {
            return ResponseEntity.ok(help);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/auth/email/{email}")
    public ResponseEntity<List<Help>> getHelpMessagesByEmail(@PathVariable("email") String email) {
        List<Help> helpMessages = helpService.getHelpMessagesByEmail(email);
        return ResponseEntity.ok(helpMessages);
    }
}