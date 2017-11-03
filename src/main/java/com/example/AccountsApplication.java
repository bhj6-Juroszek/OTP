package com.example;

import com.example.backend.utils.SessionManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication(exclude = SpringDataWebAutoConfiguration.class)
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories
public class AccountsApplication extends SpringBootServletInitializer {

  private static ApplicationContext context;
  private static final org.apache.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(AccountsApplication.class);

  private static final Logger LOGGER = Logger.getLogger(AccountsApplication.class);

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

    return application.sources(AccountsApplication.class);
  }

  public static void main(String[] args) {
    context = SpringApplication.run(AccountsApplication.class, args);
    LOGGER.log(Logger.DEBUG, "");
    // DAOHandler.usersDAO.createTable();
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



