package com.example;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication(exclude = SpringDataWebAutoConfiguration.class)
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories
public class AccountsApplication extends SpringBootServletInitializer {

	@Bean
	public Docket simpleDiffServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("trainers")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/");

	}

	private static final Logger log = LoggerFactory.getLogger(AccountsApplication.class);



	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(AccountsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run( AccountsApplication.class, args);
	}


	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Software Architecture")
				.description("Simple  web application")
				.contact(new Contact("Bartłomiej Juroszek", "", "jurbar369@gmail.com"))
				.version("1.0")
				.build();
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/**")
				.addResourceLocations("/")
				.setCachePeriod(0);
	}



}



