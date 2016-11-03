package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
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
    public GeneralDepartamentEntity getGeneralDepartamentEntityByName(String name) {
        if(name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is null!");
        return this.departamentDao.findByName(name);
    }

    @Override
    public List<GeneralDepartamentEntity> getAll() {
        return this.departamentDao.findAll();
    }

    @Override
    public GeneralDepartamentEntity getById(Integer id) {
        if(id == null || id < 0)
            throw new IllegalArgumentException("Id is null or < 0!");
        return this.departamentDao.findByID(id);
    }

    @Override
    public void add(GeneralDepartamentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.persist(entity);
    }

    @Override
    public void delete(GeneralDepartamentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.delete(entity);
    }

    @Override
    public void update(GeneralDepartamentEntity entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null!");
        this.departamentDao.update(entity);
    }

    @Override
    public void update(List<GeneralDepartamentEntity> entity) {

    }
}
