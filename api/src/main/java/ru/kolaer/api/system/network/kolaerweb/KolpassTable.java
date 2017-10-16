package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;

import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface KolpassTable {
    ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswords();
    ServerResponse<List<PasswordRepositoryDto>> getAllRepositoryPasswordsChief();
    ServerResponse<PasswordRepositoryDto> addRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto);
    ServerResponse<PasswordRepositoryDto> addHistoryPasswordToRepository(Long idRep, PasswordHistoryDto passwordHistoryDto);
    ServerResponse<PasswordRepositoryDto> addRepToOtherEmployee(PasswordRepositoryDto rep);
    ServerResponse<PasswordRepositoryDto> updateRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto);
    ServerResponse deleteRepositoryPassword(PasswordRepositoryDto passwordRepositoryDto);
}
