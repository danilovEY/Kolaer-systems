package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface PsrRegisterService extends ServiceBase<PsrRegister> {
    PsrRegister getPsrRegisterByName(String name);
    void deletePstRegister(Integer ID);
    void deletePstRegisterListById(List<PsrRegister> registers);
    List<PsrRegister> getIdAndNamePsrRegisters();
}
