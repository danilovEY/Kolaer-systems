package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;
import ru.kolaer.server.webportal.mvc.model.servirces.PostService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
@RequiredArgsConstructor
public class EmployeeConverterImpl implements EmployeeConverter {
    private final PostService postService;
    private final PostConverter postConverter;
    private final DepartmentService departmentService;
    private final DepartmentConverter departmentConverter;

    @Override
    public List<EmployeeDto> convertToDto(List<EmployeeEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> depIds = model.stream()
                .map(EmployeeEntity::getDepartmentId)
                .collect(Collectors.toList());

        List<Long> postIds = model.stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toList());

        Map<Long, PostDto> postMap = postService.getById(postIds)
                .stream()
                .collect(Collectors.toMap(PostDto::getId, Function.identity()));

        Map<Long, DepartmentDto> depMap = departmentService.getById(depIds)
                .stream()
                .collect(Collectors.toMap(DepartmentDto::getId, Function.identity()));

        return model.stream()
                .map(entity -> {
                    EmployeeDto dto = updateStatic(new EmployeeDto(), entity);
                    dto.setDepartment(depMap.get(entity.getDepartmentId()));
                    dto.setPost(postMap.get(entity.getPostId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public EmployeeEntity convertToModel(EmployeeDto dto) {
        EmployeeEntity employeeEntity = updateStatic(new EmployeeEntity(), dto);

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
        EmployeeDto employeeDto = updateStatic(new EmployeeDto(), model);


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

        oldDto = updateStatic(oldDto, newModel);

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
        EmployeeDto employeeDto = updateStatic(new EmployeeDto(), model);


        Optional.ofNullable(model.getDepartmentId())
                .map(DepartmentDto::new)
                .ifPresent(employeeDto::setDepartment);

        Optional.ofNullable(model.getPostId())
                .map(PostDto::new)
                .ifPresent(employeeDto::setPost);

        return employeeDto;
    }

    private EmployeeEntity updateStatic(EmployeeEntity entity, EmployeeDto dto) {
        if(dto == null || entity == null) {
            return null;
        }

        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setPersonnelNumber(dto.getPersonnelNumber());
        entity.setInitials(dto.getInitials());
        entity.setFirstName(dto.getFirstName());
        entity.setSecondName(dto.getSecondName());
        entity.setThirdName(dto.getThirdName());
        entity.setBirthday(dto.getBirthday());
        entity.setDismissalDate(dto.getDismissalDate());
        entity.setEmploymentDate(dto.getEmploymentDate());
        entity.setHomePhoneNumber(dto.getHomePhoneNumber());
        entity.setWorkPhoneNumber(dto.getWorkPhoneNumber());
        entity.setGender(dto.getGender());
        entity.setPhoto(dto.getPhoto());
        entity.setCategory(dto.getCategory());

        return entity;
    }

    private EmployeeDto updateStatic(EmployeeDto dto, EmployeeEntity entity) {
        if(dto == null || entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setPersonnelNumber(entity.getPersonnelNumber());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setSecondName(entity.getSecondName());
        dto.setThirdName(entity.getThirdName());
        dto.setBirthday(entity.getBirthday());
        dto.setDismissalDate(entity.getDismissalDate());
        dto.setEmploymentDate(entity.getEmploymentDate());
        dto.setHomePhoneNumber(entity.getHomePhoneNumber());
        dto.setWorkPhoneNumber(entity.getWorkPhoneNumber());
        dto.setGender(entity.getGender());
        dto.setInitials(entity.getInitials());
        dto.setCategory(entity.getCategory());
        dto.setPhoto(entity.getPhoto());

        return dto;
    }
}
