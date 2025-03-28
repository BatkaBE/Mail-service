package com.example.fileManager.controller;

import com.example.fileManager.dto.EmailRequest;
import com.example.fileManager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendShrekEmailWithAttachments(
                    emailRequest.getTo(),
                    emailRequest.getSubject(),
                    emailRequest.getMessage()
            );
            return "Mail amjilttai yvlaa";
        } catch (IOException e) {
            log.error("Suljeenii aldaa: {}", e.getMessage(), e);
            return "Email sending failed: IO error.";
        } catch (Exception e) {
            log.error("Unexpected error while sending email: {}", e.getMessage(), e);
            return "Email sending failed: Unexpected error.";
        }
    }
}
