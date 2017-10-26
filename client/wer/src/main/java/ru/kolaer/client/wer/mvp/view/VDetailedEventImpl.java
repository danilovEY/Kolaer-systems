package ru.kolaer.client.wer.mvp.view;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Data;
import ru.kolaer.client.wer.mvp.model.Event;

import java.util.function.Consumer;

/**
 * Created by danilovey on 16.03.2017.
 */
public class VDetailedEventImpl implements VDetailedEvent {
    private TableView<Data> tableView;
    private BorderPane mainPane;

    private void init() {
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<Data, String> nameColumn = new TableColumn<>("Ключ");
        nameColumn.setMinWidth(200);
        nameColumn.setMaxWidth(300);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<Data, String> dataColumn = new TableColumn<>("Значение");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        ObservableList<TableColumn<Data, ?>> columns = this.tableView.getColumns();
        columns.add(nameColumn);
        columns.add(dataColumn);

        this.mainPane.setCenter(this.tableView);
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
    public void setEvent(Event event) {
        this.tableView.getItems().clear();
        if(event != null)
            this.tableView.getItems().setAll(event.getEventData().getDatas());
    }

    @Override
    public void initView(Consumer<VDetailedEvent> viewVisit) {
        this.mainPane = new BorderPane();
        this.tableView = new TableView<>();
        this.init();

        viewVisit.accept(this);
    }
}
