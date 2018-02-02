package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.mvc.model.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatRoomService;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
public class ChatMessageServiceImpl extends AbstractDefaultService<ChatMessageDto, ChatMessageEntity, ChatMessageDao, ChatMessageConverter>
        implements ChatMessageService {
    private final AuthenticationService authenticationService;
    private final ChatRoomService chatService;

    protected ChatMessageServiceImpl(ChatMessageDao defaultEntityDao,
                                     ChatMessageConverter converter,
                                     AuthenticationService authenticationService,
                                     ChatRoomService chatService) {
        super(defaultEntityDao, converter);

        this.authenticationService = authenticationService;
        this.chatService = chatService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getAllByRoom(Long roomId) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        if(accountByAuthentication.isAccessOit()) {
            return defaultConverter.convertToDto(defaultEntityDao.findAllByRoom(roomId, accountByAuthentication.isAccessOit()));
        }

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getAllByRoom(Long room, Integer number, Integer pageSize) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        ChatRoomDto chatRoomDto = chatService.getById(room);
        if(accountByAuthentication.isAccessOit() ||
                chatRoomDto.getType() == ChatGroupType.MAIN ||
                chatRoomDto.getType() == ChatGroupType.PUBLIC) {
            Long count;
            List<ChatMessageDto> results;

            if(pageSize == null || pageSize == 0) {
                results = getAllByRoom(room);
                count = (long) results.size();
                pageSize = count.intValue();
            } else {
                count = defaultEntityDao.findCountByRoom(room, accountByAuthentication.isAccessOit());
                results = defaultConverter.convertToDto(defaultEntityDao
                        .findAllByRoom(room, accountByAuthentication.isAccessOit(), number, pageSize));
            }

            return new Page<>(results, number, count, pageSize);
        }

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }
}
