package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;
import ru.kolaer.client.usa.system.network.RestTemplateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 25.08.2016.
 */
public class ServerToolsImpl implements ServerTools, RestTemplateService {
    private static final Logger LOG = LoggerFactory.getLogger(ServerToolsImpl.class);
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String URL_GET_TIME;

    public ServerToolsImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET_TIME = path + "/non-security/tools/get/time";
    }

    @Override
    public ServerResponse<LocalDateTime> getCurrentDataTime() {
        ServerResponse<LocalDateTime> serverResponse = new ServerResponse<>();

        ServerResponse<DateTimeJson> response = getServerResponse(restTemplate, URL_GET_TIME, 
                DateTimeJson.class, objectMapper);
        if(response.isServerError()){
            serverResponse.setServerError(response.isServerError());
            serverResponse.setExceptionMessage(response.getExceptionMessage());
        } else {
            DateTimeJson dateTimeJson = response.getResponse();
            serverResponse.setResponse(LocalDateTime.parse(dateTimeJson.toString(),  DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm:ss")));
        }

        return serverResponse;
    }
}
