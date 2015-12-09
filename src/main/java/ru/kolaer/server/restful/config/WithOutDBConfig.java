package ru.kolaer.server.restful.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author Danilov
 * @version 0.1
 */
@Configuration
@EnableWebMvc
@ComponentScan("ru.kolaer.server.restful")
public class WithOutDBConfig {

}
