package ru.kolaer.server.kolpass.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.core.service.UpdatableEmployeeService;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordFilter;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordSort;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryService extends DefaultService<PasswordRepositoryDto>, UpdatableEmployeeService {
    PageDto<PasswordRepositoryDto> getAll(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                       Integer number, Integer pageSize);

    PageDto<PasswordRepositoryDto> getAllShared(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                             Integer number, Integer pageSize);

    PasswordHistoryDto addPassword(Long repId, PasswordHistoryDto passwordHistoryDto);

    PageDto<PasswordHistoryDto> getHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    void deleteByIdRep(Long id);

    void clearRepository(Long repId);

    void shareRepository(Long repId, Long accountId);

    void deleteAccountFromShare(Long repId, Long accountId);
    List<AccountDto> getAllAccountFromShare(Long repId);

    void deletePassword(Long repId, Long passId);

    PasswordHistoryDto getLastHistoryInRepository(Long repId);
}
