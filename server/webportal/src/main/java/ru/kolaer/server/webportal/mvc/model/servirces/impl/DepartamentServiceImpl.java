package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.dao.DepartamentDao;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartamentService;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
@Service
public class DepartamentServiceImpl implements DepartamentService {
    @Autowired
    private DepartamentDao departamentDao;

    @Override
    public DepartmentEntity getGeneralDepartamentEntityByName(String name) {
        if(name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is null!");
        return this.departamentDao.findByName(name);
    }

    @Override
    public List<DepartmentEntity> getAll() {
        return this.departamentDao.findAll();
    }

    @Override
    public DepartmentEntity getById(Integer id) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Id is null or < 0!");
        return this.departamentDao.findByID(id);
    }

    @Override
    public void add(DepartmentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.persist(entity);
    }

    @Override
    public void delete(DepartmentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.delete(entity);
    }

    @Override
    public void update(DepartmentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.update(entity);
    }

    @Override
    public void update(List<DepartmentEntity> entity) {

    }

    @Override
    public void delete(List<DepartmentEntity> entites) {

    }
}
