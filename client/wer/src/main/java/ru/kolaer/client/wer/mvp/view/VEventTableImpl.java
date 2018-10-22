package ru.kolaer.client.wer.mvp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.client.wer.mvp.model.Data;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.System;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by danilovey on 16.03.2017.
 */
@Slf4j
public class VEventTableImpl implements VEventTable {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private ObservableList<Event> observableList;
    private TableView<Event> eventTableView;
    private BorderPane mainPane;
    private TextField textFilter;
    private FilteredList<Event> filteredList;
    private SortedList<Event> events;

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
                    this.setText(item.getKeyword());
                }
            }
        });

        final TableColumn<Event, System> dateTimeTC = new TableColumn<>("Дата и время");
        dateTimeTC.setComparator(Comparator
                .comparing((System s) -> s.getTimeCreated().getSystemTime())
                .reversed());
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

        this.eventTableView.setItems(this.observableList);

        this.textFilter.textProperty().addListener((observable, oldValue, newValue) ->
            Optional.ofNullable(this.filteredList).ifPresent(filteredList ->
                filteredList.setPredicate(event -> {
                    if(newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }

                    final String findText = newValue.toLowerCase();

                    final System system = event.getSystem();
                    Stream<Data> datas = Stream.of(event.getEventData().getDatas());

                    return system.getComputer().toLowerCase().contains(findText)
                            || system.getEventID().toString().contains(findText)
                            || system.getKeyword().toLowerCase().contains(findText)
                            || datas.map(Data::getValue).filter(Objects::nonNull)
                            .anyMatch(value -> value.toLowerCase().contains(findText));

                })
            )
        );

        this.mainPane.setTop(this.textFilter);
        this.mainPane.setCenter(this.eventTableView);
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
        this.eventTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) ->
                        Optional.ofNullable(newSelection).ifPresent(function::apply)
        );
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
    public void addEvents(List<Event> eventList) {
        Tools.runOnWithOutThreadFX(() -> {
            this.observableList.addAll(eventList);

            Predicate<? super Event> predicate = e -> true;
            if(this.filteredList != null)
                predicate = this.filteredList.getPredicate();

            this.filteredList = new FilteredList<>(this.observableList, e -> true);
            this.filteredList.setPredicate(predicate);

            this.events = new SortedList<>(this.filteredList);
            this.events.comparatorProperty().bind(this.eventTableView.comparatorProperty());

            this.eventTableView.setItems(this.events);
        });
    }

    @Override
    public void sortByDate(Comparator<Event> comparator) {
        this.eventTableView.sort();
    }

    @Override
    public void addEvents(Event event) {
        this.eventTableView.getItems().add(event);
    }

    @Override
    public void clear() {
        this.eventTableView.getItems().clear();
    }

    @Override
    public void initView(Consumer<VEventTable> viewVisit) {
        mainPane = new BorderPane();
        eventTableView = new TableView<>();
        textFilter = new TextField();
        observableList = FXCollections.observableArrayList();

        init();

        viewVisit.accept(this);
    }
}
