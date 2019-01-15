package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.EducationMapper;
import ru.kolaer.server.employee.model.dto.EducationDto;
import ru.kolaer.server.employee.repository.EducationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    @Autowired
    public EducationService(EducationRepository educationRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    public List<EducationDto> findEducations(@Min(1) long employeeId) {
        return educationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(educationMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
