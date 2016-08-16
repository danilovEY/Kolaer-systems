package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public List<GeneralEmployeesEntity> getAll() {
        return this.employeeDao.findAll();
    }

    @Override
    public GeneralEmployeesEntity getById(Integer id) {
        if(id != null && id >= 0)
            return this.employeeDao.findByID(id);

        LOG.error("ID is NULL or < 0!");
        return null;
    }

    @Override
    public void add(GeneralEmployeesEntity accountsEntity) {
        if(accountsEntity == null) {
            LOG.error("Account is NULL");
            return;
        }

        this.employeeDao.persist(accountsEntity);
    }
}