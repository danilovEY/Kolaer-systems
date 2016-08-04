package ru.kolaer.client.psr.mvp.presenter;

import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;

/**
 * Created by danilovey on 04.08.2016.
 */
public interface PDetailsOrEditPsrRegister {
    void showAndWait();
    VDetailsOrEditPsrRegister getView();
}
