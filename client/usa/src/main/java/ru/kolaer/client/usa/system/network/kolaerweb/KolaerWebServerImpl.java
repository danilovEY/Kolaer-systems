package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.ServerStatus;
import ru.kolaer.client.core.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.client.core.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.client.core.system.network.kolaerweb.ServerTools;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {
    private final ApplicationDataBase applicationDataBase;
    private final ServerTools serverTools;
    private final String path;

    public KolaerWebServerImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path, String chatSocketUrl) {
        this.path = path;
        this.applicationDataBase = new ApplicationDataBaseImpl(objectMapper, globalRestTemplate, path, chatSocketUrl);
        this.serverTools = new ServerToolsImpl(objectMapper, globalRestTemplate, path);
    }

    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        return ServerResponse.createServerResponse(ServerStatus.AVAILABLE); //TODO replace to getting from server
    }

    @Override
    public String getUrl() {
        return path;
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
