package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.restful.RestfulServer;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultNetworkUS implements NetworkUS {
    private final RestfulServer restfulServer = new DefaultRestfulServer();
    private final KolaerWebServer kolaerWebServer = new DefaultKolaerWebServer();
    private final OtherPublicAPI publicAPI = new DefaultOtherPublicAPI();
    @Override
    public RestfulServer getRestfulServer() {
        return this.restfulServer;
    }

    @Override
    public KolaerWebServer getKolaerWebServer() {
        return this.kolaerWebServer;
    }

    @Override
    public OtherPublicAPI getOtherPublicAPI() {
        return this.publicAPI;
    }
}
