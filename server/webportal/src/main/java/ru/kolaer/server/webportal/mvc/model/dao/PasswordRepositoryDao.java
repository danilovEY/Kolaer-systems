package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryDao extends DefaultDao<PasswordRepositoryEntity> {
    Long findCountAllByPnumber(Long pnumber, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByPnumber(Long pnumber, Integer number, Integer pageSize);

    PasswordRepositoryEntity findByNameAndPnumber(String name, Long pnumber);

    List<PasswordRepositoryEntity> findAllByPnumbers(List<Long> idsChief);
}
