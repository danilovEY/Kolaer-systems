package ru.kolaer.server.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.kolaer.server.dao.DbBirthdayAllDAO;
import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.impl.DbBirthdayAllDAOImpl;
import ru.kolaer.server.dao.impl.DbDataAllDAOImpl;
import ru.kolaer.server.dao.impl.PublicHolidaysDAO;
import ru.kolaer.server.restful.tools.UsersManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
 
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("ru.kolaer.server")
@PropertySource("classpath:app.properties")
public class KolaerServerConfig extends WebMvcConfigurerAdapter {
 
    private static final String PROP_DATABASE_DRIVER = "db.driver";
    private static final String PROP_DATABASE_PASSWORD = "db.password";
    private static final String PROP_DATABASE_URL = "db.url";
    private static final String PROP_DATABASE_USERNAME = "db.username";
    private static final String PROP_HIBERNATE_DIALECT = "db.hibernate.dialect";
    private static final String PROP_HIBERNATE_SHOW_SQL = "db.hibernate.show_sql";
    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "db.entitymanager.packages.to.scan";
 
    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
    	final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder localSessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource());
        localSessionFactoryBuilder.scanPackages(env.getRequiredProperty(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN));
        localSessionFactoryBuilder.setProperty("db.hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        localSessionFactoryBuilder.setProperty("hibernate.show_sql", env.getRequiredProperty("db.hibernate.show_sql"));
        localSessionFactoryBuilder.setProperty("hibernate.hbm2ddl.auto", "none");
        return localSessionFactoryBuilder.buildSessionFactory();
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }
    
    @Bean(name = "publicHolidaysDAO")
    public PublicHolidaysDAO getPublicHolidaysDAO() {
    	final PublicHolidaysDAO publicHolidaysDAO = new PublicHolidaysDAO();
    	publicHolidaysDAO.initObjects();
    	return publicHolidaysDAO;
    }
    
    @Bean(name = "usersManager")
    public UsersManager usersManager() {
    	final UsersManager usersManager = new UsersManager();
    	usersManager.startPing();
    	return usersManager; 	
    }
    
    @Bean(name = "dbBirthdayAllDAO")
    public DbBirthdayAllDAO dbBirthdayAllDAO() {
    	final DbBirthdayAllDAO dbBirthdayAllDAO = new DbBirthdayAllDAOImpl();
    	return dbBirthdayAllDAO;
    }
    
    @Bean(name = "dbDataAllDAO")
    public DbDataAllDAO dbDataAllDAO() {
    	final DbDataAllDAO dbUser1сDAO = new DbDataAllDAOImpl();
    	return dbUser1сDAO;
    }
}