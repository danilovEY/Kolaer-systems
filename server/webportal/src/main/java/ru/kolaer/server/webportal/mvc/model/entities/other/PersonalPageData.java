package ru.kolaer.server.webportal.mvc.model.entities.other;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

/**
 * Created by danilovey on 10.11.2016.
 */
public class PersonalPageData {
    private GeneralEmployeesEntity employee;
    private GeneralAccountsEntity account;

    public GeneralEmployeesEntity getEmployee() {
        return employee;
    }

    public void setEmployee(GeneralEmployeesEntity employee) {
        this.employee = employee;
    }

    public GeneralAccountsEntity getAccount() {
        return account;
    }

    public void setAccount(GeneralAccountsEntity account) {
        this.account = account;
    }
}
