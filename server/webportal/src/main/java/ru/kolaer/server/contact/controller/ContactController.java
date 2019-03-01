package ru.kolaer.server.contact.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.ContactAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.contact.service.ContactService;
import ru.kolaer.server.core.model.dto.concact.ContactDetailsDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;

import javax.validation.constraints.Min;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@Api(tags = "Справочник контактов")
@Slf4j
@Validated
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @ApiOperation(value = "Поиск по контактам")
    @GetMapping(RouterConstants.CONTACTS)
    public PageDto<ContactDetailsDto> searchContacts(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "search") String search) {
        return contactService.searchContacts(pageNum, pageSize, search);
    }

    @ApiOperation(value = "Получить список контактов подразделения")
    @GetMapping(RouterConstants.CONTACTS_DEPARTMENTS_ID_TYPE)
    public PageDto<ContactDetailsDto> getAllContactsByDepartment(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                                       @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                       @PathVariable(PathVariableConstants.DEPARTMENT_ID) long depId,
                                                       @PathVariable(PathVariableConstants.CONTACT_TYPE) ContactType type) {
        return contactService.getAllContactsByDepartment(number, pageSize, depId, type);
    }

    @ApiOperation(value = "Обновить контакты")
    @PreAuthorize("hasAnyRole('" + ContactAccessConstant.CONTACTS_WRITE + "','" + ContactAccessConstant.CONTACTS_DEPARTMENT_WRITE + "')")
    @PutMapping(RouterConstants.CONTACTS_EMPLOYEES_ID)
    public ContactDetailsDto updateContact(@PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId,
            @RequestBody ContactRequestDto contactRequestDto) {
        return contactService.saveContact(employeeId, contactRequestDto);
    }

}
