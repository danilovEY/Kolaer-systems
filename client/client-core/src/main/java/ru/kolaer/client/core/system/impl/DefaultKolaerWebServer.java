package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.network.ServerStatus;
import ru.kolaer.client.core.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.client.core.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.client.core.system.network.kolaerweb.ServerTools;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultKolaerWebServer implements KolaerWebServer {
    private final ApplicationDataBase applicationDataBase = new DefaultApplicationDataBase();
    private final ServerTools serverTools = new DefaultServerTools();

    @Override
    public ApplicationDataBase getApplicationDataBase() {
        return this.applicationDataBase;
    }

    @Override
    public ServerTools getServerTools() {
        return this.serverTools;
    }

    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        return ServerResponse.createServerResponse(ServerStatus.AVAILABLE);
    }

    @Override
    public String getUrl() {
        return "localhost:8080";
    }
}
