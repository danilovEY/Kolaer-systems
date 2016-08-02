package ru.kolaer.client.javafx;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exeptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.client.javafx.system.network.NetworkUSImpl;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ConnectionKolaerWeb {

    public static void main(String[] argv) {
        Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append("js:8080/ru.kolaer.server.restful");

        NetworkUS networkUS = new NetworkUSImpl();
        DbDataAll[] array = networkUS.getRestfulServer().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
        Assert.assertNotNull(array);
        for(DbDataAll entity : array) {
            System.out.println(entity.getInitials());
        }
    }

    @Before
    public void init() {
        Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append("js:8080/ru.kolaer.server.restful");
        Resources.URL_TO_KOLAER_WEB.delete(0, Resources.URL_TO_KOLAER_WEB.length()).append("localhost:8080");
    }

    @Test
    public void test() {

        RestTemplate restTemplate = new RestTemplate();
        /*restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));*/
        try{

            //PsrRegister[] array = restTemplate.getForObject("http://localhost:8080/rest/psr/get/all", PsrRegister[].class);

            NetworkUS networkUS = new NetworkUSImpl();
            PsrRegister[] array = networkUS.getKolaerWebServer().getApplicationDataBase().getPsrTable().getAllPsrRegister();
            Assert.assertNotNull(array);
            for(PsrRegister entity : array) {
                System.out.println(entity.getName());
            }
        } catch (ServerException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("AAAAA");
    }
}
