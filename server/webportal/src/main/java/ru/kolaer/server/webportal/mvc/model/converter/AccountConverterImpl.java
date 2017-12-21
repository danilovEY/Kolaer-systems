package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.Optional;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
@RequiredArgsConstructor
public class AccountConverterImpl implements AccountConverter {

    private final EmployeeConverterImpl employeeConverter;
    private final PasswordEncoder passwordEncoder;

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
        accountEntity.setAccessUser(dto.isAccessUser());
        accountEntity.setAccessOit(dto.isAccessOit());

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
        accountDto.setAccessUser(model.isAccessUser());
        accountDto.setAccessOit(model.isAccessOit());

        if(model.getEmployeeId() != null) {
            accountDto.setEmployee(employeeConverter.convertToDto(model.getEmployeeEntity()));
        } else {
            accountDto.setEmployee(null);
        }

        return accountDto;
    }

    @Override
    public AccountDto convertToDtoWithOutSubEntity(AccountEntity model) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(model.getId());
        accountDto.setEmail(model.getEmail());
        accountDto.setUsername(model.getUsername());
        accountDto.setChatName(model.getChatName());
        accountDto.setAccessUser(model.isAccessUser());
        accountDto.setAccessOit(model.isAccessOit());

        Optional.ofNullable(model.getEmployeeId())
                .map(EmployeeDto::new)
                .ifPresent(accountDto::setEmployee);

        return accountDto;
    }

    @Override
    public AccountEntity convertToModel(EmployeeEntity employeeEntity) {
        if(employeeEntity == null) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmployeeId(employeeEntity.getId());
        accountEntity.setEmail(employeeEntity.getEmail());
        accountEntity.setUsername(employeeEntity.getPersonnelNumber().toString());
        accountEntity.setChatName(employeeEntity.getInitials());

        Optional.ofNullable(accountEntity.getUsername())
                .map(passwordEncoder::encode)
                .ifPresent(accountEntity::setPassword);

        accountEntity.setAccessUser(true);

        return accountEntity;
    }
}
