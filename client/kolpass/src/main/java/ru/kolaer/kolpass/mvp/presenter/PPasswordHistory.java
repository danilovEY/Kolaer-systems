package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PPasswordHistory extends BasePresenter<VPasswordHistory>, BaseModel<RepositoryPasswordHistoryDto> {

}
