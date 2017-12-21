package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.network.ChatTable;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 10.11.2017.
 */
public class ChatTableImpl implements ChatTable, RestTemplateService {
    private final String URL_GET_ALL_ACTIVE;
    private final String URL_POST_CREATE_PRIVATE_GROUP;
    private final String URL_GET_GROUP;
    private final String URL_HIDE_MESSAGES;
    private final String URL_GET_ACTIVE_BY_ACCOUNT_ID;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    

    public ChatTableImpl(String url, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.URL_HIDE_MESSAGES = url + "/message/hide";
        this.URL_GET_ALL_ACTIVE = url + "/active/all";
        this.URL_POST_CREATE_PRIVATE_GROUP = url + "/group/private";
        this.URL_GET_GROUP = url + "/group/";
        this.URL_GET_ACTIVE_BY_ACCOUNT_ID = url + "/active?account_id=";
    }


    @Override
    public ServerResponse<List<ChatGroupDto>> getActiveGroup() {
        return getServerResponses(restTemplate, URL_GET_ALL_ACTIVE, ChatGroupDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<ChatUserDto> getActiveByIdAccount(Long id) {
        return getServerResponse(restTemplate, URL_GET_ACTIVE_BY_ACCOUNT_ID + id, ChatUserDto.class, objectMapper);
    }

    @Override
    public ServerResponse<ChatGroupDto> createPrivateGroup(IdsDto idsDto, String name) {
        String atr = Optional.ofNullable(name)
                .map(n -> "?name=" + n)
                .orElse("");

        return postServerResponse(restTemplate, URL_POST_CREATE_PRIVATE_GROUP + atr,
                idsDto,
                ChatGroupDto.class, objectMapper);
    }

    @Override
    public ServerResponse<ChatGroupDto> getGroupByRoomId(String roomId) {
        return getServerResponse(restTemplate, URL_GET_GROUP + roomId, ChatGroupDto.class, objectMapper);
    }

    @Override
    public ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(String roomId) {
        return getPageResponse(restTemplate, URL_GET_GROUP + roomId + "/messages", ChatMessageDto.class, objectMapper);
    }

    @Override
    public ServerResponse hideMessage(IdsDto idsDto) {
        return postServerResponse(restTemplate, URL_HIDE_MESSAGES,
                idsDto,
                String.class, objectMapper);
    }
}