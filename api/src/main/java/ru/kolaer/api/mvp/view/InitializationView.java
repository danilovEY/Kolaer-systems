package ru.kolaer.api.mvp.view;

import java.util.function.Function;

/**
 * Created by danilovey on 06.09.2016.
 */
public interface InitializationView<T> {
    void initView(Function<T, Void> viewVisit) throws Exception;
}
