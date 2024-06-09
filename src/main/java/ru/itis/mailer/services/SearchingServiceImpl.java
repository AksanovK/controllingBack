package ru.itis.mailer.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.MessageKafkaDto;
import ru.itis.mailer.dto.SearchingResultMessage;
import ru.itis.mailer.models.SchoolBoy;
import ru.itis.mailer.dto.SearchingMessage;
import ru.itis.mailer.dto.StatusMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchingServiceImpl implements SearchingService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final String PARSING_TOPIC = "start_parsing_topic";
    private static final String RANKING_TOPIC = "start_ranking_topic";
    private static final String SEARCHING_TOPIC = "start_searching_topic";
    private static final String PARSING_RESULTS_TOPIC = "parsing_results_topic";
    private static final String RANKING_RESULTS_TOPIC = "ranking_results_topic";
    private static final String SEARCHING_RESULTS_TOPIC = "searching_results_topic";

    @Override
    public StatusMessage startParsing(String subject, String type) {
        String message = constructMessage(subject, type);
        kafkaTemplate.send(PARSING_TOPIC, message);
        return new StatusMessage("Parsing started for " + subject + " of type " + type, "");
    }

    @Override
    public StatusMessage startRanking(String filename) {
        kafkaTemplate.send(RANKING_TOPIC, filename);
        return new StatusMessage("Ranking started with data", filename);
    }

    @Override
    public StatusMessage startSearching(String filename) {
        kafkaTemplate.send(SEARCHING_TOPIC, filename);
        return new StatusMessage("Searching started", filename);
    }

    @KafkaListener(topics = PARSING_RESULTS_TOPIC)
    public void handleParsingResults(String message) {
        Gson gson = new Gson();
        StatusMessage statusMessage = gson.fromJson(message, StatusMessage.class);
        messagingTemplate.convertAndSend("/topic/searching", statusMessage);
    }

    @KafkaListener(topics = RANKING_RESULTS_TOPIC)
    public void handleRankingResults(String message) {
        Gson gson = new Gson();
        StatusMessage statusMessage = gson.fromJson(message, StatusMessage.class);
        messagingTemplate.convertAndSend("/topic/searching", statusMessage);
    }

    @KafkaListener(topics = SEARCHING_RESULTS_TOPIC)
    public void handleSearchingResults(String message) {
        Gson gson = new Gson();
        SearchingMessage searchingMessage = gson.fromJson(message, SearchingMessage.class);
        if ("completed".equals(searchingMessage.getStatus())) {
            String filePath = searchingMessage.getFileName();
            List<SchoolBoy> results = parseResultsFromFile(filePath);
            SearchingResultMessage resultMessage = SearchingResultMessage.builder()
                    .status("Completed")
                    .result(results)
                    .build();
            messagingTemplate.convertAndSend("/topic/searching", resultMessage);
        } else {
            SearchingResultMessage resultMessage = SearchingResultMessage.builder()
                    .status("Canceled")
                    .result(new ArrayList<>())
                    .build();
            messagingTemplate.convertAndSend("/topic/searching", resultMessage);
        }
    }

    private String constructMessage(String subject, String type) {
        Map<String, String> message = new HashMap<>();
        message.put("subject", subject);
        message.put("type", type);
        return new Gson().toJson(message);
    }

    private List<SchoolBoy> parseResultsFromFile(String filePath) {
        List<SchoolBoy> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "Name: (.*?), City: (.*?), School: (.*?), Grade: (\\d+), Score: ([\\d.]+), Subjects: \\[(.*?)\\], VK Profiles: (.*)");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    SchoolBoy schoolBoy = SchoolBoy.builder()
                            .name(matcher.group(1))
                            .city(matcher.group(2))
                            .school(matcher.group(3))
                            .grade(matcher.group(4))
                            .score(matcher.group(5))
                            .subject(matcher.group(6))
                            .vk(matcher.group(7))
                            .build();
                    results.add(schoolBoy);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return results;
    }
}
