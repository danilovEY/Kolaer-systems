package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartamentService extends ServiceBase<GeneralDepartamentEntity> {
    GeneralDepartamentEntity getGeneralDepartamentEntityByName(String name);
}
