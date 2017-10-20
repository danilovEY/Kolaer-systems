package ru.kolaer.kolpass.mvp.presenter;

import javafx.util.Pair;
import ru.kolaer.api.mvp.model.BaseModel;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.api.mvp.presenter.BasePresenter;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;

import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public interface PEmployeeRepositoryList extends BasePresenter<VEmployeeRepositoryList>, BaseModel<KolpassTable> {
    void clear();
    void setOnSelectEmployee(Function<Pair<EmployeeDto, List<PasswordRepositoryDto>>, Void> function);
    void selectIndex(int index);
}
