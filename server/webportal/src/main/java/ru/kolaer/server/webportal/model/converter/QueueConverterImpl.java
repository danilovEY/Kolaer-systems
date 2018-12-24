package ru.kolaer.server.webportal.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.server.webportal.model.dao.AccountDao;
import ru.kolaer.server.webportal.model.dto.queue.QueueRequestDto;
import ru.kolaer.server.webportal.model.dto.queue.QueueTargetDto;
import ru.kolaer.server.webportal.model.entity.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.model.entity.queue.QueueTargetEntity;
import ru.kolaer.server.webportal.model.entity.queue.QueueType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QueueConverterImpl implements QueueConverter {
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;

    @Autowired
    public QueueConverterImpl(AccountDao accountDao, AccountConverter accountConverter) {
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
    }

    @Override
    public QueueTargetEntity convertToModel(QueueTargetDto dto) {
        if(dto == null) {
            return null;
        }

        QueueTargetEntity queueTargetEntity = new QueueTargetEntity();
        queueTargetEntity.setId(dto.getId());
        queueTargetEntity.setName(dto.getName());

        return queueTargetEntity;
    }

    @Override
    public QueueTargetDto convertToDto(QueueTargetEntity model) {
        return updateData(new QueueTargetDto(), model);
    }

    @Override
    public QueueTargetDto updateData(QueueTargetDto oldDto, QueueTargetEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());

        return oldDto;
    }

    @Override
    public QueueRequestEntity convertToModel(QueueRequestDto queueRequestDto) {
        if (queueRequestDto == null) {
            return null;
        }

        QueueRequestEntity queueRequestEntity = new QueueRequestEntity();
        queueRequestEntity.setComment(queueRequestDto.getComment());
        queueRequestEntity.setId(queueRequestDto.getId());
        queueRequestEntity.setType(Optional.ofNullable(queueRequestDto.getType()).orElse(QueueType.ONCE));
        queueRequestEntity.setQueueFrom(queueRequestDto.getQueueFrom());
        queueRequestEntity.setQueueTo(queueRequestDto.getQueueTo());

        Optional.ofNullable(queueRequestDto.getAccount())
                .map(AccountDto::getId)
                .ifPresent(queueRequestEntity::setAccountId);

        return queueRequestEntity;
    }

    @Override
    public QueueRequestDto convertToDto(QueueRequestEntity queueRequestEntity) {
        if (queueRequestEntity == null) {
            return null;
        }

        QueueRequestDto queueRequestDto = simpleConvertRequestToDto(queueRequestEntity);

        Optional.ofNullable(queueRequestEntity.getAccountId())
                .map(accountDao::findById)
                .map(accountConverter::convertToDto)
                .ifPresent(queueRequestDto::setAccount);

        return queueRequestDto;
    }

    private QueueRequestDto simpleConvertRequestToDto(QueueRequestEntity queueRequestEntity) {
        if (queueRequestEntity == null) {
            return null;
        }

        QueueRequestDto queueRequestDto = new QueueRequestDto();
        queueRequestDto.setComment(queueRequestEntity.getComment());
        queueRequestDto.setId(queueRequestEntity.getId());
        queueRequestDto.setType(queueRequestEntity.getType());
        queueRequestDto.setQueueFrom(queueRequestEntity.getQueueFrom());
        queueRequestDto.setQueueTo(queueRequestEntity.getQueueTo());

        return queueRequestDto;
    }

    @Override
    public List<QueueRequestDto> convertToRequestDto(List<QueueRequestEntity> queueRequestEntities) {
        if (queueRequestEntities == null || queueRequestEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<QueueRequestDto> queueRequestDtos = new ArrayList<>();

        List<Long> accountIds = queueRequestEntities.stream()
                .map(QueueRequestEntity::getAccountId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Long, AccountDto> accountMap = accountConverter
                .convertToDto(accountDao.findById(accountIds))
                .stream()
                .collect(Collectors.toMap(AccountDto::getId, Function.identity()));

        for (QueueRequestEntity queueRequestEntity : queueRequestEntities) {
            QueueRequestDto queueRequestDto = simpleConvertRequestToDto(queueRequestEntity);

            Optional.ofNullable(queueRequestEntity.getAccountId())
                    .map(accountMap::get)
                    .ifPresent(queueRequestDto::setAccount);

            queueRequestDtos.add(queueRequestDto);
        }

        return queueRequestDtos;
    }
}
