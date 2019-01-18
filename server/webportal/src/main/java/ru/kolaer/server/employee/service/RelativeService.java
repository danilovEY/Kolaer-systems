package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.RelativeMapper;
import ru.kolaer.server.employee.model.dto.RelativeDto;
import ru.kolaer.server.employee.repository.RelativeRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class RelativeService {

    private final RelativeRepository relativeRepository;
    private final RelativeMapper relativeMapper;

    @Autowired
    public RelativeService(RelativeRepository relativeRepository,
            RelativeMapper relativeMapper) {
        this.relativeRepository = relativeRepository;
        this.relativeMapper = relativeMapper;
    }

    public List<RelativeDto> findRelatives(@Min(1) long employeeId) {
        return relativeRepository.findByEmployeeId(employeeId)
                .stream()
                .map(relativeMapper::mapToRelativeDto)
                .collect(Collectors.toList());
    }
}
