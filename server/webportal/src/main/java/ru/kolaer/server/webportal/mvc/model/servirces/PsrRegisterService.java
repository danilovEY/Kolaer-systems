package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterDto;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface PsrRegisterService extends DefaultService<PsrRegisterDto> {
    PsrRegisterDto getPsrRegisterByName(String name);
    void deletePstRegister(Integer ID);
    void deletePstRegisterListById(List<PsrRegisterDto> registers);
    PsrRegisterDto getLastInsertPsrRegister(PsrRegisterDto psrRegister);
    boolean uniquePsrRegister(PsrRegisterDto psrRegister);
    List<PsrRegisterDto> getIdAndNamePsrRegisters();

    Page<PsrRegisterDto> getAll(Integer number, Integer pageSize);
}
