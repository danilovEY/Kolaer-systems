package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.webportal.mvc.model.converter.PasswordHistoryConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordHistoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@Service
public class PasswordHistoryServiceImpl
        extends AbstractDefaultService<PasswordHistoryDto, PasswordHistoryEntity>
        implements PasswordHistoryService {

    private final PasswordHistoryDao passwordHistoryDao;
    private final PasswordHistoryConverter defaultConverter;

    protected PasswordHistoryServiceImpl(PasswordHistoryDao defaultEntityDao,
                                         PasswordHistoryConverter converter) {
        super(defaultEntityDao, converter);
        this.passwordHistoryDao = defaultEntityDao;
        this.defaultConverter = converter;
    }

    @Override
    public Page<PasswordHistoryDto> getHistoryByIdRepository(Long id, Integer number, Integer pageSize) {
        if(pageSize == null || pageSize == 0) {
            List<PasswordHistoryDto> result = passwordHistoryDao.findAllHistoryByIdRepository(id)
                    .stream()
                    .map(defaultConverter::convertToDto)
                    .collect(Collectors.toList());
            return new Page<>(result, 0, 0, result.size());
        } else {
            Long count = passwordHistoryDao.findCountHistoryByIdRepository(id, number, pageSize);
            List<PasswordHistoryDto> result = passwordHistoryDao.findHistoryByIdRepository(id, number, pageSize)
                    .stream()
                    .map(defaultConverter::convertToDto)
                    .collect(Collectors.toList());

            return new Page<>(result, number, count, pageSize);
        }
    }

    @Override
    public void deleteByIdRep(Long id) {
        passwordHistoryDao.deleteByIdRep(id);
    }
}
