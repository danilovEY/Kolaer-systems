package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.common.dto.BaseModel;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.common.mvp.presenter.BasePresenter;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PRepositoryPassword extends BasePresenter<VRepositoryPassword>, BaseModel<PasswordRepositoryDto> {
    void setOnSaveData(Function function);
    void setOnEditName(Function function);
    void setOnDelete(Function<PRepositoryPassword, Void> function);
}
