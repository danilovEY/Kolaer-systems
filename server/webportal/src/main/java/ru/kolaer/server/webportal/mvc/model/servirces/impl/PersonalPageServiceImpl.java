package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.entities.other.PersonalPageData;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.PersonalPageService;

/**
 * Created by danilovey on 10.11.2016.
 */
@Service
public class PersonalPageServiceImpl implements PersonalPageService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    @Override
    public PersonalPageData getPersonalPageDataById(Integer id) {
        final PersonalPageData result = new PersonalPageData();
        result.setEmployee(this.employeeService.getById(id));

        return result;
    }
}
