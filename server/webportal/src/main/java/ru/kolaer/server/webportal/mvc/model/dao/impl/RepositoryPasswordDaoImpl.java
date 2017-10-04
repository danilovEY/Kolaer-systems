package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.RepositoryPasswordDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class RepositoryPasswordDaoImpl implements RepositoryPasswordDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<RepositoryPasswordEntity> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery("FROM RepositoryPasswordDecorator").list();
    }

    @Override
    @Transactional(readOnly = true)
    public RepositoryPasswordEntity findByID(@NonNull Integer id) {
        return (RepositoryPasswordEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordDecorator r WHERE r.id = :id")
                .setParameter("id", id).uniqueResult();
    }

    @Override
    @Transactional
    public void persist(@NonNull RepositoryPasswordEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull RepositoryPasswordEntity obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull List<RepositoryPasswordEntity> objs) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM RepositoryPasswordDecorator r WHERE r.id IN :objIds")
                .setParameterList("objIds", objs.stream()
                        .filter(pass -> pass.getId() != null)
                        .map(RepositoryPasswordEntity::getId)
                        .collect(Collectors.toList())
                ).executeUpdate();
    }

    @Override
    @Transactional
    public void update(@NonNull RepositoryPasswordEntity obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    public void update(@NonNull List<RepositoryPasswordEntity> objs) {
        final Session currentSession = this.sessionFactory.getCurrentSession();
        final Transaction transaction = currentSession.getTransaction();
        try {
            transaction.begin();
            final Integer batchSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
            for (int i = 0; i < objs.size(); i++) {
                currentSession.update(objs.get(i));

                if (i % batchSize == 0) {
                    currentSession.flush();
                    currentSession.clear();
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            log.error("Ошибка при обновлении объектов!", ex);
            transaction.rollback();
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryPasswordEntity> findAllByPnumber(Integer pnumber, Integer number, Integer pageSize) {
        final Long count = (Long) this.sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(r.id) FROM RepositoryPasswordDecorator r WHERE r.employee.personnelNumber = :pnumber")
                .setParameter("pnumber", pnumber).uniqueResult();

        List<RepositoryPasswordEntity> repositories = this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordDecorator r WHERE r.employee.personnelNumber = :pnumber")
                .setParameter("pnumber", pnumber)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
        return new Page<>(repositories, number, count, pageSize);
    }

    @Transactional(readOnly = true)
    public RepositoryPasswordEntity findByNameAndPnumber(String name, Integer pnumber) {
        return (RepositoryPasswordEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordDecorator r WHERE r.employee.personnelNumber = :pnumber AND r.name = :name")
                .setParameter("pnumber", pnumber)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Transactional(readOnly = true)
    public RepositoryPasswordEntity findRepositoryWithJoinById(Integer id) {
        RepositoryPasswordEntity password = (RepositoryPasswordEntity) this.sessionFactory.getCurrentSession()
                .createQuery("SELECT r FROM RepositoryPasswordDecorator r JOIN r.employee WHERE r.id = :id")
                .setParameter("id", id).uniqueResult();
        return password;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepositoryPasswordEntity> findAllByPnumbers(List<Integer> idsChief) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordDecorator r WHERE r.employee.personnelNumber IN :iDs")
                .setParameterList("iDs", idsChief)
                .list();
    }
}
