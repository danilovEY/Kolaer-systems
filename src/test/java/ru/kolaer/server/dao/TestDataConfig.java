package ru.kolaer.server.dao;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
 
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;
 
@Configuration
@EnableTransactionManagement
@ComponentScan("ru.kolaer.server")
@EnableJpaRepositories("ru.kolaer.server.dao")

public class TestDataConfig {
 
    private static final String PROP_DATABASE_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static final String PROP_DATABASE_PASSWORD = "APP";
    private static final String PROP_DATABASE_URL = "jdbc:derby://localhost:1527/test";
    private static final String PROP_DATABASE_USERNAME = "APP";
    private static final String PROP_HIBERNATE_DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final String PROP_HIBERNATE_SHOW_SQL = "true";
    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "ru.kolaer.server.dao";
    private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "create-drop";
 
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
 
        dataSource.setDriverClassName(PROP_DATABASE_DRIVER);
        dataSource.setUrl(PROP_DATABASE_URL);
        dataSource.setUsername(PROP_DATABASE_USERNAME);
        dataSource.setPassword(PROP_DATABASE_PASSWORD);
 
        return dataSource;
    }
 
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN);
 
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
 
        return entityManagerFactoryBean;
    }
 
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
 
        return transactionManager;
    }
 
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROP_HIBERNATE_DIALECT, PROP_HIBERNATE_DIALECT);
        properties.put(PROP_HIBERNATE_SHOW_SQL, PROP_HIBERNATE_SHOW_SQL);
        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, PROP_HIBERNATE_HBM2DDL_AUTO);
 
        return properties;
    }
 
}