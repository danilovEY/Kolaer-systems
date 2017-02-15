package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public interface VEmployeeRepositoryList extends BaseView<BorderPane> {
    void addEmployee(EmployeeEntity employeeEntity);
    void removeEmployee(EmployeeEntity employeeEntity);

    void setOnSelectEmployee(Function<EmployeeEntity, Void> function);
}
