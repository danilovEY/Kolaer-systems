package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dto.BankAccountDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BankAccountConverterImpl implements BankAccountConverter {
    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;

    public BankAccountConverterImpl(EmployeeDao employeeDao, EmployeeConverter employeeConverter) {
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
    }

    @Override
    public BankAccountEntity convertToModel(BankAccountDto dto) {
        if(dto == null) {
            return null;
        }

        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(dto.getId());
        bankAccountEntity.setCheck(dto.getCheck());
        Optional.ofNullable(dto.getEmployee())
                .map(EmployeeDto::getId)
                .ifPresent(bankAccountEntity::setEmployeeId);

        return bankAccountEntity;
    }

    @Override
    public BankAccountDto convertToDto(BankAccountEntity model) {
        return updateData(new BankAccountDto(), model);
    }

    @Override
    public BankAccountDto updateData(BankAccountDto oldDto, BankAccountEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setCheck(newModel.getCheck());

        Optional.ofNullable(newModel.getEmployeeId())
                .map(employeeDao::findById)
                .map(employeeConverter::convertToDto)
                .ifPresent(oldDto::setEmployee);

        return oldDto;
    }

    @Override
    public List<BankAccountDto> convertToDto(List<BankAccountEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> employeeIds = model.stream()
                .map(BankAccountEntity::getEmployeeId)
                .collect(Collectors.toList());

        Map<Long, EmployeeDto> employeeEntityMap = employeeConverter.convertToDto(employeeDao.findById(employeeIds))
                .stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity()));

        List<BankAccountDto> accounts = new ArrayList<>();

        for (BankAccountEntity bankAccountEntity : model) {
            BankAccountDto dto = convertToDtoWithOutSubEntity(bankAccountEntity);

            Optional.ofNullable(employeeEntityMap.get(bankAccountEntity.getEmployeeId()))
                    .ifPresent(dto::setEmployee);

            accounts.add(dto);
        }

        return accounts;
    }

    @Override
    public BankAccountDto convertToDtoWithOutSubEntity(BankAccountEntity model) {
        BankAccountDto dto = new BankAccountDto();
        dto.setId(model.getId());
        dto.setCheck(model.getCheck());

        return dto;
    }
}
