package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordRepositoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class PasswordRepositoryDaoImpl extends AbstractDefaultDao<PasswordRepositoryEntity> implements PasswordRepositoryDao {

    protected PasswordRepositoryDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PasswordRepositoryEntity.class);
    }

    @Override
    public Long findCountAllByPnumber(Long pnumber, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("SELECT COUNT(r.id) FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber", Long.class)
                .setParameter("pnumber", pnumber)
                .uniqueResult();
    }

    @Override
    public List<PasswordRepositoryEntity> findAllByPnumber(Long pnumber, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber", getEntityClass())
                .setParameter("pnumber", pnumber)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public PasswordRepositoryEntity findByNameAndPnumber(String name, Long pnumber) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber AND r.name = :name", getEntityClass())
                .setParameter("pnumber", pnumber)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public List<PasswordRepositoryEntity> findAllByPnumbers(List<Long> idsChief) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber IN :ids", getEntityClass())
                .setParameterList("ids", idsChief)
                .list();
    }
}
