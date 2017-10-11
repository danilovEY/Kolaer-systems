package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultKolpassTable implements KolpassTable {
    private final List<RepositoryPasswordDto> repositoryPasswords;

    public DefaultKolpassTable() {
        this.repositoryPasswords = new ArrayList<>(5);
    }

    @Override
    public ServerResponse<List<RepositoryPasswordDto>> getAllRepositoryPasswords() {
        return ServerResponse.createServerResponse(repositoryPasswords);
    }

    @Override
    public ServerResponse<List<RepositoryPasswordDto>> getAllRepositoryPasswordsChief() {
        return ServerResponse.createServerResponse(repositoryPasswords);
    }

    @Override
    public ServerResponse<RepositoryPasswordDto> addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(repositoryPasswordDto);
        return ServerResponse.createServerResponse(repositoryPasswordDto);
    }

    @Override
    public ServerResponse<RepositoryPasswordDto> addHistoryPasswordToRepository(Long idRep,
                                                                RepositoryPasswordHistoryDto repositoryPasswordHistory) {
        log.info("Добавлен пароль");
        RepositoryPasswordDto repositoryPasswordBase = new RepositoryPasswordDto();
        repositoryPasswordBase.setId(idRep);
        repositoryPasswordBase.setLastPassword(repositoryPasswordHistory);
        return ServerResponse.createServerResponse(repositoryPasswordBase);
    }

    @Override
    public ServerResponse<RepositoryPasswordDto> updateRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Обновлен репозиторий паролей");
        return ServerResponse.createServerResponse(repositoryPasswordDto);
    }

    @Override
    public ServerResponse deleteRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Удален репозиторий паролей");
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<RepositoryPasswordDto> addRepToOtherEmployee(RepositoryPasswordDto rep) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(rep);
        return ServerResponse.createServerResponse(rep);
    }
}
