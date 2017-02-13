package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface KolpassTable {
    RepositoryPasswordDto[] getAllRepositoryPasswords();
    RepositoryPasswordDto addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    RepositoryPasswordDto updateRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    void deleteRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
}
