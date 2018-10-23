package ru.kolaer.server.webportal.microservice.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.error.ErrorCode;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.common.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.account.service.AuthenticationService;
import ru.kolaer.server.webportal.microservice.chat.entity.ChatRoomEntity;
import ru.kolaer.server.webportal.microservice.chat.repository.ChatRoomRepository;
import ru.kolaer.server.webportal.microservice.chat.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.microservice.chat.entity.ChatMessageEntity;
import ru.kolaer.server.webportal.microservice.chat.repository.ChatMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 08.11.2017.
 */
@Service
@Slf4j
public class ChatMessageServiceImpl extends AbstractDefaultService<ChatMessageDto, ChatMessageEntity, ChatMessageRepository, ChatMessageConverter>
        implements ChatMessageService {
    private final AuthenticationService authenticationService;
    private final ChatRoomRepository chatRoomDao;

    protected ChatMessageServiceImpl(ChatMessageRepository defaultEntityDao,
                                     ChatMessageConverter converter,
                                     AuthenticationService authenticationService,
                                     ChatRoomRepository chatRoomDao) {
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
