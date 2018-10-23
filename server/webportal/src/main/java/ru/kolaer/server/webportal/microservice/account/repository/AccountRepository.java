package ru.kolaer.server.webportal.microservice.account.repository;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.account.entity.AccountEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountRepository extends DefaultRepository<AccountEntity> {
    AccountEntity findName(String username);

    List<AccountEntity> findByEmployeeIds(List<Long> employeeIds);
}
