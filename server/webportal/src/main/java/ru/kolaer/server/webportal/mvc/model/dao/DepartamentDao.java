package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartamentDao extends DaoStandard<GeneralDepartamentEntity> {
    GeneralDepartamentEntity findByName(String name);
}
