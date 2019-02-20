package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.ChatTable;
import ru.kolaer.client.core.system.network.ServerStatus;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.IdDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 10.11.2017.
 */
public class ChatTableImpl implements ChatTable, RestTemplateService {
    private final String URL_GET_ALL_ACTIVE;
    private final String URL_POST_CREATE_PRIVATE_GROUP;
    private final String URL_POST_CREATE_SINGLE_GROUP;
    private final String URL_POST_CREATE_SINGLES_GROUP;
    private final String URL_GET_GROUP;
    private final String URL_GET_ONLINE_USER;
    private final String URL_HIDE_MESSAGES;
    private final String URL_READ_MESSAGES;
    private final String URL_POST_QUIT_FROM_ROOM;
    private final String URL_DELETE_MESSAGES;
    private final String URL_GET_ACTIVE_BY_ACCOUNT_ID;
    private final String socketUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    

    public ChatTableImpl(RestTemplate restTemplate, ObjectMapper objectMapper, String url, String socketUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.URL_HIDE_MESSAGES = url + "/message/hide";
        this.URL_DELETE_MESSAGES = url + "/message/delete";
        this.URL_READ_MESSAGES = url + "/message/read";
        this.URL_GET_ALL_ACTIVE = url + "/room/all";
        this.URL_GET_ONLINE_USER = url + "/user/all";
        this.URL_POST_CREATE_PRIVATE_GROUP = url + "/room/private";
        this.URL_POST_QUIT_FROM_ROOM = url + "/room/quit";
        this.URL_POST_CREATE_SINGLE_GROUP = url + "/room/single";
        this.URL_POST_CREATE_SINGLES_GROUP = url + "/room/singles";
        this.URL_GET_GROUP = url + "/room/";
        this.URL_GET_ACTIVE_BY_ACCOUNT_ID = url + "/active?account_id=";
        this.socketUrl = socketUrl;
    }


    @Override
    public ServerResponse<List<ChatRoomDto>> getRooms() {
        return getServerResponses(restTemplate, URL_GET_ALL_ACTIVE, ChatRoomDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<List<ChatUserDto>> getOnlineUser() {
        return getServerResponses(restTemplate, URL_GET_ONLINE_USER, ChatUserDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<ChatUserDto> getActiveByIdAccount(Long id) {
        return getServerResponse(restTemplate, URL_GET_ACTIVE_BY_ACCOUNT_ID + id, ChatUserDto.class, objectMapper);
    }

    @Override
    public ServerResponse<ChatRoomDto> createPrivateRoom(IdsDto idsDto, String name) {
        String atr = Optional.ofNullable(name)
                .map(n -> "?name=" + n)
                .orElse("");

        return postServerResponse(restTemplate, URL_POST_CREATE_PRIVATE_GROUP + atr,
                idsDto,
                ChatRoomDto.class, objectMapper);
    }

    @Override
    public ServerResponse<ChatRoomDto> createSingleRoom(IdDto idDto) {
        return postServerResponse(restTemplate, URL_POST_CREATE_SINGLE_GROUP,
                idDto,
                ChatRoomDto.class, objectMapper);
    }

    @Override
    public ServerResponse quitFromRoom(IdsDto idsDto) {
        return postServerResponses(restTemplate, URL_POST_QUIT_FROM_ROOM,
                idsDto,
                null, objectMapper);
    }

    @Override
    public ServerResponse<List<ChatRoomDto>> createSingleRooms(IdsDto idDto) {
        return postServerResponses(restTemplate, URL_POST_CREATE_SINGLES_GROUP,
                idDto,
                ChatRoomDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<ChatRoomDto> getRoomById(long roomId) {
        return getServerResponse(restTemplate, URL_GET_GROUP + roomId, ChatRoomDto.class, objectMapper);
    }

    @Override
    public ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(long roomId) {
        return getPageResponse(restTemplate, URL_GET_GROUP + roomId + "/messages", ChatMessageDto.class, objectMapper);
    }

    @Override
    public ServerResponse hideMessage(IdsDto idsDto) {
        return postServerResponse(restTemplate, URL_HIDE_MESSAGES,
                idsDto,
                String.class, objectMapper);
    }

    @Override
    public ServerResponse deleteMessage(IdsDto idsDto) {
        return postServerResponse(restTemplate, URL_DELETE_MESSAGES,
                idsDto,
                String.class, objectMapper);
    }

    @Override
    public ServerResponse markAsReadMessage(IdsDto idsDto) {
        return postServerResponse(restTemplate, URL_READ_MESSAGES,
                idsDto,
                String.class, objectMapper);
    }

    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public String getUrl() {
        return this.socketUrl;
    }
}