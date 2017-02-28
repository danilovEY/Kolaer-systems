package ru.kolaer.client.psr.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface VPsrRegisterTable extends BaseView<Parent> {
    void addPsrProjectAll(List<PsrRegister> psrRegisters);
    void addPsrProject(PsrRegister psrRegister);

    void clear();
}
