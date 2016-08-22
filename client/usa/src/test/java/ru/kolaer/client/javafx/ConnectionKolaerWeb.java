package ru.kolaer.client.javafx;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageBase;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.javafx.system.network.AuthenticationOnNetwork;
import ru.kolaer.client.javafx.system.network.NetworkUSImpl;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.Date;

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
        Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append("localhost:8080");//"js:8080/ru.kolaer.server.restful");
        Resources.URL_TO_KOLAER_WEB.delete(0, Resources.URL_TO_KOLAER_WEB.length()).append("localhost:8080");
    }

    @Test
    @Ignore
    public void testAuth() {
        Authentication authentication = new AuthenticationOnNetwork();
        if(authentication.login(new UserAndPassJson("user", "user"))) {
            System.out.println("УРААА");
            if(authentication.isAuthentication()) {
                System.out.println("Auth");
                System.out.println(authentication.getAuthorizedUser().getUsername());
                for(GeneralRolesEntity role : authentication.getAuthorizedUser().getRoles()) {
                    System.out.println(role.getType());
                }
            }
        }
    }

    @Test
    public void test() {

        RestTemplate restTemplate = new RestTemplate();
        /*restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));*/
        try{

            //PsrRegister[] array = restTemplate.getForObject("http://localhost:8080/rest/psr/get/all", PsrRegister[].class);
            UniformSystemEditorKitSingleton.getInstance().setAuthentication(new AuthenticationOnNetwork());
            UniformSystemEditorKitSingleton.getInstance().setUSNetwork(new NetworkUSImpl());
            //UniformSystemEditorKitSingleton.getInstance().getAuthentication().login(new UserAndPassJson("kolaeradmin", "kolaeradmin"));
            final NetworkUS networkUS = UniformSystemEditorKitSingleton.getInstance().getUSNetwork();
            Integer i = networkUS.getRestfulServer().getKolaerDataBase().getUserBirthdayAllDataBase().getCountUsersBirthday(new Date());
            System.out.println(i);
            /*NotifyMessage mess = new NotifyMessageBase();
            mess.setMessage("Система обновлена до версии 2.0!");
            networkUS.getKolaerWebServer().getApplicationDataBase().getNotifyMessageTable().addNotifyMessage(mess);
            mess = networkUS.getKolaerWebServer().getApplicationDataBase().getNotifyMessageTable().getLastNotifyMessage();
            Assert.assertNotNull(mess);
            System.out.println(mess.getMessage());*/
        } catch (ServerException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("AAAAA");
    }
}
