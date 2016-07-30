package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.ServerStatus;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {

    private WebResource serviceWeb;

    public KolaerWebServerImpl(WebResource resource) {

    }

    @Override
    public ServerStatus getServerStatus() {
        return null;
    }

    @Override
    public ApplicationDataBase getApplicationDataBase() {
        return null;
    }
}
