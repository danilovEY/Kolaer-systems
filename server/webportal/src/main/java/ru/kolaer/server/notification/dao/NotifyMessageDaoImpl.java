package ru.kolaer.server.notification.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.notification.model.entity.NotifyMessageEntity;

import javax.persistence.EntityManagerFactory;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository("notifyMessageDao")
public class NotifyMessageDaoImpl extends AbstractDefaultDao<NotifyMessageEntity> implements NotifyMessageDao {

    protected NotifyMessageDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, NotifyMessageEntity.class);
    }

    @Override
    public NotifyMessageEntity getLastNotifyMessage() {
        return findById(1L);
    }
}
