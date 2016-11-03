package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrRegisterService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class PsrRegisterServiceImpl implements PsrRegisterService {

    @Autowired
    private PsrRegisterDao psrRegisterDao;

    @Override
    public List<PsrRegister> getAll() {
        return psrRegisterDao.findAll();
    }

    @Override
    public PsrRegister getById(Integer id) {
        if(id != null && id >= 0)
            return psrRegisterDao.findByID(id);

        return null;
    }

    @Override
    public void add(PsrRegister entity) {
        if(entity != null)
            psrRegisterDao.persist(entity);
    }

    @Override
    public void delete(PsrRegister entity) {
        if(entity != null && entity.getId() != null)
            this.psrRegisterDao.delete(entity);
    }

    @Override
    public void update(PsrRegister entity) {
        if(entity != null) {
            this.psrRegisterDao.update(entity);
        }
    }

    @Override
    public void update(List<PsrRegister> entity) {

    }

    @Override
    public PsrRegister getPsrRegisterByName(String name) {
        if(name != null && !name.isEmpty())
            this.psrRegisterDao.getPsrRegisterByName(name);
        return null;
    }

    @Override
    public List<PsrRegister> getIdAndNamePsrRegisters() {
        return this.psrRegisterDao.getIdAndNamePsrRegister();
    }
}
