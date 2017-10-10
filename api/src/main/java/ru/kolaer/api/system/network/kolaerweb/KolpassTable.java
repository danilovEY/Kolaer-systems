package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;

import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface KolpassTable {
    List<RepositoryPasswordDto> getAllRepositoryPasswords();
    List<RepositoryPasswordDto> getAllRepositoryPasswordsChief();
    RepositoryPasswordDto addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    RepositoryPasswordDto addHistoryPasswordToRepository(Long idRep,
                                                         RepositoryPasswordHistoryDto repositoryPasswordHistory);
    RepositoryPasswordDto updateRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    void deleteRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);

    RepositoryPasswordDto addRepToOtherEmployee(RepositoryPasswordDto rep);
}
