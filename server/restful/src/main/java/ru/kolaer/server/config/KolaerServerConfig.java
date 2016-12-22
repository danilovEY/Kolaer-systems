package ru.kolaer.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.kolaer.server.restful.tools.UsersManager;

 
@Configuration
@EnableWebMvc
@ComponentScan("ru.kolaer.server.restful.controller")
public class KolaerServerConfig extends WebMvcConfigurerAdapter {
    
    @Bean(name = "usersManager")
    public UsersManager usersManager() {
    	final UsersManager usersManager = new UsersManager();
    	usersManager.startPing();
    	return usersManager; 	
    }
}