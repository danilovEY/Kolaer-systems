package ru.kolaer.client.wer.mvp.view;

import javafx.beans.binding.Binding;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.System;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 16.03.2017.
 */
public class VEventTableImpl implements VEventTable {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private final TableView<Event> eventTableView;
    private final BorderPane mainPane;

    public VEventTableImpl() {
        this.mainPane = new BorderPane();
        this.eventTableView = new TableView<>();

        this.init();
    }

    private void init() {
        this.eventTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<Event, System> keywordsTC = new TableColumn<>("Ключевые слова");
        keywordsTC.setCellValueFactory(new PropertyValueFactory<>("system"));
        keywordsTC.setCellFactory(param -> new TableCell<Event, System>(){
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    this.setText("");
                } else {
                    this.setText(convertKeyword(item.getKeyword()));
                }
            }
        });

        final TableColumn<Event, System> dateTimeTC = new TableColumn<>("Дата и время");
        dateTimeTC.setCellValueFactory(new PropertyValueFactory<>("system"));
        dateTimeTC.setCellFactory(param -> new TableCell<Event, System>(){
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    this.setText("");
                } else {
                    this.setText(sdf.format(item.getTimeCreated().getSystemTime()));
                }
            }
        });

        final TableColumn<Event, System> eventIdTC = new TableColumn<>("Код события");
        eventIdTC.setCellValueFactory(new PropertyValueFactory<>("system"));
        eventIdTC.setCellFactory(param -> new TableCell<Event, System>(){
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    this.setText("");
                } else {
                    this.setText(item.getEventID().toString());
                }
            }
        });

        final TableColumn<Event, System> taskTC = new TableColumn<>("Категория задач");
        taskTC.setCellValueFactory(new PropertyValueFactory<>("system"));
        taskTC.setCellFactory(param -> new TableCell<Event, System>(){
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    this.setText("");
                } else {
                    this.setText(convertTask(item.getTask()));
                }
            }
        });

        final TableColumn<Event, System> computerTC = new TableColumn<>("Компьютер");
        computerTC.setCellValueFactory(new PropertyValueFactory<>("system"));
        computerTC.setCellFactory(param -> new TableCell<Event, System>(){
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    this.setText("");
                } else {
                    this.setText(item.getComputer());
                }
            }
        });

        ObservableList<TableColumn<Event, ?>> columns = this.eventTableView.getColumns();
        columns.add(keywordsTC);
        columns.add(dateTimeTC);
        columns.add(computerTC);
        columns.add(eventIdTC);
        columns.add(taskTC);

        this.mainPane.setCenter(this.eventTableView);
    }

    private String convertKeyword(String keyword) {
        switch (keyword) {
            case "0x8020000000000000": return "Аудит успеха";
            case "0x8010000000000000": return "Аудит отказа";
            default: return keyword;
        }
    }

    private String convertTask(Integer task) {
        switch (task) {
            case 12800: return "Файловая система";
            case 12812: return "Съемные носители";
            case 12544: return "Вход в систему";
            case 12548: return "Специальный вход";
            case 13568: return "Аудит изменения политики";
            case 12545: return "Выход из системы";
            case 12808: return "Общий файловый ресурс";
            case 12288: return "Изменение состояния безопасности";
            case 14336: return "Проверка учетных данных";
            default: return task.toString();
        }
    }

    @Override
    public void setOnSelectEvent(Function<Event, Void> function) {
        this.eventTableView.setOnMouseClicked(e -> {
            function.apply(this.eventTableView.getSelectionModel().getSelectedItem());
        });
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
    public void setEvents(List<Event> eventList) {
        this.eventTableView.getItems().setAll(eventList);
    }

    @Override
    public void addEvents(Event event) {
        this.eventTableView.getItems().add(event);
    }

    @Override
    public void clear() {
        this.eventTableView.getItems().clear();
    }
}
