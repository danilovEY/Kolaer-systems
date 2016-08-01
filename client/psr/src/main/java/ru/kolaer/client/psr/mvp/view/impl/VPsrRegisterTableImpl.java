package ru.kolaer.client.psr.mvp.view.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;

import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VPsrRegisterTableImpl implements VPsrRegisterTable {
    private BorderPane tablePane;
    private TableView<PsrRegister> table;
    private ObservableList<PsrRegister> tableData = FXCollections.observableArrayList();

    public VPsrRegisterTableImpl() {
        this.tablePane = new BorderPane();

        this.table = new TableView<>();
        this.table.setItems(this.tableData);

        final TableColumn<PsrRegister, String> psrRegisterNameColumn = new TableColumn<>("Название");
        psrRegisterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.table.getColumns().add(psrRegisterNameColumn);

        this.tablePane.setCenter(this.table);
    }

    @Override
    public void setContent(Parent content) {
        this.tablePane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.tablePane;
    }

    @Override
    public void addPsrProjectAll(List<PsrRegister> psrRegisters) {
        this.tableData.addAll(psrRegisters);
    }
}
