package ru.kolaer.server.webportal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.webportal.model.converter.UrlSecurityConverter;
import ru.kolaer.server.webportal.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.model.entity.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.service.UrlSecurityService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class UrlSecurityServiceImpl extends AbstractDefaultService<UrlSecurityDto, UrlSecurityEntity, UrlSecurityDao, UrlSecurityConverter>
        implements UrlSecurityService {

    @Autowired
    public UrlSecurityServiceImpl(UrlSecurityDao urlSecurityDao, UrlSecurityConverter converter) {
        super(urlSecurityDao, converter);

    }

    @Override
    @Transactional(readOnly = true)
    public UrlSecurityDto getPathByUrlAndMethod(String userUrl, String method) {
        log.debug("Method: {} Url: {} ", userUrl, method);
        String url = userUrl;
        if(userUrl.contains("?")) {
            url = userUrl.substring(0, userUrl.indexOf("?"));
        }

        return defaultConverter.convertToDto(defaultEntityDao.findPathByUrlAndMethod(url, method));
    }

    @Override
    public List<String> getAccesses(UrlSecurityDto urlPath) {
       return defaultConverter.convertToAccesses(urlPath);
    }

    @Override
    @Transactional
    public void clear() {
        this.defaultEntityDao.clear();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UrlSecurityDto> getPathByMethod(String method) {
        return defaultConverter.convertToDto(defaultEntityDao.findPathByMethod(method));
    }

}
