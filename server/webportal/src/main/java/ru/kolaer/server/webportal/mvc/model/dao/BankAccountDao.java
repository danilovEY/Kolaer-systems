package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccount;

import java.util.List;

/**
 * Created by danilovey on 13.12.2016.
 */
public interface BankAccountDao extends DefaultDao<BankAccount> {
    BankAccount findByInitials(String initials);
    Integer getCountAllAccount();

    void updateOrSave(List<BankAccount> bankAccountList);
}
