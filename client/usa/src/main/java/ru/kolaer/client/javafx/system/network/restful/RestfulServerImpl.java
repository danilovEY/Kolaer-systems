package ru.kolaer.client.javafx.system.network.restful;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.network.restful.KolaerDataBase;
import ru.kolaer.api.system.network.restful.RestfulServer;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.client.javafx.system.network.kolaerweb.KolaerDataBaseRESTful;

/**
 * Created by danilovey on 29.07.2016.
 */
public class RestfulServerImpl implements RestfulServer {
    /**БД через RESTful.*/
    private KolaerDataBase kolaerDataBase;

    private WebResource serviceRest;

    public RestfulServerImpl(WebResource resource) {
        this.kolaerDataBase = new KolaerDataBaseRESTful(serviceRest.path("database"));
    }

    @Override
    public ServerStatus getServerStatus() {
        try {
            final WebResource webRes = serviceRest.path("system").path("server").path("status");

            final String status = webRes.get(String.class);
            if(status == null)
                return ServerStatus.NOT_AVAILABLE;
            switch (status) {
                case "available": return ServerStatus.AVAILABLE;
                case "not available": return ServerStatus.NOT_AVAILABLE;
                default: return ServerStatus.UNKNOWN;
            }
        } catch(final UniformInterfaceException | ClientHandlerException ex) {
            return ServerStatus.NOT_AVAILABLE;
        }
    }

    @Override
    public KolaerDataBase getKolaerDataBase() {
        return this.kolaerDataBase;
    }

}
