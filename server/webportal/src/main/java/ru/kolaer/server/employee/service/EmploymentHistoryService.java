package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.EmploymentHistoryMapper;
import ru.kolaer.server.employee.model.dto.EmploymentHistoryDto;
import ru.kolaer.server.employee.repository.EmploymentHistoryRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class EmploymentHistoryService {

    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final EmploymentHistoryMapper employmentHistoryMapper;

    @Autowired
    public EmploymentHistoryService(EmploymentHistoryRepository employmentHistoryRepository,
            EmploymentHistoryMapper employmentHistoryMapper) {
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.employmentHistoryMapper = employmentHistoryMapper;
    }

    public List<EmploymentHistoryDto> findEmploymentHistoryByEmployeeId(@Min(1) long employeeId) {
        return employmentHistoryRepository.findByEmployeeId(employeeId)
                .stream()
                .map(employmentHistoryMapper::mapToEmploymentHistoryDto)
                .collect(Collectors.toList());
    }
}
