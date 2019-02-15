package ru.kolaer.server.contact.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.ContactAccessConstant;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.contact.service.ContactService;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;

import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@Api(tags = "Справочник контактов")
@Slf4j
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @ApiOperation(value = "Поиск по контактам")
    @PreAuthorize("permitAll()")
    @GetMapping(RouterConstants.CONTACTS)
    public Page<ContactDto> searchContacts(@RequestParam(value = "page", defaultValue = "1") Integer number,
            @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "search") String search) {
        return contactService.searchContacts(number, pageSize, search);
    }

    @ApiOperation(value = "Получить список подразделений")
    @PreAuthorize("permitAll()")
    @GetMapping(RouterConstants.CONTACTS_DEPARTMENTS)
    public List<DepartmentDto> getAllDepartments() {
        return contactService.getAllDepartments();
    }

    @ApiOperation(value = "Получить список контактов подразделения")
    @PreAuthorize("permitAll()")
    @GetMapping(RouterConstants.CONTACTS_DEPARTMENTS_ID_TYPE)
    public Page<ContactDto> getAllContactsByDepartment(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                                       @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                       @PathVariable(PathVariableConstants.DEPARTMENT_ID) long depId,
                                                       @PathVariable(PathVariableConstants.CONTACT_TYPE) ContactType type) {
        return contactService.getAllContactsByDepartment(number, pageSize, depId, type);
    }

    @ApiOperation(value = "Обновить контакты")
    @PreAuthorize("hasRole('" + ContactAccessConstant.CONTACTS_EDIT + "')")
    @PutMapping(RouterConstants.CONTACTS_EMPLOYEES_ID)
    public ContactDto updateContact(@PathVariable(PathVariableConstants.EMPLOYEE_ID) long employeeId,
                                    @RequestBody ContactRequestDto contactRequestDto) {
        return contactService.saveContact(employeeId, contactRequestDto);
    }

}
