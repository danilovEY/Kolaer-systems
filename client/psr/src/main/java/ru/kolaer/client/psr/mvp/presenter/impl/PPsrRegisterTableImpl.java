package ru.kolaer.client.psr.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.model.MPsrRegister;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.impl.VPsrRegisterTableImpl;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PPsrRegisterTableImpl implements PPsrRegisterTable {
    private static final Logger LOG = LoggerFactory.getLogger(PPsrRegisterTableImpl.class);
    private final VPsrRegisterTable view;
    private final UniformSystemEditorKit editorKit;
    private MPsrRegister model;

    public PPsrRegisterTableImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.view = new VPsrRegisterTableImpl(editorKit);
    }

    @Override
    public MPsrRegister getModel() {
        return this.model;
    }

    @Override
    public void setModel(MPsrRegister model) {
        this.model = model;
    }

    @Override
    public VPsrRegisterTable getView() {
        return this.view;
    }

    @Override
    public void updateTableData() {
        this.view.clear();
        this.view.addPsrProjectAll(this.model.getAllPstRegister());
    }
}
