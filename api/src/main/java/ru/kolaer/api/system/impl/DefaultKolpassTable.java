package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordBase;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultKolpassTable implements KolpassTable {
    private final List<RepositoryPassword> repositoryPasswords;

    public DefaultKolpassTable() {
        this.repositoryPasswords = new ArrayList<>(5);
    }

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswords() {
        return this.repositoryPasswords;
    }

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswordsChief() {
        return this.repositoryPasswords;
    }

    @Override
    public RepositoryPassword addRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        log.info("Добавлен репозиторий паролей");
        this.repositoryPasswords.add(repositoryPasswordDto);
        return repositoryPasswordDto;
    }

    @Override
    public RepositoryPassword addHistoryPasswordToRepository(Integer idRep,
                                                             RepositoryPasswordHistory repositoryPasswordHistory) {
        log.info("Добавлен пароль");
        RepositoryPassword repositoryPasswordBase = new RepositoryPasswordBase();
        repositoryPasswordBase.setId(idRep);
        repositoryPasswordBase.setLastPassword(repositoryPasswordHistory);
        return repositoryPasswordBase;
    }

    @Override
    public RepositoryPassword updateRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        log.info("Обновлен репозиторий паролей");
        return repositoryPasswordDto;
    }

    @Override
    public void deleteRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        log.info("Удален репозиторий паролей");
    }
}
