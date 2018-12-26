package ru.kolaer.server.account.dao;


import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.core.dao.DefaultDao;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountDao extends DefaultDao<AccountEntity> {
    AccountEntity findName(String username);

    List<AccountEntity> findByEmployeeIds(List<Long> employeeIds);
}
