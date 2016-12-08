package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.mvc.model.entities.Page;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface PsrRegisterService extends ServiceBase<PsrRegister> {
    PsrRegister getPsrRegisterByName(String name);
    void deletePstRegister(Integer ID);
    void deletePstRegisterListById(List<PsrRegister> registers);
    PsrRegister getLastInsertPsrRegister(PsrRegister psrRegister);
    boolean uniquePsrRegister(PsrRegister psrRegister);
    List<PsrRegister> getIdAndNamePsrRegisters();

    Page<PsrRegister> getAll(Integer number, Integer pageSize);
}
