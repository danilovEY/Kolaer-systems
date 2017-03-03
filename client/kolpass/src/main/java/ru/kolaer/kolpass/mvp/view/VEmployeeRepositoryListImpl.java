package ru.kolaer.kolpass.mvp.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public class VEmployeeRepositoryListImpl implements VEmployeeRepositoryList {
    private final BorderPane mainPane;
    private final Button loadEmployee;
    private final ListView<EmployeeEntity> employeeEntityListView;

    public VEmployeeRepositoryListImpl() {
        this.mainPane = new BorderPane();

        this.employeeEntityListView = new ListView<>();
        this.employeeEntityListView.setCellFactory(param -> new ListCell<EmployeeEntity>() {
            @Override
            protected void updateItem(EmployeeEntity item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    this.setText(item.getInitials() + " (" + item.getDepartment().getAbbreviatedName() + ")");
                } else {
                    this.setText("");
                }
            }
        });

        this.loadEmployee = new Button("Загрузить пароли");
        this.loadEmployee.setMaxWidth(Double.MAX_VALUE);

        this.mainPane.setTop(this.loadEmployee);
        this.mainPane.setCenter(this.employeeEntityListView);

    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void addEmployee(EmployeeEntity employeeEntity) {
        this.employeeEntityListView.getItems().add(employeeEntity);
    }

    @Override
    public void removeEmployee(EmployeeEntity employeeEntity) {
        this.employeeEntityListView.getItems().remove(employeeEntity);
    }

    @Override
    public void setOnLoadOtherEmployee(Function function) {
        this.loadEmployee.setOnAction(function::apply);
    }

    @Override
    public void setOnSelectEmployee(Function<EmployeeEntity, Void> function) {
        this.employeeEntityListView.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            function.apply(newValue);
        });
    }

    @Override
    public void selectIndex(int index) {
        this.employeeEntityListView.getSelectionModel().selectFirst();
    }
}
