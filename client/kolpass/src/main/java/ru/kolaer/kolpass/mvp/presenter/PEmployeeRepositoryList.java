package ru.kolaer.kolpass.mvp.presenter;

import javafx.util.Pair;
import ru.kolaer.common.dto.BaseModel;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.common.mvp.presenter.BasePresenter;
import ru.kolaer.common.system.network.kolaerweb.KolpassTable;
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
