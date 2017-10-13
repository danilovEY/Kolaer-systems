package ru.kolaer.client.usa.system.network.restful;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.restful.RestfulServer;

/**
 * Created by danilovey on 29.07.2016.
 */
public class RestfulServerImpl implements RestfulServer {
    private RestTemplate globalRestTemplate;
    private StringBuilder url;
    private final String urlToStatus;

    public RestfulServerImpl(RestTemplate globalRestTemplate, StringBuilder url) {
        this.globalRestTemplate = globalRestTemplate;
        this.url = url;
        this.urlToStatus = url.toString() + "/system/server/status";
    }

    @Override
    public ServerStatus getServerStatus() {
        try {
            final String status = this.globalRestTemplate.getForObject(this.urlToStatus, String.class);
            if(status == null)
                return ServerStatus.NOT_AVAILABLE;
            switch (status) {
                case "available": return ServerStatus.AVAILABLE;
                case "not available": return ServerStatus.NOT_AVAILABLE;
                default: return ServerStatus.UNKNOWN;
            }
        } catch(final RestClientException ex) {
            return ServerStatus.NOT_AVAILABLE;
        }
    }
}
