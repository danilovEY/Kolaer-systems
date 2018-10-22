package ru.kolaer.server.webportal.microservice.account.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.microservice.employee.EmployeeConverter;
import ru.kolaer.server.webportal.microservice.employee.EmployeeRepository;
import ru.kolaer.server.webportal.microservice.contact.ContactEntity;
import ru.kolaer.server.webportal.microservice.account.AccountEntity;
import ru.kolaer.server.webportal.microservice.employee.EmployeeEntity;
import ru.kolaer.server.webportal.common.servirces.impl.UtilService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
@RequiredArgsConstructor
public class AccountConverterImpl implements AccountConverter {

    private final EmployeeConverter employeeConverter;
    private final EmployeeRepository employeeDao;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;

    @Override
    public AccountEntity convertToModel(AccountDto dto) {
        if(dto == null) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(dto.getId());
        accountEntity.setEmail(dto.getEmail());
        accountEntity.setUsername(dto.getUsername());
        accountEntity.setChatName(dto.getChatName());
        accountEntity.setAvatarUrl(Optional.ofNullable(dto.getAvatarUrl()).map(photo -> photo.substring(utilService.getCurrentHostUrl().length())).orElse(null));
        accountEntity.setAccessUser(dto.isAccessUser());
        accountEntity.setAccessOit(dto.isAccessOit());
        accountEntity.setAccessOk(dto.isAccessOk());
        accountEntity.setAccessVacationAdmin(dto.isAccessVacationAdmin());
        accountEntity.setAccessVacationDepEdit(dto.isAccessVacationDepEdit());
        accountEntity.setAccessVacationDepEdit(dto.isAccessVacationDepEdit());
        accountEntity.setAccessTypeWork(dto.isAccessTypeWork());

        accountEntity.setEmployeeId(Optional.ofNullable(dto.getEmployee())
                .map(EmployeeDto::getId)
                .orElse(null));

        return accountEntity;
    }

    @Override
    public AccountDto convertToDto(AccountEntity model) {
        if(model == null) {
            return null;
        }

        return updateData(new AccountDto(), model);
    }

    @Override
    public AccountDto updateData(AccountDto accountDto, AccountEntity model) {
        accountDto.setId(model.getId());
        accountDto.setEmail(model.getEmail());
        accountDto.setUsername(model.getUsername());
        accountDto.setChatName(model.getChatName());
        accountDto.setAvatarUrl(Optional.ofNullable(model.getAvatarUrl()).map(photo -> utilService.getCurrentHostUrl() + photo).orElse(null));
        accountDto.setAccessUser(model.isAccessUser());
        accountDto.setAccessOit(model.isAccessOit());
        accountDto.setAccessOk(model.isAccessOk());
        accountDto.setAccessVacationAdmin(model.isAccessVacationAdmin());
        accountDto.setAccessVacationDepEdit(model.isAccessVacationDepEdit());
        accountDto.setAccessTypeWork(model.isAccessTypeWork());

        if(model.getEmployeeId() != null) {
            accountDto.setEmployee(employeeConverter.convertToDto(model.getEmployee()));
        } else {
            accountDto.setEmployee(null);
        }

        return accountDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> convertToDto(List<AccountEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> employeeIds = model.stream()
                .map(AccountEntity::getEmployeeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (employeeIds.isEmpty()) {
            return model.stream()
                    .map(this::convertToDto).
                    collect(Collectors.toList());
        }

        Map<Long, EmployeeDto> employeeMap = employeeConverter.convertToDto(employeeDao.findById(employeeIds))
                .stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity()));

        ArrayList<AccountDto> results = new ArrayList<>();

        for (AccountEntity accountEntity : model) {
            AccountDto accountDto = this.convertToDtoWithOutSubEntity(accountEntity);
            accountDto.setEmployee(employeeMap.getOrDefault(accountEntity.getEmployeeId(), accountDto.getEmployee()));

            results.add(accountDto);
        }

        return results;
    }

