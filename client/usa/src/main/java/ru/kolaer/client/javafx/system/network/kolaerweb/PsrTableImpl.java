package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

/**
 * Created by Danilov on 31.07.2016.
 */
public class PsrTableImpl implements PsrTable {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_GET_ALL;
    private static final Logger LOG = LoggerFactory.getLogger(PsrTableImpl.class);

    public PsrTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public PsrRegister[] getAllPsrRegister() throws ServerException {
        try {
            if(!UniformSystemEditorKitSingleton.getInstance().getAuthentication().isAuthentication())
                throw new ServerException("Не авторизован!");
            return this.restTemplate.getForObject(this.URL_GET_ALL + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(), PsrRegister[].class);
        } catch (RestClientException ex) {
            LOG.error(ex.getMessage());
            throw new ServerException(ex);
        }
    }
}
