package ru.kolaer.server.service.account.repository;


import ru.kolaer.server.service.account.entity.AccountEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountRepository extends DefaultRepository<AccountEntity> {
    AccountEntity findName(String username);

    List<AccountEntity> findByEmployeeIds(List<Long> employeeIds);
}
