package ru.kolaer.server.webportal.security.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.kolaer.server.webportal.bean.ToolsLDAP;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;
import java.util.Collection;
import java.util.Hashtable;

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
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                this.messages.getMessage("LdapAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        final UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = userToken.getName();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Processing authentication request for user: " + username);
        }

        if (!StringUtils.hasLength(username)) {
            throw new BadCredentialsException(this.messages.getMessage(
                    "LdapAuthenticationProvider.emptyUsername", "Empty Username"));
        }

        DirContextOperations userData = doAuthentication(userToken);

        if(userData == null)
            return null;

        UserDetails user = this.userDetailsContextMapper.mapUserFromContext(userData,
                authentication.getName(),
                loadUserAuthorities(userData, authentication.getName(),
                        (String) authentication.getCredentials()));
        return createSuccessfulAuthentication(userToken, user);
    }

    @Override
    protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
        final Hashtable<String, Object> props = new Hashtable<>();
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
        }  catch(NamingException e){
            LOG.error("Ошибка при подключении к LDAP! {}", e.getMessage());
        }

        return null;
    }

    @Override
    protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
        LOG.debug("Dir: {}", userData);
        return ToolsLDAP.getRolesFromAttributes(userData.getAttributes());
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
