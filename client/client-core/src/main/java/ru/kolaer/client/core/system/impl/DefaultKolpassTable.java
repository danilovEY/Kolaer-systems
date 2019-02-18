package ru.kolaer.client.core.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.core.system.network.kolaerweb.KolpassTable;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultKolpassTable implements KolpassTable {
    private final List<PasswordRepositoryDto> repositoryPasswords;

    public DefaultKolpassTable() {
        this.repositoryPasswords = new ArrayList<>(5);
    }

    @Override
    public ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswords() {
        return ServerResponse.createServerResponse(repositoryPasswords);
    }

    @Override
    public ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswordsChief() {
        return ServerResponse.createServerResponse(repositoryPasswords);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> addRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(passwordRepositoryDto);
        return ServerResponse.createServerResponse(passwordRepositoryDto);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> addHistoryPasswordToRepository(Long idRep,
                                                                                PasswordHistoryDto repositoryPasswordHistory) {
        log.info("Добавлен пароль");
        PasswordRepositoryDto repositoryPasswordBase = new PasswordRepositoryDto();
        repositoryPasswordBase.setId(idRep);
        return ServerResponse.createServerResponse(repositoryPasswordBase);
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> updateRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto) {
        log.info("Обновлен репозиторий паролей");
        return ServerResponse.createServerResponse(passwordRepositoryDto);
    }

    @Override
    public ServerResponse deleteRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto) {
        log.info("Удален репозиторий паролей");
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<PasswordRepositoryDto> addRepToOtherEmployee(PasswordRepositoryDto rep) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(rep);
        return ServerResponse.createServerResponse(rep);
    }
}
