package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.system.network.Server;

/**
 * Created by Danilov on 28.07.2016.
 */
public interface KolaerWebServer extends Server {
    ApplicationDataBase getApplicationDataBase();
}
