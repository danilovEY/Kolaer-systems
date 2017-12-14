package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.mvc.model.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ChatInfoDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
public class ChatMessageServiceImpl extends AbstractDefaultService<ChatMessageDto, ChatMessageEntity> implements ChatMessageService {
    private final ChatMessageDao chatMessageDao;
    private final ChatInfoDao chatInfoDao;
    private final AuthenticationService authenticationService;
    private final ChatService chatService;

    protected ChatMessageServiceImpl(ChatMessageDao defaultEntityDao,
                                     ChatMessageConverter converter,
                                     ChatInfoDao chatInfoDao,
                                     AuthenticationService authenticationService,
                                     ChatService chatService) {
        super(defaultEntityDao, converter);
        this.chatMessageDao = defaultEntityDao;
        this.chatInfoDao = chatInfoDao;

        this.authenticationService = authenticationService;
        this.chatService = chatService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getAllByRoom(String room) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        if(accountByAuthentication.isAccessOit() || chatInfoDao.existInvite(room, accountByAuthentication.getId())) {
            return baseConverter.convertToDto(chatMessageDao.findAllByRoom(room));
        }

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getAllByRoom(String room, Integer number, Integer pageSize) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        if(accountByAuthentication.isAccessOit() ||
                chatService.getByRoomId(room).getType() == ChatGroupType.MAIN ||
                chatService.getByRoomId(room).getType() == ChatGroupType.PUBLIC ||
                chatInfoDao.existInvite(room, accountByAuthentication.getId())) {
            Long count;
            List<ChatMessageDto> results;

            if(pageSize == null || pageSize == 0) {
                results = getAllByRoom(room);
                count = (long) results.size();
                pageSize = count.intValue();
            } else {
                count = chatMessageDao.findCountByRoom(room);
                results = baseConverter.convertToDto(chatMessageDao.findAllByRoom(room, number, pageSize));
            }

            return new Page<>(results, number, count, pageSize);
        }

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }
}
