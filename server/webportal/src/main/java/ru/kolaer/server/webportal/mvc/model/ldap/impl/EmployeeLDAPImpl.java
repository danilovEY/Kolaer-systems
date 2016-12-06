package ru.kolaer.server.webportal.mvc.model.ldap.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 31.08.2016.
 */
@Repository
public class EmployeeLDAPImpl implements EmployeeLDAP {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeLDAPImpl.class);

    @Autowired
    private InitialLdapContext ldapContext;

    @Override
    public GeneralEmployeesEntity getEmployeeByLogin(String login) {
        LOG.debug("Поиск аккаунта: {}", login);

        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "name", "employeeID"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            final GeneralEmployeesEntity generalEmployeesEntity = new GeneralEmployeesEntityBase();
            final Attributes attributes = answer.next().getAttributes();
            final Attribute name = attributes.get("employeeID");
            if(name != null) {
                generalEmployeesEntity.setPnumber(Integer.valueOf(name.get().toString()));
            }
            generalEmployeesEntity.setInitials(attributes.get("name").get().toString());

            answer.close();
            return generalEmployeesEntity;
        } catch (NamingException e) {
            LOG.error("Ошибка при получении аккаунта!", e);
            throw new BadRequestException("Аккаунт: " + login + " не найден!");
        }
    }
}
