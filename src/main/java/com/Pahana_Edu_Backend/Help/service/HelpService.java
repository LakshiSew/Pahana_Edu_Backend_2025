package com.Pahana_Edu_Backend.Help.service;

import com.Pahana_Edu_Backend.Help.entity.Help;
import java.util.List;

public interface HelpService {
    Help submitHelpMessage(Help help);
    Help replyToHelpMessage(String id, String reply);
    List<Help> getAllHelpMessages();
    Help getHelpMessageById(String id);
    List<Help> getHelpMessagesByEmail(String email);
}