package ru.kolaer.api.mvp.presenter;

import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface BasePresenter<T extends BaseView> {
    T getView();
    void setView(T view);
}
