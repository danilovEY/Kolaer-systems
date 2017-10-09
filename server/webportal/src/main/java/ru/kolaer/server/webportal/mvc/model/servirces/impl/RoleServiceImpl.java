package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.RoleDto;
import ru.kolaer.server.webportal.mvc.model.converter.RoleConverter;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.RoleEntity;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class RoleServiceImpl extends AbstractDefaultService<RoleDto, RoleEntity> implements RoleService {

    private final RoleLDAP roleDao;

    protected RoleServiceImpl(RoleDao roleDao, RoleConverter converter, RoleLDAP roleLDAP) {
        super(roleDao, converter);
        this.roleDao = roleLDAP;
    }

}
