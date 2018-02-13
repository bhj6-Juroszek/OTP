package com.example;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import static org.apache.log4j.Level.INFO;

@ComponentScan
@SpringBootApplication(exclude = SpringDataWebAutoConfiguration.class)
public class AccountsApplication extends SpringBootServletInitializer {

  private static ApplicationContext context;
  private static final Logger LOGGER = Logger.getLogger(AccountsApplication.class);

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(AccountsApplication.class);
  }

  public static void main(String[] args) {
    context = SpringApplication.run(AccountsApplication.class, args);
    LOGGER.log(INFO, "Started Application");
  }

  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("/")
        .setCachePeriod(0);
  }

}



