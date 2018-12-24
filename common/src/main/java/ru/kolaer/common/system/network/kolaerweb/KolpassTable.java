package ru.kolaer.common.system.network.kolaerweb;

import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;

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
