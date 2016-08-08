package ru.kolaer.client.psr.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;

/**
 * Created by danilovey on 04.08.2016.
 */
public interface PDetailsOrEditPsrRegister {
    void showAndWait();
    VDetailsOrEditPsrRegister getView();

    PsrRegister getPsrRegister();
    void setPsrRegister(PsrRegister psrRegister);
}
