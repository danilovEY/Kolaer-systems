package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.dto.UploadFileDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class TicketRegisterConverterImpl implements TicketRegisterConverter {
    private final UploadFileService uploadFileService;

    public TicketRegisterConverterImpl(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @Override
    public TicketRegisterEntity convertToModel(TicketRegisterDto dto) {
        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setId(dto.getId());
        ticketRegisterEntity.setClose(dto.isClose());
        ticketRegisterEntity.setCreateRegister(dto.getCreateRegister());
        ticketRegisterEntity.setSendRegisterTime(dto.getSendRegisterTime());

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

        Optional.ofNullable(model.getAttachmentId())
                .map(UploadFileDto::new)
                .ifPresent(dto::setAttachment);

        return dto;
    }
}
