package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentDao extends DefaultDao<DepartmentEntity> {
    DepartmentEntity findByName(String name);
}
