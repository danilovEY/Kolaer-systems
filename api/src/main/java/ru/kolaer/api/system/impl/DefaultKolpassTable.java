package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultKolpassTable implements KolpassTable {

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswords() {
        return Collections.emptyList();
    }

    @Override
    public RepositoryPassword addRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        log.info("Добавлен репозиторий паролей");
        return repositoryPasswordDto;
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
