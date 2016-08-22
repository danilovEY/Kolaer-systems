package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.server.webportal.mvc.model.dao.NotifyMessageDao;
import ru.kolaer.server.webportal.mvc.model.servirces.NotifyMessageService;

import java.util.List;

/**
 * Created by danilovey on 18.08.2016.
 */
@Service("notifyMessageService")
public class NotifyMessageImpl implements NotifyMessageService {

    @Autowired
    @Qualifier(value = "jdbcNotifyMessageDao")
    private NotifyMessageDao notifyMessageDao;

    @Override
    public List<NotifyMessage> getAll() {
        return this.notifyMessageDao.findAll();
    }

    @Override
    public NotifyMessage getById(Integer id) {
        if(id != null && id >= 0)
            return this.notifyMessageDao.findByID(id);
        else
            return null;
    }

    @Override
    public void add(NotifyMessage entity) {
        if(entity != null)
            this.notifyMessageDao.persist(entity);
    }

    @Override
    public void delete(NotifyMessage entity) {
        if(entity != null)
            this.notifyMessageDao.delete(entity);
    }

    @Override
    public void update(NotifyMessage entity) {
        if(entity != null)
            this.notifyMessageDao.update(entity);
    }

    @Override
    public NotifyMessage getLastNotifyMessage() {
        return this.notifyMessageDao.getLastNotifyMessage();
    }
}
