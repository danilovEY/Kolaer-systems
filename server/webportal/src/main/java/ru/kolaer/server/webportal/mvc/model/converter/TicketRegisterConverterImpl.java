package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class TicketRegisterConverterImpl implements TicketRegisterConverter {

    @Override
    public TicketRegisterEntity convertToModel(TicketRegisterDto dto) {
        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setId(dto.getId());
        ticketRegisterEntity.setClose(dto.isClose());
        ticketRegisterEntity.setCreateRegister(dto.getCreateRegister());
        ticketRegisterEntity.setSendRegisterTime(dto.getSendRegisterTime());
        ticketRegisterEntity.setAttachmentId(dto.getAttachmentId());

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
        oldDto.setAttachmentId(newModel.getAttachmentId());

        return oldDto;
    }
}
