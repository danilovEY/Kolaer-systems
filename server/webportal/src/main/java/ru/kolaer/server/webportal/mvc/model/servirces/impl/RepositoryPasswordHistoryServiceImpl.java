package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.RepositoryPasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.servirces.RepositoryPasswordHistoryService;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Service
public class RepositoryPasswordHistoryServiceImpl implements RepositoryPasswordHistoryService {

    @Autowired
    private RepositoryPasswordHistoryDao repositoryPasswordHistoryDao;

    @Override
    public List<RepositoryPasswordHistory> getAll() {
        return this.repositoryPasswordHistoryDao.findAll();
    }

    @Override
    public RepositoryPasswordHistory getById(Integer id) {
        if(id == null || id < 0)
            throw new BadRequestException("ID должно быть больше 0!");

        return this.repositoryPasswordHistoryDao.findByID(id);
    }

    @Override
    public void add(RepositoryPasswordHistory entity) {
        this.repositoryPasswordHistoryDao.persist(entity);
    }

    @Override
    public void delete(RepositoryPasswordHistory entity) {
        this.repositoryPasswordHistoryDao.delete(entity);
    }

    @Override
    public void update(RepositoryPasswordHistory entity) {
        this.repositoryPasswordHistoryDao.update(entity);
    }

    @Override
    public void update(List<RepositoryPasswordHistory> entites) {
        this.repositoryPasswordHistoryDao.update(entites);
    }

    @Override
    public void delete(List<RepositoryPasswordHistory> entites) {
        this.repositoryPasswordHistoryDao.delete(entites);
    }

    @Override
    public Page<RepositoryPasswordHistory> getHistoryByIdRepository(Integer id, Integer number, Integer pageSize) {
        return this.repositoryPasswordHistoryDao.findHistoryByIdRepository(id, number, pageSize);
    }

    @Override
    public void deleteByIdRep(Integer id) {
        this.repositoryPasswordHistoryDao.deleteByIdRep(id);
    }
}
