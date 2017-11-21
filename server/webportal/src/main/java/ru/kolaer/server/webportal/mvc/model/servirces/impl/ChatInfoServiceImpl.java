package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.server.webportal.mvc.model.converter.ChatInfoConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ChatInfoDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatInfoEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatInfoService;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
public class ChatInfoServiceImpl extends AbstractDefaultService<ChatInfoDto, ChatInfoEntity> implements ChatInfoService {

    protected ChatInfoServiceImpl(ChatInfoDao defaultEntityDao, ChatInfoConverter converter) {
        super(defaultEntityDao, converter);
    }

}
