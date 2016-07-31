package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.ServerStatus;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {
    private ApplicationDataBase applicationDataBase;

    public KolaerWebServerImpl(WebResource resource) {
        this.applicationDataBase = new ApplicationDataBaseImpl(resource.path("rest"));
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
