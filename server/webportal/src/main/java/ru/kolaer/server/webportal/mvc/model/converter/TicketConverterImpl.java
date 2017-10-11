package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.mvc.model.dto.TicketDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;

import java.util.Optional;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class TicketConverterImpl implements TicketConverter {
    private final EmployeeConverter employeeConverter;

    public TicketConverterImpl(EmployeeConverter employeeConverter) {
        this.employeeConverter = employeeConverter;
    }

    @Override
    public TicketEntity convertToModel(TicketDto dto) {
        if(dto == null) {
            return null;
        }

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(dto.getId());
        ticketEntity.setCount(dto.getCount());
        ticketEntity.setRegisterId(dto.getRegisterId());

        Optional.ofNullable(dto.getEmployee())
                .map(EmployeeDto::getId)
                .ifPresent(ticketEntity::setEmployeeId);

        return ticketEntity;
    }

    @Override
    public TicketDto convertToDto(TicketEntity model) {
        return updateData(new TicketDto(), model);
    }

    @Override
    public TicketDto updateData(TicketDto oldDto, TicketEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setCount(newModel.getCount());
        oldDto.setRegisterId(newModel.getRegisterId());

        if(newModel.getEmployeeId() != null && newModel.getEmployeeId() > 0) {
            oldDto.setEmployee(employeeConverter.convertToDto(newModel.getEmployee()));
        }
        return oldDto;
    }
}
