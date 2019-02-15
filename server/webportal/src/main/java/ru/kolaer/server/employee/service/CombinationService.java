package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.CombinationMapper;
import ru.kolaer.server.employee.model.dto.CombinationDto;
import ru.kolaer.server.employee.repository.CombinationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class CombinationService {
    private final CombinationRepository combinationRepository;
    private final CombinationMapper combinationMapper;

    @Autowired
    public CombinationService(CombinationRepository combinationRepository, CombinationMapper combinationMapper) {
        this.combinationRepository = combinationRepository;
        this.combinationMapper = combinationMapper;
    }

    @Transactional(readOnly = true)
    public List<CombinationDto> findCombinationByEmployeeId(@Min(1) long employeeId) {
        return combinationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(combinationMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
