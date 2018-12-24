package ru.kolaer.common.system.impl;

import ru.kolaer.common.system.network.NetworkUS;
import ru.kolaer.common.system.network.OtherPublicAPI;
import ru.kolaer.common.system.network.kolaerweb.KolaerWebServer;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultNetworkUS implements NetworkUS {
    private final KolaerWebServer kolaerWebServer = new DefaultKolaerWebServer();
    private final OtherPublicAPI publicAPI = new DefaultOtherPublicAPI();

    @Override
    public KolaerWebServer getKolaerWebServer() {
        return this.kolaerWebServer;
    }

    @Override
    public OtherPublicAPI getOtherPublicAPI() {
        return this.publicAPI;
    }

}
