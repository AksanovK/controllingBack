package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.dto.InstructionsResponse;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.User;
import ru.itis.mailer.services.AuthService;
import ru.itis.mailer.services.InstructionsService;

import java.util.List;

@RequestMapping("/instructions")
@RestController
public class InstructionsController {
    @Autowired
    private InstructionsService instructionsService;

    @GetMapping("/get")
    public ResponseEntity<InstructionsResponse> getInstructionParams(@RequestParam Long userId) {
        return ResponseEntity.ok(instructionsService.getInstructions(userId));
    }

    @PostMapping("/put")
    public ResponseEntity<String> sendMailingInstructions() {
        return ResponseEntity.ok("success");
    }
}
