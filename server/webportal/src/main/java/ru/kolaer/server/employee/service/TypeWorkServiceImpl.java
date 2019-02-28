package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.employee.converter.TypeWorkConverter;
import ru.kolaer.server.employee.dao.TypeWorkDao;
import ru.kolaer.server.employee.model.entity.TypeWorkEntity;
import ru.kolaer.server.employee.model.request.FindTypeWorkRequest;

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
    public PageDto<TypeWorkDto> getAll(FindTypeWorkRequest request) {
        Long allCount = defaultEntityDao.findCountAll(request);
        List<TypeWorkDto> all = defaultConverter.convertToDto(defaultEntityDao.findAll(request));

        return new PageDto<>(all, request.getPageNum(), allCount, request.getPageSize());

    }
}
