package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.StaffMovementMapper;
import ru.kolaer.server.employee.model.dto.StaffMovementDto;
import ru.kolaer.server.employee.repository.StaffMovementRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class StaffMovementService {
    private final StaffMovementRepository staffMovementRepository;
    private final StaffMovementMapper staffMovementMapper;

    @Autowired
    public StaffMovementService(StaffMovementRepository staffMovementRepository, StaffMovementMapper staffMovementMapper) {
        this.staffMovementRepository = staffMovementRepository;
        this.staffMovementMapper = staffMovementMapper;
    }

    @Transactional(readOnly = true)
    public List<StaffMovementDto> findStaffMovementsByEmployeeId(@Min(1) long employeeId) {
        return staffMovementRepository.findByEmployeeId(employeeId)
                .stream()
                .map(staffMovementMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
