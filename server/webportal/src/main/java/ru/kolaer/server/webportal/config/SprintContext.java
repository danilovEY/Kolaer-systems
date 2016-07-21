package ru.kolaer.server.webportal.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by danilovey on 14.07.2016.
 */
@Configuration
@EnableWebMvc
@ComponentScan("ru.kolaer.server.webportal.mvc.controllers")
@PropertySource("classpath:database.properties")
@ImportResource(value = "/WEB-INF/spring-config/spring-context.groovy")
public class SprintContext extends WebMvcConfigurerAdapter {

    @Resource
    private Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.user"));
        dataSource.setPassword(env.getRequiredProperty("db.pass"));
        return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactoryBean(final DataSource dataSource) {
        final LocalSessionFactoryBuilder sessionFactoryBean = new LocalSessionFactoryBuilder(dataSource);
        sessionFactoryBean.scanPackages(env.getRequiredProperty("db.entitymanager.packages.to.scan"));
        sessionFactoryBean.setProperty("db.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        sessionFactoryBean.setProperty("hibernate.show_sql", "true");
        sessionFactoryBean.setProperty("db.hibernate.max_fetch_depth", String.valueOf(3));
        sessionFactoryBean.setProperty("db.hibernate.jdbc.fetch_size", String.valueOf(50));
        sessionFactoryBean.setProperty("db.hibernate.jdbc.batch_size", String.valueOf(10));
        sessionFactoryBean.setProperty("hibernate.hbm2ddl.auto", "update");
        return sessionFactoryBean.buildSessionFactory();
    }

    @Autowired
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

}
