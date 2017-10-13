package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
class KolpassTableImpl implements KolpassTable, TokenToHeader {
    private static final Logger log = LoggerFactory.getLogger(KolpassTableImpl.class);
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final String PATH;
    private final String UPDATE_REPOSITORY_PASSWORD;
    private final String DELETE_REPOSITORY_PASSWORD;
    private final String GET_ALL_MY_REPOSITORY_PASS;
    private final String GET_ALL_CHIEF_REPOSITORY_PASS;
    private final String ADD_REPOSITORY_PASSWORD;
    private final String ADD_HISTORY_PASSWORD_TO_REP;
    private final String ADD_REPOSITORY_PASSWORD_OTHER_EMP;

    KolpassTableImpl(RestTemplate globalRestTemplate, String path) {
        this.restTemplate = globalRestTemplate;
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.PATH = path;
        this.GET_ALL_MY_REPOSITORY_PASS = this.PATH + "/get/all/personal";
        this.GET_ALL_CHIEF_REPOSITORY_PASS = this.PATH + "/get/all/chief";
        this.ADD_REPOSITORY_PASSWORD = this.PATH + "/add";
        this.ADD_REPOSITORY_PASSWORD_OTHER_EMP = this.PATH + "/add/employee";
        this.ADD_HISTORY_PASSWORD_TO_REP = this.PATH + "/passwords/add";
        this.DELETE_REPOSITORY_PASSWORD = this.PATH + "/delete";
        this.UPDATE_REPOSITORY_PASSWORD = this.PATH + "/update";
    }

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswords() {
        final String body = restTemplate
                .exchange(this.GET_ALL_MY_REPOSITORY_PASS,
                        HttpMethod.GET,
                        new HttpEntity<>(this.getTokenToHeader()),
                        String.class)
                .getBody();
        try {
            return ((Page<RepositoryPassword>) mapper
                    .readValue(body, new TypeReference<Page<RepositoryPassword>>(){})).getData();
        } catch (IOException e) {
            throw new ServerException("Ошибка при чтении!", e);
        }
    }

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswordsChief() {
         return Arrays.asList(this.restTemplate.exchange(this.GET_ALL_CHIEF_REPOSITORY_PASS,
                HttpMethod.GET,
                new HttpEntity<>(this.getTokenToHeader()),
                RepositoryPassword[].class).getBody());
    }


    @Override
    public RepositoryPassword addRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        return restTemplate.exchange(this.ADD_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(repositoryPasswordDto, this.getTokenToHeader()),
                RepositoryPassword.class).getBody();
    }

    @Override
    public RepositoryPassword addHistoryPasswordToRepository(Integer idRep,
                                                             RepositoryPasswordHistory repositoryPasswordHistory) {
        final RepositoryPassword repositoryPassword = new PasswordRepositoryDto();
        repositoryPassword.setId(idRep);
        repositoryPassword.setLastPassword(repositoryPasswordHistory);

        return restTemplate.exchange(this.ADD_HISTORY_PASSWORD_TO_REP,
                HttpMethod.POST,
                new HttpEntity<>(repositoryPassword, this.getTokenToHeader()),
                RepositoryPassword.class).getBody();
    }

    @Override
    public RepositoryPassword updateRepositoryPassword(RepositoryPassword repositoryPassword) {
        final RepositoryPassword request = new PasswordRepositoryDto();
        request.setId(repositoryPassword.getId());
        request.setName(repositoryPassword.getName());

        return restTemplate.exchange(this.UPDATE_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(request, this.getTokenToHeader()),
                RepositoryPassword.class).getBody();
    }

    @Override
    public void deleteRepositoryPassword(RepositoryPassword repositoryPassword) {
        final RepositoryPassword request = new PasswordRepositoryDto();
        request.setId(repositoryPassword.getId());

        restTemplate.exchange(this.DELETE_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(request, this.getTokenToHeader()),
                String.class);
    }

    @Override
    public RepositoryPassword addRepToOtherEmployee(RepositoryPassword rep) {
        final RepositoryPassword repositoryPassword = new PasswordRepositoryDto();
        repositoryPassword.setName(rep.getName());
        repositoryPassword.setEmployee(rep.getEmployee());

        return restTemplate.exchange(this.ADD_REPOSITORY_PASSWORD_OTHER_EMP,
                HttpMethod.POST,
                new HttpEntity<>(repositoryPassword, this.getTokenToHeader()),
                RepositoryPassword.class).getBody();
    }
}
