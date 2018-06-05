package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 13.12.2016.
 */
public interface BankAccountDao extends DefaultDao<BankAccountEntity> {
    BankAccountEntity findByInitials(String initials);
    Integer getCountAllAccount();

    BankAccountEntity findByEmployeeId(Long employeeId);

    List<Long> findAllEmployeeIds();

    Optional<BankAccountEntity> findByCheck(String check);
}
