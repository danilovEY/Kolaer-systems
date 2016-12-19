package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface JournalViolationService extends ServiceBase<JournalViolation> {
    List<JournalViolation> getAllByDep(Integer id);
}
