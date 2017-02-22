package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MGroupDataService;
import ru.kolaer.asmc.mvp.view.VGroupTree;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface PGroupList extends BasePresenter<VGroupTree>, BaseModel<MGroupDataService> {
    void setOnSelectItem(Function<MGroup, Void> function);
}
