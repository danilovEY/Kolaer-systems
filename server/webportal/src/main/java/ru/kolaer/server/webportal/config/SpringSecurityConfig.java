package ru.kolaer.server.webportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by Danilov on 17.07.2016.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends AbstractSecurityWebApplicationInitializer {

}
