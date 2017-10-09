package ru.kolaer.server.webportal.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kolaer.server.webportal.beans.ToolsLDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.Collection;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 26.08.2016.
 */
@Slf4j
public class UserDetailsServiceLDAP implements UserDetailsService {
    private final InitialLdapContext ldapContext;

    private String server;
    private String dc;
    private boolean ssl;
    private String admin;
    private String pass;
    private final String[] userAttributes = {
            "distinguishedName","cn","name","uid",
            "sn","givenname","memberOf","samaccountname",
            "userPrincipalName"
    };

    public UserDetailsServiceLDAP(InitialLdapContext ldapContext) {
        this.ldapContext = ldapContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String principalName = username + "@" + this.dc;

        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(userAttributes);

        try{
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName="+principalName+")(objectClass=person))", controls);

            final Collection<? extends GrantedAuthority> roles;

            roles = ToolsLDAP.getRolesFromAttributes(answer.next().getAttributes());

            answer.close();
            return new User(username, "123", true,true,true,true, roles);
        }
        catch(NamingException e){
            log.error("Ошибка при парсинге атрибутов!", e);
        }

        return null;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
