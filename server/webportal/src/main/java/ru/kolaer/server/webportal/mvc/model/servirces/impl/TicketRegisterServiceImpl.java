package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.TicketRegisterConverter;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 30.11.2016.
 */
@Service
public class TicketRegisterServiceImpl extends AbstractDefaultService<TicketRegisterDto, TicketRegisterEntity> implements TicketRegisterService {
    private final TicketRegisterConverter defaultConverter;
    private final TicketRegisterDao ticketRegisterDao;
    private final TicketDao ticketDao;

    public TicketRegisterServiceImpl(TicketRegisterConverter defaultConverter, TicketRegisterDao ticketRegisterDao, TicketDao ticketDao) {
        super(ticketRegisterDao, defaultConverter);
        this.defaultConverter = defaultConverter;
        this.ticketRegisterDao = ticketRegisterDao;
        this.ticketDao = ticketDao;
    }

    @Override
    public TicketRegisterDto save(TicketRegisterDto entity) {
        List<TicketRegisterEntity> ticketRegisterByDateAndDep = ticketRegisterDao.
                getTicketRegisterByDateAndDep(entity.getCreateRegister(), entity.getDepartment().getName());

        List<TicketRegisterEntity> collect = ticketRegisterByDateAndDep.stream().filter(ticketRegister ->
                !ticketRegister.isClose()
        ).collect(Collectors.toList());
        if(collect.size() > 0) {
            throw new UnexpectedRequestParams("Открытый реестр уже существует в этом месяце и году!");
        } else {
            int day = entity.getCreateRegister()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .getDayOfMonth();
            ticketRegisterByDateAndDep.forEach(ticketRegister -> {
                    if(ticketRegister.getCreateRegister()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            .getDayOfMonth() == day)
                        throw new UnexpectedRequestParams("Реестр существует!");
            });
        }

        return defaultConverter.convertToDto(ticketRegisterDao.persist(defaultConverter.convertToModel(entity)));
    }

    @Override
    public List<TicketRegisterDto> getAllByDepName(String name) {
        return ticketRegisterDao.findAllByDepName(name)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TicketRegisterDto> getAllByDepName(int page, int pageSize, String name) {
        if(pageSize == 0) {
            List<TicketRegisterDto> all = this.getAll();
            return new Page<>(all, 0, 0, all.size());
        } else {
            Long count = ticketRegisterDao.findCountAllByDepName(page, pageSize, name);
            List<TicketRegisterDto> result = ticketRegisterDao.findAllByDepName(page, pageSize, name)
                    .stream()
                    .map(defaultConverter::convertToDto)
                    .collect(Collectors.toList());

            return new Page<>(result, page, count, pageSize);
        }
    }

    @Override
    public List<TicketRegisterDto> getAllOpenRegister() {
        return ticketRegisterDao.findAllOpenRegister()
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
