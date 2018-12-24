package ru.kolaer.common.system.network.kolaerweb;

import ru.kolaer.common.system.network.Server;

/**
 * Created by Danilov on 28.07.2016.
 */
public interface KolaerWebServer extends Server {
    ApplicationDataBase getApplicationDataBase();
    ServerTools getServerTools();
}
