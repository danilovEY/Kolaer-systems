package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VRepositoryContent;

import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface PRepositoryContent extends BasePresenter<VRepositoryContent>, BaseModel<KolpassTable> {
    void addRepositoryPassword(PRepositoryPassword pRepositoryPassword);
    void setAllRepositoryPassword(List<PRepositoryPassword> pRepositoryPasswords);
    void removeRepositoryPassword(PRepositoryPassword pRepositoryPassword);
    void clear();

    void setEmployee(EmployeeDto key);
}
