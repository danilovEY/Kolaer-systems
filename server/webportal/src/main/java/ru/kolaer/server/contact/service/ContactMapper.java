package ru.kolaer.server.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.kolaer.server.contact.model.entity.ContactEntity;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.core.model.dto.concact.ContactDetailsDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.employee.converter.DepartmentConverter;
import ru.kolaer.server.employee.converter.PostConverter;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.dao.PostDao;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.entity.PostEntity;
import ru.kolaer.server.employee.repository.DepartmentRepository;
import ru.kolaer.server.placement.model.entity.PlacementEntity;
import ru.kolaer.server.placement.repository.PlacementRepository;
import ru.kolaer.server.placement.service.PlacementConverter;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ContactMapper {
    private final PlacementRepository placementRepository;
    private final PlacementConverter placementConverter;
    private final EmployeeDao employeeDao;
    private final DepartmentConverter departmentConverter;
    private final DepartmentRepository departmentRepository;
    private final PostDao postDao;
    private final PostConverter postConverter;

    @Autowired
    public ContactMapper(PlacementRepository placementRepository, PlacementConverter placementConverter,
            EmployeeDao employeeDao, DepartmentConverter departmentConverter, DepartmentRepository departmentRepository,
            PostDao postDao, PostConverter postConverter
    ) {
        this.placementRepository = placementRepository;
        this.placementConverter = placementConverter;
        this.employeeDao = employeeDao;
        this.departmentConverter = departmentConverter;
        this.departmentRepository = departmentRepository;
        this.postDao = postDao;
        this.postConverter = postConverter;
    }

    public ContactDetailsDto updateDto(ContactDetailsDto contactDetailsDto, ContactEntity contactEntity) {
        if (contactDetailsDto == null || contactEntity == null) {
            return null;
        }

        contactDetailsDto.setEmail(contactEntity.getEmail());
        contactDetailsDto.setPager(contactEntity.getPager());
        contactDetailsDto.setWorkPhoneNumber(contactEntity.getWorkPhoneNumber());
        contactDetailsDto.setMobilePhoneNumber(contactEntity.getMobilePhoneNumber());
        contactDetailsDto.setType(contactEntity.getType());

        return contactDetailsDto;
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
        contactEntity.setPlacementId(contactRequestDto.getPlacementId());

        return contactEntity;
    }

    public List<ContactDetailsDto> convertToDto(@NotNull List<ContactEntity> entities) {
        return convertToDto(entities, Collections.emptyList());
    }

    public List<ContactDetailsDto> convertToDto(@NotNull List<ContactEntity> entities, @NotNull Collection<Long> employeeIds) {
        return convertToDto(entities, employeeIds, null);
    }

    public List<ContactDetailsDto> convertToDto(@NotNull List<ContactEntity> entities, @NotNull Collection<Long> employeeIds,
            @Nullable ContactType type
    ) {
        Set<Long> placementIds = entities
                .stream()
                .map(ContactEntity::getPlacementId)
                .collect(Collectors.toSet());

        Stream<Long> employeeIdFromContacts = entities
                .stream()
                .map(ContactEntity::getEmployeeId);

        Set<Long> allEmployeeIds = Stream.concat(employeeIdFromContacts, employeeIds.stream())
                .collect(Collectors.toSet());

        List<EmployeeEntity> employees = employeeDao.findById(allEmployeeIds);

        Map<Long, PlacementEntity> placementMap = placementIds.isEmpty()
                ? Collections.emptyMap()
                : placementRepository.findAllById(placementIds)
                .stream()
                .collect(Collectors.toMap(PlacementEntity::getId, Function.identity()));

        Set<Long> departmentIds = employees
                .stream()
                .map(EmployeeEntity::getDepartmentId)
                .collect(Collectors.toSet());

        Set<Long> postIds = employees
                .stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toSet());

        Map<Long, DepartmentEntity> departmentMap = departmentRepository.findAllById(departmentIds)
                .stream()
                .collect(Collectors.toMap(DepartmentEntity::getId, Function.identity()));

        Map<Long, PostEntity> postMap = postDao.findById(postIds)
                .stream()
                .collect(Collectors.toMap(PostEntity::getId, Function.identity()));

        Map<Long, ContactEntity> contactMap = entities
                .stream()
                .collect(Collectors.toMap(ContactEntity::getEmployeeId, Function.identity()));

        return employees
                .stream()
                .filter(employee -> contactMap.containsKey(employee.getId()) || type == null)
                .sorted(Comparator.comparing(EmployeeEntity::getInitials))
                .map(employee -> convertToContactDetails(
                        employee,
                        contactMap.get(employee.getId()),
                        departmentMap.get(employee.getDepartmentId()),
                        postMap.get(employee.getPostId()),
                        placementMap.get(employee.getId())
                    )
                )
                .collect(Collectors.toList());
    }

    private ContactDetailsDto convertToContactDetails(@NotNull EmployeeEntity employee, @Nullable ContactEntity contact,
            @NotNull DepartmentEntity department, @NotNull PostEntity post, @NotNull PlacementEntity placement
    ) {
        ContactDetailsDto contactDetailsDto = new ContactDetailsDto();
        contactDetailsDto.setDepartment(departmentConverter.convertToDto(department));
        contactDetailsDto.setPost(postConverter.convertToDto(post));
        contactDetailsDto.setPlacement(placementConverter.convertToDto(placement));
        contactDetailsDto.setPhoto(employee.getPhoto());
        contactDetailsDto.setEmployeeId(employee.getId());
        contactDetailsDto.setInitials(employee.getInitials());

        if (contact != null) {
            contactDetailsDto.setWorkPhoneNumber(contact.getWorkPhoneNumber());
            contactDetailsDto.setMobilePhoneNumber(contact.getMobilePhoneNumber());
            contactDetailsDto.setEmail(contact.getEmail());
            contactDetailsDto.setPager(contact.getPager());
            contactDetailsDto.setType(contact.getType());
        }

        return contactDetailsDto;
    }
}
