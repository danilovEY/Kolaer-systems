package ru.kolaer.client.psr.mvp.model.impl;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.model.MPsrRegister;

/**
 * Created by danilovey on 01.08.2016.
 */
public class MPsrRegisterImpl implements MPsrRegister{
    private final UniformSystemEditorKit editorKit;

    public MPsrRegisterImpl(final UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    @Override
    public PsrRegister[] getAllPstRegister() {
        return this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().getAllPsrRegister();
    }
}
