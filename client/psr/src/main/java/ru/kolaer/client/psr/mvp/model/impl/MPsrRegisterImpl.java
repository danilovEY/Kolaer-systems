package ru.kolaer.client.psr.mvp.model.impl;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.client.psr.mvp.model.MPsrRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public class MPsrRegisterImpl implements MPsrRegister{
    private final UniformSystemEditorKit editorKit;
    private final List<PsrRegister> registers = new ArrayList<>();
    public MPsrRegisterImpl(final UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    @Override
    public List<PsrRegister> getAllPstRegister() {
        if(registers.size() == 0) {
            if (this.editorKit.getUSNetwork().getKolaerWebServer().getServerStatus() == ServerStatus.AVAILABLE) {
                if (this.editorKit.getAuthentication().isAuthentication()) {
                    try {
                        for(PsrRegister register : this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().getAllPsrRegister()) {
                            this.registers.add(register);
                        }
                    } catch (ServerException e) {
                        this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Ошибка на сервере!");
                    }
                } else {
                    this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Вы не авторизовались или нет доступа!");
                }
            } else {
                this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка!", "Сервер не доступен!");
            }
        }

        return this.registers;
    }

    @Override
    public void addPsrProject(PsrRegister psrRegister) {
        this.registers.add(psrRegister);
    }
}
