package ru.kolaer.client.psr.mvp.presenter;

import ru.kolaer.client.psr.mvp.model.MPsrRegister;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface PPsrRegisterTable {
    MPsrRegister getModel();
    VPsrRegisterTable getView();
}
