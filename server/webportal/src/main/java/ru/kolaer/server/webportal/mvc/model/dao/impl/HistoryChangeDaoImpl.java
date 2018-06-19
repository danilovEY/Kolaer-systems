package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.HistoryChangeDao;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEntity;

@Repository
public class HistoryChangeDaoImpl extends AbstractDefaultDao<HistoryChangeEntity> implements HistoryChangeDao {

    protected HistoryChangeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, HistoryChangeEntity.class);
    }

}