    @Override
    public AccountDto convertToDtoWithOutSubEntity(AccountEntity model) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(model.getId());
        accountDto.setEmail(model.getEmail());
        accountDto.setUsername(model.getUsername());
        accountDto.setChatName(model.getChatName());
        accountDto.setAccessUser(model.isAccessUser());
        accountDto.setAvatarUrl(Optional.ofNullable(model.getAvatarUrl()).map(photo -> utilService.getCurrentHostUrl() + photo).orElse(null));
        accountDto.setAccessOit(model.isAccessOit());
        accountDto.setAccessOk(model.isAccessOk());
        accountDto.setAccessVacationAdmin(model.isAccessVacationAdmin());
        accountDto.setAccessVacationDepEdit(model.isAccessVacationDepEdit());
        accountDto.setAccessTypeWork(model.isAccessTypeWork());

        Optional.ofNullable(model.getEmployeeId())
                .map(EmployeeDto::new)
                .ifPresent(accountDto::setEmployee);

        return accountDto;
    }

    @Override
    public AccountEntity convertToModel(EmployeeDto employeeDto) {
        return convertToModel(employeeConverter.convertToModel(employeeDto));
    }

    @Override
    public AccountEntity convertToModel(EmployeeEntity employeeEntity) {
        if(employeeEntity == null) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmployeeId(employeeEntity.getId());
        accountEntity.setAvatarUrl(employeeEntity.getPhoto());
        accountEntity.setUsername(employeeEntity.getPersonnelNumber().toString());
        accountEntity.setChatName(employeeEntity.getInitials());

        Optional.ofNullable(employeeEntity.getContact())
                .map(ContactEntity::getEmail)
                .ifPresent(accountEntity::setEmail);

        Optional.ofNullable(accountEntity.getUsername())
                .map(passwordEncoder::encode)
                .ifPresent(accountEntity::setPassword);

        accountEntity.setAccessUser(true);

        return accountEntity;
    }

    @Override
    public AccountSimpleDto convertToSimpleDto(AccountEntity accountEntity) {
        AccountSimpleDto accountSimpleDto = new AccountSimpleDto();
        accountSimpleDto.setId(accountEntity.getId());
        accountSimpleDto.setEmployeeId(accountEntity.getEmployeeId());
        accountSimpleDto.setEmail(accountEntity.getEmail());
        accountSimpleDto.setUsername(accountEntity.getUsername());
        accountSimpleDto.setChatName(accountEntity.getChatName());
        accountSimpleDto.setAvatarUrl(Optional.ofNullable(accountEntity.getAvatarUrl()).map(photo -> utilService.getCurrentHostUrl() + photo).orElse(null));
        accountSimpleDto.setAccessOit(accountEntity.isAccessOit());
        accountSimpleDto.setAccessUser(accountEntity.isAccessUser());
        accountSimpleDto.setAccessOk(accountEntity.isAccessOk());
        accountSimpleDto.setAccessVacationAdmin(accountEntity.isAccessVacationAdmin());
        accountSimpleDto.setAccessVacationDepEdit(accountEntity.isAccessVacationDepEdit());
        accountSimpleDto.setAccessTypeWork(accountEntity.isAccessTypeWork());

        return accountSimpleDto;
    }

    @Override
    public AccountSimpleDto convertToSimpleDto(AccountDto dto) {
        AccountSimpleDto accountSimpleDto = new AccountSimpleDto();
        accountSimpleDto.setId(dto.getId());
        accountSimpleDto.setEmail(dto.getEmail());
        accountSimpleDto.setUsername(dto.getUsername());
        accountSimpleDto.setChatName(dto.getChatName());
        accountSimpleDto.setAvatarUrl(dto.getAvatarUrl());
        accountSimpleDto.setAccessOit(dto.isAccessOit());
        accountSimpleDto.setAccessUser(dto.isAccessUser());
        accountSimpleDto.setAccessOk(dto.isAccessOk());
        accountSimpleDto.setAccessVacationAdmin(dto.isAccessVacationAdmin());
        accountSimpleDto.setAccessVacationDepEdit(dto.isAccessVacationDepEdit());
        accountSimpleDto.setAccessTypeWork(dto.isAccessTypeWork());

        Optional.ofNullable(dto.getEmployee()).map(EmployeeDto::getId).ifPresent(accountSimpleDto::setEmployeeId);

        return accountSimpleDto;
    }
}
