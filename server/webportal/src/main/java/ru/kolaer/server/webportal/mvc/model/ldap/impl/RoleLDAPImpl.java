package ru.kolaer.server.webportal.mvc.model.ldap.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntityBase;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 11.11.2016.
 */
@Repository
public class RoleLDAPImpl implements RoleLDAP {
    private final Logger LOG = LoggerFactory.getLogger(RoleLDAPImpl.class);

    @Autowired
    private InitialLdapContext ldapContext;

    @Override
    public List<GeneralRolesEntity> findAllRoles() {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "cn"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(objectclass=group)", controls);
            int i=0;
            final List<GeneralRolesEntity> roles = new ArrayList<>();
            while (answer.hasMoreElements()) {
                final String groupName = answer.next().getAttributes().get("cn").get().toString();
                final GeneralRolesEntity generalRolesEntity = new GeneralRolesEntityBase();
                generalRolesEntity.setId(i++);
                generalRolesEntity.setType(groupName);

                roles.add(generalRolesEntity);
            }
            return roles;
        } catch (NamingException e) {
            LOG.error("Ошибка при получении аккаунта!", e);
        }

        return Collections.emptyList();
    }
}
