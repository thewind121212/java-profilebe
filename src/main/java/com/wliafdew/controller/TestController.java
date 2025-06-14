package com.wliafdew.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wliafdew.service.MailService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send-email")
    public String testEmail() {
        try {
            mailService.sendVerificationEmail("wliafdew@gmail.com", UUID.fromString("0daf4b19-d69e-43c5-b899-7c93e4e6a164"));
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
} 