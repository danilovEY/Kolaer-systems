package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.view.VLabel;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface PLabel extends BasePresenter<VLabel>, BaseModel<MLabel> {
    void setOnEdit(Function<MLabel, Void> function);
    void setOnDelete(Function<MLabel, Void> function);
}
