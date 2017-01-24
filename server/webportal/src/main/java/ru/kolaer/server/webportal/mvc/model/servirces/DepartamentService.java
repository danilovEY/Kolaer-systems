package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartamentService extends ServiceBase<DepartmentEntity> {
    DepartmentEntity getGeneralDepartamentEntityByName(String name);
}
