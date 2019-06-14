package com.github.ivansenchukov.topjavagraduation.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private final String API_DESCRIPTION =
            "Voting system for deciding where to have lunch.\n" +
            "\n" +
            " * 2 types of users: admin and regular users\n" +
            " * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)\n" +
            " * Menu changes each day (admins do the updates)\n" +
            " * Users can vote on which restaurant they want to have lunch at\n" +
            " * Only one vote counted per user\n" +
            " * If user votes again the same day:\n" +
            "    - If it is before 11:00 we asume that he changed his mind.\n" +
            "    - If it is after 11:00 then it is too late, vote can't be changed\n" +
            "\n" +
            "Each restaurant provides new menu each day.\n";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Restaurant Voting Service API",
                API_DESCRIPTION,
                "1.0",
                "Terms Of Service",
                new Contact("Ivan Senchukov", "https://github.com/IvanSenchukov", "iasench@yandex.ru"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }

}
