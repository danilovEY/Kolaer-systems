package ru.kolaer.client.usa.system.network.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.restful.RestfulServer;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.client.usa.tools.Resources;

/**
 * Created by danilovey on 29.07.2016.
 */
public class RestfulServerImpl implements RestfulServer, RestTemplateService {
    private final ObjectMapper objectMapper;
    private RestTemplate globalRestTemplate;
    private final String urlToStatus;

    public RestfulServerImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, StringBuilder url) {
        this.objectMapper = objectMapper;
        this.globalRestTemplate = globalRestTemplate;
        this.urlToStatus = url.toString() + "/system/server/status";
    }

    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        ServerResponse<String> serverResponse = getServerResponse(globalRestTemplate, urlToStatus,
                String.class, objectMapper);

        if (serverResponse.isServerError()) {
            return ServerResponse.createServerResponse(ServerStatus.NOT_AVAILABLE);
        } else {
            return ServerResponse.createServerResponse(ServerStatus.AVAILABLE);
        }
    }

    @Override
    public String getUrl() {
        return Resources.URL_TO_PRIVATE_SERVER.toString();
    }
}
