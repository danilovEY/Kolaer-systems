package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kolaer.server.webportal.security.ldap.ToolsLDAP;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.Collection;
import java.util.Hashtable;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 26.08.2016.
 */
public class UserDetailsServiceLDAP implements UserDetailsService {
    private final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceLDAP.class);

    @Autowired
    private ToolsLDAP toolsLDAP;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Hashtable props = new Hashtable();
        String principalName = username + "@" + this.dc;
        props.put(Context.SECURITY_PRINCIPAL, this.admin);
        props.put(Context.SECURITY_CREDENTIALS, this.pass);

        String ldapURL;
        if(!this.ssl){
            ldapURL = "ldap://";
        } else {
            ldapURL = "ldaps://";
            props.put(Context.SECURITY_PROTOCOL, "ssl");
        }

        ldapURL += this.server + "." + this.dc;

        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ldapURL);

        try{
            final InitialLdapContext context = new InitialLdapContext(props, null);

            final SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);
            controls.setReturningAttributes(userAttributes);

            final NamingEnumeration<SearchResult> answer = context.search("DC=kolaer,DC=local", "(& (userPrincipalName="+principalName+")(objectClass=person))", controls);

            final Collection<? extends GrantedAuthority> roles;

            roles = toolsLDAP.getRolesFromAttributes(answer.next().getAttributes());

            answer.close();
            final UserDetails userDetails = new User(username, "123", true,true,true,true, roles);
            return userDetails;
        }
        catch(NamingException e){
            LOG.error("Ошибка при парсинге атрибутов!", e);
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
