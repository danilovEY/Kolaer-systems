package ru.kolaer.server.webportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.server.webportal.beans.SeterProviderBean;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;
import ru.kolaer.server.webportal.security.*;
import ru.kolaer.server.webportal.security.ldap.CustomLdapAuthenticationProvider;
import ru.kolaer.server.webportal.beans.ToolsLDAP;

import javax.annotation.Resource;
import java.util.Arrays;

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
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UrlPathService urlPathService;

    /**Секретный ключ для шифрования пароля.*/
    @Value("${secret_key}")
    private String secretKey;

    @Resource
    private Environment env;

    @Autowired
    private SeterProviderBean seterProviderBean;

    @Autowired
    private ToolsLDAP toolsLDAP;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authProviderLDAP()).authenticationProvider(this.authProviderSQL());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**").antMatchers("/app/**").antMatchers("/rest/non-security/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final UserDetailsService userDetailsService = this.userDetailsServiceLDAP();

        //Отключаем csrf хак
        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        .httpBasic()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
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
    public UserDetailsService userDetailsServiceSQL() {
        return new UserDetailsServiceImpl();
    }

    private CustomLdapAuthenticationProvider authProviderLDAP() {
        final CustomLdapAuthenticationProvider authProvider = new CustomLdapAuthenticationProvider(this.seterProviderBean, this.toolsLDAP);
        authProvider.setDc(this.env.getProperty("ldap.dc"));
        authProvider.setServer(this.env.getProperty("ldap.server"));
        authProvider.setSsl(Boolean.getBoolean(this.env.getProperty("ldap.ssl")));
        return authProvider;
    }

    /**Создание provider для проверки пользователей из БД с шифрованием пароля.*/
    private DaoAuthenticationProvider authProviderSQL() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsServiceSQL());
        authProvider.setPasswordEncoder(new StandardPasswordEncoder(this.secretKey));
        return authProvider;
    }
}
