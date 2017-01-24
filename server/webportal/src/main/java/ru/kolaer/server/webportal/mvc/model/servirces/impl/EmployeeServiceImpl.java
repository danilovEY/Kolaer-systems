package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.util.Date;
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
    public List<EmployeeEntity> getAll() {
        return this.employeeDao.findAll();
    }

    @Override
    public EmployeeEntity getById(Integer id) {
        if(id != null && id >= 0)
            return this.employeeDao.findByID(id);

        LOG.error("ID is NULL or < 0!");
        return null;
    }

    @Override
    public void add(EmployeeEntity accountsEntity) {
        if(accountsEntity == null) {
            LOG.error("Account is NULL");
            return;
        }

        this.employeeDao.persist(accountsEntity);
    }

    @Override
    public void delete(EmployeeEntity entity) {

    }

    @Override
    public void update(EmployeeEntity entity) {

    }

    @Override
    public void update(List<EmployeeEntity> entity) {

    }

    @Override
    public void delete(List<EmployeeEntity> entites) {

    }

    @Override
    public List<EmployeeEntity> getUserRangeBirthday(Date startData, Date endData) {
        return this.employeeDao.getUserRangeBirthday(startData, endData);
    }

    @Override
    public List<EmployeeEntity> getUsersByBirthday(Date date) {
        return this.employeeDao.getUsersByBirthday(date);
    }

    @Override
    public List<EmployeeEntity> getUserBirthdayToday() {
        return this.employeeDao.getUserBirthdayToday();
    }

    @Override
    public List<EmployeeEntity> getUsersByInitials(String initials) {
        return this.employeeDao.getUsersByInitials(initials);
    }

    @Override
    public List<EmployeeEntity> getUsersByDepartamentId(Integer id) {
        return this.employeeDao.findByDepartamentById(id);
    }

    @Override
    public Page<EmployeeEntity> getUsersByDepartamentId(int page, int pageSize, Integer id) {
        if(page == 0) {
            List<EmployeeEntity> usersByDepartamentId = this.getUsersByDepartamentId(id);
            return new Page<>(usersByDepartamentId, 0, 0, usersByDepartamentId.size());
        }
        return this.employeeDao.findByDepartamentById(page, pageSize, id);
    }

    @Override
    public int getCountUserBirthday(Date date) {
        return this.employeeDao.getCountUserBirthday(date);
    }
}
