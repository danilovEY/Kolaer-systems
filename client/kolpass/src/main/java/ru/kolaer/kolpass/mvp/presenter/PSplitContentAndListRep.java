package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.client.core.mvp.presenter.BasePresenter;
import ru.kolaer.client.core.system.network.kolaerweb.KolpassTable;
import ru.kolaer.common.dto.BaseModel;
import ru.kolaer.kolpass.mvp.view.VSplitContentAndListRep;

/**
 * Created by danilovey on 16.02.2017.
 */
public interface PSplitContentAndListRep extends BasePresenter<VSplitContentAndListRep>,
        BaseModel<KolpassTable> {
    void setEmployeeList(PEmployeeRepositoryList list);
    void setContent(PRepositoryContent content);

    void clear();
}
