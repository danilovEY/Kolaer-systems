package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryService extends DefaultService<PasswordRepositoryDto> {
    Page<PasswordRepositoryDto> getAllAuthAccount(Integer number, Integer pageSize);
    Page<PasswordRepositoryDto> getAllByAccountId(Long accountId, Integer number, Integer pageSize);

    List<PasswordRepositoryDto> getAllByAccountIds(List<Long> idsAccount);

    PasswordHistoryDto addPassword(Long repId, PasswordHistoryDto passwordHistoryDto);

    Page<PasswordHistoryDto> getHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    void deleteByIdRep(Long id);

    void clearRepository(Long repId);

    void deletePassword(Long repId, Long passId);

    PasswordHistoryDto getLastHistoryInRepository(Long repId);
}
