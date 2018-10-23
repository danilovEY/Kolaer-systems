package ru.kolaer.server.webportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.kolaer.server.webportal.common.servirces.ExceptionHandlerService;
import ru.kolaer.server.account.service.UrlSecurityService;
import ru.kolaer.server.account.service.TokenService;
import ru.kolaer.server.webportal.security.*;
import ru.kolaer.server.webportal.spring.ExceptionHandlerFilter;
import ru.kolaer.server.webportal.spring.UnauthorizedEntryPoint;

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
@Slf4j
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**Секретный ключ для шифрования пароля.*/
    private final String secretKey;
    private final TokenService tokenService;
    private final UrlSecurityService urlSecurityService;
    private final ExceptionHandlerService exceptionHandlerService;
    private final UserDetailsService userDetailsService;

    @Resource
    private Environment env;

    @Autowired
    public SpringSecurityConfig(@Value("${secret_key}") String secretKey,
                                TokenService tokenService,
                                UrlSecurityService urlSecurityService,
                                ExceptionHandlerService exceptionHandlerService,
                                UserDetailsService userDetailsService) {
        this.secretKey = secretKey;
        this.tokenService = tokenService;
        this.urlSecurityService = urlSecurityService;
        this.exceptionHandlerService = exceptionHandlerService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider(userDetailsService));
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/rest/non-security/**",
                "/rest/v2/api-docs",
                "/rest/configuration/ui/**",
                "/rest/swagger-resources/**",
                "/rest/configuration/security/**",
                "/rest/swagger-ui.html",
                "/rest/webjars/**");

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Отключаем csrf хак
        http.csrf().disable()
                .httpBasic()
//                .authenticationEntryPoint(new SimpleCorsFilter())
                .and()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint(exceptionHandlerService))
                .and()
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                //Фильтер для проверки http request'а на наличие правильного токена
                .addFilterBefore(new AuthenticationTokenProcessingFilter(userDetailsService, tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new ExceptionHandlerFilter(exceptionHandlerService), ExceptionTranslationFilter.class)
                //Фильтер для проверки URL'ов.
                .addFilter(filter(urlSecurityService));
    }

    private CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**Фильтер для проверки URL'ов.*/
//    @Bean
//    @Autowired
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
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder(secretKey);
    }

    @Bean
    @Autowired
    public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
