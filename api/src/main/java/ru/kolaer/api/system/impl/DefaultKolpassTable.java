package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultKolpassTable implements KolpassTable {

    @Override
    public RepositoryPasswordDto[] getAllRepositoryPasswords() {
        return new RepositoryPasswordDto[0];
    }

    @Override
    public RepositoryPasswordDto addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto) {
        log.info("Добавлен репозиторий паролей");
        return repositoryPasswordDto;
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
}
