package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.MilitaryRegistrationMapper;
import ru.kolaer.server.employee.model.dto.MilitaryRegistrationDto;
import ru.kolaer.server.employee.repository.MilitaryRegistrationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MilitaryRegistrationService {
    private final MilitaryRegistrationRepository militaryRegistrationRepository;
    private final MilitaryRegistrationMapper militaryRegistrationMapper;

    public MilitaryRegistrationService(MilitaryRegistrationRepository militaryRegistrationRepository,
            MilitaryRegistrationMapper militaryRegistrationMapper) {
        this.militaryRegistrationRepository = militaryRegistrationRepository;
        this.militaryRegistrationMapper = militaryRegistrationMapper;
    }

    public List<MilitaryRegistrationDto> findMilitaryRegistrationsByEmployeeId(@Min(1) long employeeId) {
        return militaryRegistrationRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(militaryRegistrationMapper::mapToMilitaryRegistrationDto)
                .collect(Collectors.toList());
    }
}
