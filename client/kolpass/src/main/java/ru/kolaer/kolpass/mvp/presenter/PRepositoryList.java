package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;

import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public interface PRepositoryList extends BasePresenter<VEmployeeRepositoryList>, BaseModel<KolpassTable> {
    void setOnSelectEmployee(Function<List<RepositoryPassword>, Void> function);
}
