package ru.kolaer.client.javafx;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.client.javafx.system.network.NetworkUSImpl;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ConnectionKolaerWeb {

    @Before
    public void init() {
        Resources.URL_TO_KOLAER_WEB.delete(0, Resources.URL_TO_KOLAER_WEB.length()).append("localhost:8080/");
    }

    @Test
    public void test() {

        RestTemplate restTemplate = new RestTemplate();
        GeneralEmployeesEntity[] array = restTemplate.getForObject("http://localhost:8080/rest/general/employees/get/all", GeneralEmployeesEntityBase[].class);

        //NetworkUS networkUS = new NetworkUSImpl();
        //GeneralEmployeesEntity[] array = networkUS.getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getAllUser();
        Assert.assertNotNull(array);
        for(GeneralEmployeesEntity entity : array) {
            System.out.println(entity.getInitials());
        }
    }
}
