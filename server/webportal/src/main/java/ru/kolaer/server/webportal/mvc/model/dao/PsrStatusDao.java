package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;

/**
 * Created by Danilov on 07.08.2016.
 */
public interface PsrStatusDao extends DaoStandard<PsrStatus> {
    PsrStatus getStatusByType(String type);
}
