package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.UserDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralUsersEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<GeneralUsersEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralUsersEntity.class).list();
    }

    @Override
    public GeneralUsersEntity findByID(short id) {
        return null;
    }

    @Override
    @Transactional
    public void save(GeneralUsersEntity obj) {
        if(obj != null) {
            this.sessionFactory.getCurrentSession().save(obj);
        }
    }
}
