package com.example.utils;

import com.google.gwt.thirdparty.guava.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MailManager {

  private static final Logger LOGGER = LogManager.getLogger(MailManager.class);
  final private JavaMailSenderImpl mailSender;
  private ThreadPoolExecutor executor;
  ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
      .setNameFormat("async-mail-sender-%d").build();

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
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4, namedThreadFactory);
  }

  public boolean sendMail(@Nonnull final String from, @Nonnull final String to, @Nonnull final String subject, @Nonnull final String content) {
    final SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setSubject(subject);
    msg.setTo(to);
    msg.setText(content);
    try {
      this.mailSender.send(msg);
      LOGGER.info("Mail have been sent from {} to {}", from , to);
      return true;
    } catch (MailException ex) {
      LOGGER.error(ex);
      return false;
    }
  }
  public void sendMailAsynchronously(@Nonnull final String from, @Nonnull final String to, @Nonnull final String subject, @Nonnull final String content) {
    executor.submit(() -> sendMail(from, to, subject, content));
  }

}