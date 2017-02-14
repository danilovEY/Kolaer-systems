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
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.server.webportal.mvc.model.dao.RepositoryPasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordHistoryDecorator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class RepositoryPasswordHistoryDaoImpl implements RepositoryPasswordHistoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<RepositoryPasswordHistory> findAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordHistoryDecorator")
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public RepositoryPasswordHistoryDecorator findByID(@NonNull Integer id) {
        return (RepositoryPasswordHistoryDecorator) this.sessionFactory.getCurrentSession()
                .createQuery("FROM RepositoryPasswordHistoryDecorator r WHERE r.id = :id")
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    @Transactional
    public void persist(@NonNull RepositoryPasswordHistory obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull RepositoryPasswordHistory obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull List<RepositoryPasswordHistory> objs) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM RepositoryPasswordHistoryDecorator r WHERE r.id = :objIds")
                .setParameterList("objIds", objs.stream()
                    .filter(pass -> pass.getId() != null)
                    .map(RepositoryPasswordHistory::getId)
                    .collect(Collectors.toList())
                ).executeUpdate();
    }

    @Override
    @Transactional
    public void update(@NonNull RepositoryPasswordHistory obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    public void update(@NonNull List<RepositoryPasswordHistory> objs) {
        final Session currentSession = this.sessionFactory.getCurrentSession();
        final Transaction transaction = currentSession.getTransaction();
        try {
            transaction.begin();
            final Integer batchSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
            for(int i = 0; i < objs.size(); i++) {
                currentSession.update(objs.get(i));
                if(i % batchSize == 0) {
                    currentSession.flush();
                    currentSession.clear();
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            log.info("Ошибка при обновлении объектов!", ex);
            transaction.rollback();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryPasswordHistory> findHistoryByIdRepository(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = this.sessionFactory.getCurrentSession();
        final Long count = (Long) currentSession
                .createQuery("SELECT COUNT(p.id) FROM RepositoryPasswordHistoryDecorator p WHERE p.repositoryPassword.id = :id")
                .setParameter("id", id)
                .uniqueResult();

        List<RepositoryPasswordHistoryDecorator> result = currentSession
                .createQuery("FROM RepositoryPasswordHistoryDecorator p WHERE p.repositoryPassword.id = :id")
                .setParameter("id", id)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();

        return new Page(result, number, count, pageSize);
    }

    @Override
    @Transactional
    public void deleteByIdRep(Integer id) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM RepositoryPasswordHistoryDecorator r WHERE r.repositoryPassword.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
