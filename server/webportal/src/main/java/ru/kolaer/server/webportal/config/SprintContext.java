package ru.kolaer.server.webportal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import ru.kolaer.server.webportal.mvc.model.ApiMapping;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by danilovey on 14.07.2016.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan({"ru.kolaer.server.webportal.mvc.model.dao.impl", "ru.kolaer.server.webportal.mvc.controllers"})
@PropertySource("classpath:database.properties")
@ImportResource({"/WEB-INF/spring-config/spring-context.groovy"})
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
        sessionFactoryBean.scanPackages("ru.kolaer.server.webportal.mvc.model");
        sessionFactoryBean.setProperty("db.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        sessionFactoryBean.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        sessionFactoryBean.setProperty("db.hibernate.max_fetch_depth", String.valueOf(3));
        sessionFactoryBean.setProperty("db.hibernate.jdbc.fetch_size", String.valueOf(50));
        sessionFactoryBean.setProperty("db.hibernate.jdbc.batch_size", String.valueOf(10));
        sessionFactoryBean.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return sessionFactoryBean.buildSessionFactory();
    }

    @Bean
    public ApiMapping apiMapping() {
        final ApiMapping apiMapping = new ApiMapping();
        apiMapping.put(PathMapping.ABSOLUTE_PATH_TO_GET_ALL_RSS,"Получить все RSS.");
        apiMapping.put(PathMapping.ABSOLUTE_PATH_PARAM_TO_GET_RSS_BY_ID,"Получить RSS по id: (" + PathMapping.ABSOLUTE_PATH_PATH_TO_GET_RSS + "?id=0).");

        return apiMapping;
    }

    @Autowired
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        MappingJackson2HttpMessageConverter messageConverter = new  MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate5Module());

        messageConverter.setObjectMapper(mapper);
        return messageConverter;

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jacksonMessageConverter());
        super.configureMessageConverters(converters);
    }

}
