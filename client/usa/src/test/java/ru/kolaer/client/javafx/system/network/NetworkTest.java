package ru.kolaer.client.javafx.system.network;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class NetworkTest {
    private NetworkUSImpl networkUS;
    private Authentication authentication;

    @Before
    public void init() {
        StringBuilder urlToKolaerWeb = Resources.URL_TO_KOLAER_WEB;
        urlToKolaerWeb.delete(0, urlToKolaerWeb.length());
        //urlToKolaerWeb.append("js:9090/kolaer-web-test");
        urlToKolaerWeb.append("localhost:8080");

        UniformSystemEditorKitSingleton instance = UniformSystemEditorKitSingleton.getInstance();
        this.networkUS = new NetworkUSImpl();
        this.authentication = new AuthenticationOnNetwork();
        instance.setUSNetwork(this.networkUS);
        instance.setAuthentication(this.authentication);

        this.authentication.login(new UserAndPassJson("danilovey", "lordbot"));
    }

    @Test
    @Ignore
    public void testRepositoryPasswordAdd() {
        KolpassTable kolpassTable = this.networkUS.getKolaerWebServer().getApplicationDataBase().getKolpassTable();

        RepositoryPasswordDto repositoryPasswordDto = new RepositoryPasswordDto();
        repositoryPasswordDto.setName("Test123");

        RepositoryPasswordDto repositoryPasswordDto1 = kolpassTable.addRepositoryPassword(repositoryPasswordDto);
        log.info(repositoryPasswordDto1.getId().toString());

    }

    @Test
    public void testRepositoryPasswordGetAll() {
        for (RepositoryPasswordDto repositoryPasswordDto : this.networkUS.getKolaerWebServer().getApplicationDataBase()
                .getKolpassTable().getAllRepositoryPasswords()) {
            log.info(repositoryPasswordDto.getName());
            RepositoryPasswordHistoryDto lastPassword = repositoryPasswordDto.getLastPassword();
            log.info(lastPassword.getLogin());
            log.info(lastPassword.getPassword());
            log.info(lastPassword.getPasswordWriteDate().toString());
        }

    }

}
