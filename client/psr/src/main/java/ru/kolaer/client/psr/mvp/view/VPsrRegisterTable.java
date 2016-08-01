package ru.kolaer.client.psr.mvp.view;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.view.VComponentUI;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface VPsrRegisterTable extends VComponentUI {
    void addPsrProjectAll(List<PsrRegister> psrRegisters);
}
