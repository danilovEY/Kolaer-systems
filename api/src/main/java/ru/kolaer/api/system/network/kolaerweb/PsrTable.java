package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

import java.util.List;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface PsrTable {
    PsrRegister[] getAllPsrRegister();
}
