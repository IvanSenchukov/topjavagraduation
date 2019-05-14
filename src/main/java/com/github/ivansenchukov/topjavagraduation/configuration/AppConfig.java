package com.github.ivansenchukov.topjavagraduation.configuration;

import com.github.ivansenchukov.topjavagraduation.repository.DishRepository;
import com.github.ivansenchukov.topjavagraduation.repository.RestaurantRepository;
import com.github.ivansenchukov.topjavagraduation.repository.UserRepository;
import com.github.ivansenchukov.topjavagraduation.repository.VoteRepository;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryDishRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryRestaurantRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryUserRepositoryImpl;
import com.github.ivansenchukov.topjavagraduation.repository.inmemory.InMemoryVoteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.time.LocalTime;

@Configuration
@ComponentScan(basePackages = "com.github.ivansenchukov.topjavagraduation.**.service")
@PropertySource(
        value = {"classpath:app.properties"}
)
public class AppConfig {

    @Value("${stoptime}")
    String stoptime;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepositoryImpl();
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return new InMemoryRestaurantRepositoryImpl();
    }

    @Bean
    public DishRepository dishRepository() {
        return new InMemoryDishRepositoryImpl();
    }

    @Bean
    public VoteRepository VoteRepository() {
        return new InMemoryVoteRepositoryImpl();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public LocalTime stopVotingTime() {
        return LocalTime.parse(stoptime);
    }
}
