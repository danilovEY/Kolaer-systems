package ru.kolaer.server.webportal.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.HistoryChangeDao;
import ru.kolaer.server.webportal.model.entity.historychange.HistoryChangeEntity;

import javax.persistence.EntityManagerFactory;

@Repository
public class HistoryChangeDaoImpl extends AbstractDefaultDao<HistoryChangeEntity> implements HistoryChangeDao {

    protected HistoryChangeDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, HistoryChangeEntity.class);
    }

}
