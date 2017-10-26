package ru.kolaer.api.system.impl;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import ru.kolaer.api.system.ui.DialogUS;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultDialogUS implements DialogUS {
    @Override
    public Dialog<?> createSimpleDialog(String title, String text) {
        return null;
    }

    @Override
    public Dialog<?> createErrorDialog(String title, String text) {
        return null;
    }

    @Override
    public Dialog<?> createWarningDialog(String title, String text) {
        return null;
    }

    @Override
    public Dialog<?> createLoadingDialog(Task<?> task) {
        return null;
    }

    @Override
    public void createAndShowLoginToSystemDialog() {

    }

    @Override
    public Dialog<?> createInfoDialog(String title, String text) {
        return null;
    }

    @Override
    public Dialog<?> createLoadingDialog(Service<?> service) {
        return null;
    }

    @Override
    public Dialog<?> createLoginDialog() {
        return null;
    }
}
