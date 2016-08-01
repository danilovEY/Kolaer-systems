package ru.kolaer.client.psr.mvp.view.impl;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VPsrRegisterTableImpl implements VPsrRegisterTable {
    private BorderPane tablePane;

    public VPsrRegisterTableImpl() {
        this.tablePane = new BorderPane(new Button("TABLE"));
    }

    @Override
    public void setContent(Parent content) {
        this.tablePane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.tablePane;
    }
}
