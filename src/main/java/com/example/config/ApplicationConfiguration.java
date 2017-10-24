package com.example.config;

import com.example.backend.utils.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public SessionManager sessionManager() {
    return new SessionManager();
  }

}
