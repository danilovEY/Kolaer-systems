package ru.kolaer.client.core.mvp.view;

import java.util.function.Consumer;

/**
 * Created by danilovey on 06.09.2016.
 */
public interface InitializationView<V extends BaseView> {
    void initView(Consumer<V> viewVisit);
}
