package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.view.VContentLabel;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface PContentLabel extends BasePresenter<VContentLabel> {
    void addPLabel(PLabel label);
    void removePLabel(PLabel label);

    void clear();
}
