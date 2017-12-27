package com.example.utils;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailManager {

  final private JavaMailSenderImpl mailSender;

  public MailManager() {
    this.mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setProtocol("smtp");
    mailSender.setUsername("juroszeksender@gmail.com");
    mailSender.setPassword("DefaultPassword");
    final Properties javaMailProperties = new Properties();
    javaMailProperties.setProperty("mail.smtp.auth", "true");
    javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
    mailSender.setJavaMailProperties(javaMailProperties);
  }

  public boolean sendMail(String from, String to, String subject, String content) {

    final SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setSubject(subject);
    msg.setTo(to);
    msg.setText(content);
    try {
      this.mailSender.send(msg);
      return true;
    } catch (MailException ex) {
      return false;
    }
  }

}