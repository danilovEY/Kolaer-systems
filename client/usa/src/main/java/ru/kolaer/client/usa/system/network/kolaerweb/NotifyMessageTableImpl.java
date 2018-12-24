package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.network.kolaerweb.NotifyMessageTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotifyMessageTableImpl implements NotifyMessageTable, RestTemplateService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private String URL_GET_LAST;
    private String URL_ADD;
    private String URL_GET;

    public NotifyMessageTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET_LAST = path + "/get/last";
        this.URL_GET = path + "/get?page={page}&pagesize={pagesize}";
        this.URL_ADD = path + "/add";
    }

    @Override
    public ServerResponse<NotifyMessageDto> getLastNotifyMessage() {
        return getServerResponse(restTemplate, URL_GET_LAST, NotifyMessageDto.class, objectMapper);
    }

    @Override
    public ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage) {
        return getServerResponse(restTemplate
                .postForEntity(URL_ADD + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(),
                        notifyMessage, String.class), null, objectMapper);
    }

    @Override
    public ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages() {
        return getAllNotifyMessages(0, 7);
    }

    @Override
    public ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages(int page, int pageSize) {
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("page", page);
        urlVariables.put("pagesize", pageSize);

        return getPageResponse(restTemplate, URL_GET, NotifyMessageDto.class, objectMapper, urlVariables);
    }
}
