package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.dto.vacation.VacationBalanceDto;
import ru.kolaer.server.webportal.model.dto.vacation.VacationDto;
import ru.kolaer.server.webportal.model.dto.vacation.VacationPeriodDto;
import ru.kolaer.server.webportal.model.entity.vacation.VacationBalanceEntity;
import ru.kolaer.server.webportal.model.entity.vacation.VacationEntity;
import ru.kolaer.server.webportal.model.entity.vacation.VacationPeriodEntity;

import java.util.List;


public interface VacationConverter extends BaseConverter<VacationDto, VacationEntity> {
    VacationPeriodDto convertToDto(VacationPeriodEntity entity);
    List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities);

    VacationBalanceDto convertToDto(VacationBalanceEntity balance);
}
