package ru.kolaer.server.webportal.mvc.model.dao;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface ViolationDao extends DefaultDao<Violation> {

    @Transactional(readOnly = true)
    List<Violation> findByJournalAndEffective(Integer idJournal);
}
