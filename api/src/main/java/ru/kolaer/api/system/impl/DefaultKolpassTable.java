package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
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
    public List<RepositoryPasswordDto> getAllRepositoryPasswords() {
        return this.repositoryPasswords;
    }

    @Override
    public List<RepositoryPasswordDto> getAllRepositoryPasswordsChief() {
        return this.repositoryPasswords;
    }

    @Override
    public RepositoryPasswordDto addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(repositoryPasswordDto);
        return repositoryPasswordDto;
    }

    @Override
    public RepositoryPasswordDto addHistoryPasswordToRepository(Long idRep,
                                                                RepositoryPasswordHistoryDto repositoryPasswordHistory) {
        log.info("Добавлен пароль");
        RepositoryPasswordDto repositoryPasswordBase = new RepositoryPasswordDto();
        repositoryPasswordBase.setId(idRep);
        repositoryPasswordBase.setLastPassword(repositoryPasswordHistory);
        return repositoryPasswordBase;
    }

    @Override
    public RepositoryPasswordDto updateRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Обновлен репозиторий паролей");
        return repositoryPasswordDto;
    }

    @Override
    public void deleteRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Удален репозиторий паролей");
    }

    @Override
    public RepositoryPasswordDto addRepToOtherEmployee(RepositoryPasswordDto rep) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(rep);
        return rep;
    }
}
