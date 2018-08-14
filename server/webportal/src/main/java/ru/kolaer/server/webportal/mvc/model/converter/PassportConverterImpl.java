package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dto.passport.PassportDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PassportConverterImpl implements PassportConverter {
    private final EmployeeConverter employeeConverter;

    public PassportConverterImpl(EmployeeConverter employeeConverter) {
        this.employeeConverter = employeeConverter;
    }

    @Override
    public PassportEntity convertToModel(PassportDto dto) {
        PassportEntity passportEntity = new PassportEntity();
        passportEntity.setId(dto.getId());
        passportEntity.setNumber(dto.getNumber());
        passportEntity.setSerial(dto.getSerial());

        if(dto.getEmployee() == null) {
            passportEntity.setEmployeeId(null);
        } else {
            passportEntity.setEmployeeId(dto.getEmployee().getId());
        }

        return passportEntity;
    }

    @Override
    public PassportDto convertToDto(PassportEntity model) {
        PassportDto passportEntity = new PassportDto();
        passportEntity.setId(model.getId());
        passportEntity.setNumber(model.getNumber());
        passportEntity.setSerial(model.getSerial());

        if(model.getEmployeeId() == null) {
            passportEntity.setEmployee(null);
        } else {
            passportEntity.setEmployee(employeeConverter.convertToDto(model.getEmployee()));
        }

        return passportEntity;
    }

    @Override
    public PassportDto updateData(PassportDto oldDto, PassportEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setNumber(newModel.getNumber());
        oldDto.setSerial(newModel.getSerial());

        if(newModel.getEmployeeId() == null) {
            oldDto.setEmployee(null);
        } else {
            oldDto.setEmployee(employeeConverter.convertToDto(newModel.getEmployee()));
        }

        return oldDto;
    }
}
