package ru.kolaer.server.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.dao.HistoryChangeDao;
import ru.kolaer.server.core.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.core.model.entity.historychange.HistoryChangeEntity;
import ru.kolaer.server.core.model.entity.historychange.HistoryChangeEvent;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.core.service.HistoryChangeService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class HistoryChangeServiceImpl implements HistoryChangeService {
    private final HistoryChangeDao historyChangeDao;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    public HistoryChangeServiceImpl(HistoryChangeDao historyChangeDao,
                                    AuthenticationService authenticationService) {
        this.historyChangeDao = historyChangeDao;
        this.authenticationService = authenticationService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <T extends DefaultEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event) {
        return this.createHistoryChange(objectToJson(valueOld), objectToJson(valueNew), event);
    }

    @Override
    public HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event) {
        HistoryChangeEntity historyChangeEntity = new HistoryChangeEntity();
        historyChangeEntity.setValueOld(valueOld);
        historyChangeEntity.setValueNew(valueNew);
        historyChangeEntity.setEventDate(LocalDateTime.now());
        historyChangeEntity.setEvent(Optional.ofNullable(event).orElse(HistoryChangeEvent.UNKNOWN));

        AccountAuthorizedDto accountSimpleByAuthentication = null;

        if(authenticationService.isAuth()) {
            accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

            Optional.ofNullable(accountSimpleByAuthentication)
                    .map(AccountAuthorizedDto::getId)
                    .ifPresent(historyChangeEntity::setAccountId);
        }

        HistoryChangeDto historyChangeDto = convertToDto(historyChangeDao.persist(historyChangeEntity));
//        historyChangeDto.setAccount(accountSimpleByAuthentication); TODO: refactoring
        return historyChangeDto;
    }

    @Override
    public HistoryChangeDto createHistoryChange(HistoryChangeEvent event) {
        HistoryChangeEntity historyChangeEntity = new HistoryChangeEntity();
        historyChangeEntity.setEventDate(LocalDateTime.now());
        historyChangeEntity.setEvent(Optional.ofNullable(event).orElse(HistoryChangeEvent.UNKNOWN));

        AccountAuthorizedDto accountSimpleByAuthentication = null;

        if(authenticationService.isAuth()) {
            accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

            Optional.ofNullable(accountSimpleByAuthentication)
                    .map(AccountAuthorizedDto::getId)
                    .ifPresent(historyChangeEntity::setAccountId);
        }

        HistoryChangeDto historyChangeDto = convertToDto(historyChangeDao.persist(historyChangeEntity));
//        historyChangeDto.setAccount(accountSimpleByAuthentication); TODO: refactoring
        return historyChangeDto;
    }

    @Override
    public String objectToJson(Object obj) {
        if (obj != null) {
            try {
                return this.objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                return obj.toString();
            }
        }

        return null;
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
