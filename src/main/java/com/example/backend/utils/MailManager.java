package com.example.backend.utils;

import com.vaadin.ui.Notification;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailManager {

  ApplicationContext context =
      new ClassPathXmlApplicationContext("Mail.xml");

  final private JavaMailSenderImpl mailSender;

  public MailManager(String password, String username) {
    this.mailSender = (JavaMailSenderImpl) context.getBean("mailSender");
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setPassword(password);
    mailSender.setUsername(username);
    mailSender.setProtocol("smtp");

  }

  public MailManager() {
    this.mailSender = (JavaMailSenderImpl) context.getBean("mailSender");
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setProtocol("smtp");
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