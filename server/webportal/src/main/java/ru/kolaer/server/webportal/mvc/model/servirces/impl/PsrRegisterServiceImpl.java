package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
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
        if(id == null || id <0)
            throw new BadRequestException("ID проекта пустой или меньше нуля!");

        return psrRegisterDao.findByPersonnelNumber(id);
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
    public void delete(List<PsrRegister> entites) {

    }

    @Override
    public PsrRegister getPsrRegisterByName(String name) {
        if(name != null && !name.isEmpty())
            this.psrRegisterDao.getPsrRegisterByName(name);
        return null;
    }

    @Override
    public void deletePstRegister(Integer ID) {
        if(ID != null && ID >= 0) {
            this.psrRegisterDao.deleteById(ID);
        } else
            throw new IllegalArgumentException("Psr register ID in NULL!");
    }

    @Override
    public void deletePstRegisterListById(List<PsrRegister> registers) {
        registers.stream().map(PsrRegister::getId).forEach(this::deletePstRegister);
    }

    @Override
    public PsrRegister getLastInsertPsrRegister(PsrRegister psrRegister) {
        final List<PsrRegister> results = this.psrRegisterDao
                .getPsrRegisterByStatusTitleComment(psrRegister.getStatus().getId(), psrRegister.getName(), psrRegister.getComment());

        if(results.size() != 1)
            throw new IllegalArgumentException("Таких проектов больше чем 1!");
        else
            return results.get(0);
    }

    @Override
    public boolean uniquePsrRegister(PsrRegister psrRegister) {
        if(psrRegister == null || psrRegister.getName() == null || psrRegister.getComment() == null)
            throw new BadRequestException("Пср проект, его имя и описание не может быть null!");
        return this.psrRegisterDao.getCountEqualsPsrRegister(psrRegister).equals(0L);
    }

    @Override
    public List<PsrRegister> getIdAndNamePsrRegisters() {
        return this.psrRegisterDao.getIdAndNamePsrRegister();
    }

    @Override
    public Page<PsrRegister> getAll(Integer number, Integer pageSize) {
        if(number == 0) {
            return new Page<>(this.getAll(), 0, 0, 0);
        }
        return this.psrRegisterDao.findAll(number, pageSize);
    }
}
