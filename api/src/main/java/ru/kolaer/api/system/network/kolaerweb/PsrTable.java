package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterDto;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface PsrTable {
    PsrRegisterDto[] getAllPsrRegister() throws ServerException;
    PsrRegisterDto persistPsrRegister(PsrRegisterDto psrRegister) throws ServerException;
    void deletePsrRegister(PsrRegisterDto psrRegister) throws ServerException;
    void updatePsrRegister(PsrRegisterDto psrRegister) throws ServerException;
}
