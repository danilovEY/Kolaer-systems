package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.webportal.mvc.model.dao.HistoryChangeDao;
import ru.kolaer.server.webportal.mvc.model.dto.HistoryChangeDto;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEvent;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.HistoryChangeService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HistoryChangeServiceImpl implements HistoryChangeService {
    private final HistoryChangeDao historyChangeDao;
    private final AuthenticationService authenticationService;

    public HistoryChangeServiceImpl(HistoryChangeDao historyChangeDao,
                                    AuthenticationService authenticationService) {
        this.historyChangeDao = historyChangeDao;
        this.authenticationService = authenticationService;
    }

    @Override
    public HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event) {
        HistoryChangeEntity historyChangeEntity = new HistoryChangeEntity();
        historyChangeEntity.setValueOld(valueOld);
        historyChangeEntity.setValueNew(valueNew);
        historyChangeEntity.setEventDate(LocalDateTime.now());
        historyChangeEntity.setEvent(Optional.ofNullable(event).orElse(HistoryChangeEvent.UNKNOWN));

        AccountSimpleDto accountSimpleByAuthentication = null;

        if(authenticationService.isAuth()) {
            accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

            Optional.ofNullable(accountSimpleByAuthentication)
                    .map(AccountSimpleDto::getId)
                    .ifPresent(historyChangeEntity::setAccountId);
        }

        HistoryChangeDto historyChangeDto = convertToDto(historyChangeDao.persist(historyChangeEntity));
        historyChangeDto.setAccount(accountSimpleByAuthentication);
        return historyChangeDto;
    }

    @Override
    public HistoryChangeDto createHistoryChange(HistoryChangeEvent event) {
        HistoryChangeEntity historyChangeEntity = new HistoryChangeEntity();
        historyChangeEntity.setEventDate(LocalDateTime.now());
        historyChangeEntity.setEvent(Optional.ofNullable(event).orElse(HistoryChangeEvent.UNKNOWN));

        AccountSimpleDto accountSimpleByAuthentication = null;

        if(authenticationService.isAuth()) {
            accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

            Optional.ofNullable(accountSimpleByAuthentication)
                    .map(AccountSimpleDto::getId)
                    .ifPresent(historyChangeEntity::setAccountId);
        }

        HistoryChangeDto historyChangeDto = convertToDto(historyChangeDao.persist(historyChangeEntity));
        historyChangeDto.setAccount(accountSimpleByAuthentication);
        return historyChangeDto;
    }

    private HistoryChangeDto convertToDto(HistoryChangeEntity historyChangeEntity) {
        if(historyChangeEntity == null) {
            return null;
        }

        HistoryChangeDto historyChangeDto = new HistoryChangeDto();
        historyChangeDto.setValueOld(historyChangeEntity.getValueOld());
        historyChangeDto.setValueNew(historyChangeEntity.getValueNew());
        historyChangeDto.setEvent(historyChangeEntity.getEvent());
        historyChangeDto.setEventDate(historyChangeEntity.getEventDate());

        return historyChangeDto;
    }

}
