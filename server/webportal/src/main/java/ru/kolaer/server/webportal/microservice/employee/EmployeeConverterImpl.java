package ru.kolaer.server.webportal.microservice.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.common.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.microservice.department.DepartmentConverter;
import ru.kolaer.server.webportal.microservice.post.PostConverter;
import ru.kolaer.server.webportal.microservice.typework.TypeWorkConverter;
import ru.kolaer.server.webportal.common.servirces.DepartmentService;
import ru.kolaer.server.webportal.common.servirces.PostService;
import ru.kolaer.server.webportal.common.servirces.TypeWorkService;
import ru.kolaer.server.webportal.common.servirces.impl.UtilService;

import java.util.*;
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
    private final TypeWorkService typeWorkService;
    private final TypeWorkConverter typeWorkConverter;
    private final UtilService utilService;

    @Override
    public List<EmployeeDto> convertToDto(List<EmployeeEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> depIds = model.stream()
                .map(EmployeeEntity::getDepartmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> postIds = model.stream()
                .map(EmployeeEntity::getPostId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> typeWordIds = model.stream()
                .map(EmployeeEntity::getTypeWorkId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, PostDto> postMap = postService.getById(postIds)
                .stream()
                .collect(Collectors.toMap(PostDto::getId, Function.identity()));

        Map<Long, DepartmentDto> depMap = departmentService.getById(depIds)
                .stream()
                .collect(Collectors.toMap(DepartmentDto::getId, Function.identity()));

        Map<Long, TypeWorkDto> typeWorkMap = typeWorkService.getById(typeWordIds)
                .stream()
                .collect(Collectors.toMap(TypeWorkDto::getId, Function.identity()));

        return model.stream()
                .map(entity -> {
                    EmployeeDto dto = updateStatic(new EmployeeDto(), entity);
                    dto.setDepartment(depMap.get(entity.getDepartmentId()));
                    dto.setPost(postMap.get(entity.getPostId()));
                    dto.setTypeWork(typeWorkMap.get(entity.getTypeWorkId()));
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

        if(employeeDto == null) {
            return null;
        }

        if(model.getPostId() != null) {
            Optional.ofNullable(model.getPostId())
                    .map(postService::getById)
                    .ifPresent(employeeDto::setPost);
        }

        if(model.getDepartmentId() != null) {
            Optional.ofNullable(model.getDepartmentId())
                    .map(departmentService::getById)
                    .ifPresent(employeeDto::setDepartment);
        }

        if(model.getTypeWorkId() != null) {
            Optional.ofNullable(model.getTypeWorkId())
                    .map(typeWorkService::getById)
                    .ifPresent(employeeDto::setTypeWork);
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

        if(newModel.getTypeWorkId() != null && !newModel.getTypeWorkId().equals(Optional
                .ofNullable(oldDto.getTypeWork())
                .map(TypeWorkDto::getId)
                .orElse(null))) {
            Optional.ofNullable(newModel.getTypeWork())
                    .map(typeWorkConverter::convertToDto)
                    .ifPresent(oldDto::setTypeWork);
        } else {
            oldDto.setTypeWork(null);
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

        Optional.ofNullable(model.getTypeWorkId())
                .map(TypeWorkDto::new)
                .ifPresent(employeeDto::setTypeWork);

        return employeeDto;
    }

    private EmployeeEntity updateStatic(EmployeeEntity entity, EmployeeDto dto) {
        if(dto == null || entity == null) {
            return null;
        }

        entity.setId(dto.getId());
        entity.setPersonnelNumber(dto.getPersonnelNumber());
        entity.setInitials(dto.getInitials());
        entity.setFirstName(dto.getFirstName());
        entity.setSecondName(dto.getSecondName());
        entity.setThirdName(dto.getThirdName());
        entity.setBirthday(dto.getBirthday());
        entity.setDismissalDate(dto.getDismissalDate());
        entity.setEmploymentDate(dto.getEmploymentDate());
        entity.setGender(dto.getGender());
        entity.setPhoto(Optional.ofNullable(dto.getPhoto()).map(photo -> photo.substring(utilService.getCurrentHostUrl().length())).orElse(null));
        entity.setCategory(dto.getCategory());
        entity.setHarmfulness(dto.isHarmfulness());

        return entity;
    }

    private EmployeeDto updateStatic(EmployeeDto dto, EmployeeEntity entity) {
        if(dto == null || entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setPersonnelNumber(entity.getPersonnelNumber());
        dto.setFirstName(entity.getFirstName());
        dto.setSecondName(entity.getSecondName());
        dto.setThirdName(entity.getThirdName());
        dto.setBirthday(entity.getBirthday());
        dto.setDismissalDate(entity.getDismissalDate());
        dto.setEmploymentDate(entity.getEmploymentDate());
        dto.setGender(entity.getGender());
        dto.setInitials(entity.getInitials());
        dto.setCategory(entity.getCategory());
        dto.setPhoto(Optional.ofNullable(entity.getPhoto()).map(photo -> utilService.getCurrentHostUrl() + photo).orElse(null));
        dto.setHarmfulness(entity.isHarmfulness());

        return dto;
    }
}
