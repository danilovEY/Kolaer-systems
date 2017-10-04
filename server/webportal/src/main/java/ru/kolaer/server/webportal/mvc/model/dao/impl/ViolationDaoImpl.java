package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationEntity;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 13.09.2016.
 */
@Repository
public class ViolationDaoImpl implements ViolationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Violation> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(ViolationEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public Violation findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(ViolationEntity.class, id);
    }

    @Override
    @Transactional
    public void persist(Violation obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(Violation obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void delete( List<Violation> objs) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM ViolationDecorator v WHERE v.id IN (:iDs)")
                .setParameterList("iDs", objs.stream().map(Violation::getId).collect(Collectors.toList()))
                .executeUpdate();
    }

    @Override
    @Transactional
    public void update(Violation entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update( List<Violation> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<Violation> findByJournalAndEffective(Integer idJournal) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ViolationDecorator v WHERE v.journalViolation.id = :id AND v.effective = true")
                .setParameter("id", idJournal)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Violation> findByJournalAndPnumber(Integer idJournal, Integer pnumber) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ViolationDecorator v WHERE v.journalViolation.id = :id AND v.writer.personnelNumber = :pnumber")
                .setParameter("id", idJournal)
                .setParameter("pnumber", pnumber)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Violation> findByJournalId(Integer id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ViolationDecorator v WHERE v.journalViolation.id = :id")
                .setParameter("id", id)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Violation> findByJournalId(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = this.sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("FROM ViolationDecorator v WHERE v.journalViolation.id = :id")
                .setParameter("id", id);
        Long count = 0L;

        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(v.id) FROM ViolationDecorator v WHERE v.journalViolation.id = :id")
                    .setParameter("id", id).uniqueResult();

            query = query.setFirstResult((number-1)*pageSize).setMaxResults(pageSize);
        }

        List<Violation> result = query.list();


        return new Page<>(result, number, count, pageSize);
    }

    @Transactional
    public void deleteByJournalId(Integer idJournal) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM ViolationDecorator v WHERE v.journalViolation.id = :id")
                .setParameter("id", idJournal)
                .executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<Violation> findAllEffective() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ViolationDecorator v WHERE v.effective = true")
                .list();
    }

    @Transactional(readOnly = true)
    public List<Violation> findAllEffectiveBenween(Date createStart, Date createEnd) {
        String queryStr = "FROM ViolationDecorator v WHERE v.effective = true";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }
        Query query = this.sessionFactory.getCurrentSession()
                .createQuery(queryStr);

        if(createStart != null) {
            query = query.setParameter("createStart", createStart);
        }
        if(createEnd != null) {
            query = query.setParameter("createEnd", createEnd);
        }

        return query.list();
    }

    @Transactional(readOnly = true)
    public List<Violation> findByJournalAndEffectiveBetween(Integer idJournal, Date createStart, Date createEnd) {
        String queryStr = "FROM ViolationDecorator v WHERE v.journalViolation.id = :id AND v.effective = true";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }
        Query query = this.sessionFactory.getCurrentSession()
                .createQuery(queryStr)
                .setParameter("id", idJournal);

        if(createStart != null) {
            query = query.setParameter("createStart", createStart);
        }
        if(createEnd != null) {
            query = query.setParameter("createEnd", createEnd);
        }

        return query.list();

    }

    @Transactional(readOnly = true)
    public Long findCountViolationEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd) {
        String queryStr = "SELECT COUNT(v.id) FROM ViolationDecorator v WHERE v.effective = true AND v.typeViolation.id = :idType AND v.stageEnum = :stage";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }
        Query query = this.sessionFactory.getCurrentSession()
                .createQuery(queryStr)
                .setParameter("idType", idType)
                .setParameter("stage", stage);

        if(createStart != null) {
            query = query.setParameter("createStart", createStart);
        }
        if(createEnd != null) {
            query = query.setParameter("createEnd", createEnd);
        }

        return (Long) query.uniqueResult();
    }
}
