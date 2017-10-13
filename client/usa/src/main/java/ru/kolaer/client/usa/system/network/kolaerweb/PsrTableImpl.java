package ru.kolaer.client.usa.system.network.kolaerweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.network.kolaerweb.PsrStatusTable;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

/**
 * Created by Danilov on 31.07.2016.
 */
public class PsrTableImpl implements PsrTable {
    private static final Logger LOG = LoggerFactory.getLogger(PsrTableImpl.class);
    private final RestTemplate restTemplate;
    private final PsrStatusTable psrStatusTable;
    private final String URL_GET_ALL;
    private final String URL_ADD;
    private final String URL_DELETE;
    private final String URL_UPDATE;

    public PsrTableImpl(RestTemplate globalRestTemplate, String path) {
        this.restTemplate = globalRestTemplate;
        this.psrStatusTable = new PsrStatusTableImpl(path + "/status");
        this.URL_GET_ALL = path + "/get/all";
        this.URL_ADD = path + "/add";
        this.URL_DELETE = path + "/delete";
        this.URL_UPDATE = path + "/update";
    }

    @Override
    public PsrStatusTable getPsrStatusTable() {
        return this.psrStatusTable;
    }

    @Override
    public PsrRegister[] getAllPsrRegister() throws ServerException {
        try {
            this.checkAuthorization();
            PsrRegisterPage psrRegisterPage = this.restTemplate.getForObject(this.URL_GET_ALL + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(), PsrRegisterPage.class);
            return psrRegisterPage.getData().stream().toArray(PsrRegister[]::new);
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

    @Override
    public void deletePsrRegister(PsrRegister psrRegister) throws ServerException {
        if(psrRegister == null) {
            LOG.info("Psr register is NULL!");
            return;
        }

        try {
            this.checkAuthorization();
            this.restTemplate.postForObject(this.URL_DELETE + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(),psrRegister, PsrRegister.class);
        } catch (RestClientException ex) {
            LOG.error(ex.getMessage());
            throw new ServerException(ex);
        }
    }

    @Override
    public void updatePsrRegister(PsrRegister psrRegister) throws ServerException {
        if(psrRegister == null) {
            LOG.info("Psr register is NULL!");
            return;
        }

        try {
            this.checkAuthorization();
            this.restTemplate.postForObject(this.URL_UPDATE + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(),psrRegister, PsrRegister.class);
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

class PsrRegisterPage extends Page<PsrRegister> {

}
