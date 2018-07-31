package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.exception.ForbiddenException;
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
    public void deleteQueueRequest(Long requestId) {
        defaultEntityDao.deleteRequestById(requestId);
    }

    @Transactional
    @Override
    public Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize) {
        Long size = defaultEntityDao.findCountRequestByTargetId(targetId);
        List<QueueRequestEntity> requests = defaultEntityDao.findRequestByTargetId(targetId, number, pageSize);

        return new Page<>(defaultConverter.convertToRequestDto(requests), number, size, pageSize);
    }
}
