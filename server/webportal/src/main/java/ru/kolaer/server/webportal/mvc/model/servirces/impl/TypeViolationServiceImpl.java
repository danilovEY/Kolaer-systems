package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.server.webportal.mvc.model.dao.TypeViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.TypeViolationService;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
@Service
public class TypeViolationServiceImpl implements TypeViolationService {

    @Autowired
    private TypeViolationDao typeViolationDao;

    @Override
    public List<TypeViolation> getAll() {
        return this.typeViolationDao.findAll();
    }

    @Override
    public TypeViolation getById(Integer id) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Id is null!");
        return this.typeViolationDao.findByPersonnelNumber(id);
    }

    @Override
    public void add(TypeViolation entity) {
        if(entity == null)
            throw new IllegalArgumentException("TypeViolation is null!");
        this.typeViolationDao.persist(entity);
    }

    @Override
    public void delete(TypeViolation entity) {
        if(entity == null)
            throw new IllegalArgumentException("TypeViolation is null!");
        this.typeViolationDao.delete(entity);
    }

    @Override
    public void update(TypeViolation entity) {
        if(entity == null)
            throw new IllegalArgumentException("TypeViolation is null!");
        this.typeViolationDao.update(entity);
    }

    @Override
    public void update(List<TypeViolation> entity) {

    }

    @Override
    public void delete(List<TypeViolation> entites) {

    }
}
