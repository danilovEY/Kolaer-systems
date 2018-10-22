package ru.kolaer.server.webportal.common.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.common.exception.ForbiddenException;
import ru.kolaer.server.webportal.common.exception.NotFoundDataException;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.common.servirces.AuthenticationService;
import ru.kolaer.server.webportal.common.servirces.QueueService;
import ru.kolaer.server.webportal.microservice.account.converter.AccountConverter;
import ru.kolaer.server.webportal.microservice.queue.QueueConverter;
import ru.kolaer.server.webportal.microservice.account.repository.AccountRepository;
import ru.kolaer.server.webportal.microservice.queue.QueueRepository;
import ru.kolaer.server.webportal.microservice.queue.PageQueueRequest;
import ru.kolaer.server.webportal.microservice.queue.QueueRequestDto;
import ru.kolaer.server.webportal.microservice.queue.QueueScheduleDto;
import ru.kolaer.server.webportal.microservice.queue.QueueTargetDto;
import ru.kolaer.server.webportal.microservice.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.microservice.queue.QueueTargetEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QueueServiceImpl
        extends AbstractDefaultService<QueueTargetDto, QueueTargetEntity, QueueRepository, QueueConverter>
        implements QueueService {
    private final AuthenticationService authenticationService;
    private final AccountRepository accountDao;
    private final AccountConverter accountConverter;

    protected QueueServiceImpl(QueueRepository defaultEntityDao,
                               QueueConverter converter,
                               AuthenticationService authenticationService,
                               AccountRepository accountDao,
                               AccountConverter accountConverter) {
        super(defaultEntityDao, converter);
        this.authenticationService = authenticationService;
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
    }

    @Transactional
    @Override
    public QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto) {
        QueueTargetEntity queueTargetEntity = defaultEntityDao.findById(targetId);

        if(!queueTargetEntity.isActive()) {
            throw new ForbiddenException("Очередь не доступна");
        }

        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        QueueRequestEntity queueRequestEntity = defaultConverter.convertToModel(queueRequestDto);
        queueRequestEntity.setId(null);
        queueRequestEntity.setAccountId(accountSimpleByAuthentication.getId());
        queueRequestEntity.setQueueTargetId(targetId);

        return defaultConverter.convertToDto(defaultEntityDao.addRequest(queueRequestEntity));
    }

    @Transactional
    @Override
    public QueueRequestDto updateQueueRequest(Long targetId, Long requestId, QueueRequestDto queueRequestDto) {
        QueueRequestEntity queueRequestEntity = this.defaultEntityDao.findRequestByTargetIdAndId(targetId, requestId);

        if (queueRequestEntity == null) {
            throw new NotFoundDataException("Очередь не найдена");
        }

        queueRequestEntity.setQueueTo(queueRequestDto.getQueueTo());
        queueRequestEntity.setQueueFrom(queueRequestDto.getQueueFrom());
        queueRequestEntity.setComment(queueRequestDto.getComment());

        return defaultConverter.convertToDto(defaultEntityDao.updateRequest(queueRequestEntity));
    }

    @Transactional
    @Override
    public void deleteQueueRequest(Long targetId, Long requestId) {
        defaultEntityDao.deleteRequestByIdAndTarget(targetId, requestId);
    }

    @Transactional
    @Override
    public Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize) {
        LocalDateTime now = LocalDateTime.now().minusHours(1);

        Long size = defaultEntityDao.findCountRequestByTargetId(targetId, now);
        List<QueueRequestEntity> requests = defaultEntityDao.findRequestById(targetId, now, number, pageSize);

        return new Page<>(defaultConverter.convertToRequestDto(requests), number, size, pageSize);
    }

    @Override
    @Transactional
    public QueueTargetDto update(Long targetId, QueueTargetDto queueTargetDto) {
        if (queueTargetDto == null || targetId == null || StringUtils.isEmpty(queueTargetDto.getName())) {
            throw new UnexpectedRequestParams("ID и Имя не должно быть пустым");
        }

        QueueTargetEntity entity = this.defaultEntityDao.findById(targetId);

        if(entity == null) {
            throw new NotFoundDataException("Цель не найдена");
        }

        entity.setName(queueTargetDto.getName());

        return this.defaultConverter.convertToDto(this.defaultEntityDao.update(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QueueScheduleDto> getSchedulers(PageQueueRequest pageQueueRequest) {
        if(pageQueueRequest.getAfterFrom() == null) {
            pageQueueRequest.setAfterFrom(LocalDateTime.now().minusHours(1));
        }

        Long count = this.defaultEntityDao.findCountLastRequests(pageQueueRequest);
        List<QueueRequestEntity> requests = this.defaultEntityDao.findLastRequests(pageQueueRequest);

        if (requests.isEmpty()) {
            return new Page<>(Collections.emptyList(), pageQueueRequest.getNumber(), 0, pageQueueRequest.getPageSize());
        }

        Map<Long, QueueRequestDto> requestMap = defaultConverter.convertToRequestDto(requests)
                .stream()
                .collect(Collectors.toMap(QueueRequestDto::getId, Function.identity()));

        List<Long> targetIds = requests.stream()
                .map(QueueRequestEntity::getQueueTargetId)
                .collect(Collectors.toList());

        Map<Long, QueueTargetDto> targetMap = defaultConverter.convertToDto(defaultEntityDao.findById(targetIds))
                .stream()
                .collect(Collectors.toMap(QueueTargetDto::getId, Function.identity()));


        List<QueueScheduleDto> schedulers = new ArrayList<>();

        for (QueueRequestEntity request : requests) {
            QueueScheduleDto queueScheduleDto = new QueueScheduleDto();
            queueScheduleDto.setRequest(requestMap.get(request.getId()));
            queueScheduleDto.setTarget(targetMap.get(request.getQueueTargetId()));

            schedulers.add(queueScheduleDto);
        }

        return new Page<>(schedulers, pageQueueRequest.getNumber(), count, pageQueueRequest.getPageSize());
    }

    @Override
    @Transactional
    public QueueTargetDto add(QueueTargetDto dto) {
        if (dto == null || StringUtils.isEmpty(dto.getName())) {
            throw new UnexpectedRequestParams("Имя не должно быть пустым");
        }

        QueueTargetEntity entity = new QueueTargetEntity();
        entity.setActive(true);
        entity.setName(dto.getName());

        return this.defaultConverter.convertToDto(this.defaultEntityDao.persist(entity));
    }

    @Override
    @Transactional
    public long delete(Long id) {
        this.defaultEntityDao.deleteRequestsByTargetId(id);
        return this.defaultEntityDao.delete(id);
    }


}
