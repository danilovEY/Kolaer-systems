package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;

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
    public ServerStatus getServerStatus() {
        return ServerStatus.AVAILABLE;
    }
}
