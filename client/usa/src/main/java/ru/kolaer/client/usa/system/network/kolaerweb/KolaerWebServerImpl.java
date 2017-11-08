package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;
import ru.kolaer.client.usa.tools.Resources;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {
    private final ObjectMapper objectMapper;
    private final RestTemplate globalRestTemplate;
    private ApplicationDataBase applicationDataBase;
    private ServerTools serverTools;

    public KolaerWebServerImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, StringBuilder path) {
        this.objectMapper = objectMapper;
        this.globalRestTemplate = globalRestTemplate;
        this.applicationDataBase = new ApplicationDataBaseImpl(objectMapper, globalRestTemplate,
                path.append("/rest").toString());
        this.serverTools = new ServerToolsImpl(objectMapper, globalRestTemplate, path.toString());
    }

    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://"+Resources.URL_TO_PUBLIC_SERVER.toString()).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if(200 >= responseCode && responseCode <= 399) {
                return ServerResponse.createServerResponse(ServerStatus.AVAILABLE);
            }
        } catch (Exception ex) {
            return ServerResponse.createServerResponse(ServerStatus.NOT_AVAILABLE);
        }

        return ServerResponse.createServerResponse(ServerStatus.UNKNOWN);
    }

    @Override
    public String getUrl() {
        return Resources.URL_TO_PUBLIC_SERVER.toString();
    }

    @Override
    public ApplicationDataBase getApplicationDataBase() {
        return this.applicationDataBase;
    }

    @Override
    public ServerTools getServerTools() {
        return this.serverTools;
    }
}
