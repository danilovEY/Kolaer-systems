package ru.kolaer.server.webportal.microservice.vacation.converter;

import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.vacation.dto.VacationBalanceDto;
import ru.kolaer.server.webportal.microservice.vacation.dto.VacationDto;
import ru.kolaer.server.webportal.microservice.vacation.dto.VacationPeriodDto;
import ru.kolaer.server.webportal.microservice.vacation.entity.VacationBalanceEntity;
import ru.kolaer.server.webportal.microservice.vacation.entity.VacationEntity;
import ru.kolaer.server.webportal.microservice.vacation.entity.VacationPeriodEntity;

import java.util.List;


public interface VacationConverter extends BaseConverter<VacationDto, VacationEntity> {
    VacationPeriodDto convertToDto(VacationPeriodEntity entity);
    List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities);

    VacationBalanceDto convertToDto(VacationBalanceEntity balance);
}
