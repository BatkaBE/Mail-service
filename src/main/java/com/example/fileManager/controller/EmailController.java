package com.example.fileManager.controller;

import com.example.fileManager.dto.EmailRequest;
import com.example.fileManager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            log.info("Attempting to send email to: {}", emailRequest.getTo());

            emailService.sendShrekEmailWithAttachments(
                    emailRequest.getTo(),
                    emailRequest.getSubject(),
                    emailRequest.getMessage()
            );

            log.info("Email sent successfully to: {}", emailRequest.getTo());
            return ResponseEntity.ok("Mail amjilttai yvlaa");

        } catch (MessagingException e) {
            log.error("Email messaging error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Email sending failed: Messaging error - " + e.getMessage());

        } catch (IOException e) {
            log.error("File operation error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Email sending failed: File error - " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Email sending failed: Unexpected error - " + e.getMessage());
        }
    }
}