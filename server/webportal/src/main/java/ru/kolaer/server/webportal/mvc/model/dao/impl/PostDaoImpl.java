package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;

/**
 * Created by danilovey on 12.10.2017.
 */
@Repository
public class PostDaoImpl extends AbstractDefaultDao<PostEntity> implements PostDao {
    protected PostDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PostEntity.class);
    }
}
