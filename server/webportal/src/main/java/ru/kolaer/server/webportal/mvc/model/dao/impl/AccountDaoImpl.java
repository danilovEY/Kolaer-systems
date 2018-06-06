package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dto.FilterValue;
import ru.kolaer.server.webportal.mvc.model.dto.SortField;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
@Slf4j
public class AccountDaoImpl extends AbstractDefaultDao<AccountEntity> implements AccountDao {

    @Autowired
    public AccountDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, AccountEntity.class);
    }

    @Override
    public AccountEntity findName(String username) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " ac WHERE ac.username=:username", getEntityClass())
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public AccountEntity checkValueBeforePersist(AccountEntity entity) {
        List<AccountEntity> result = getSession()
                .createQuery("FROM " + getEntityName() + " ac WHERE " +
                        "ac.username=:username OR ac.chatName=:chatName OR ac.email=:email", getEntityClass())
                .setParameter("username", entity.getUsername())
                .setParameter("chatName", entity.getChatName())
                .setParameter("email", entity.getEmail())
                .list();

        return checkValue(entity, result);
    }

    @Override
    public AccountEntity checkValueBeforeUpdate(AccountEntity entity) {
        Session session = getSession();
        session.detach(entity);

        List<AccountEntity> result = getSession()
                .createQuery("FROM " + getEntityName() + " ac WHERE ac.id <> :id AND " +
                        "(ac.username=:username OR ac.chatName=:chatName OR ac.email=:email)", getEntityClass())
                .setParameter("id", entity.getId())
                .setParameter("username", entity.getUsername())
                .setParameter("chatName", entity.getChatName())
                .setParameter("email", entity.getEmail())
                .list();

        return checkValue(entity, result);
    }

    private AccountEntity checkValue(AccountEntity entity, List<AccountEntity> results) {
        if(results.isEmpty()) {
            return entity;
        }

        StringBuilder message = new StringBuilder();

        for (AccountEntity result : results) {
            if(entity.getUsername().equals(result.getUsername())) {
                message = message.append("Такой логин уже существует").append(System.lineSeparator());
            } if(entity.getChatName().equals(result.getChatName())) {
                message = message.append("Такое имя в чате уже существует").append(System.lineSeparator());
            } if(entity.getEmail().equals(result.getEmail())) {
                message = message.append("Такой email уже существует").append(System.lineSeparator());
            }
        }

        throw new UnexpectedRequestParams(message.toString());
    }

    @Override
    public List<AccountEntity> findAll(SortField sortField, Map<String, FilterValue> filter, Integer number, Integer pageSize) {
        String queryOrderBy = Optional.ofNullable(sortField)
                .map(SortField::toString)
                .orElse("");

        Map<String, FilterValue> tempFilter = new HashMap<>(filter);
        FilterValue username = tempFilter.get("filterInitials");

        if(username != null) {
            tempFilter.remove("filterInitials");
        }

        String queryFilter = filtersToString(tempFilter);

        if(username != null) {
            queryFilter += tempFilter.isEmpty()
                    ? " WHERE username LIKE :username OR employee.initials LIKE :username"
                    : " AND (username LIKE :username OR employee.initials LIKE :username)";
        }

        Query<AccountEntity> query = getSession().createQuery("FROM " + getEntityName() +
                queryFilter + " " + queryOrderBy, getEntityClass());

        query = setParams(query, tempFilter);

        if(username != null) {
            query = query.setParameter("username", "%" + username.getValue() + "%");
        }

        if(number != null && number > 0 && pageSize != null && pageSize > 0) {
            query = query.setFirstResult((number - 1) * pageSize)
                    .setMaxResults(pageSize);
        }

        return query.list();
    }

    @Override
    public long findAllCount(Map<String, FilterValue> filter) {
        Map<String, FilterValue> tempFilter = new HashMap<>(filter);
        FilterValue username = tempFilter.get("filterInitials");

        if(username != null) {
            tempFilter.remove("filterInitials");
        }

        String queryFilter = filtersToString(tempFilter);

        if(username != null) {
            queryFilter += tempFilter.isEmpty()
                    ? " WHERE username LIKE :username OR employee.initials LIKE :username"
                    : " AND (username LIKE :username OR employee.initials LIKE :username)";
        }

        Query<Long> query = getSession()
                .createQuery("SELECT COUNT(id) FROM " + getEntityName() + queryFilter,
                        Long.class);

        query = setParams(query, tempFilter);

        if(username != null) {
            query = query.setParameter("username", "%" + username.getValue() + "%");
        }

        return query.uniqueResult();
    }
}
