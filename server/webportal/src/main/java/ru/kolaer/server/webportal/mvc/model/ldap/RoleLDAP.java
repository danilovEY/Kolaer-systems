package ru.kolaer.server.webportal.mvc.model.ldap;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;

import java.util.List;

/**
 * Created by danilovey on 11.11.2016.
 */
public interface RoleLDAP {
    List<GeneralRolesEntity> findAllRoles();
}
