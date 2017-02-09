package ru.kolaer.client.psr.mvp.view;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface VPsrRegisterTable extends BaseView {
    void addPsrProjectAll(List<PsrRegister> psrRegisters);
    void addPsrProject(PsrRegister psrRegister);

    void clear();
}
