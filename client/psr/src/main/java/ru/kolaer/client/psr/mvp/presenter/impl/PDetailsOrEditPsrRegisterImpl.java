package ru.kolaer.client.psr.mvp.presenter.impl;

import ru.kolaer.client.psr.mvp.presenter.PDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.view.impl.VDetailsOrEditPsrRegisterImpl;

/**
 * Created by danilovey on 04.08.2016.
 */
public class PDetailsOrEditPsrRegisterImpl implements PDetailsOrEditPsrRegister {
    private final VDetailsOrEditPsrRegister view;

    public PDetailsOrEditPsrRegisterImpl() {
        this.view = new VDetailsOrEditPsrRegisterImpl();
    }

    public VDetailsOrEditPsrRegister getView() {
        return this.view;
    }

    @Override
    public void showAndWait() {
        this.view.showAndWait();


    }
}
