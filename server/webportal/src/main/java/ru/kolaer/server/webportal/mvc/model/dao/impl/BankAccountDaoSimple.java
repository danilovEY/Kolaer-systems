package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.bank.BankAccount;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        final BankAccount bankAccount = new BankAccount(entity, "null");

        for (Map.Entry<String, String> entry : initialsAccountMap.entrySet()) {
            if(entry.getValue().equals(initials)) {
                bankAccount.setCheck(entry.getKey());
                return bankAccount;
            }
        }

        return bankAccount;
    }

    @Override
    public Integer getCountAllAccount() {
        return this.initialsAccountMap.size();
    }

    @Override
    public List<BankAccount> findAll() {
        return this.initialsAccountMap.entrySet().stream()
                .map(stringStringEntry -> {
                    final GeneralEmployeesEntity entity = new GeneralEmployeesEntityBase();
                    entity.setInitials(stringStringEntry.getValue());

                    return new BankAccount(entity, stringStringEntry.getKey());
                }).collect(Collectors.toList());
    }

    @Override
    public BankAccount findByID(Integer id) {
        return null;
    }

    @Override
    public void persist(BankAccount obj) {
        this.initialsAccountMap.put(obj.getCheck(), obj.getGeneralEmployeesEntity().getInitials());
    }

    @Override
    public void delete(BankAccount obj) {

    }

    @Override
    public void delete(List<BankAccount> objs) {

    }

    @Override
    public void update(BankAccount obj) {

    }

    @Override
    public void update(List<BankAccount> objs) {

    }
}
