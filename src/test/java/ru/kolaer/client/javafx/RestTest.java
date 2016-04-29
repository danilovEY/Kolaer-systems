package ru.kolaer.client.javafx;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import ru.kolaer.api.mvp.model.DbBirthdayAll;
import ru.kolaer.api.mvp.model.DbDataAll;
import ru.kolaer.api.mvp.model.PublicHolidays;
import ru.kolaer.client.javafx.system.JsonConverterSinleton;
import ru.kolaer.client.javafx.system.NetworkUSImpl;
import ru.kolaer.client.javafx.system.UserDataAllDataBaseRESTful;
import ru.kolaer.client.javafx.tools.Resources;

public class RestTest {
	
	@Before
	public void before() {
		 Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append("js:8080/ru.kolaer.server.restful");
	}
	
	@Test
	public void name() throws InterruptedException {
		NetworkUSImpl networkUSImpl = new NetworkUSImpl();
		
		Integer in = networkUSImpl.getKolaerDataBase().getUserDataAllDataBase().getCountUsersBirthday(new Date());
		System.out.println(in);
		
		
		TimeUnit.SECONDS.sleep(10);
		
	}
	
	@Test
	@Ignore
	public void testTest() throws IOException, InterruptedException {
		ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(UriBuilder.fromUri("http://js:8080/ru.kolaer.server.restful/database" + "/birthdayAll/get/users/birthday/today").build());
        // getting XML data

        service.path("dataAll/get/users/birthday/today").type(MediaType.APPLICATION_JSON);
        /*System.out.println(dbData);
        ObjectMapper m = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(dbData);
        jp.nextToken();
        while (jp.nextToken() == JsonToken.START_OBJECT) {
        	DbDataAll foobar = m.readValue(jp, DbDataAll.class);
            System.out.println(foobar.getName());
          }*/
        
        for(final DbBirthdayAll d : JsonConverterSinleton.getInstance().getEntitys(service, DbBirthdayAll.class)) {
        	System.out.println(d.getInitials());
        }
        
        TimeUnit.SECONDS.sleep(10);
        // getting JSON data
        //System.out.println(service. path("restPath").path("resourcePath").accept(MediaType.APPLICATION_XML).get(String.class));
        //restTemplate.getForObject(, );
	}
}
