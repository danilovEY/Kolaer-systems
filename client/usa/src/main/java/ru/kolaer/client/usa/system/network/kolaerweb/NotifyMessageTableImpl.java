package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.network.RestTemplateService;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotifyMessageTableImpl implements NotifyMessageTable, RestTemplateService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private String URL_GET_LAST;
    private String URL_ADD;

    public NotifyMessageTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET_LAST = path + "/get/last";
        this.URL_ADD = path + "/add";
    }

    @Override
    public ServerResponse<NotifyMessageDto> getLastNotifyMessage() {
        return getServerResponse(restTemplate.getForEntity(this.URL_GET_LAST, String.class), NotifyMessageDto.class, objectMapper);

    }

    @Override
    public ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage) {
        return getServerResponse(restTemplate
                .postForEntity(URL_ADD + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(),
                        notifyMessage, String.class), null, objectMapper);
    }
}
