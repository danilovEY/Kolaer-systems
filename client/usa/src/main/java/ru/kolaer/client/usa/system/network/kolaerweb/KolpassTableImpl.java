package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.common.system.network.kolaerweb.KolpassTable;

import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
class KolpassTableImpl implements KolpassTable, TokenToHeader, RestTemplateService {
    private static final Logger log = LoggerFactory.getLogger(KolpassTableImpl.class);
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String PATH;
    private final String UPDATE_REPOSITORY_PASSWORD;
    private final String DELETE_REPOSITORY_PASSWORD;
    private final String GET_ALL_MY_REPOSITORY_PASS;
    private final String GET_ALL_CHIEF_REPOSITORY_PASS;
    private final String ADD_REPOSITORY_PASSWORD;
    private final String ADD_HISTORY_PASSWORD_TO_REP;
    private final String ADD_REPOSITORY_PASSWORD_OTHER_EMP;

    KolpassTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;

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
    public ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswords() {
        ResponseEntity<String> response = restTemplate.exchange(GET_ALL_MY_REPOSITORY_PASS,
                HttpMethod.GET,
                new HttpEntity<>(this.getTokenToHeader()),
                String.class);
        return getServerResponses(response,PasswordRepositoryDto[].class, objectMapper);
    }

    @Override
    public ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswordsChief() {
        ResponseEntity<String> response = this.restTemplate.exchange(this.GET_ALL_CHIEF_REPOSITORY_PASS,
                HttpMethod.GET,
                new HttpEntity<>(this.getTokenToHeader()),
                String.class);

        return getServerResponses(response,PasswordRepositoryDto[].class, objectMapper);
    }


    @Override
    public ServerResponse<PasswordRepositoryDto> addRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto) {
        return getServerResponse(restTemplate.exchange(this.ADD_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(passwordRepositoryDto, this.getTokenToHeader()),
                String.class),
                PasswordRepositoryDto.class, objectMapper);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> addHistoryPasswordToRepository(Long idRep,
                                                                PasswordHistoryDto passwordHistoryDto) {
        PasswordRepositoryDto repositoryPassword = new PasswordRepositoryDto();
        repositoryPassword.setId(idRep);

        return getServerResponse(restTemplate.exchange(this.ADD_HISTORY_PASSWORD_TO_REP,
                HttpMethod.POST,
                new HttpEntity<>(repositoryPassword, this.getTokenToHeader()),
                String.class), PasswordRepositoryDto.class, objectMapper);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> updateRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto) {
        return getServerResponse(restTemplate.exchange(this.UPDATE_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(passwordRepositoryDto, this.getTokenToHeader()),
                String.class), PasswordRepositoryDto.class, objectMapper);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> deleteRepositoryPassword(PasswordRepositoryDto repositoryDto) {
        return getServerResponse(restTemplate.exchange(this.DELETE_REPOSITORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(repositoryDto, this.getTokenToHeader()),
                String.class), null, objectMapper);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> addRepToOtherEmployee(PasswordRepositoryDto rep) {
        return getServerResponse(restTemplate.exchange(this.ADD_REPOSITORY_PASSWORD_OTHER_EMP,
                HttpMethod.POST,
                new HttpEntity<>(rep, this.getTokenToHeader()),
                String.class), PasswordRepositoryDto.class, objectMapper);
    }
}
