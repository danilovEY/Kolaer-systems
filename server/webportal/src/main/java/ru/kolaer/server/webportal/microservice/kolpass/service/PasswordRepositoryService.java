package ru.kolaer.server.webportal.microservice.kolpass.service;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.kolpass.dto.RepositoryPasswordFilter;
import ru.kolaer.server.webportal.microservice.kolpass.dto.RepositoryPasswordSort;
import ru.kolaer.server.webportal.microservice.sync.UpdatableEmployeeService;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryService extends DefaultService<PasswordRepositoryDto>, UpdatableEmployeeService {
    Page<PasswordRepositoryDto> getAll(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                       Integer number, Integer pageSize);

    Page<PasswordRepositoryDto> getAllShared(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                             Integer number, Integer pageSize);

    PasswordHistoryDto addPassword(Long repId, PasswordHistoryDto passwordHistoryDto);

    Page<PasswordHistoryDto> getHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    void deleteByIdRep(Long id);

    void clearRepository(Long repId);

    void shareRepository(Long repId, Long accountId);

    void deleteAccountFromShare(Long repId, Long accountId);
    List<AccountDto> getAllAccountFromShare(Long repId);

    void deletePassword(Long repId, Long passId);

    PasswordHistoryDto getLastHistoryInRepository(Long repId);
}
