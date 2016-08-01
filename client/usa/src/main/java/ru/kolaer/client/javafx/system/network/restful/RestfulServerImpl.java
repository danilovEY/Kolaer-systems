package ru.kolaer.client.javafx.system.network.restful;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.restful.KolaerDataBase;
import ru.kolaer.api.system.network.restful.RestfulServer;

/**
 * Created by danilovey on 29.07.2016.
 */
public class RestfulServerImpl implements RestfulServer {
    /**БД через RESTful.*/
    private KolaerDataBase kolaerDataBase;
    private StringBuilder url;
    private final String urlToStatus;

    public RestfulServerImpl(StringBuilder url) {
        this.url = url;
        this.urlToStatus = url.toString() + "/system/server/status";
        this.kolaerDataBase = new KolaerDataBaseRESTful(url.toString() + "/database");
    }

    @Override
    public ServerStatus getServerStatus() {
        try {
            final RestTemplate webRes = new RestTemplate();

            final String status = webRes.getForObject(this.urlToStatus, String.class);
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

    @Override
    public KolaerDataBase getKolaerDataBase() {
        return this.kolaerDataBase;
    }

}
