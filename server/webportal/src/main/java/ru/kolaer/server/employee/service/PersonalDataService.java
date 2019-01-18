package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.PersonalDataMapper;
import ru.kolaer.server.employee.model.dto.PersonalDataDto;
import ru.kolaer.server.employee.repository.PersonalDataRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;
    private final PersonalDataMapper personalDataMapper;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository,
            PersonalDataMapper personalDataMapper) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
    }

    public List<PersonalDataDto> findPersonalDataByEmployeeId(@Min(1) long employeeId) {
        return personalDataRepository.findByEmployeeId(employeeId)
                .stream()
                .map(personalDataMapper::mapPersonalDataDto)
                .collect(Collectors.toList());
    }

}
