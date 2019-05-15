package com.github.ivansenchukov.topjavagraduation.configuration;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.LocalTime;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.github.ivansenchukov.topjavagraduation.service",
        "com.github.ivansenchukov.topjavagraduation.repository.inmemory"
})
@PropertySource(
        value = {
                "classpath:app.properties",
                "classpath:db/hsqldb.properties"
        }
)
public class InMemoryAppConfig {

    @Value("${stoptime}")
    String stoptime;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public LocalTime stopVotingTime() {
        return LocalTime.parse(stoptime);
    }


    // TODO - workaround this
    // Mock for enabeling @Transaction annotation work in AbstractServiceTest classes
    @Bean
    @Autowired
    public DataSource dataSource(Environment environment) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                environment.getRequiredProperty("database.url"),
                environment.getRequiredProperty("database.username"),
                environment.getRequiredProperty("database.password"));
        dataSource.setDriverClassName(environment.getRequiredProperty("database.driverClassName"));

        return dataSource;
    }

    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
