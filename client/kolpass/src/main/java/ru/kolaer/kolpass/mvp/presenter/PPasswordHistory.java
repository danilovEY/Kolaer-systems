package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.common.mvp.model.BaseModel;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.mvp.presenter.BasePresenter;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PPasswordHistory extends BasePresenter<VPasswordHistory>, BaseModel<PasswordHistoryDto> {
    void setEditable(boolean edit);
    boolean isEditable();
}
