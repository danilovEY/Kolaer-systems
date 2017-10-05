package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegisterDao extends DefaultDao<PsrRegisterEntity> {
    List<PsrRegisterEntity> getIdAndNamePsrRegister();
    PsrRegisterEntity getPsrRegisterByName(String name);
    Long getCountEqualsPsrRegister(PsrRegisterEntity psrRegister);
    List getPsrRegisterByStatusTitleComment(Integer status, String name, String comment);
}
