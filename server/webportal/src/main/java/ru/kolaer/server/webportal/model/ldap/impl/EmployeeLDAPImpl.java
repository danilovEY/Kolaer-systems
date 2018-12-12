package ru.kolaer.server.webportal.model.ldap.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.model.ldap.EmployeeLDAP;

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
@Slf4j
public class EmployeeLDAPImpl implements EmployeeLDAP {

    private final InitialLdapContext ldapContext;

    @Autowired
    public EmployeeLDAPImpl(InitialLdapContext ldapContext) {
        this.ldapContext = ldapContext;
    }

    @Override
    public EmployeeDto getEmployeeByLogin(String login) {
        log.debug("Поиск аккаунта: {}", login);

        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "name", "employeeID"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=" + login + "@kolaer.local" + ")(objectClass=person))", controls);

            final EmployeeDto employeeEntity = new EmployeeDto();
            final Attributes attributes = answer.next().getAttributes();
            final Attribute name = attributes.get("employeeID");
            if(name != null) {
                employeeEntity.setPersonnelNumber(Long.valueOf(name.get().toString()));
            }
            employeeEntity.setInitials(attributes.get("name").get().toString());

            answer.close();
            return employeeEntity;
        } catch (NamingException e) {
            log.error("Ошибка при получении аккаунта!", e);
            throw new NotFoundDataException("Аккаунт: " + login + " не найден!");
        }
    }
}
