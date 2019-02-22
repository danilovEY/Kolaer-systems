package ru.kolaer.server.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.constant.assess.ChatAccessConstant;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.chat.dao.ChatMessageDao;
import ru.kolaer.server.chat.dao.ChatRoomDao;
import ru.kolaer.server.chat.model.entity.ChatMessageEntity;
import ru.kolaer.server.chat.model.entity.ChatRoomEntity;
import ru.kolaer.server.core.exception.CustomHttpCodeException;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.core.service.AuthenticationService;

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
        AccountAuthorizedDto accountByAuthentication = authenticationService.getAccountAuthorized();

        ChatRoomEntity chatRoomEntity = chatRoomDao.findById(roomId);

        if(chatRoomEntity != null && (accountByAuthentication.hasAccess(ChatAccessConstant.CHAT_ROOMS_READ) ||
                chatRoomEntity.getType() == ChatGroupType.MAIN ||
                chatRoomEntity.getType() == ChatGroupType.PUBLIC ||
                chatRoomEntity.getUserCreatedId().equals(accountByAuthentication.getId()) ||
                chatRoomEntity.getAccountIds().contains(accountByAuthentication.getId()))) {
            return checkReads(defaultConverter.convertToDto(defaultEntityDao.findAllByRoom(roomId, accountByAuthentication.hasAccess(ChatAccessConstant.CHAT_ROOMS_READ))),
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
        AccountAuthorizedDto accountByAuthentication = authenticationService.getAccountAuthorized();
        ChatRoomEntity chatRoomEntity = chatRoomDao.findById(room);

        if(chatRoomEntity != null && (accountByAuthentication.hasAccess(ChatAccessConstant.CHAT_ROOMS_READ) ||
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
                count = defaultEntityDao.findCountByRoom(room, accountByAuthentication.hasAccess(ChatAccessConstant.CHAT_ROOMS_READ));
                results = defaultConverter.convertToDto(defaultEntityDao
                        .findAllByRoom(room, accountByAuthentication.hasAccess(ChatAccessConstant.CHAT_ROOMS_READ), number, pageSize));
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
