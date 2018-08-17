package ru.kolaer.server.webportal.mvc.model.converter;

import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationBalanceDto;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationDto;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationPeriodDto;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationBalanceEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationPeriodEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.BaseConverter;

import java.util.List;


public interface VacationConverter extends BaseConverter<VacationDto, VacationEntity> {
    VacationPeriodDto convertToDto(VacationPeriodEntity entity);
    List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities);

    VacationBalanceDto convertToDto(VacationBalanceEntity balance);
}
