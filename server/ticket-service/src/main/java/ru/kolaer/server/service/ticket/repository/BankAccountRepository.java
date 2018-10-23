package ru.kolaer.server.service.ticket.repository;

import ru.kolaer.server.service.ticket.entity.BankAccountEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.common.dto.FilterValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by danilovey on 13.12.2016.
 */
public interface BankAccountRepository extends DefaultRepository<BankAccountEntity> {
    BankAccountEntity findByInitials(String initials);
    Integer getCountAllAccount();

    BankAccountEntity findByEmployeeId(Long employeeId);

    List<Long> findAllEmployeeIds(Map<String, FilterValue> filters);

    Optional<BankAccountEntity> findByCheck(String check);
}
