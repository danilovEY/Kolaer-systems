package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.dto.FilterValue;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by danilovey on 13.12.2016.
 */
public interface BankAccountDao extends DefaultDao<BankAccountEntity> {
    BankAccountEntity findByInitials(String initials);
    Integer getCountAllAccount();

    BankAccountEntity findByEmployeeId(Long employeeId);

    List<Long> findAllEmployeeIds(Map<String, FilterValue> filters);

    Optional<BankAccountEntity> findByCheck(String check);
}
