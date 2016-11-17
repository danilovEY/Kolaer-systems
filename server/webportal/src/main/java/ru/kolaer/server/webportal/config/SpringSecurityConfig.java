package ru.kolaer.server.webportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import ru.kolaer.server.webportal.beans.ToolsLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;
import ru.kolaer.server.webportal.security.*;
import ru.kolaer.server.webportal.security.ldap.CustomLdapAuthenticationProvider;
import ru.kolaer.server.webportal.spring.SimpleCORSFilter;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by Danilov on 17.07.2016.
 * Конфигурация spring security. Динамически считывает с БД существующие пути и их доступность для ролей,
 * а так же динамически идет поиск пользователей из БД.
 * Описание:
 *      Анонимный пользователь идет по некоторой ссылке и если она защищена, то сервер выдает 403 ошибку.
 *      Для того, чтобы авторизоваться, необходимо сгенерировать токен и прикреплять его в запросе.
 */
@Configuration
@ComponentScan("ru.kolaer.server.webportal.security")
@PropertySource("classpath:ldap.properties")
@EnableWebSecurity
@EnableScheduling
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Autowired
    private UrlPathService urlPathService;

    /**Секретный ключ для шифрования пароля.*/
    @Value("${secret_key}")
    private String secretKey;

    @Resource
    private Environment env;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authProviderLDAP());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**").antMatchers("/rest/non-security/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Отключаем csrf хак
        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
        .httpBasic()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
                .exceptionHandling().authenticationEntryPoint(new SimpleCORSFilter()).and()
        //Фильтер для проверки http request'а на наличие правильного токена
        .addFilterBefore(authenticationTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        //Фильтер для проверки URL'ов.
        .addFilter(filter());
    }

    @Bean
    public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
        return new AuthenticationTokenProcessingFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**Фильтер для проверки URL'ов.*/
    @Bean
    public FilterSecurityInterceptor filter() throws Exception {
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAuthenticationManager(authenticationManagerBean());
        RoleVoter role = new RoleVoter();
        role.setRolePrefix("");
        filter.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(role, new AuthenticatedVoter())));
        filter.setSecurityMetadataSource(new SecurityMetadataSourceFilter(this.urlPathService));

        return filter;
    }

    @Bean
    @Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public InitialLdapContext ldapContext() {
        final String server = this.env.getProperty("ldap.server");
        final String dc = this.env.getProperty("ldap.dc");
        final String admin = this.env.getProperty("ldap.server.admin");
        final String pass = this.env.getProperty("ldap.server.pass");
        final boolean ssl = Boolean.getBoolean(this.env.getProperty("ldap.ssl"));

        final Hashtable props = new Hashtable();
        props.put(Context.SECURITY_PRINCIPAL, admin);
        props.put(Context.SECURITY_CREDENTIALS, pass);

        String ldapURL;
        if(!ssl){
            ldapURL = "ldap://";
        } else {
            ldapURL = "ldaps://";
            props.put(Context.SECURITY_PROTOCOL, "ssl");
        }

        ldapURL += server + "." + dc + "/" + ToolsLDAP.toDC(dc);

        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ldapURL);

        try {
            return new InitialLdapContext(props, null);
        } catch (NamingException e) {
            this.logger.error("Ошибка при создании LDAP конфигурации!", e);
            return null;
        }
    }

    @Bean
    public UserDetailsService userDetailsServiceLDAP() {
        UserDetailsServiceLDAP userDetailsServiceLDAP = new UserDetailsServiceLDAP();
        userDetailsServiceLDAP.setDc(this.env.getProperty("ldap.dc"));
        userDetailsServiceLDAP.setServer(this.env.getProperty("ldap.server"));
        userDetailsServiceLDAP.setAdmin(this.env.getProperty("ldap.server.admin"));
        userDetailsServiceLDAP.setPass(this.env.getProperty("ldap.server.pass"));
        userDetailsServiceLDAP.setSsl(Boolean.getBoolean(this.env.getProperty("ldap.ssl")));
        return userDetailsServiceLDAP;
    }

    @Bean
    public CustomLdapAuthenticationProvider authProviderLDAP() {
        final CustomLdapAuthenticationProvider authProvider = new CustomLdapAuthenticationProvider();
        authProvider.setDc(this.env.getProperty("ldap.dc"));
        authProvider.setServer(this.env.getProperty("ldap.server"));
        authProvider.setSsl(Boolean.getBoolean(this.env.getProperty("ldap.ssl")));
        return authProvider;
    }

    @Deprecated
    public UserDetailsService userDetailsServiceSQL() {
        return new UserDetailsServiceImpl();
    }

    /**Создание provider для проверки пользователей из БД с шифрованием пароля.*/
    @Deprecated
    public DaoAuthenticationProvider authProviderSQL() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsServiceSQL());
        authProvider.setPasswordEncoder(new StandardPasswordEncoder(this.secretKey));
        return authProvider;
    }
}
