package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
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

    @Override
    public PostEntity checkValue(PostEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Должность NULL");
        }

        if(StringUtils.isEmpty(entity.getName()) || StringUtils.isEmpty(entity.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В должности пустое имя или абревиатура" + entity.toString(),
                    ErrorCode.PRE_SQL_EXCEPTION);
        }

        return entity;
    }
}
