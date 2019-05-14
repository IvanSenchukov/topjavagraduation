package com.github.ivansenchukov.topjavagraduation.configuration;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(
        value = {"classpath:db/hsqldb.properties"}
)
@ComponentScan(basePackages = "com.github.ivansenchukov.topjavagraduation.repository.jpa")
public class DbConfig {

    @Value("${database.driverClassName}")
    private String databaseClassName;
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Value("${classpath:db/scripts/db-schema.sql}")
    private Resource schemaScript;
    @Value("${classpath:db/scripts/db-test-data.sql}")
    private Resource dataScript;


    //        <!--no pooling-->
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(databaseClassName);

        return dataSource;
    }

//<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"
//              p:dataSource-ref="dataSource"
//              p:packagesToScan="ru.javawebinar.**.model">
//            <!--p:persistenceUnitName="persistenceUnit">-->
//
//            <property name="jpaPropertyMap">
//                <map>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).FORMAT_SQL}" value="${hibernate.format_sql}"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).USE_SQL_COMMENTS}"
//                           value="${hibernate.use_sql_comments}"/>
//                    <!--<entry key="#{T(org.hibernate.cfg.AvailableSettings).HBM2DDL_AUTO}" value="${hibernate.hbm2ddl.auto}"/>-->
//
//                    <!--https://github.com/hibernate/hibernate-orm/blob/master/documentation/src/main/asciidoc/userguide/chapters/caching/Caching.adoc#caching-provider-jcache-->
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).CACHE_REGION_FACTORY}"
//                           value="org.hibernate.cache.jcache.internal.JCacheRegionFactory"/>
//                    <entry key="#{T(org.hibernate.cache.jcache.ConfigSettings).PROVIDER}"
//                           value="org.ehcache.jsr107.EhcacheCachingProvider"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).USE_SECOND_LEVEL_CACHE}" value="true"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).USE_QUERY_CACHE}" value="false"/> <!--default-->
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).DIALECT}" value="${hibernate.dialect}"/> <!--default-->
//
//<!--
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).HBM2DDL_SCRIPTS_ACTION}" value="drop-and-create"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).HBM2DDL_SCRIPTS_CREATE_TARGET}" value="${TJ_ROOT}/config/ddl/create.ddl"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).HBM2DDL_SCRIPTS_DROP_TARGET}" value="${TJ_ROOT}/config/ddl/drop.ddl"/>
//                    <entry key="#{T(org.hibernate.cfg.AvailableSettings).HBM2DDL_AUTO}" value="create"/>
//-->
//                </map>
//            </property>
//
//            <property name="jpaVendorAdapter">
//                <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter" p:showSql="${jpa.showSql}">
//                </bean>
//            </property>
//        </bean>+

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
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource, Environment environment) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        initializer.setEnabled(Boolean.valueOf(environment.getRequiredProperty("database.init")));
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }
}
