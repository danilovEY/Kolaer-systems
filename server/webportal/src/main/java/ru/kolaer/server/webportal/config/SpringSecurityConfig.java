package ru.kolaer.server.webportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.kolaer.server.webportal.security.MyFilterSecurityMetadataSource;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by Danilov on 17.07.2016.
 */
@Configuration
@ComponentScan("ru.kolaer.server.webportal.security")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.addFilterBefore(filter(),)
                .authorizeRequests();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterSecurityInterceptor filter() throws Exception {
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAuthenticationManager(authenticationManagerBean());
        //filter.setAccessDecisionManager(new CustomAccessDecisionManager());
        filter.setSecurityMetadataSource(new MyFilterSecurityMetadataSource());

        //SecurityExpressionHandler<FilterInvocation> securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        //LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        //map.put(new AntPathRequestMatcher("/test"), Arrays.<ConfigAttribute>asList(new SecurityConfig("permitAll")));

        return filter;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }
}
