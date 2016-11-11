package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.server.webportal.mvc.model.entities.other.PersonalPageData;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.PersonalPageService;

/**
 * Created by danilovey on 10.11.2016.
 */
@Service
public class PersonalPageServiceImpl implements PersonalPageService {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public PersonalPageData getPersonalPageData(Integer id) {
        final PersonalPageData result = new PersonalPageData();

        return result;
    }

    @Override
    public PersonalPageData getPersonalPageData(GeneralAccountsEntity accountsEntity) {
        final PersonalPageData result = new PersonalPageData();
        result.setAccount(accountsEntity);

        return result;
    }
}
