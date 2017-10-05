package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 13.12.2016.
 */
public class BankAccountDaoSimple implements BankAccountDao {
    private Map<String, String> initialsAccountMap = new HashMap<>();

    @Override
    public BankAccountEntity findByInitials(String initials) {
        final EmployeeEntity entity = new EmployeeEntity();
        entity.setInitials(initials);

        final BankAccountEntity bankAccount = new BankAccountEntity(null, null, entity, "null");

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
    public List<BankAccountEntity> findAll() {
        return this.initialsAccountMap.entrySet().stream()
                .map(stringStringEntry -> {
                    final EmployeeEntity entity = new EmployeeEntity();
                    entity.setInitials(stringStringEntry.getValue());

                    return new BankAccountEntity(null, null, entity, stringStringEntry.getKey());
                }).collect(Collectors.toList());
    }

    @Override
    public BankAccountEntity findByID(@NonNull Long id) {
        return null;
    }

    @Override
    public BankAccountEntity persist(BankAccountEntity obj) {
        this.initialsAccountMap.put(obj.getCheck(), obj.getEmployeeEntity().getInitials());
        return obj;
    }

    @Override
    public List<BankAccountEntity> persist(@NonNull List<BankAccountEntity> obj) {
        return obj;
    }

    @Override
    public BankAccountEntity delete(BankAccountEntity obj) {
        return obj;
    }

    @Override
    public int delete(@NonNull Long id) {
        return 0;
    }

    @Override
    public List<BankAccountEntity> delete(List<BankAccountEntity> objs) {
        return objs;
    }

    @Override
    public BankAccountEntity update(BankAccountEntity obj) {
        return obj;
    }

    @Override
    public List<BankAccountEntity> update(List<BankAccountEntity> objs) {
        return objs;
    }

    @Override
    public Page<BankAccountEntity> findAll(Integer number, Integer pageSize) {
        return null;
    }

    @Override
    public Class<BankAccountEntity> getEntityClass() {
        return BankAccountEntity.class;
    }
}
