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

        return ticketRegisterEntity;
    }

    @Override
    public TicketRegisterDto convertToDto(TicketRegisterEntity model) {
        TicketRegisterDto ticketRegisterDto = new TicketRegisterDto();
        ticketRegisterDto.setId(model.getId());
        ticketRegisterDto.setClose(model.isClose());
        ticketRegisterDto.setCreateRegister(model.getCreateRegister());
        ticketRegisterDto.setSendRegisterTime(model.getSendRegisterTime());

        return ticketRegisterDto;
    }

    @Override
    public TicketRegisterDto updateData(TicketRegisterDto oldDto, TicketRegisterEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setClose(newModel.isClose());
        oldDto.setCreateRegister(newModel.getCreateRegister());
        oldDto.setSendRegisterTime(newModel.getSendRegisterTime());

        return oldDto;
    }
}
