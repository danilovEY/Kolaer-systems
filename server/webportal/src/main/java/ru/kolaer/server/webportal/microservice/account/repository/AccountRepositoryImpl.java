package ru.kolaer.server.webportal.microservice.account.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.microservice.account.AccountEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
@Slf4j
public class AccountRepositoryImpl extends AbstractDefaultRepository<AccountEntity> implements AccountRepository {

    @Autowired
    public AccountRepositoryImpl(SessionFactory sessionFactory) {
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
    public List<AccountEntity> findByEmployeeIds(List<Long> employeeIds) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " ac WHERE ac.employeeId IN(:employeeIds)", getEntityClass())
                .setParameter("employeeIds", employeeIds)
                .list();
    }

//    @Override
//    public AccountEntity checkValueBeforePersist(AccountEntity entity) {
//        List<AccountEntity> result = getSession()
//                .createQuery("FROM " + getEntityName() + " ac WHERE " +
//                        "ac.username=:username OR ac.chatName=:chatName OR ac.email=:email", getEntityClass())
//                .setParameter("username", entity.getUsername())
//                .setParameter("chatName", entity.getChatName())
//                .setParameter("email", entity.getEmail())
//                .list();
//
//        return checkValue(entity, result);
//    }
//
//    @Override
//    public AccountEntity checkValueBeforeUpdate(AccountEntity entity) {
//        Session session = getSession();
//        session.detach(entity);
//
//        List<AccountEntity> result = getSession()
//                .createQuery("FROM " + getEntityName() + " ac WHERE ac.id <> :id AND " +
//                        "(ac.username=:username OR ac.chatName=:chatName OR ac.email=:email)", getEntityClass())
//                .setParameter("id", entity.getId())
//                .setParameter("username", entity.getUsername())
//                .setParameter("chatName", entity.getChatName())
//                .setParameter("email", entity.getEmail())
//                .list();
//
//        return checkValue(entity, result);
//    }



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
}
