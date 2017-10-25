package ru.kolaer.api.mvp.view;

import java.util.function.Consumer;

/**
 * Created by danilovey on 06.09.2016.
 */
public interface InitializationView<T> {
    void initView(Consumer<T> viewVisit);
}
