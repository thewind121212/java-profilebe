package com.wliafdew.app;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.wliafdew.controller.TodoController;
import com.wliafdew.repo.Database;
import com.wliafdew.repo.TodoRepository;
import com.wliafdew.service.MailService;

import jakarta.mail.MessagingException;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Database database(Environment env) {
        Database db = new Database(env);
        db.connectToDB();
        return db;
    }

    @Bean
    public TodoRepository todoRepository(Database database) {
        return new TodoRepository(database);
    }

    @Bean
    public TodoController todoController(TodoRepository todoRepository) {
        return new TodoController(todoRepository);
    }

    @Bean(name = "emailTemplateEngine")
    public TemplateEngine emailTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);
        return templateEngine;
    }

    @Bean
    public JavaMailSender getJavaMailSender(Environment env, TemplateEngine emailTemplateEngine) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));

        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");

        MailService mailService = new MailService(mailSender, emailTemplateEngine);

        try {
            mailService.sendVerificationEmail("wliafdew@gmail.com", "http://localhost:3000/verify?token=1234567890");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mailSender;
    }
}
