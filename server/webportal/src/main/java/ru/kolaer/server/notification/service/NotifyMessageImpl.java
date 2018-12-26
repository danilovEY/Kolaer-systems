package ru.kolaer.server.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.notification.dao.NotifyMessageDao;
import ru.kolaer.server.notification.model.entity.NotifyMessageEntity;

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
