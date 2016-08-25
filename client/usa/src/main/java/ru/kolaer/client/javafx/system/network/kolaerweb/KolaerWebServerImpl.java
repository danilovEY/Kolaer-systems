package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;
import ru.kolaer.client.javafx.tools.Resources;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Danilov on 28.07.2016.
 */
public class KolaerWebServerImpl implements KolaerWebServer {
    private ApplicationDataBase applicationDataBase;
    private ServerTools serverTools;

    public KolaerWebServerImpl(StringBuilder path) {
        this.applicationDataBase = new ApplicationDataBaseImpl(path.append("/rest").toString());
        this.serverTools = new ServerToolsImpl(path.toString());
    }

    @Override
    public ServerStatus getServerStatus() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://"+Resources.URL_TO_KOLAER_WEB.toString()).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if(200 >= responseCode && responseCode <= 399) {
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

    @Override
    public ServerTools getServerTools() {
        return this.serverTools;
    }
}
