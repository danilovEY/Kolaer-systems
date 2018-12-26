package ru.kolaer.server.contact.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.contact.service.ContactService;
import ru.kolaer.server.core.annotation.UrlDeclaration;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;

import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping
@Api(tags = "Справочник контактов")
@Slf4j
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @ApiOperation(value = "Поиск по контактам")
    @UrlDeclaration(isAccessAll = true)
    @RequestMapping(value = "/non-security/contact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ContactDto> searchContacts(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                           @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                           @RequestParam(value = "search") String search) {
        return contactService.searchContacts(number, pageSize, search);
    }

    @ApiOperation(value = "Получить список подразделений")
    @UrlDeclaration(isAccessAll = true)
    @RequestMapping(value = "/non-security/contact/departments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DepartmentDto> getAllDepartments() {
        return contactService.getAllDepartments();
    }

    @ApiOperation(value = "Получить список контактов подразделения")
    @UrlDeclaration(isAccessAll = true)
    @RequestMapping(value = "/non-security/contact/{depId}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ContactDto> getAllContactsByDepartment(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                                       @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                       @PathVariable("depId") long depId,
                                                       @PathVariable("type") ContactType type) {
        return contactService.getAllContactsByDepartment(number, pageSize, depId, type);
    }

    @ApiOperation(value = "Обновить контакты")
    @UrlDeclaration(isUser = false, isOk = true)
    @RequestMapping(value = "/contact/employee/{employeeId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ContactDto updateContact(@PathVariable("employeeId") long employeeId,
                                    @RequestBody ContactRequestDto contactRequestDto) {
        return contactService.saveContact(employeeId, contactRequestDto);
    }

}
