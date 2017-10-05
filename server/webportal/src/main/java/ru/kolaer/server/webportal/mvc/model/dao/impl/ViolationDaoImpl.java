package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
@Repository
public class ViolationDaoImpl extends AbstractDefaultDao<ViolationEntity> implements ViolationDao {

    protected ViolationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ViolationEntity.class);
    }

    @Override
    public List<ViolationEntity> findByJournalAndEffective(Integer idJournal) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " v WHERE v.journalViolation.id = :id AND v.effective = true", getEntityClass())
                .setParameter("id", idJournal)
                .list();
    }

    @Override
    public List<ViolationEntity> findByJournalAndPnumber(Integer idJournal, Integer pnumber) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " v WHERE v.journalViolation.id = :id AND v.writer.personnelNumber = :pnumber", getEntityClass())
                .setParameter("id", idJournal)
                .setParameter("pnumber", pnumber)
                .list();
    }

    @Override
    public List<ViolationEntity> findByJournalId(Integer id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " v WHERE v.journalViolation.id = :id", getEntityClass())
                .setParameter("id", id)
                .list();
    }

    @Override
    public Page<ViolationEntity> findByJournalId(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = getSession();

        Long count = 0L;
        List<ViolationEntity> result = Collections.emptyList();


        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(v.id) FROM ViolationDecorator v WHERE v.journalViolation.id = :id")
                    .setParameter("id", id).uniqueResult();

            result = currentSession
                    .createQuery("FROM " + getEntityName() + " v WHERE v.journalViolation.id = :id", getEntityClass())
                    .setParameter("id", id)
                    .setFirstResult((number-1)*pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }

        return new Page<>(result, number, count, pageSize);
    }

    @Override
    public void deleteByJournalId(Integer idJournal) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName() + " v WHERE v.journalViolation.id = :id")
                .setParameter("id", idJournal)
                .executeUpdate();
    }

    @Override
    public List<ViolationEntity> findAllEffective() {
        return getSession()
                .createQuery("FROM " + getEntityName() + " v WHERE v.effective = true", getEntityClass())
                .list();
    }

    @Override
    public List<ViolationEntity> findAllEffectiveBenween(Date createStart, Date createEnd) {
        String queryStr = "FROM ViolationDecorator v WHERE v.effective = true";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }

        Query<ViolationEntity> query = getSession()
                .createQuery(queryStr, getEntityClass());

        if(createStart != null) {
            query = query.setParameter("createStart", createStart);
        }
        if(createEnd != null) {
            query = query.setParameter("createEnd", createEnd);
        }

        return query.list();
    }

    @Override
    public List<ViolationEntity> findByJournalAndEffectiveBetween(Integer idJournal, Date createStart, Date createEnd) {
        String queryStr = "FROM ViolationDecorator v WHERE v.journalViolation.id = :id AND v.effective = true";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }

        Query<ViolationEntity> query = getSession()
                .createQuery(queryStr, getEntityClass())
                .setParameter("id", idJournal);

        if(createStart != null) {
            query = query.setParameter("createStart", createStart);
        }
        if(createEnd != null) {
            query = query.setParameter("createEnd", createEnd);
        }

        return query.list();
    }

    @Override
    public Long findCountViolationEntityEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd) {
        String queryStr = "SELECT COUNT(v.id) FROM ViolationDecorator v WHERE v.effective = true AND v.typeViolation.id = :idType AND v.stageEnum = :stage";
        if(createStart != null) {
            queryStr += " AND v.startMakingViolation >= :createStart";
        }
        if(createEnd != null) {
            queryStr += " AND v.startMakingViolation <= :createEnd";
        }
        Query query = getSession()
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
