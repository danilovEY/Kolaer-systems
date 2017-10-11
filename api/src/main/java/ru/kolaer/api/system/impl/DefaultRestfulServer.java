package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.restful.RestfulServer;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultRestfulServer implements RestfulServer {
    @Override
    public ServerResponse<ServerStatus> getServerStatus() {
        return ServerResponse.createServerResponse(ServerStatus.AVAILABLE);
    }
}
