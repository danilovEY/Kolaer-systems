package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
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
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatRoomDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatRoomEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
@Slf4j
public class ChatMessageServiceImpl extends AbstractDefaultService<ChatMessageDto, ChatMessageEntity, ChatMessageDao, ChatMessageConverter>
        implements ChatMessageService {
    private final AuthenticationService authenticationService;
    private final ChatRoomDao chatRoomDao;

    protected ChatMessageServiceImpl(ChatMessageDao defaultEntityDao,
                                     ChatMessageConverter converter,
                                     AuthenticationService authenticationService,
                                     ChatRoomDao chatRoomDao) {
        super(defaultEntityDao, converter);

        this.authenticationService = authenticationService;
        this.chatRoomDao = chatRoomDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getAllByRoom(Long roomId) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        ChatRoomEntity chatRoomEntity = chatRoomDao.findById(roomId);

        if(chatRoomEntity != null && (accountByAuthentication.isAccessOit() ||
                chatRoomEntity.getType() == ChatGroupType.MAIN ||
                chatRoomEntity.getType() == ChatGroupType.PUBLIC ||
                chatRoomEntity.getUserCreatedId().equals(accountByAuthentication.getId()) ||
                chatRoomEntity.getAccountIds().contains(accountByAuthentication.getId()))) {
            return checkReads(defaultConverter.convertToDto(defaultEntityDao.findAllByRoom(roomId, accountByAuthentication.isAccessOit())),
                    accountByAuthentication.getId());
        }

        log.info("У пользователя: {} нет доступа к чату: {}", accountByAuthentication.getId(), roomId);

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getAllByRoom(Long room, Integer number, Integer pageSize) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        ChatRoomEntity chatRoomEntity = chatRoomDao.findById(room);

        if(chatRoomEntity != null && (accountByAuthentication.isAccessOit() ||
                chatRoomEntity.getType() == ChatGroupType.MAIN ||
                chatRoomEntity.getType() == ChatGroupType.PUBLIC ||
                chatRoomEntity.getUserCreatedId().equals(accountByAuthentication.getId()) ||
                chatRoomEntity.getAccountIds().contains(accountByAuthentication.getId()))) {
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

            results = checkReads(results, accountByAuthentication.getId());

            return new Page<>(results, number, count, pageSize);
        }

        log.info("У пользователя: {} нет доступа к чату: {}", accountByAuthentication.getId(), room);

        throw new CustomHttpCodeException("У вас нет доступа к чату!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }

    private List<ChatMessageDto> checkReads(List<ChatMessageDto> messages, long accountId) {
        List<Long> messageIds = messages.stream()
                .map(ChatMessageDto::getId)
                .collect(Collectors.toList());

        if(!messageIds.isEmpty()) {
            List<Long> messageReads = defaultEntityDao.findReadByMessageAndAccount(messageIds, accountId);

            messages.stream()
                    .filter(message -> messageReads.contains(message.getId()))
                    .forEach(message -> message.setRead(true));
        }

        return messages;
    }
}
