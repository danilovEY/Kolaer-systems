package ru.kolaer.server.ticket.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.model.dto.upload.UploadFileDto;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.service.EmployeeConverter;
import ru.kolaer.server.ticket.dao.BankAccountDao;
import ru.kolaer.server.ticket.model.dto.TicketDto;
import ru.kolaer.server.ticket.model.dto.TicketRegisterDto;
import ru.kolaer.server.ticket.model.entity.BankAccountEntity;
import ru.kolaer.server.ticket.model.entity.TicketEntity;
import ru.kolaer.server.ticket.model.entity.TicketRegisterEntity;
import ru.kolaer.server.upload.service.UploadFileService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class TicketRegisterConverterImpl implements TicketRegisterConverter {
    private final UploadFileService uploadFileService;
    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;
    private final BankAccountDao bankAccountDao;

    public TicketRegisterConverterImpl(UploadFileService uploadFileService,
                                       EmployeeDao employeeDao,
                                       EmployeeConverter employeeConverter,
                                       BankAccountDao bankAccountDao) {
        this.uploadFileService = uploadFileService;
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
        this.bankAccountDao = bankAccountDao;
    }

    @Override
    public TicketRegisterEntity convertToModel(TicketRegisterDto dto) {
        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setId(dto.getId());
        ticketRegisterEntity.setClose(dto.isClose());
        ticketRegisterEntity.setCreateRegister(dto.getCreateRegister());
        ticketRegisterEntity.setSendRegisterTime(dto.getSendRegisterTime());
        ticketRegisterEntity.setAccountId(dto.getAccountId());

        Optional.ofNullable(dto.getAttachment())
                .map(UploadFileDto::getId)
                .ifPresent(ticketRegisterEntity::setAttachmentId);

        return ticketRegisterEntity;
    }

    @Override
    public TicketRegisterDto convertToDto(TicketRegisterEntity model) {
        return updateData(new TicketRegisterDto(), model);
    }

    @Override
    public TicketRegisterDto updateData(TicketRegisterDto oldDto, TicketRegisterEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setClose(newModel.isClose());
        oldDto.setCreateRegister(newModel.getCreateRegister());
        oldDto.setSendRegisterTime(newModel.getSendRegisterTime());
        oldDto.setAccountId(newModel.getAccountId());

        if(newModel.getAttachmentId() != null) {
            Optional.ofNullable(this.uploadFileService.getById(newModel.getAttachmentId()))
                    .ifPresent(oldDto::setAttachment);
        }


        return oldDto;
    }

    @Override
    public List<TicketRegisterDto> convertToDto(List<TicketRegisterEntity> model) {
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        List<TicketRegisterDto> results = model.stream()
                .map(this::convertToDtoWithOutSubEntity)
                .collect(Collectors.toList());

        List<Long> attachmentIds = model.stream()
                .map(TicketRegisterEntity::getAttachmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(!attachmentIds.isEmpty()) {
            Map<Long, UploadFileDto> uploads = this.uploadFileService.getById(attachmentIds)
                    .stream()
                    .collect(Collectors.toMap(UploadFileDto::getId, Function.identity()));

            for (TicketRegisterDto result : results) {
                UploadFileDto attachment = result.getAttachment();
                if(attachment != null) {
                    attachment.setFileName(uploads.get(attachment.getId()).getFileName());
                }
            }
        }

        return results;
    }

    @Override
    public TicketRegisterDto convertToDtoWithOutSubEntity(TicketRegisterEntity model) {
        TicketRegisterDto dto = new TicketRegisterDto();
        dto.setId(model.getId());
        dto.setClose(model.isClose());
        dto.setCreateRegister(model.getCreateRegister());
        dto.setSendRegisterTime(model.getSendRegisterTime());
        dto.setAccountId(model.getAccountId());

        Optional.ofNullable(model.getAttachmentId())
                .map(UploadFileDto::new)
                .ifPresent(dto::setAttachment);

        return dto;
    }

    @Override
    public List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities) {
        if(CollectionUtils.isEmpty(ticketEntities)) {
            return Collections.emptyList();
        }

        List<Long> backAccountIds = ticketEntities.stream()
                .map(TicketEntity::getBankAccountId)
                .collect(Collectors.toList());

        Map<Long, BankAccountEntity> bankAccountMap = bankAccountDao.findById(backAccountIds)
                .stream()
                .collect(Collectors.toMap(BankAccountEntity::getId, Function.identity()));


        List<Long> employeeIds = bankAccountMap.values()
                .stream()
                .map(BankAccountEntity::getEmployeeId)
                .collect(Collectors.toList());

        Map<Long, EmployeeDto> employeesMap = employeeConverter.convertToDto(employeeDao.findById(employeeIds))
                .stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity()));

        List<TicketDto> results =  new ArrayList<>();

        for (TicketEntity ticketEntity : ticketEntities) {
            TicketDto ticketDto = convertToDtoWithOutSubEntity(ticketEntity);
            Optional.ofNullable(bankAccountMap.get(ticketEntity.getBankAccountId()))
                    .map(BankAccountEntity::getEmployeeId)
                    .map(employeesMap::get)
                    .ifPresent(ticketDto::setEmployee);
            results.add(ticketDto);
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDto convertToTicketDto(@NonNull TicketEntity ticketEntity) {
        TicketDto ticketDto = convertToDtoWithOutSubEntity(ticketEntity);

        Optional.ofNullable(bankAccountDao.findById(ticketEntity.getBankAccountId()))
                .map(BankAccountEntity::getEmployeeId)
                .map(employeeDao::findById)
                .map(employeeConverter::convertToDto)
                .ifPresent(ticketDto::setEmployee);

        return ticketDto;
    }

    @Override
    public TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticketEntity.getId());
        ticketDto.setType(ticketEntity.getTypeOperation());
        ticketDto.setCount(ticketEntity.getCount());

        return ticketDto;
    }
}
