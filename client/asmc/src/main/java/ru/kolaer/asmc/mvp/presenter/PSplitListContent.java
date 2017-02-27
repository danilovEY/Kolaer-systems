package ru.kolaer.asmc.mvp.presenter;

import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.asmc.mvp.view.VSplitListContent;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface PSplitListContent extends BasePresenter<VSplitListContent> {
    void setPGroupList(PGroupTree groupList);
    void setPContentLabel (PContentLabel contentLabel);
}
