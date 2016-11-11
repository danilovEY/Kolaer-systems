package ru.kolaer.server.webportal.mvc.model.ldap.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntityBase;
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
import java.util.stream.Collectors;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 31.08.2016.
 */
@Repository
public class AccountLDAPImpl implements AccountLDAP {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeLDAPImpl.class);

    @Autowired
    private InitialLdapContext ldapContext;


    @Override
    public GeneralAccountsEntity getAccountByLogin(String login) {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "samaccountname", "memberOf", "userprincipalname"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            final GeneralAccountsEntity generalEmployeesEntity = new GeneralAccountsEntityBase();
            final Attributes attributes = answer.next().getAttributes();
            generalEmployeesEntity.setUsername(attributes.get("samaccountname").get().toString());
            generalEmployeesEntity.setEmail(attributes.get("userprincipalname").get().toString());
            final Collection<? extends GrantedAuthority> rolesFromAttributes = ToolsLDAP.getRolesFromAttributes(attributes);

            generalEmployeesEntity.setRoles(rolesFromAttributes.stream().map(role -> {
                final GeneralRolesEntity generalRolesEntity = new GeneralRolesEntityBase();
                generalRolesEntity.setType(role.getAuthority());
                return generalRolesEntity;
            }).collect(Collectors.toList()));

            answer.close();
            return generalEmployeesEntity;
        } catch (NamingException e) {
            LOG.error("Ошибка при получении аккаунта!", e);
        }
        return null;
    }

    @Override
    public byte[] getPhotoByLogin(String login) {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{"userprincipalname", "jpegPhoto"});
        LOG.debug("Login: {}", login);
        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            if(answer.hasMoreElements()) {
                final Attributes attributes = answer.next().getAttributes();
                LOG.debug("Photo: {}", attributes);
                Attribute jpegphoto = attributes.get("jpegphoto");

                if(jpegphoto != null) {
                    LOG.debug("Photo is have!");
                    return (byte[]) jpegphoto.get(0);
                }
            }

        } catch (NamingException e) {
            LOG.error("Ошибка при получении аккаунта!", e);
        }
        return null;
    }
}
