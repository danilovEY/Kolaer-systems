package ru.kolaer.server.vacation.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.vacation.model.dto.VacationBalanceDto;
import ru.kolaer.server.vacation.model.dto.VacationDto;
import ru.kolaer.server.vacation.model.dto.VacationPeriodDto;
import ru.kolaer.server.vacation.model.entity.VacationBalanceEntity;
import ru.kolaer.server.vacation.model.entity.VacationEntity;
import ru.kolaer.server.vacation.model.entity.VacationPeriodEntity;

import java.util.List;


public interface VacationConverter extends BaseConverter<VacationDto, VacationEntity> {
    VacationPeriodDto convertToDto(VacationPeriodEntity entity);
    List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities);

    VacationBalanceDto convertToDto(VacationBalanceEntity balance);
}
