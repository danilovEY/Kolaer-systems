package ru.kolaer.server.webportal.model.dao;


import ru.kolaer.server.webportal.model.entity.general.AccountEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountDao extends DefaultDao<AccountEntity> {
    AccountEntity findName(String username);

    List<AccountEntity> findByEmployeeIds(List<Long> employeeIds);
}
