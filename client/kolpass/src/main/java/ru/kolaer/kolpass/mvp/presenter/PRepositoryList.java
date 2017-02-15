package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;

/**
 * Created by danilovey on 15.02.2017.
 */
public interface PRepositoryList extends BasePresenter<VEmployeeRepositoryList>, BaseModel<KolpassTable> {
}
