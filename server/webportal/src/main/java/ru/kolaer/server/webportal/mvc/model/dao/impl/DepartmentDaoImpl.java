package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
@Repository
public class DepartmentDaoImpl implements DepartmentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<DepartmentEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(DepartmentEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentEntity findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(DepartmentEntity.class, id);
    }

    @Override
    @Transactional
    public void persist(DepartmentEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(DepartmentEntity obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(List<DepartmentEntity> objs) {

    }

    @Override
    @Transactional
    public void update(DepartmentEntity entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(List<DepartmentEntity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentEntity findByName(String name) {
        return (DepartmentEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM DepartmentEntityDecorator dep WHERE dep.name = :name")
                .setParameter("name", name).uniqueResult();
    }
}
