package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {
    private ApplicationDataBase applicationDataBase;

    public KolaerWebServerImpl(StringBuilder path) {
        this.applicationDataBase = new ApplicationDataBaseImpl(path.append("/rest").toString());
    }

    @Override
    public ServerStatus getServerStatus() {
        return ServerStatus.NOT_AVAILABLE;
    }

    @Override
    public ApplicationDataBase getApplicationDataBase() {
        return this.applicationDataBase;
    }
}
