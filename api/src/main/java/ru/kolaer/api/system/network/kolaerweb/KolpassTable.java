package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;

import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface KolpassTable {
    List<RepositoryPassword> getAllRepositoryPasswords();
    RepositoryPassword addRepositoryPassword(RepositoryPassword repositoryPasswordDto);
    RepositoryPassword updateRepositoryPassword(RepositoryPassword repositoryPasswordDto);
    void deleteRepositoryPassword(RepositoryPassword repositoryPasswordDto);
}
