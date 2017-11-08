package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.server.webportal.mvc.model.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
public class ChatMessageServiceImpl extends AbstractDefaultService<ChatMessageDto, ChatMessageEntity> implements ChatMessageService {
    protected ChatMessageServiceImpl(ChatMessageDao defaultEntityDao, ChatMessageConverter converter) {
        super(defaultEntityDao, converter);
    }
}
