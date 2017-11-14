package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.system.network.ChatTable;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public class ChatTableImpl implements ChatTable, RestTemplateService {
    private final String URL_GET_ACTIVE;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    

    public ChatTableImpl(String url, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.URL_GET_ACTIVE = url + "/active/all";
    }


    @Override
    public ServerResponse<List<ChatGroupDto>> getActiveGroup() {
        return getServerResponses(restTemplate, URL_GET_ACTIVE, ChatGroupDto[].class, objectMapper);
    }
}