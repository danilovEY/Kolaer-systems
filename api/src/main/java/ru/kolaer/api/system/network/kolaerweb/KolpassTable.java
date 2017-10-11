package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;

import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface KolpassTable {
    ServerResponse<List<RepositoryPasswordDto>> getAllRepositoryPasswords();
    ServerResponse<List<RepositoryPasswordDto>> getAllRepositoryPasswordsChief();
    ServerResponse<RepositoryPasswordDto> addRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    ServerResponse<RepositoryPasswordDto> addHistoryPasswordToRepository(Long idRep,
                                                         RepositoryPasswordHistoryDto repositoryPasswordHistory);
    ServerResponse<RepositoryPasswordDto> updateRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);
    ServerResponse deleteRepositoryPassword(RepositoryPasswordDto repositoryPasswordDto);

    ServerResponse<RepositoryPasswordDto> addRepToOtherEmployee(RepositoryPasswordDto rep);
}
