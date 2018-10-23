package ru.kolaer.server.service.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.account.service.AuthenticationService;
import ru.kolaer.server.service.event.HistoryChangeEvent;
import ru.kolaer.server.service.event.dto.HistoryChangeDto;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.service.event.entity.HistoryChangeEntity;
import ru.kolaer.server.service.event.repository.HistoryChangeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class HistoryChangeServiceImpl implements HistoryChangeService {
    private final HistoryChangeRepository historyChangeDao;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    public HistoryChangeServiceImpl(HistoryChangeRepository historyChangeDao,
                                    AuthenticationService authenticationService) {
        this.historyChangeDao = historyChangeDao;
        this.authenticationService = authenticationService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <T extends BaseEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event) {
        return this.createHistoryChange(objectToJson(valueOld), objectToJson(valueNew), event);
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
