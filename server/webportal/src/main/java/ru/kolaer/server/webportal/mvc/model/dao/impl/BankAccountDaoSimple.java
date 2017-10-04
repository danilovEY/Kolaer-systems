package ru.kolaer.server.webportal.mvc.model.dao.impl;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 13.12.2016.
 */
//@Repository
public class BankAccountDaoSimple implements BankAccountDao {
    private Map<String, String> initialsAccountMap = new HashMap<>();

    @Override
    public BankAccountEntity findByInitials(String initials) {
        final EmployeeEntity entity = new EmployeeDto();
        entity.setInitials(initials);

        final BankAccountEntity bankAccount = new BankAccountEntity(-1, entity, "null");

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
    public void updateOrSave(List<BankAccountEntity> bankAccountList) {

    }

    @Override
    public List<BankAccountEntity> findAll() {
        return this.initialsAccountMap.entrySet().stream()
                .map(stringStringEntry -> {
                    final EmployeeEntity entity = new EmployeeDto();
                    entity.setInitials(stringStringEntry.getValue());

                    return new BankAccountEntity(-1, entity, stringStringEntry.getKey());
                }).collect(Collectors.toList());
    }

    @Override
    public BankAccountEntity findByID(Integer id) {
        return null;
    }

    @Override
    public void persist(BankAccountEntity obj) {
        this.initialsAccountMap.put(obj.getCheck(), obj.getEmployeeEntity().getInitials());
    }

    @Override
    public void delete(BankAccountEntity obj) {

    }

    @Override
    public void delete(List<BankAccountEntity> objs) {

    }

    @Override
    public void update(BankAccountEntity obj) {

    }

    @Override
    public void update(List<BankAccountEntity> objs) {

    }
}
