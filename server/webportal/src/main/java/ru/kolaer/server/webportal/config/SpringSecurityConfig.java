package ru.kolaer.server.webportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kolaer.server.webportal.beans.ToolsLDAP;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;
import ru.kolaer.server.webportal.security.*;
import ru.kolaer.server.webportal.security.ldap.CustomLdapAuthenticationProvider;
import ru.kolaer.server.webportal.spring.SimpleCORSFilter;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
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
@Slf4j
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationProvider authenticationProvider;
    private final FilterSecurityInterceptor filter;

    /**Секретный ключ для шифрования пароля.*/
    private String secretKey;

    @Resource
    private Environment env;

    @Autowired
    public SpringSecurityConfig(@Value("${secret_key}") String secretKey,
                                AuthenticationProvider authenticationProvider,
                                FilterSecurityInterceptor filter) {
        this.secretKey = secretKey;
        this.authenticationProvider = authenticationProvider;
        this.filter = filter;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**").antMatchers("/rest/non-security/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Отключаем csrf хак
        http.csrf().disable()
        .httpBasic()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
                .exceptionHandling().authenticationEntryPoint(new SimpleCORSFilter()).and()
        //Фильтер для проверки http request'а на наличие правильного токена
        .addFilterBefore(authenticationTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        //Фильтер для проверки URL'ов.
        .addFilter(filter);
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
    @Autowired
    public FilterSecurityInterceptor filter(UrlSecurityService urlSecurityService) throws Exception {
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAuthenticationManager(authenticationManagerBean());
        RoleVoter role = new RoleVoter();
        role.setRolePrefix("");
        filter.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(role, new AuthenticatedVoter())));
        filter.setSecurityMetadataSource(new SecurityMetadataSourceFilter(urlSecurityService));

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
            log.error("Ошибка при создании LDAP конфигурации!", e);
            return null;
        }
    }

    private UserDetailsService userDetailsServiceLDAP(InitialLdapContext context) {
        UserDetailsServiceLDAP userDetailsServiceLDAP = new UserDetailsServiceLDAP(context);
        userDetailsServiceLDAP.setDc(this.env.getProperty("ldap.dc"));
        userDetailsServiceLDAP.setServer(this.env.getProperty("ldap.server"));
        userDetailsServiceLDAP.setAdmin(this.env.getProperty("ldap.server.admin"));
        userDetailsServiceLDAP.setPass(this.env.getProperty("ldap.server.pass"));
        userDetailsServiceLDAP.setSsl(Boolean.getBoolean(this.env.getProperty("ldap.ssl")));
        return userDetailsServiceLDAP;
    }

    private UserDetailsService userDetailsServiceSQL(AccountDao context) {
        return new UserDetailsServiceImpl(context);
    }

    private CustomLdapAuthenticationProvider authProviderLDAP() {
        final CustomLdapAuthenticationProvider authProvider = new CustomLdapAuthenticationProvider();
        authProvider.setDc(this.env.getProperty("ldap.dc"));
        authProvider.setServer(this.env.getProperty("ldap.server"));
        authProvider.setSsl(Boolean.getBoolean(this.env.getProperty("ldap.ssl")));
        return authProvider;
    }

    /**Создание provider для проверки пользователей из БД с шифрованием пароля.*/
    private DaoAuthenticationProvider authProviderSQL(UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new StandardPasswordEncoder(this.secretKey));
        return authProvider;
    }

    @Bean
    @Autowired
    public UserDetailsService userDetailsService(@Value("${server.type.auth}") ServerAuthType serverAuthType,
                                                 AccountDao accountDao,
                                                 InitialLdapContext context) {
        switch (serverAuthType) {
            case LDAP: return userDetailsServiceLDAP(context);
            default: return userDetailsServiceSQL(accountDao);
        }
    }

    @Bean
    public AuthenticationProvider authenticationProvider(@Value("${server.type.auth}") ServerAuthType serverAuthType,
                                                         UserDetailsService userDetailsService) {
        switch (serverAuthType) {
            case LDAP: return authProviderLDAP();
            default: return authProviderSQL(userDetailsService);
        }
    }
}
