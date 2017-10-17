package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.api.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 17.10.2017.
 */
public interface VMainFrame<T> extends BaseView<T> {
    void show();
    void exit();
    void hide();

    void setOnMinimize(Function<Boolean, Void> function);
    void setOnExit(Function<Object, Void> function);
}