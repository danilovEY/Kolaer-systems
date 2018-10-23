package ru.kolaer.server.service.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.service.notification.converter.NotifyMessageConverter;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.service.notification.repository.NotifyMessageRepository;
import ru.kolaer.server.service.notification.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
@Service
public class NotifyMessageServiceImpl extends AbstractDefaultService<NotifyMessageDto, NotifyMessageEntity, NotifyMessageRepository, NotifyMessageConverter> implements NotifyMessageService {

    protected NotifyMessageServiceImpl(NotifyMessageRepository notifyMessageDao, NotifyMessageConverter converter) {
        super(notifyMessageDao, converter);
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyMessageDto getLastNotifyMessage() {
        return defaultConverter.convertToDto(defaultEntityDao.getLastNotifyMessage());
    }
}
