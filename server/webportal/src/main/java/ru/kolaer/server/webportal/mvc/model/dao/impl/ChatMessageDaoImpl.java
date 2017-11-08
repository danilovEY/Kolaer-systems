package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;

/**
 * Created by danilovey on 08.11.2017.
 */
@Repository
public class ChatMessageDaoImpl extends AbstractDefaultDao<ChatMessageEntity> implements ChatMessageDao {
    protected ChatMessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ChatMessageEntity.class);
    }
}
