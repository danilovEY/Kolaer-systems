package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.Optional;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
@RequiredArgsConstructor
public class EmployeeConverterImpl implements EmployeeConverter {
    private final PostConverterImpl postConverter;
    private final DepartmentConverterImpl departmentConverter;

    @Override
    public EmployeeEntity convertToModel(EmployeeDto dto) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(dto.getId());
        employeeEntity.setEmail(dto.getEmail());
        employeeEntity.setFirstName(dto.getFirstName());
        employeeEntity.setSecondName(dto.getSecondName());
        employeeEntity.setThirdName(dto.getThirdName());
        employeeEntity.setBirthday(dto.getBirthday());
        employeeEntity.setDismissalDate(dto.getDismissalDate());
        employeeEntity.setEmploymentDate(dto.getEmploymentDate());
        employeeEntity.setHomePhoneNumber(dto.getHomePhoneNumber());
        employeeEntity.setWorkPhoneNumber(dto.getWorkPhoneNumber());
        employeeEntity.setGender(dto.getGender());
        employeeEntity.setInitials(dto.getInitials());
        employeeEntity.setPhoto(dto.getPhoto());
        employeeEntity.setCategory(dto.getCategory());

        employeeEntity.setPostId(Optional.ofNullable(dto.getPost())
                .map(PostDto::getId)
                .orElse(null));

        employeeEntity.setDepartmentId(Optional.ofNullable(dto.getDepartment())
                .map(DepartmentDto::getId)
                .orElse(null));

        return employeeEntity;
    }

    @Override
    public EmployeeDto convertToDto(EmployeeEntity model) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(model.getId());
        employeeDto.setEmail(model.getEmail());
        employeeDto.setFirstName(model.getFirstName());
        employeeDto.setSecondName(model.getSecondName());
        employeeDto.setThirdName(model.getThirdName());
        employeeDto.setBirthday(model.getBirthday());
        employeeDto.setDismissalDate(model.getDismissalDate());
        employeeDto.setEmploymentDate(model.getEmploymentDate());
        employeeDto.setHomePhoneNumber(model.getHomePhoneNumber());
        employeeDto.setWorkPhoneNumber(model.getWorkPhoneNumber());
        employeeDto.setGender(model.getGender());
        employeeDto.setInitials(model.getInitials());
        employeeDto.setPhoto(model.getPhoto());
        employeeDto.setCategory(model.getCategory());

        if(model.getPostId() != null) {
            Optional.ofNullable(model.getPost())
                    .map(postConverter::convertToDto)
                    .ifPresent(employeeDto::setPost);
        }

        if(model.getDepartmentId() != null) {
            Optional.ofNullable(model.getDepartment())
                    .map(departmentConverter::convertToDto)
                    .ifPresent(employeeDto::setDepartment);
        }

        return employeeDto;
    }

    @Override
    public EmployeeDto updateData(EmployeeDto oldDto, EmployeeEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setEmail(newModel.getEmail());
        oldDto.setFirstName(newModel.getFirstName());
        oldDto.setSecondName(newModel.getSecondName());
        oldDto.setThirdName(newModel.getThirdName());
        oldDto.setBirthday(newModel.getBirthday());
        oldDto.setDismissalDate(newModel.getDismissalDate());
        oldDto.setEmploymentDate(newModel.getEmploymentDate());
        oldDto.setHomePhoneNumber(newModel.getHomePhoneNumber());
        oldDto.setWorkPhoneNumber(newModel.getWorkPhoneNumber());
        oldDto.setGender(newModel.getGender());
        oldDto.setInitials(newModel.getInitials());
        oldDto.setPhoto(newModel.getPhoto());
        oldDto.setCategory(newModel.getCategory());

        if(newModel.getPostId() != null && !newModel.getPostId().equals(Optional
                .ofNullable(oldDto.getPost())
                .map(PostDto::getId)
                .orElse(null))) {
            Optional.ofNullable(newModel.getPost())
                    .map(postConverter::convertToDto)
                    .ifPresent(oldDto::setPost);
        } else {
            oldDto.setPost(null);
        }

        if(newModel.getDepartmentId() != null && !newModel.getDepartmentId().equals(Optional
                .ofNullable(oldDto.getDepartment())
                .map(DepartmentDto::getId)
                .orElse(null))) {
            Optional.ofNullable(newModel.getDepartment())
                    .map(departmentConverter::convertToDto)
                    .ifPresent(oldDto::setDepartment);
        } else {
            oldDto.setDepartment(null);
        }

        return oldDto;
    }

    @Override
    public EmployeeDto convertToDtoWithOutSubEntity(EmployeeEntity model) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(model.getId());
        employeeDto.setEmail(model.getEmail());
        employeeDto.setFirstName(model.getFirstName());
        employeeDto.setSecondName(model.getSecondName());
        employeeDto.setThirdName(model.getThirdName());
        employeeDto.setBirthday(model.getBirthday());
        employeeDto.setDismissalDate(model.getDismissalDate());
        employeeDto.setEmploymentDate(model.getEmploymentDate());
        employeeDto.setHomePhoneNumber(model.getHomePhoneNumber());
        employeeDto.setWorkPhoneNumber(model.getWorkPhoneNumber());
        employeeDto.setGender(model.getGender());
        employeeDto.setInitials(model.getInitials());
        employeeDto.setPhoto(model.getPhoto());
        employeeDto.setCategory(model.getCategory());

        Optional.ofNullable(model.getDepartmentId())
                .map(DepartmentDto::new)
                .ifPresent(employeeDto::setDepartment);

        Optional.ofNullable(model.getPostId())
                .map(PostDto::new)
                .ifPresent(employeeDto::setPost);

        return employeeDto;
    }
}
