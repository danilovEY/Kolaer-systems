package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.PersonalDocumentMapper;
import ru.kolaer.server.employee.model.dto.PersonalDocumentDto;
import ru.kolaer.server.employee.repository.PersonalDocumentRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonalDocumentService {
    private final PersonalDocumentRepository personalDocumentRepository;
    private final PersonalDocumentMapper personalDocumentMapper;

    @Autowired
    public PersonalDocumentService(PersonalDocumentRepository personalDocumentRepository, PersonalDocumentMapper personalDocumentMapper) {
        this.personalDocumentRepository = personalDocumentRepository;
        this.personalDocumentMapper = personalDocumentMapper;
    }

    @Transactional(readOnly = true)
    public List<PersonalDocumentDto> findPersonalDocumentByEmployeeId(@Min(1) long employeeId) {
        return personalDocumentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(personalDocumentMapper::convertToDto)
                .collect(Collectors.toList());
    }

}
