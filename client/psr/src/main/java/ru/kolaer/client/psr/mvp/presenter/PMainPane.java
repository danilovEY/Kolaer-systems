package ru.kolaer.client.psr.mvp.presenter;

import ru.kolaer.client.psr.mvp.view.VMainPane;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface PMainPane {
    VMainPane getView();
    void updatePluginPage();
}
