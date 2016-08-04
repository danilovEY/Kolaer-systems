package ru.kolaer.client.psr.mvp.view;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;

/**
 * Created by danilovey on 04.08.2016.
 */
public interface VDetailsOrEditPsrRegister extends InitializationView{
    void showAndWait();

    PsrRegister getPsrRegister();
    void setPsrRegister(PsrRegister psrRegister);
}
