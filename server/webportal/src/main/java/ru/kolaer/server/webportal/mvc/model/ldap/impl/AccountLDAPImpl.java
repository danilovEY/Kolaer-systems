package ru.kolaer.server.webportal.mvc.model.ldap.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.beans.ToolsLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.Collection;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 31.08.2016.
 */
@Repository
@Slf4j
public class AccountLDAPImpl implements AccountLDAP {
    private final InitialLdapContext ldapContext;

    @Autowired
    public AccountLDAPImpl(InitialLdapContext ldapContext) {
        this.ldapContext = ldapContext;
    }

    @Override
    public AccountDto getAccountByLogin(String login) {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "samaccountname", "memberOf", "userprincipalname"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            final AccountDto generalAccountEntity = new AccountDto();
            final Attributes attributes = answer.next().getAttributes();
            generalAccountEntity.setUsername(attributes.get("samaccountname").get().toString());
            generalAccountEntity.setEmail(attributes.get("userprincipalname").get().toString());
            final Collection<? extends GrantedAuthority> rolesFromAttributes = ToolsLDAP.getRolesFromAttributes(attributes);

            /*generalAccountEntity.setRoles(rolesFromAttributes.stream().map(role -> {
                final RoleDto roleEntity = new RoleDto();
                roleEntity.setType(role.getAuthority());
                return roleEntity;
            }).collect(Collectors.toList()));*/ //TODO !!!!

            answer.close();
            return generalAccountEntity;
        } catch (NamingException e) {
            log.error("Ошибка при получении аккаунта!", e);
        }
        return null;
    }

    @Override
    public byte[] getPhotoByLogin(String login) {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{"userprincipalname", "jpegPhoto"});
        log.debug("Login: {}", login);
        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            if(answer.hasMoreElements()) {
                final Attributes attributes = answer.next().getAttributes();
                log.debug("Photo: {}", attributes);
                Attribute jpegphoto = attributes.get("jpegphoto");

                if(jpegphoto != null) {
                    log.debug("Photo is have!");
                    return (byte[]) jpegphoto.get(0);
                }
            }

        } catch (NamingException e) {
            log.error("Ошибка при получении аккаунта!", e);
        }
        return null;
    }
}
