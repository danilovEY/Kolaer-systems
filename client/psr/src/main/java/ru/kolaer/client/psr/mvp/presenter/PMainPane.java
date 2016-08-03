package ru.kolaer.client.psr.mvp.presenter;

import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.client.psr.mvp.view.VMainPane;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface PMainPane extends AuthenticationObserver {
    VMainPane getView();
    void updatePluginPage();
}
