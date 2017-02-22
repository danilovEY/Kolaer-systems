package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.view.VGroupTreeItem;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface PGroupTreeItem extends BasePresenter<VGroupTreeItem>, BaseModel<MGroup> {
    void clear();
}
