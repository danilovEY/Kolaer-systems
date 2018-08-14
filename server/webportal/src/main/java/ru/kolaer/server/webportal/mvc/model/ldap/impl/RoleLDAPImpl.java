package ru.kolaer.server.webportal.mvc.model.ldap.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dto.account.RoleDto;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
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
@Slf4j
public class RoleLDAPImpl implements RoleLDAP {
    private final InitialLdapContext ldapContext;

    @Autowired
    public RoleLDAPImpl(InitialLdapContext ldapContext) {
        this.ldapContext = ldapContext;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "cn"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(objectclass=group)", controls);
            final List<RoleDto> roles = new ArrayList<>();
            while (answer.hasMoreElements()) {
                final String groupName = answer.next().getAttributes().get("cn").get().toString();
                final RoleDto roleEntity = new RoleDto();
                roleEntity.setType(groupName);

                roles.add(roleEntity);
            }
            return roles;
        } catch (NamingException e) {
            log.error("Ошибка при получении аккаунта!", e);
        }

        return Collections.emptyList();
    }
}
