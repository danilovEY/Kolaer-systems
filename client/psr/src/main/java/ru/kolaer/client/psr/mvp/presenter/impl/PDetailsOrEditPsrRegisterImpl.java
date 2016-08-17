package ru.kolaer.client.psr.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.presenter.PDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.view.impl.VDetailsOrEditPsrRegisterImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 04.08.2016.
 */
public class PDetailsOrEditPsrRegisterImpl implements PDetailsOrEditPsrRegister {
    private final VDetailsOrEditPsrRegister view;
    private static final Logger LOG = LoggerFactory.getLogger(PDetailsOrEditPsrRegisterImpl.class);
    private PsrRegister psrRegister;

    public PDetailsOrEditPsrRegisterImpl() {
        this.view = new VDetailsOrEditPsrRegisterImpl();
    }

    public VDetailsOrEditPsrRegister getView() {
        return this.view;
    }

    @Override
    public PsrRegister getPsrRegister() {
        return this.psrRegister;
    }

    @Override
    public void setPsrRegister(PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
    }

    @Override
    public void showAndWait() {
        this.view.showAndWait();
        this.psrRegister = this.view.getPsrRegister();
    }
}
