package com.github.ivansenchukov.topjavagraduation.configuration;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(
        value = {"classpath:db/hsqldb.properties"}
)
@EnableTransactionManagement
@ComponentScan(basePackages = "com.github.ivansenchukov.topjavagraduation.repository.jpa")
public class DbConfig {


    //        <!--no pooling-->
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
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment environment) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.github.ivansenchukov.topjavagraduation.model");

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(Boolean.valueOf(environment.getRequiredProperty("jpa.showSql")));
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);


        Properties jpaProperties = new Properties();

        jpaProperties.put(AvailableSettings.FORMAT_SQL, environment.getRequiredProperty("hibernate.format_sql"));
        jpaProperties.put(AvailableSettings.USE_SQL_COMMENTS, environment.getRequiredProperty("hibernate.use_sql_comments"));


        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }


    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean
    @Autowired
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource, Environment environment, ApplicationContext applicationContext) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator(applicationContext));
        initializer.setEnabled(Boolean.valueOf(environment.getRequiredProperty("database.init")));
        return initializer;
    }

    private DatabasePopulator databasePopulator(ApplicationContext appContext) {

        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        Resource schemaScript = appContext.getResource("classpath:db/scripts/db-schema.sql");
        Resource dataScript = appContext.getResource("classpath:db/scripts/db-test-data.sql");

        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }
}
