package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordHistoryDao extends DefaultDao<PasswordHistoryEntity> {
    Long findCountHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findAllHistoryByIdRepository(Long id);

    void deleteAllByIdRep(Long repId);

    PasswordHistoryEntity findByRepAndId(Long repId, Long passId);
}
