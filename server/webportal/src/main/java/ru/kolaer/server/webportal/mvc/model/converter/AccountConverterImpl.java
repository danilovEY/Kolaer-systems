package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import java.util.Optional;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
@RequiredArgsConstructor
public class AccountConverterImpl implements AccountConverter {

    private final EmployeeConverterImpl employeeConverter;

    @Override
    public AccountEntity convertToModel(AccountDto dto) {
        if(dto == null) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(dto.getId());
        accountEntity.setEmail(dto.getEmail());
        accountEntity.setPassword(dto.getPassword());
        accountEntity.setUsername(dto.getUsername());

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
        accountDto.setPassword(model.getPassword());
        accountDto.setUsername(model.getUsername());

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
        accountDto.setPassword(model.getPassword());
        accountDto.setUsername(model.getUsername());

        Optional.ofNullable(model.getEmployeeId())
                .map(EmployeeDto::new)
                .ifPresent(accountDto::setEmployee);

        return accountDto;
    }

}
