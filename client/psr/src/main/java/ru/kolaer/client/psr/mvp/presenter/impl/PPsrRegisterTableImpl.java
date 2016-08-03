package ru.kolaer.client.psr.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.model.MPsrRegister;
import ru.kolaer.client.psr.mvp.model.impl.MPsrRegisterImpl;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.impl.VPsrRegisterTableImpl;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PPsrRegisterTableImpl implements PPsrRegisterTable {
    private static final Logger LOG = LoggerFactory.getLogger(PPsrRegisterTableImpl.class);
    private final VPsrRegisterTable view;
    private final MPsrRegister model;
    private final UniformSystemEditorKit editorKit;

    public PPsrRegisterTableImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.view = new VPsrRegisterTableImpl();
        this.model = new MPsrRegisterImpl(editorKit);
    }

    @Override
    public MPsrRegister getModel() {
        return this.model;
    }

    @Override
    public VPsrRegisterTable getView() {
        return this.view;
    }

    @Override
    public void updateTableData() {
        final PsrRegister[] psrRegisters = this.model.getAllPstRegister();
        if(psrRegisters != null) {
            for (PsrRegister psr : psrRegisters) {
                this.view.addPsrProject(psr);
            }
        }
    }
}
