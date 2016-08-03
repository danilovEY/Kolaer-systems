package ru.kolaer.client.psr.mvp.model.impl;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
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
        if(this.editorKit.getUSNetwork().getKolaerWebServer().getServerStatus() == ServerStatus.AVAILABLE) {
            if(this.editorKit.getAuthentication().isAuthentication()) {
                try {
                    return this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().getAllPsrRegister();
                } catch (ServerException e) {
                    this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Ошибка на сервере!");
                }
            } else {
                this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Вы не авторизовались или нет доступа!");
            }
        } else {
            this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Сервер не доступен!");
        }

        return null;
    }
}
