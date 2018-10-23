package ru.kolaer.server.service.vacation.converter;

import ru.kolaer.server.service.vacation.dto.VacationBalanceDto;
import ru.kolaer.server.service.vacation.dto.VacationDto;
import ru.kolaer.server.service.vacation.dto.VacationPeriodDto;
import ru.kolaer.server.service.vacation.entity.VacationBalanceEntity;
import ru.kolaer.server.service.vacation.entity.VacationEntity;
import ru.kolaer.server.service.vacation.entity.VacationPeriodEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

import java.util.List;


public interface VacationConverter extends BaseConverter<VacationDto, VacationEntity> {
    VacationPeriodDto convertToDto(VacationPeriodEntity entity);
    List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities);

    VacationBalanceDto convertToDto(VacationBalanceEntity balance);
}
