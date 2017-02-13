package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VRepositoryPane;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PRepositoryPane extends BasePresenter<VRepositoryPane>, BaseModel<KolpassTable> {
    void addRepositoryPassword(PRepositoryPassword pRepositoryPassword);
    void removeRepositoryPassword(PRepositoryPassword pRepositoryPassword);
}
