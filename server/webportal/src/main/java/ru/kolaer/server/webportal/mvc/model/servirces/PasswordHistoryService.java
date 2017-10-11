package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordHistoryService extends DefaultService<PasswordHistoryDto> {
    Page<PasswordHistoryDto> getHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    void deleteByIdRep(Long id);
}
