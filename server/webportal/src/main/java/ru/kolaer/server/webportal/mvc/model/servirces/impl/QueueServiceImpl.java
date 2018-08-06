package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.QueueConverter;
import ru.kolaer.server.webportal.mvc.model.dao.QueueDao;
import ru.kolaer.server.webportal.mvc.model.dto.QueueRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.QueueTargetDto;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.QueueService;

import java.util.List;

@Service
public class QueueServiceImpl
        extends AbstractDefaultService<QueueTargetDto, QueueTargetEntity, QueueDao, QueueConverter>
        implements QueueService {
    private AuthenticationService authenticationService;

    protected QueueServiceImpl(QueueDao defaultEntityDao, QueueConverter converter,
                               AuthenticationService authenticationService) {
        super(defaultEntityDao, converter);
        this.authenticationService = authenticationService;
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
        Long size = defaultEntityDao.findCountRequestByTargetId(targetId);
        List<QueueRequestEntity> requests = defaultEntityDao.findRequestById(targetId, number, pageSize);

        return new Page<>(defaultConverter.convertToRequestDto(requests), number, size, pageSize);
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
    public QueueTargetDto update(QueueTargetDto dto) {
        if (dto == null || dto.getId() == null || StringUtils.isEmpty(dto.getName())) {
            throw new UnexpectedRequestParams("ID и Имя не должно быть пустым");
        }

        QueueTargetEntity entity = this.defaultEntityDao.findById(dto.getId());
        entity.setName(dto.getName());

        return this.defaultConverter.convertToDto(this.defaultEntityDao.update(entity));
    }

    @Override
    @Transactional
    public long delete(Long id) {
        this.defaultEntityDao.deleteRequestsByTargetId(id);
        return this.defaultEntityDao.delete(id);
    }


}
