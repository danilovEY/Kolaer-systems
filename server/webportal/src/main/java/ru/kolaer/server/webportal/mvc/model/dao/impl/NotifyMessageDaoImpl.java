package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.server.webportal.mvc.model.dao.NotifyMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageDecorator;

import java.util.List;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository
public class NotifyMessageDaoImpl implements NotifyMessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public NotifyMessage getLastNotifyMessage() {
        return this.findByID(1);
    }

    @Override
    public List<NotifyMessage> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(NotifyMessageDecorator.class).list();
    }

    @Override
    public NotifyMessage findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(NotifyMessageDecorator.class, id);
    }

    @Override
    public void persist(NotifyMessage obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public void delete(NotifyMessage obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void update(NotifyMessage entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }
}
