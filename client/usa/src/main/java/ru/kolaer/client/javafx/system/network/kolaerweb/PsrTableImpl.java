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
    private final String URL_ADD;
    private static final Logger LOG = LoggerFactory.getLogger(PsrTableImpl.class);

    public PsrTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
        this.URL_ADD = path + "/add";
    }

    @Override
    public PsrRegister[] getAllPsrRegister() throws ServerException {
        try {
            this.checkAuthorization();
            return this.restTemplate.getForObject(this.URL_GET_ALL + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(), PsrRegister[].class);
        } catch (RestClientException ex) {
            LOG.error(ex.getMessage());
            throw new ServerException(ex);
        }
    }

    @Override
    public PsrRegister persistPsrRegister(PsrRegister psrRegister) throws ServerException {
        if(psrRegister == null) {
            LOG.info("Psr register is NULL!");
            return psrRegister;
        }
        try {
            this.checkAuthorization();
            return this.restTemplate.postForObject(this.URL_ADD + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(),psrRegister, PsrRegister.class);
        } catch (RestClientException ex) {
            LOG.error(ex.getMessage());
            throw new ServerException(ex);
        }
    }

    private void checkAuthorization( ) {
        if(!UniformSystemEditorKitSingleton.getInstance().getAuthentication().isAuthentication())
            throw new ServerException("Не авторизован!");
    }
}
