package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.mvc.model.converter.NotifyMessageConverter;
import ru.kolaer.server.webportal.mvc.model.dao.NotifyMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.NotifyMessageService;

/**
 * Created by danilovey on 18.08.2016.
 */
@Service
public class NotifyMessageImpl extends AbstractDefaultService<NotifyMessageDto, NotifyMessageEntity, NotifyMessageDao, NotifyMessageConverter> implements NotifyMessageService {

    protected NotifyMessageImpl(NotifyMessageDao notifyMessageDao, NotifyMessageConverter converter) {
        super(notifyMessageDao, converter);
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessageDto getLastNotifyMessage() {
        return defaultConverter.convertToDto(defaultEntityDao.getLastNotifyMessage());
    }
}
