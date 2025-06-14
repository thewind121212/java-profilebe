/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.wliafdew.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.wliafdew.crypto.JwtTokenProvider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


/**
 *
 * @author kotomiichinose
 */
@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.base-url}")
    private String baseUrl;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine, JwtTokenProvider jwtTokenProvider) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void sendVerificationEmail(String toEmail) throws MessagingException {
        String token = jwtTokenProvider.generateEmailVerificationToken(toEmail);
        String verifyLink = baseUrl + "/verify?token=" + token;
        
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Verification Link: " + verifyLink);

        Context context = new Context();
        context.setVariable("verificationUrl", verifyLink);

        String htmlContent = templateEngine.process("email/verify-registration", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Email sent successfully");
    }
}
