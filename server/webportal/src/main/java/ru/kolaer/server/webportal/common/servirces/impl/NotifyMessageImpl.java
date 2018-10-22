package ru.kolaer.server.webportal.common.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.notification.NotifyMessageService;
import ru.kolaer.server.webportal.microservice.notification.NotifyMessageConverter;
import ru.kolaer.server.webportal.microservice.notification.NotifyMessageRepository;
import ru.kolaer.server.webportal.microservice.notification.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
@Service
public class NotifyMessageImpl extends AbstractDefaultService<NotifyMessageDto, NotifyMessageEntity, NotifyMessageRepository, NotifyMessageConverter> implements NotifyMessageService {

    protected NotifyMessageImpl(NotifyMessageRepository notifyMessageDao, NotifyMessageConverter converter) {
        super(notifyMessageDao, converter);
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessageDto getLastNotifyMessage() {
        return defaultConverter.convertToDto(defaultEntityDao.getLastNotifyMessage());
    }
}
