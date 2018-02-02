package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.PasswordRepositoryConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordRepositoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordRepositoryService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@Service
public class PasswordRepositoryServiceImpl
        extends AbstractDefaultService<PasswordRepositoryDto, PasswordRepositoryEntity, PasswordRepositoryDao, PasswordRepositoryConverter>
        implements PasswordRepositoryService {

    protected PasswordRepositoryServiceImpl(PasswordRepositoryDao passwordRepositoryDao,
                                            PasswordRepositoryConverter converter) {
        super(passwordRepositoryDao, converter);
    }

    @Override
    public Page<PasswordRepositoryDto> getAllByPnumber(Long pnumber, Integer number, Integer pageSize) {
        if(pageSize == null || pageSize == 0) {
            List<PasswordRepositoryDto> result = this.getAllByPnumbers(Collections.singletonList(pnumber));
            return new Page<>(result, 0, 0, result.size());
        } else {
            Long count = defaultEntityDao.findCountAllByPnumber(pnumber, number, pageSize);
            List<PasswordRepositoryDto> result = defaultEntityDao.findAllByPnumber(pnumber, number, pageSize)
                    .stream()
                    .map(defaultConverter::convertToDto)
                    .collect(Collectors.toList());
            return new Page<>(result, number, count, pageSize);
        }
    }

    @Override
    public PasswordRepositoryDto getByNameAndPnumber(String name, Long pnumber) {
        if(name == null || pnumber == null) {
            throw new UnexpectedRequestParams("Имя и табельный номер не может быть пустым!");
        }

        return defaultConverter.convertToDto(defaultEntityDao.findByNameAndPnumber(name, pnumber));
    }

    @Override
    public List<PasswordRepositoryDto> getAllByPnumbers(List<Long> idsChief) {
        return defaultEntityDao.findAllByPnumbers(idsChief)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
