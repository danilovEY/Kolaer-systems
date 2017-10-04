package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.server.webportal.mvc.model.dao.NotifyMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageEntity;

import java.util.List;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository("notifyMessageDao")
public class NotifyMessageDaoImpl implements NotifyMessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public NotifyMessage getLastNotifyMessage() {
        return this.findByID(1);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotifyMessage> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(NotifyMessageEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessage findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(NotifyMessageEntity.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public void persist(NotifyMessage obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(NotifyMessage obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(List<NotifyMessage> objs) {

    }

    @Override
    @Transactional
    public void update(NotifyMessage entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(List<NotifyMessage> objs) {

    }
}
