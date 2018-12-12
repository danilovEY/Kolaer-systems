package ru.kolaer.server.webportal.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.NotifyMessageDao;
import ru.kolaer.server.webportal.model.entity.other.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository("notifyMessageDao")
public class NotifyMessageDaoImpl extends AbstractDefaultDao<NotifyMessageEntity> implements NotifyMessageDao {

    protected NotifyMessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, NotifyMessageEntity.class);
    }

    @Override
    public NotifyMessageEntity getLastNotifyMessage() {
        return findById(1L);
    }
}
