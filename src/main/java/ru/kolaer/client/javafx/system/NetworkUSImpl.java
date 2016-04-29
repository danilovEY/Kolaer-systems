package ru.kolaer.client.javafx.system;

import java.util.Objects;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import ru.kolaer.api.mvp.model.DbDataAll;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.KolaerDataBase;
import ru.kolaer.api.system.NetworkUS;
import ru.kolaer.api.system.OtherPublicAPI;
import ru.kolaer.api.system.ServerStatus;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Реализация интерфейса для работы с сетью.
 *
 * @author danilovey
 * @version 0.1
 */
public class NetworkUSImpl implements NetworkUS {
	/**БД через RESTful.*/
	private KolaerDataBase kolaerDataBase;
	/**БД через RESTful.*/
	private OtherPublicAPI otherPublicAPI;
	
	private final Client client;
	private WebResource service;
	
	public NetworkUSImpl() {
        this.client = Client.create();
        this.service = client.resource("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString());
        this.kolaerDataBase = new KolaerDataBaseRESTful(service.path("database"));
        this.otherPublicAPI = new OtherPublicAPIImpl();
        /*WebResource service = client.resource("http://js:8080/ru.kolaer.server.restful/database");
        // getting XML data

        String dbData = service.path("dataAll/get/users/birthday/today").type(MediaType.APPLICATION_JSON).get(String.class);
        Objects.nonNull(dbData);
        System.out.println(dbData);
        ObjectMapper m = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(dbData);
        jp.nextToken();
        while (jp.nextToken() == JsonToken.START_OBJECT) {
        	DbDataAll foobar = m.readValue(jp, DbDataAll.class);
            System.out.println(foobar.getInitials());
            }*/
	}
	
	@Override
	public KolaerDataBase getKolaerDataBase() {
		return this.kolaerDataBase;
	}

	@Override
	public ServerStatus getServerStatus() {
		try {
			final WebResource webRes = service.path("system").path("server").path("status");

			final String status = webRes.get(String.class);
			if(status == null)
				return ServerStatus.NOT_AVAILABLE;
			switch (status) {
				case "available": return ServerStatus.AVAILABLE;
				case "not available": return ServerStatus.NOT_AVAILABLE;
				default: return ServerStatus.UNKNOW;
			}
		} catch(final UniformInterfaceException | ClientHandlerException ex) {
			return ServerStatus.NOT_AVAILABLE;
		}
	}

	@Override
	public OtherPublicAPI getOtherPublicAPI() {
		return this.otherPublicAPI;
	}
}
