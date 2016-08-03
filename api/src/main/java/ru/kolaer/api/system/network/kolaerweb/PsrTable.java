package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface PsrTable {
    PsrRegister[] getAllPsrRegister() throws ServerException;
}
