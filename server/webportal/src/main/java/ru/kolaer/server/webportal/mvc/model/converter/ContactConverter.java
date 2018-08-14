package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.dao.ContactDao;
import ru.kolaer.server.webportal.mvc.model.dao.PlacementDao;
import ru.kolaer.server.webportal.mvc.model.dto.concact.ContactDto;
import ru.kolaer.server.webportal.mvc.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactEntity;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactType;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;
import ru.kolaer.server.webportal.mvc.model.servirces.PostService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContactConverter {
    private final PlacementDao placementDao;
    private final PlacementConverter placementConverter;
    private final ContactDao contactDao;
    private final DepartmentService departmentService;
    private final PostService postService;

    public List<ContactDto> employeeToContact(List<EmployeeEntity> employees) {
        if (CollectionUtils.isEmpty(employees)) {
            return Collections.emptyList();
        }

        List<Long> contactIds = employees
                .stream()
                .map(EmployeeEntity::getContactId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Long> depIds = employees
                .stream()
                .map(EmployeeEntity::getDepartmentId)
                .collect(Collectors.toList());

        List<Long> postIds = employees
                .stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toList());

        Map<Long, ContactEntity> contactEntityMap = contactIds.isEmpty()
                ? Collections.emptyMap()
                : contactDao.findById(contactIds)
                .stream()
                .collect(Collectors.toMap(ContactEntity::getId, Function.identity()));

        Map<Long, DepartmentDto> depMap = departmentService.getById(depIds)
                .stream()
                .collect(Collectors.toMap(DepartmentDto::getId, Function.identity()));

        Map<Long, PostDto> postMap = postService.getById(postIds)
                .stream()
                .collect(Collectors.toMap(PostDto::getId, Function.identity()));

        List<Long> placementIds = contactEntityMap.isEmpty()
                ? Collections.emptyList()
                : contactEntityMap
                .values()
                .stream()
                .map(ContactEntity::getPlacementId)
                .collect(Collectors.toList());

        Map<Long, PlacementDto> placementMap = placementIds.isEmpty()
                ? Collections.emptyMap()
                : placementConverter
                .convertToDto(placementDao.findById(placementIds))
                .stream()
                .collect(Collectors.toMap(PlacementDto::getId, Function.identity()));

        List<ContactDto> result = new ArrayList<>();

        for (EmployeeEntity employee : employees) {
            ContactDto contactDto = new ContactDto();
            contactDto.setEmployeeId(employee.getId());
            contactDto.setInitials(employee.getInitials());
            contactDto.setPhoto(employee.getPhoto());

            contactDto.setDepartment(depMap.get(employee.getDepartmentId()));
            contactDto.setPost(postMap.get(employee.getPostId()));

            ContactEntity contactEntity = contactEntityMap.get(employee.getContactId());
            if(contactEntity != null) {
                contactDto = updateDto(contactDto, contactEntity);
                contactDto.setPlacement(placementMap.get(contactEntity.getPlacementId()));
            } else {
                contactDto.setType(ContactType.OTHER);
            }

            result.add(contactDto);
        }

        return result;
    }

    public ContactDto employeeToContact(EmployeeEntity employee) {
        if (employee == null) {
            return null;
        }

        ContactDto contactDto = new ContactDto();
        contactDto.setEmployeeId(employee.getId());
        contactDto.setInitials(employee.getInitials());
        contactDto.setPhoto(employee.getPhoto());

        contactDto.setDepartment(departmentService.getById(employee.getDepartmentId()));
        contactDto.setPost(postService.getById(employee.getPostId()));

        ContactEntity contactEntity = employee.getContact();
        if(contactEntity != null) {
            contactDto = updateDto(contactDto, contactEntity);
            contactDto.setPlacement(placementConverter.convertToDto(contactEntity.getPlacement()));
        } else {
            contactDto.setType(ContactType.OTHER);
        }

        return contactDto;

    }

    public ContactDto updateDto(ContactDto contactDto, ContactEntity contactEntity) {
        if (contactDto == null || contactEntity == null) {
            return null;
        }

        contactDto.setEmail(contactEntity.getEmail());
        contactDto.setPager(contactEntity.getPager());
        contactDto.setWorkPhoneNumber(contactEntity.getWorkPhoneNumber());
        contactDto.setMobilePhoneNumber(contactEntity.getMobilePhoneNumber());
        contactDto.setType(contactEntity.getType());

        return contactDto;
    }

    public ContactEntity convertToModel(ContactRequestDto contactRequestDto) {
        return contactRequestDto != null
                ? updateToModel(new ContactEntity(), contactRequestDto)
                : null;
    }

    public ContactEntity updateToModel(ContactEntity contactEntity, ContactRequestDto contactRequestDto) {
        if(contactEntity == null || contactRequestDto == null) {
            return null;
        }

        contactEntity.setWorkPhoneNumber(contactRequestDto.getWorkPhoneNumber());
        contactEntity.setMobilePhoneNumber(contactRequestDto.getMobilePhoneNumber());
        contactEntity.setPager(contactRequestDto.getPager());
        contactEntity.setEmail(contactRequestDto.getEmail());
        contactEntity.setPlacementId(contactRequestDto.getPlacementId());
        contactEntity.setType(Optional.ofNullable(contactRequestDto.getType()).orElse(ContactType.OTHER));

        return contactEntity;
    }
}
