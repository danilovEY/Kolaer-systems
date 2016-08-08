package ru.kolaer.server.webportal.mvc.model.dao;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegisterDao extends DaoStandard<PsrRegister> {
    @Transactional(readOnly = true)
    List<PsrRegister> getIdAndNamePsrRegister();
    @Transactional(readOnly = true)
    PsrRegister getPsrRegisterByName(String name);
}
