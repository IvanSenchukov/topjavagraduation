package com.github.ivansenchukov.topjavagraduation.configuration;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalTime;

@Configuration
@ComponentScan(basePackages = "com.github.ivansenchukov.topjavagraduation.**.service")
@PropertySource(
        value = {"classpath:app.properties"}
)
public class AppConfig {

    @Value("${stoptime}")
    String stoptime;

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    @Bean
//    public UserRepository userRepository() {
//        return new InMemoryUserRepositoryImpl();
//    }
//
//    @Bean
//    public RestaurantRepository restaurantRepository() {
//        return new InMemoryRestaurantRepositoryImpl();
//    }
//
//    @Bean
//    public DishRepository dishRepository() {
//        return new InMemoryDishRepositoryImpl();
//    }
//
//    @Bean
//    public VoteRepository VoteRepository() {
//        return new InMemoryVoteRepositoryImpl();
//    }

    @Bean(autowire = Autowire.BY_NAME)
    public LocalTime stopVotingTime() {
        return LocalTime.parse(stoptime);
    }
}
