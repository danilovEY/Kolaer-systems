package ru.kolaer.server.webportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;
import ru.kolaer.server.webportal.security.AuthenticationTokenProcessingFilter;
import ru.kolaer.server.webportal.security.SecurityMetadataSourceFilter;
import ru.kolaer.server.webportal.security.UnauthorizedEntryPoint;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

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
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**Сервис для проверки на наличии пользователя в системе.*/
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UrlPathService urlPathService;

    /**Дао для получение из БД существующие ссылки и права на них.*/
    @Autowired
    private UrlPathDao urlPathDao;

    /**Дао для получение ролей из БД.*/
    @Autowired
    private RoleDao roleDao;

    /**Секретный ключ для шифрования пароля.*/
    @Value("${secret_key}")
    private String secretKey;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProviderLDAP());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**").antMatchers("/app/**").antMatchers("/rest/non-security/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Отключаем csrf хак
        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        .httpBasic()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
        //Фильтер для проверки http request'а на наличие правильного токена
        .addFilterBefore(new AuthenticationTokenProcessingFilter(this.userDetailsService), UsernamePasswordAuthenticationFilter.class)
        //Фильтер для проверки URL'ов.
        .addFilter(filter());
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
    public DefaultSpringSecurityContextSource ldapDataSource() {
        DefaultSpringSecurityContextSource dataSource = new DefaultSpringSecurityContextSource("ldap://aerdc01.kolaer.local:389/dc=kolaer,dc=local");
        dataSource.setUserDn("admindc");
        dataSource.setPassword("2Serdce3");
        return dataSource;
    }


    private AbstractLdapAuthenticationProvider authProviderLDAP() {
        final AbstractLdapAuthenticationProvider authProvider = new AbstractLdapAuthenticationProvider(){
            private final Logger LOG = LoggerFactory.getLogger(SpringSecurityConfig.class);

            @Override
            protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
                LOG.info("UP: {}", auth);
                LOG.info("UP.name: {}", auth.getName());

                Hashtable props = new Hashtable();
                String principalName = auth.getName() + "@kolaer.local";
                System.out.println(principalName);
                props.put(Context.SECURITY_PRINCIPAL, principalName);
                props.put(Context.SECURITY_CREDENTIALS, auth.getCredentials().toString());


                String ldapURL = "ldap://aerdc01.kolaer.local/";
                props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                props.put(Context.PROVIDER_URL, ldapURL);
                //props.put(Context.SECURITY_PROTOCOL, "ssl");
                try{
                    InitialLdapContext context = new InitialLdapContext(props, null);

                    SearchControls controls = new SearchControls();
                    controls.setSearchScope(SUBTREE_SCOPE);
                    controls.setReturningAttributes(new String [] {
                            "nsRole",
                            "userpassword",
                            "uid",
                            "objectClass",
                            "givenName",
                            "sn",
                            "cn"
                    });

                    NamingEnumeration<SearchResult> answer = context.search( "DC=kolaer,DC=local", "(& (userPrincipalName="+principalName+")(objectClass=person))", controls);
                    DirContextAdapter dir = new DirContextAdapter(answer.next().getAttributes(), new LdapName("dn="+auth.getName()), new LdapName("userPrincipalName="+principalName));
                    answer.close();
                    return dir;
                }
                catch(NamingException e){
                    e.printStackTrace();
                }


                return null;

            }

            @Override
            protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
                LOG.info("Dir: {}", userData);
                LOG.info("Dir.username: {}", username);
                LOG.info("Dir.pass: {}", password);
                return Collections.emptyList();
            }
        };
        return authProvider;
    }

    /**Создание provider для проверки пользователей из БД с шифрованием пароля.*/
    private DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(new StandardPasswordEncoder(this.secretKey));
        return authProvider;
    }
}
