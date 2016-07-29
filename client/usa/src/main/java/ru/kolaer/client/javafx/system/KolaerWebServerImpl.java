package ru.kolaer.client.javafx.system;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.KolaerWebServer;
import ru.kolaer.api.system.ServerStatus;

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
}
