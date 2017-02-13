package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordService extends ServiceBase<RepositoryPassword> {
    Page<RepositoryPassword> getAllByPnumber(Integer pnumber, Integer number, Integer pageSize);

    RepositoryPassword getByNameAndPnumber(String name, Integer pnumber);

    RepositoryPassword getRepositoryWithJoinById(Integer id);
}
