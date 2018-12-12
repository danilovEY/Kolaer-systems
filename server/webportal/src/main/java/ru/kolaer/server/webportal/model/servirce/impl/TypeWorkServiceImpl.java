package ru.kolaer.server.webportal.model.servirce.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.model.converter.TypeWorkConverter;
import ru.kolaer.server.webportal.model.dao.TypeWorkDao;
import ru.kolaer.server.webportal.model.dto.typework.FindTypeWorkRequest;
import ru.kolaer.server.webportal.model.entity.typework.TypeWorkEntity;
import ru.kolaer.server.webportal.model.servirce.AbstractDefaultService;
import ru.kolaer.server.webportal.model.servirce.TypeWorkService;

import java.util.List;

@Service
public class TypeWorkServiceImpl
        extends AbstractDefaultService<TypeWorkDto, TypeWorkEntity, TypeWorkDao, TypeWorkConverter>
        implements TypeWorkService {

    public TypeWorkServiceImpl(TypeWorkDao defaultEntityDao, TypeWorkConverter converter) {
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
