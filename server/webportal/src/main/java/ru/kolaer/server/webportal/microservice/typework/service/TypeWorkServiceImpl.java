package ru.kolaer.server.webportal.microservice.typework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.typework.converter.TypeWorkConverter;
import ru.kolaer.server.webportal.microservice.typework.dto.FindTypeWorkRequest;
import ru.kolaer.server.webportal.microservice.typework.entity.TypeWorkEntity;
import ru.kolaer.server.webportal.microservice.typework.repository.TypeWorkRepository;

import java.util.List;

@Service
public class TypeWorkServiceImpl
        extends AbstractDefaultService<TypeWorkDto, TypeWorkEntity, TypeWorkRepository, TypeWorkConverter>
        implements TypeWorkService {

    public TypeWorkServiceImpl(TypeWorkRepository defaultEntityDao, TypeWorkConverter converter) {
        super(defaultEntityDao, converter);
    }

    @Override
    @Transactional
    public TypeWorkDto updateTypeWork(long typeWorkId, TypeWorkDto request) {
        if (!StringUtils.hasText(request.getName())) {
            throw new UnexpectedRequestParams("Имя не должно быть пустым");
        }

        TypeWorkEntity typeWorkEntity = defaultEntityDao.findById(typeWorkId);
        typeWorkEntity.setName(request.getName());

        return defaultConverter.convertToDto(defaultEntityDao.save(typeWorkEntity));
    }

    @Override
    @Transactional
    public void deleteTypeWork(long typeWorkId) {
        defaultEntityDao.delete(typeWorkId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeWorkDto> getAll(FindTypeWorkRequest request) {
        Long allCount = defaultEntityDao.findCountAll(request);
        List<TypeWorkDto> all = defaultConverter.convertToDto(defaultEntityDao.findAll(request));

        return new Page<>(all, request.getNumber(), allCount, request.getPageSize());

    }
}
