package ru.kolaer.server.kolpass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.account.dao.AccountDao;
import ru.kolaer.server.account.service.AccountConverter;
import ru.kolaer.server.kolpass.model.entity.PasswordRepositoryEntity;
import ru.kolaer.server.webportal.service.AuthenticationService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 11.10.2017.
 */
@Component
@RequiredArgsConstructor
public class PasswordRepositoryConverterImpl implements PasswordRepositoryConverter {
    private final AuthenticationService authenticationService;
    private final AccountConverter accountConverter;
    private final AccountDao accountDao;

    @Override
    public PasswordRepositoryEntity convertToModel(PasswordRepositoryDto dto) {
        PasswordRepositoryEntity passwordRepositoryEntity = new PasswordRepositoryEntity();
        passwordRepositoryEntity.setId(dto.getId());
        passwordRepositoryEntity.setName(dto.getName());
        passwordRepositoryEntity.setUrlImage(dto.getUrlImage());

        Optional.ofNullable(dto.getAccount())
                .map(AccountDto::getId)
                .ifPresent(passwordRepositoryEntity::setAccountId);

        return passwordRepositoryEntity;
    }

    @Override
    public PasswordRepositoryDto convertToDto(PasswordRepositoryEntity model) {
        return updateData(new PasswordRepositoryDto(), model);
    }

    @Override
    public PasswordRepositoryDto updateData(PasswordRepositoryDto oldDto, PasswordRepositoryEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());
        oldDto.setUrlImage(newModel.getUrlImage());

        AccountSimpleDto currentAccount = authenticationService.getAccountSimpleByAuthentication();
        if(newModel.getAccountId() != null && !currentAccount.getId().equals(newModel.getAccountId())) {
            oldDto.setAccount(accountConverter.convertToDto(newModel.getAccount()));
        }

        return oldDto;
    }

    @Override
    public List<PasswordRepositoryDto> convertToDto(List<PasswordRepositoryEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> accountIds = model.stream()
                .map(PasswordRepositoryEntity::getAccountId)
                .collect(Collectors.toList());

        AccountSimpleDto currentAccount = authenticationService.getAccountSimpleByAuthentication();
        accountIds.remove(currentAccount.getId());

        Map<Long, AccountDto> accountMap = accountConverter.convertToDto(accountDao.findById(accountIds))
                .stream()
                .collect(Collectors.toMap(AccountDto::getId, Function.identity()));


        List<PasswordRepositoryDto> result = new ArrayList<>();

        for (PasswordRepositoryEntity passwordRepositoryEntity : model) {
            PasswordRepositoryDto passwordRepositoryDto = this.convertToDtoWithOutSubEntity(passwordRepositoryEntity);
            passwordRepositoryDto.setAccount(accountMap.getOrDefault(passwordRepositoryEntity.getAccountId(), null));

            result.add(passwordRepositoryDto);
        }


        return result;
    }

    @Override
    public PasswordRepositoryDto convertToDtoWithOutSubEntity(PasswordRepositoryEntity model) {
        PasswordRepositoryDto dto = new PasswordRepositoryDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setUrlImage(model.getUrlImage());

        if(model.getAccountId() != null) {
            AccountDto accountDto = new AccountDto();
            accountDto.setId(model.getAccountId());

            dto.setAccount(accountDto);
        }

        return dto;
    }
}
