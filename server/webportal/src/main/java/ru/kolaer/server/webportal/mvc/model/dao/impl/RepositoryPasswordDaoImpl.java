package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.RepositoryPasswordDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class RepositoryPasswordDaoImpl extends AbstractDefaultDao<RepositoryPasswordEntity> implements RepositoryPasswordDao {

    protected RepositoryPasswordDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, RepositoryPasswordEntity.class);
    }

    @Override
    public Page<RepositoryPasswordEntity> findAllByPnumber(Integer pnumber, Integer number, Integer pageSize) {
        Long count = getSession()
                .createQuery("SELECT COUNT(r.id) FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber", Long.class)
                .setParameter("pnumber", pnumber)
                .uniqueResult();

        List<RepositoryPasswordEntity> repositories = getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber", getEntityClass())
                .setParameter("pnumber", pnumber)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();

        return new Page<>(repositories, number, count, pageSize);
    }

    public RepositoryPasswordEntity findByNameAndPnumber(String name, Integer pnumber) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber = :pnumber AND r.name = :name", getEntityClass())
                .setParameter("pnumber", pnumber)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public List<RepositoryPasswordEntity> findAllByPnumbers(List<Integer> idsChief) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.employee.personnelNumber IN :ids", getEntityClass())
                .setParameterList("ids", idsChief)
                .list();
    }
}
