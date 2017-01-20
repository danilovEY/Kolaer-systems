package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordHistory;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordHistoryService extends ServiceBase<RepositoryPasswordHistory> {
    Page<RepositoryPasswordHistory> getHistoryByIdRepository(Integer id, Integer number, Integer pageSize);
}
