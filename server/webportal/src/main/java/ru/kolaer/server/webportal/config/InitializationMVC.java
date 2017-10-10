package ru.kolaer.server.webportal.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Инициализация Spring-MVC.
 */
public class InitializationMVC extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //servletContext.addListener(new RequestContextListener());
        //Фильтр для Spring Security.
        servletContext
                .addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
                .addMappingForUrlPatterns(null, false, PathMapping.DISPATCHER_SERVLET + "/*");

        //Поддержка UTF-8.
        servletContext
                .addFilter("encoding-filter", new CharacterEncodingFilter("UTF-8",true))
                .addMappingForUrlPatterns(null, true, PathMapping.DISPATCHER_SERVLET + "/*");

        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SpringContext.class, SpringSecurityConfig.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { PathMapping.DISPATCHER_SERVLET + "/*" };
    }
 
}