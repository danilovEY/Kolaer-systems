package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;
import ru.kolaer.server.webportal.mvc.model.converter.UrlSecurityConverter;
import ru.kolaer.server.webportal.mvc.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class UrlSecurityServiceImpl extends AbstractDefaultService<UrlSecurityDto, UrlSecurityEntity> implements UrlSecurityService {
    private final UrlSecurityDao urlSecurityDao;
    private final UrlSecurityConverter converter;

    @Autowired
    public UrlSecurityServiceImpl(UrlSecurityDao urlSecurityDao, UrlSecurityConverter converter) {
        super(urlSecurityDao, converter);
        this.urlSecurityDao = urlSecurityDao;
        this.converter = converter;

    }

    @Override
    @Transactional(readOnly = true)
    public UrlSecurityDto getPathByUrl(String userUrl) {
        String url = userUrl;
        if(userUrl.contains("?")) {
            url = userUrl.substring(0, userUrl.indexOf("?"));
        }

        return converter.convertToDto(urlSecurityDao.getPathByUrl(url));
    }

    @Override
    public List<String> getAccesses(UrlSecurityDto urlPath) {
       return converter.convertToAccesses(urlPath);
    }

    @Override
    @Transactional
    public void clear() {
        this.urlSecurityDao.clear();
    }

}
