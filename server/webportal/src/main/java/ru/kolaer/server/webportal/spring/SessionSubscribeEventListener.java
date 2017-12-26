package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    private final ChatService chatService;
    private final AuthenticationService authenticationService;


    public SessionSubscribeEventListener(ChatService chatService,
                                         AuthenticationService authenticationService) {
        this.chatService = chatService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
//        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
//
//        Principal user = sha.getUser();
//
//        sha.getDestination();
//
//        log.info("{}", sha);

//        AccountDto accountDto = authenticationService
//                .getAccountWithEmployeeByLogin(user.getName());
//
//        ChatUserDto chatUserDto = chatService.createChatUserDto(accountDto);
//        chatUserDto.setSessionId(sha.getSessionId());
//
//        ChatUserDto oldActive = chatService.getUserByAccountId(accountDto.getId());
//        if (oldActive != null) {
//            chatService.removeActiveUser(oldActive);
//            chatService.sendDisconnect(oldActive.getSessionId());
//        }
//
//        chatService.addActiveUser(chatUserDto);
//
//        ChatInfoDto chatInfoDto = new ChatInfoDto();
//        chatInfoDto.setCommand(ChatInfoCommand.CONNECT);
//        chatInfoDto.setCreateInfo(new Date());
//        chatInfoDto.setAccountId(chatUserDto.getAccountId());
//        chatInfoDto.setAccount(chatUserDto.getAccount());
//
//        chatService.send(chatInfoDto);
//
//        ChatMessageDto chatMessageDto = new ChatMessageDto();
//        chatMessageDto.setCreateMessage(new Date());
//        chatMessageDto.setType(ChatMessageType.SERVER_INFO);
//        chatMessageDto.setMessage("Пользователь \"" + chatUserDto.getName() + "\" вошел в чат");
//
//        for (ChatGroupDto chatGroupDto : chatService.getAll()) {
//            if(chatGroupDto.getUsers().contains(chatUserDto)) {
//                chatMessageDto.setId(null);
//                chatMessageDto.setRoom(chatGroupDto.getRoomId());
//                chatService.send(chatMessageDto);
//            }
//        }
    }
}
