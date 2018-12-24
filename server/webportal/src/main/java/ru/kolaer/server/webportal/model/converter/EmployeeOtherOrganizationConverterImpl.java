package ru.kolaer.server.webportal.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.model.entity.birthday.EmployeeOtherOrganizationEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class EmployeeOtherOrganizationConverterImpl implements EmployeeOtherOrganizationConverter {
    @Override
    public EmployeeOtherOrganizationEntity convertToModel(EmployeeOtherOrganizationDto dto) {
        EmployeeOtherOrganizationEntity employeeEntity = new EmployeeOtherOrganizationEntity();
        employeeEntity.setId(dto.getId());
        employeeEntity.setOrganization(dto.getOrganization());
        employeeEntity.setEmail(dto.getEmail());
        employeeEntity.setFirstName(dto.getFirstName());
        employeeEntity.setSecondName(dto.getSecondName());
        employeeEntity.setThirdName(dto.getThirdName());
        employeeEntity.setBirthday(dto.getBirthday());
        employeeEntity.setWorkPhoneNumber(dto.getWorkPhoneNumber());
        employeeEntity.setInitials(dto.getInitials());
        employeeEntity.setCategory(dto.getCategory());
        employeeEntity.setDepartment(dto.getDepartment());
        employeeEntity.setPost(dto.getPost());
        employeeEntity.setGender(dto.getGender());

        return employeeEntity;
    }

    @Override
    public EmployeeOtherOrganizationDto convertToDto(EmployeeOtherOrganizationEntity model) {
        EmployeeOtherOrganizationDto dto = new EmployeeOtherOrganizationDto();
        dto.setId(model.getId());
        dto.setOrganization(model.getOrganization());
        dto.setEmail(model.getEmail());
        dto.setFirstName(model.getFirstName());
        dto.setSecondName(model.getSecondName());
        dto.setThirdName(model.getThirdName());
        dto.setBirthday(model.getBirthday());
        dto.setWorkPhoneNumber(model.getWorkPhoneNumber());
        dto.setInitials(model.getInitials());
        dto.setCategory(model.getCategory());
        dto.setDepartment(model.getDepartment());
        dto.setPost(model.getPost());
        dto.setGender(model.getGender());
        return dto;
    }

    @Override
    public EmployeeOtherOrganizationDto updateData(EmployeeOtherOrganizationDto oldDto, EmployeeOtherOrganizationEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setEmail(newModel.getEmail());
        oldDto.setOrganization(newModel.getOrganization());
        oldDto.setFirstName(newModel.getFirstName());
        oldDto.setSecondName(newModel.getSecondName());
        oldDto.setThirdName(newModel.getThirdName());
        oldDto.setBirthday(newModel.getBirthday());
        oldDto.setWorkPhoneNumber(newModel.getWorkPhoneNumber());
        oldDto.setInitials(newModel.getInitials());
        oldDto.setCategory(newModel.getCategory());
        oldDto.setDepartment(newModel.getDepartment());
        oldDto.setPost(newModel.getPost());
        oldDto.setGender(newModel.getGender());
        return oldDto;
    }
}
