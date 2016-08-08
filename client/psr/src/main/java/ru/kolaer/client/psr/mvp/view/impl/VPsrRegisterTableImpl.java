package ru.kolaer.client.psr.mvp.view.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.view.VPsrRegisterTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VPsrRegisterTableImpl implements VPsrRegisterTable {
    private static final Logger LOG = LoggerFactory.getLogger(VPsrRegisterTableImpl.class);
    private BorderPane tablePane;
    private TableView<PsrRegister> table;
    private ObservableList<PsrRegister> tableData = FXCollections.observableArrayList();
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public VPsrRegisterTableImpl() {
        this.tablePane = new BorderPane();

        this.table = new TableView<>();
        this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.table.setItems(this.tableData);


        final TableColumn<PsrRegister, Integer> psrRegisterIdColumn = new TableColumn<>("ID");
        psrRegisterIdColumn.setResizable(false);
        psrRegisterIdColumn.setMinWidth(50);
        psrRegisterIdColumn.setMaxWidth(50);
        psrRegisterIdColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        final TableColumn<PsrRegister, String> psrRegisterNameColumn = new TableColumn<>("Наименование проекта");
        psrRegisterNameColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<PsrRegister, GeneralEmployeesEntity> psrRegisterAuthorColumn = new TableColumn<>("Руководитель проекта");
        psrRegisterAuthorColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        psrRegisterAuthorColumn.setCellFactory(column ->
            new TableCell<PsrRegister, GeneralEmployeesEntity>() {
                @Override
                protected void updateItem(GeneralEmployeesEntity item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty && item != null)
                        this.setText(item.getInitials());
                }
            }
        );

        final TableColumn<PsrRegister, Date> psrRegisterDateOpenColumn = new TableColumn<>("Дата открытия");
        psrRegisterDateOpenColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterDateOpenColumn.setCellValueFactory(new PropertyValueFactory<>("dateOpen"));
        psrRegisterDateOpenColumn.setCellFactory(column ->
            new TableCell<PsrRegister, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty && item != null)
                        this.setText(dateFormat.format(item));
                }
            }
        );

        final TableColumn<PsrRegister, Date> psrRegisterDateCloseColumn = new TableColumn<>("Дата закрытия");
        psrRegisterDateCloseColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterDateCloseColumn.setCellValueFactory(new PropertyValueFactory<>("dateClose"));
        psrRegisterDateCloseColumn.setCellFactory(psrRegisterDateOpenColumn.getCellFactory());

        final TableColumn<PsrRegister, Date> psrRegisterDatesColumn = new TableColumn<>("Сроки реализации проекта");
        psrRegisterDatesColumn.getColumns().addAll(psrRegisterDateOpenColumn, psrRegisterDateCloseColumn);

        final TableColumn<PsrRegister, String> psrRegisterCommentColumn = new TableColumn<>("Описание");
        psrRegisterCommentColumn.setMinWidth(500);
        psrRegisterCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        psrRegisterCommentColumn.setCellFactory(column ->
            new TableCell<PsrRegister, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty && item != null) {
                        Tools.runOnThreadFX(() -> {
                            final HTMLEditor htmlEditor = new HTMLEditor();
                            htmlEditor.setDisable(true);
                            htmlEditor.setHtmlText(item);
                            htmlEditor.setPrefSize(600,100);
                            Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
                            for(Node node : nodes) {
                                node.setVisible(false);
                                node.setManaged(false);
                            }
                            this.setGraphic(new BorderPane(htmlEditor));
                        });
                    }
                }
            }
        );

        final TableColumn<PsrRegister, List<PsrAttachment>> psrRegisterAttachmentColumn = new TableColumn<>("Вложения");
        psrRegisterAttachmentColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterAttachmentColumn.setCellValueFactory(new PropertyValueFactory<>("attachments"));
        psrRegisterAttachmentColumn.setCellFactory(column ->
            new TableCell<PsrRegister, List<PsrAttachment>>(){
                @Override
                protected void updateItem(List<PsrAttachment> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty) {
                        if(item.size() > 0) {
                            this.setText("Есть (" + item.size() + ")");
                        } else {
                            this.setText("Пусто");
                        }
                    }
                }
            }
        );

        this.table.getColumns().addAll(psrRegisterIdColumn, psrRegisterAuthorColumn,
                psrRegisterNameColumn, psrRegisterDatesColumn, psrRegisterCommentColumn, psrRegisterAttachmentColumn);

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

    @Override
    public void addPsrProject(PsrRegister psrRegister) {
        Tools.runOnThreadFX(() -> {
            this.tableData.add(psrRegister);
        });
    }
}
