package ru.kolaer.client.psr.mvp.model;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface MPsrRegister {
    List<PsrRegister> getAllPstRegister();

    void addPsrProject(PsrRegister psrRegister);
    void deletePsrProject(PsrRegister psrRegister);
}
