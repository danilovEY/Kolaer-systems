package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VContentLabel;

import java.util.function.Function;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface PContentLabel extends BasePresenter<VContentLabel>, Access {
    void addPLabel(PLabel label);
    void removePLabel(PLabel label);

    void clear();

    void setOnAddLabel(Function<MLabel, Void> function);
    void setOnPlaceLabel(Function<Void, Void> function);
}
