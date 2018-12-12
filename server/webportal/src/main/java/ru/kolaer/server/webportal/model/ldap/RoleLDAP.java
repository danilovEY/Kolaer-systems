package ru.kolaer.server.webportal.model.ldap;


import ru.kolaer.server.webportal.model.dto.account.RoleDto;

import java.util.List;

/**
 * Created by danilovey on 11.11.2016.
 */
public interface RoleLDAP {
    List<RoleDto> findAllRoles();
}
