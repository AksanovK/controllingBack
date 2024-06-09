package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.mailer.dto.StartParsingMessage;
import ru.itis.mailer.dto.StatusMessage;
import ru.itis.mailer.dto.LoadingMessage;
import ru.itis.mailer.services.SearchingService;

@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    private SearchingService searchingService;
    @MessageMapping("/loading/startParsing")
    @SendTo("/topic/searching")
    public StatusMessage parsing(StartParsingMessage startParsingMessage) throws Exception {
        Thread.sleep(1000);
        return searchingService.startParsing(startParsingMessage.getSubject(), startParsingMessage.getType());
    }

    @MessageMapping("/loading/startRanking")
    @SendTo("/topic/searching")
    public StatusMessage ranking(LoadingMessage message) throws Exception {
        Thread.sleep(1000);
        return searchingService.startRanking(message.getName());
    }

    @MessageMapping("/loading/startSearching")
    @SendTo("/topic/searching")
    public StatusMessage searching(LoadingMessage message) throws Exception {
        Thread.sleep(1000);
        return searchingService.startSearching(message.getName());
    }
}
