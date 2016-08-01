package ru.kolaer.client.psr.mvp.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.model.MPsrRegister;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public class MPsrRegisterImpl implements MPsrRegister{
    private final UniformSystemEditorKit editorKit;
    private static final Logger LOG = LoggerFactory.getLogger(MPsrRegisterImpl.class);

    public MPsrRegisterImpl(final UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    @Override
    public List<PsrRegister> getAllPstRegister() {
        List<PsrRegister> list = this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().getAllPsrRegister();
        list.parallelStream().forEach(psr -> {
            LOG.info(psr.getName());
        });
        return list;
    }
}
