package ru.kolaer.server.webportal.security.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.config.SpringSecurityConfig;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;
import java.util.*;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 29.08.2016.
 */
public class CustomLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {
    private final Logger LOG = LoggerFactory.getLogger(CustomLdapAuthenticationProvider.class);
    private final String[] userAttributes = {
            "distinguishedName","cn","name","uid",
            "sn","givenname","memberOf","samaccountname",
            "userPrincipalName"
    };

    private String dc;
    private String server;
    private boolean ssl;


    @Override
    protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
        final Hashtable props = new Hashtable();
        String principalName = auth.getName() + "@" + this.dc;
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, auth.getCredentials().toString());


        String ldapURL;
        if(!this.ssl){
            ldapURL = "ldap://";
        } else {
            ldapURL = "ldaps://";
            props.put(Context.SECURITY_PROTOCOL, "ssl");
        }

        ldapURL +=this.server + "." + this.dc;

        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ldapURL);

        try{
            final InitialLdapContext context = new InitialLdapContext(props, null);

            final SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);
            controls.setReturningAttributes(userAttributes);

            final NamingEnumeration<SearchResult> answer = context.search( "DC=kolaer,DC=local", "(& (userPrincipalName="+principalName+")(objectClass=person))", controls);
            final DirContextAdapter dir = new DirContextAdapter(answer.next().getAttributes(), new LdapName("dn="+auth.getName()), new LdapName("userPrincipalName="+principalName));
            answer.close();
            return dir;
        }
        catch(NamingException e){
            LOG.error("Ошибка при парсинге атрибутов!", e);
        }

        return null;
    }

    @Override
    protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
        LOG.debug("Dir: {}", userData);
        final List<GrantedAuthority> roles = new ArrayList<>();
        try {
            final Attribute cn = userData.getAttributes().get("memberOf");
            for(int i = 0; i< cn.size(); i++) {
                final String value = (String) cn.get(i);
                final String group = value.substring(3, value.indexOf(','));
                final SimpleGrantedAuthority simpleGrantedAuthority = this.getRole(group);
                if(simpleGrantedAuthority != null)
                    roles.add(simpleGrantedAuthority);
            }
        } catch (NamingException e) {
            LOG.error("Ошибка при парсинге атрибутов!", e);
        }
        return roles;
    }

    private SimpleGrantedAuthority getRole(String role) {
        switch (role) {
            case "Domain users": return new SimpleGrantedAuthority("USER");
            case "OIT": return new SimpleGrantedAuthority("SUPER_ADMIN");
            default: return null;
        }
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
