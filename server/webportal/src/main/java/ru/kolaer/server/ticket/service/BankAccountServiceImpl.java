package ru.kolaer.server.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.model.dto.*;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.employee.converter.EmployeeConverter;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.ticket.dao.BankAccountDao;
import ru.kolaer.server.ticket.model.dto.BankAccountDto;
import ru.kolaer.server.ticket.model.entity.BankAccountEntity;
import ru.kolaer.server.ticket.model.request.BankAccountRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankAccountServiceImpl
        extends AbstractDefaultService<BankAccountDto, BankAccountEntity, BankAccountDao, BankAccountConverter>
        implements BankAccountService {

    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;

    protected BankAccountServiceImpl(BankAccountDao defaultEntityDao,
                                     BankAccountConverter converter,
                                     EmployeeDao employeeDao,
                                     EmployeeConverter employeeConverter) {
        super(defaultEntityDao, converter);
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
    }

    @Override
    @Transactional
    public BankAccountDto add(BankAccountRequest bankAccountRequest) {
        if (StringUtils.isEmpty(bankAccountRequest.getCheck())) {
            throw new UnexpectedRequestParams("Счет не должен быть пустым");
        }

        if (defaultEntityDao.findByCheck(bankAccountRequest.getCheck()).isPresent()) {
            throw new UnexpectedRequestParams("Такой счет уже существует");
        }

        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setCheck(bankAccountRequest.getCheck());
        bankAccountEntity.setEmployeeId(bankAccountRequest.getEmployeeId());

        return defaultConverter.convertToDto(defaultEntityDao.persist(bankAccountEntity));
    }

    @Override
    @Transactional
    public BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest) {
        if (StringUtils.isEmpty(bankAccountRequest.getCheck())) {
            throw new UnexpectedRequestParams("Счет не должен быть пустым");
        }

        BankAccountEntity bankAccountEntity = defaultEntityDao.findById(bankId);

        if (!bankAccountEntity.getCheck().equals(bankAccountRequest.getCheck()) &&
                defaultEntityDao.findByCheck(bankAccountRequest.getCheck()).isPresent()) {
            throw new UnexpectedRequestParams("Такой счет уже существует");
        }

        bankAccountEntity.setEmployeeId(bankAccountRequest.getEmployeeId());
        bankAccountEntity.setCheck(bankAccountRequest.getCheck());

        return defaultConverter.convertToDto(defaultEntityDao.update(bankAccountEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize) {
        Map<String, FilterValue> filtersForEmployee = new HashMap<>();
        filtersForEmployee.put("deleted", new FilterValue("deleted", false, FilterType.EQUAL));

        List<Long> allEmployeeIds = this.defaultEntityDao.findAllEmployeeIds(filtersForEmployee);

        Map<String, FilterValue> filters = getFilters(filterParam);
        filters.put("ids", new FilterValue("id", allEmployeeIds, FilterType.IN));

        SortField sort = getSortField(sortParam);


        Long employeeCount = employeeDao.findAllCount(filters);
        List<EmployeeDto> employeeAll = employeeConverter.convertToDto(employeeDao.findAll(sort, filters, number, pageSize));

        return new Page<>(employeeAll, number, employeeCount, pageSize);
    }

    @Override
    @Transactional
    public long delete(Long id) {
        BankAccountEntity bankAccountEntity = defaultEntityDao.findById(id);
        bankAccountEntity.setDeleted(true);

        defaultEntityDao.update(bankAccountEntity);
        return 1;
    }
}