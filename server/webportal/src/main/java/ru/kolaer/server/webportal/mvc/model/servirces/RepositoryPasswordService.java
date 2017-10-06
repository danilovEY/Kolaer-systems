package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordService extends ServiceBase<RepositoryPasswordDto> {
    Page<RepositoryPasswordDto> getAllByPnumber(Integer pnumber, Integer number, Integer pageSize);

    RepositoryPasswordDto getByNameAndPnumber(String name, Integer pnumber);

    List<RepositoryPasswordDto> getAllByPnumbers(List<Integer> idsChief);
}
