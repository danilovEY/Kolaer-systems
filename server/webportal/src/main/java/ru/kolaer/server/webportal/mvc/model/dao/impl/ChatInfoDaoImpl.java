package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatInfoDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatInfoEntity;

/**
 * Created by danilovey on 08.11.2017.
 */
@Repository
public class ChatInfoDaoImpl extends AbstractDefaultDao<ChatInfoEntity> implements ChatInfoDao {
    protected ChatInfoDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ChatInfoEntity.class);
    }

    @Override
    public boolean existInvite(String room, Long accountId) {
        return !sessionFactory.getCurrentSession()
                .createQuery("SELECT id FROM " + getEntityName() + " WHERE data = :room " +
                        "AND (account_id = :account_id OR to_account_id = :account_id)", Long.class)
                .setParameter("room", room)
                .setParameter("account_id", accountId)
                .setMaxResults(1)
                .list()
                .isEmpty();
    }
}
