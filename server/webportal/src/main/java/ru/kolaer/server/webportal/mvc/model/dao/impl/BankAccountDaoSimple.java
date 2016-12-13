package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.bank.BankAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 13.12.2016.
 */
@Repository
public class BankAccountDaoSimple implements BankAccountDao {
    private Map<String, String> initialsAccountMap = new HashMap<>();

    @Override
    public BankAccount findByInitials(String initials) {
        final GeneralEmployeesEntity entity = new GeneralEmployeesEntityBase();
        entity.setInitials(initials);

        return new BankAccount(entity, initialsAccountMap.get(initials.toUpperCase()));
    }

    @Override
    public List<BankAccount> findAll() {
        return null;
    }

    @Override
    public BankAccount findByID(int id) {
        return null;
    }

    @Override
    public void persist(BankAccount obj) {
        this.initialsAccountMap.put(obj.getGeneralEmployeesEntity().getInitials(), obj.getCheck());
    }

    @Override
    public void delete(BankAccount obj) {

    }

    @Override
    public void update(BankAccount obj) {

    }
}
