package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationBalanceDto;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationDto;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.VacationPeriodDto;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationBalanceEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationPeriodEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationConverterImpl implements VacationConverter {

    @Override
    public VacationEntity convertToModel(VacationDto dto) {
        if (dto == null) return null;

        VacationEntity vacationEntity = new VacationEntity();
        vacationEntity.setId(dto.getId());
        vacationEntity.setVacationDays(dto.getVacationDays());
        vacationEntity.setEmployeeId(dto.getEmployeeId());
        vacationEntity.setNote(dto.getNote());
        vacationEntity.setVacationFrom(dto.getVacationFrom());
        vacationEntity.setVacationTo(dto.getVacationTo());
        vacationEntity.setVacationType(dto.getVacationType());
        return vacationEntity;
    }

    @Override
    public VacationDto convertToDto(VacationEntity model) {
        return updateData(new VacationDto(), model);
    }

    @Override
    public VacationDto updateData(VacationDto oldDto, VacationEntity newModel) {
        if (oldDto == null || newModel == null) return null;

        oldDto.setId(newModel.getId());
        oldDto.setVacationDays(newModel.getVacationDays());
        oldDto.setEmployeeId(newModel.getEmployeeId());
        oldDto.setNote(newModel.getNote());
        oldDto.setVacationFrom(newModel.getVacationFrom());
        oldDto.setVacationTo(newModel.getVacationTo());
        oldDto.setVacationType(newModel.getVacationType());
        return oldDto;
    }

    @Override
    public VacationPeriodDto convertToDto(VacationPeriodEntity entity) {
        if (entity == null) return null;

        VacationPeriodDto vacationPeriodDto = new VacationPeriodDto();
        vacationPeriodDto.setId(entity.getId());
        vacationPeriodDto.setStatus(entity.getStatus());
        vacationPeriodDto.setYear(entity.getYear());
        return vacationPeriodDto;
    }

    @Override
    public List<VacationPeriodDto> convertToDtos(List<VacationPeriodEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();

        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public VacationBalanceDto convertToDto(VacationBalanceEntity balance) {
        if (balance == null) return null;

        VacationBalanceDto vacationBalanceDto = new VacationBalanceDto();
        vacationBalanceDto.setId(balance.getId());
        vacationBalanceDto.setEmployeeId(balance.getEmployeeId());
        vacationBalanceDto.setCurrentYearBalance(balance.getCurrentYearBalance());
        vacationBalanceDto.setNextYearBalance(balance.getNextYearBalance());
        vacationBalanceDto.setPrevYearBalance(balance.getPrevYearBalance());
        return vacationBalanceDto;
    }
}
