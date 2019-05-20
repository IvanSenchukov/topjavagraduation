package com.github.ivansenchukov.topjavagraduation.configuration;

import com.github.ivansenchukov.topjavagraduation.configuration.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.time.LocalTime;

@Configuration
@Import(SecurityConfig.class)
@ComponentScan(basePackages = "com.github.ivansenchukov.topjavagraduation.**.service")
@PropertySource(
        value = {"classpath:app.properties"}
)
public class RootApplicationConfig {

    @Value("${stoptime}")
    String stoptime;

    @Bean(autowire = Autowire.BY_NAME)
    public LocalTime stopVotingTime() {
        return LocalTime.parse(stoptime);
    }
}
