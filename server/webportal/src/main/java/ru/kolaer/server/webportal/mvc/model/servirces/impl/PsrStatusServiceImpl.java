package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.mvc.model.dao.PsrStatusDao;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrStatusService;

import java.util.List;

/**
 * Created by danilovey on 23.08.2016.
 */
@Service("psrStatusService")
public class PsrStatusServiceImpl implements PsrStatusService {

    @Autowired
    private PsrStatusDao psrStatusDao;

    @Override
    public List<PsrStatus> getAll() {
        return psrStatusDao.findAll();
    }

    @Override
    public PsrStatus getById(Integer id) {
        return null;
    }

    @Override
    public void add(PsrStatus entity) {

    }

    @Override
    public void delete(PsrStatus entity) {

    }

    @Override
    public void update(PsrStatus entity) {

    }
}
