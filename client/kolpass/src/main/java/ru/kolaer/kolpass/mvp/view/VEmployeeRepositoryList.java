package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public interface VEmployeeRepositoryList extends BaseView<VEmployeeRepositoryList, BorderPane> {
    void addEmployee(EmployeeDto employeeEntity);
    void removeEmployee(EmployeeDto employeeEntity);

    void setOnLoadOtherEmployee(Function function);
    void setOnSelectEmployee(Function<EmployeeDto, Void> function);

    void selectIndex(int index);
}
