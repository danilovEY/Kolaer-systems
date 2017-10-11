package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryService extends DefaultService<PasswordRepositoryDto> {
    Page<PasswordRepositoryDto> getAllByPnumber(Long pnumber, Integer number, Integer pageSize);

    PasswordRepositoryDto getByNameAndPnumber(String name, Long pnumber);

    List<PasswordRepositoryDto> getAllByPnumbers(List<Long> idsChief);
}
