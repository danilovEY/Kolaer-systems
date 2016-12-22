package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;

import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface JournalViolationDao extends DefaultDao<JournalViolation> {
    List<JournalViolation> findAllByDep(Integer id);
}
