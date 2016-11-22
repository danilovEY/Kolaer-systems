package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegisterDao extends DaoStandard<PsrRegister> {
    List<PsrRegister> getIdAndNamePsrRegister();
    void deleteById(Integer ID);
    PsrRegister getPsrRegisterByName(String name);
    Object getCountEqualsPsrRegister(PsrRegister psrRegister);
    List getPsrRegisterByStatusTitleComment(Integer status, String name, String comment);
}
