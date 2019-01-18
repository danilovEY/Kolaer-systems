package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.PunishmentMapper;
import ru.kolaer.server.employee.model.dto.PunishmentDto;
import ru.kolaer.server.employee.repository.PunishmentRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PunishmentService {
    private final PunishmentRepository punishmentRepository;
    private final PunishmentMapper punishmentMapper;

    public PunishmentService(PunishmentRepository punishmentRepository,
            PunishmentMapper punishmentMapper) {
        this.punishmentRepository = punishmentRepository;
        this.punishmentMapper = punishmentMapper;
    }

    public List<PunishmentDto> findPunishmentsByEmployeeId(@Min(1) long employeeId) {
        return punishmentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(punishmentMapper::mapToPunishmentDto)
                .collect(Collectors.toList());
    }
}
