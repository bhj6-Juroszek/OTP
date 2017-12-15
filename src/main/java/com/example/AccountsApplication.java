package com.example;

import com.example.backend.utils.SessionManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

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
    LOGGER.log(INFO, "Finished loading cities list");
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Software Architecture")
        .description("Simple  web application")
        .contact(new Contact("Bartłomiej Juroszek", "", "jurbar369@gmail.com"))
        .version("1.0")
        .build();
  }

  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("/")
        .setCachePeriod(0);
  }

  public static SessionManager getSessionManager() {
    return context.getBean(SessionManager.class);
  }

}



