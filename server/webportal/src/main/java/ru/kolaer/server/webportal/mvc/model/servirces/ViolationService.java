package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.controllers.ViolationController;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface ViolationService extends ServiceBase<Violation> {
    List<Violation> getAllByJournalAndEffective(Integer idJournal);
    List<Violation> getByIdJournal(Integer id);
}
