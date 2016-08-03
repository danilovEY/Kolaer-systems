package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

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
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://js:8080/kolaer-web-test").openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if(200 <= responseCode && responseCode <= 399) {
                return ServerStatus.AVAILABLE;
            }
        } catch (Exception ex) {
            return ServerStatus.NOT_AVAILABLE;
        }

        return ServerStatus.UNKNOWN;
    }

    @Override
    public ApplicationDataBase getApplicationDataBase() {
        return this.applicationDataBase;
    }
}
