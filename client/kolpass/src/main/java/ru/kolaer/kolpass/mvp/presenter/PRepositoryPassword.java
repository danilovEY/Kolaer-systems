package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PRepositoryPassword extends BasePresenter<VRepositoryPassword>, BaseModel<RepositoryPassword> {
    void setOnSaveData(Function function);
    void setOnEditName(Function function);
    void setOnDelete(Function<PRepositoryPassword, Void> function);
}
