package ru.kolaer.server.webportal.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import ru.kolaer.server.webportal.beans.TypeServer;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by danilovey on 14.07.2016.
 * Спринговый контекст на java. Имеет в наличие бинов для работы с БД и скан для контроллеров.
 * Так же есть импорт внешней конфигурации на груви.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableSwagger2
@EnableScheduling
@EnableCaching
@EnableAsync
@ComponentScan({"ru.kolaer.server.webportal.spring",
        "ru.kolaer.server.webportal.beans",
        "ru.kolaer.server.webportal.mvc.model.converter",
        "ru.kolaer.server.webportal.mvc.model.dao.impl",
        "ru.kolaer.server.webportal.mvc.model.ldap.impl",
        "ru.kolaer.server.webportal.mvc.model.servirces.impl",
        "ru.kolaer.server.webportal.mvc.controllers"})
@PropertySources({
        @PropertySource("classpath:database.properties"),
        @PropertySource("classpath:mail.properties"),
        @PropertySource("classpath:common.properties")
})
public class SpringContext extends WebMvcConfigurerAdapter {

    @Resource
    private Environment env;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("resources/**");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/configuration/ui", "/rest/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/configuration/security", "/rest/swagger-resources/configuration/security");
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("ASYNC-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Primary
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                .name("x-token")
                .description("Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .build()));
    }

    @Bean
    public TypeServer typeServer() {
        TypeServer typeServer = new TypeServer();
        typeServer.setTest(env.getRequiredProperty("test").equals("true"));
        return typeServer;
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
    public DataSource dataSourceKolaerBase() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url_kolaer_base"));
        dataSource.setUsername(env.getRequiredProperty("db.user_kolaer_base"));
        dataSource.setPassword(env.getRequiredProperty("db.pass_kolaer_base"));
        return dataSource;
    }

    @Bean
    @Qualifier(value = "dataSourceKolaerBase")
    @Autowired
    public JdbcTemplate jdbcTemplateKolaerBase(DataSource dataSourceKolaerBase) {
        return new JdbcTemplate(dataSourceKolaerBase);
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

    @Bean
    @Qualifier(value = "dataSource")
    @Autowired
    public JdbcTemplate jdbcTemplateOrigin(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Autowired
    @Qualifier(value = "dataSource")
    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactoryBean(final DataSource dataSource) {
        final LocalSessionFactoryBuilder sessionFactoryBean = new LocalSessionFactoryBuilder(dataSource);
        sessionFactoryBean.scanPackages("ru.kolaer.server.webportal.mvc.model");
        sessionFactoryBean.setProperty("db.hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        sessionFactoryBean.setProperty("hibernate.cache.use_second_level_cache", "true");
        sessionFactoryBean.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        sessionFactoryBean.setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        sessionFactoryBean.setProperty("hibernate.use_sql_comments", env.getRequiredProperty("hibernate.use_sql_comments"));
        sessionFactoryBean.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        sessionFactoryBean.setProperty("hibernate.jdbc.batch_size", env.getRequiredProperty("hibernate.batch.size"));
        sessionFactoryBean.setProperty("hibernate.order_inserts", "true");
        sessionFactoryBean.setProperty("hibernate.order_updates", "true");
        return sessionFactoryBean.buildSessionFactory();
    }

    @Autowired
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Bean
    @Autowired
    public SpringLiquibase liquibase(@Qualifier("dataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @Bean
    public JavaMailSender mailSender() {
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.getJavaMailProperties().put("mail.smtps.auth", env.getRequiredProperty("mail.smtps.auth"));
        javaMailSender.setHost(env.getRequiredProperty("mail.host"));
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setPort(Integer.valueOf(env.getRequiredProperty("mail.port")));
        javaMailSender.setUsername(env.getRequiredProperty("mail.username"));
        javaMailSender.setPassword(env.getRequiredProperty("mail.password"));
        javaMailSender.setProtocol(env.getRequiredProperty("mail.protocol"));
        return javaMailSender;
    }

    @Bean
    public SimpleMailMessage templateMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(env.getRequiredProperty("mail.from"));
        return simpleMailMessage;
    }

    @Bean(name = "springCM")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("accounts");
    }

    /**Позволяет мапить объект в json даже с учетом что стоит LAZY над property в entities.*/
    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        MappingJackson2HttpMessageConverter messageConverter = new  MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate5Module());
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setObjectMapper(mapper);
        return messageConverter;

    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(50000000);
        return commonsMultipartResolver;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jacksonMessageConverter());
        super.configureMessageConverters(converters);
    }

}
