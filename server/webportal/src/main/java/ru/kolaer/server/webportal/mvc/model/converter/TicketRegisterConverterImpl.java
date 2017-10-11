package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;

import java.util.Optional;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class TicketRegisterConverterImpl implements TicketRegisterConverter {
    private final DepartmentConverter departmentConverter;

    public TicketRegisterConverterImpl(DepartmentConverter departmentConverter) {
        this.departmentConverter = departmentConverter;
    }

    @Override
    public TicketRegisterEntity convertToModel(TicketRegisterDto dto) {
        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setId(dto.getId());
        ticketRegisterEntity.setClose(dto.isClose());
        ticketRegisterEntity.setCreateRegister(dto.getCreateRegister());

        Optional.ofNullable(dto.getDepartment())
                .map(DepartmentDto::getId)
                .ifPresent(ticketRegisterEntity::setDepartmentId);

        return ticketRegisterEntity;
    }

    @Override
    public TicketRegisterDto convertToDto(TicketRegisterEntity model) {
        TicketRegisterDto ticketRegisterDto = new TicketRegisterDto();
        ticketRegisterDto.setId(model.getId());
        ticketRegisterDto.setClose(model.isClose());
        ticketRegisterDto.setCreateRegister(model.getCreateRegister());

        if(model.getDepartmentId() != null && model.getDepartmentId() > 0) {
            ticketRegisterDto.setDepartment(departmentConverter.convertToDto(model.getDepartment()));
        }

        return ticketRegisterDto;
    }

    @Override
    public TicketRegisterDto updateData(TicketRegisterDto oldDto, TicketRegisterEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setClose(newModel.isClose());
        oldDto.setCreateRegister(newModel.getCreateRegister());

        if(newModel.getDepartmentId() != null && newModel.getDepartmentId() > 0) {
            oldDto.setDepartment(departmentConverter.convertToDto(newModel.getDepartment()));
        }

        return oldDto;
    }
}
