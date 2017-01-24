package ru.kolaer.client.psr.mvp.view.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.presenter.PDetailsOrEditPsrRegister;
import ru.kolaer.client.psr.mvp.presenter.impl.PDetailsOrEditPsrRegisterImpl;
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
    private final UniformSystemEditorKit editorKit;

    public VPsrRegisterTableImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.tablePane = new BorderPane();

        this.table = new TableView<>();
        this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.table.setItems(this.tableData);


        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem editMenuItem = new MenuItem("Редактировать");
        editMenuItem.setOnAction(e -> {
            Tools.runOnThreadFX(() -> {
                final PsrRegister selectRegister = table.getSelectionModel().getSelectedItem();
                final PDetailsOrEditPsrRegister detailsOrEditPsrRegister = new PDetailsOrEditPsrRegisterImpl(editorKit, selectRegister);
                detailsOrEditPsrRegister.getView().initializationView();
                detailsOrEditPsrRegister.showAndWait();
                tableData.remove(selectRegister);
                this.addPsrProject(selectRegister);
                editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().updatePsrRegister(selectRegister);
            });
        });

        final MenuItem removeMenuItem = new MenuItem("Удалить");
        removeMenuItem.setOnAction(e -> {
            final PsrRegister selectRegister = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectRegister);
            editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getPsrTable().deletePsrRegister(selectRegister);
        });
        contextMenu.getItems().addAll(editMenuItem, removeMenuItem);

        table.setContextMenu(contextMenu);
        table.setOnContextMenuRequested(e -> {
            if(!this.editorKit.getAuthentication().isAuthentication()){
                this.editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка авторизации!", "Вы не авторизовались!");
                return;
            }


            final PsrRegister selectPsr = table.getSelectionModel().getSelectedItem();
            if(selectPsr == null) {
                contextMenu.hide();
                return;
            }

            final EmployeeEntity selectPsrEmployee = selectPsr.getAuthor();

            final GeneralAccountsEntity accountsEntity = editorKit.getAuthentication().getAuthorizedUser();
            final EmployeeEntity authorizedEmployee = accountsEntity.getGeneralEmployeesEntity();
            final EnumRole[] roles = this.editorKit.getAuthentication().getRoles();
            for(EnumRole role : roles) {
                if(role == EnumRole.PSR_ADMIN || role == EnumRole.SUPER_ADMIN) {
                    return;
                }
            }

            if(!selectPsrEmployee.getPnumber().equals(authorizedEmployee.getPnumber())) {
                contextMenu.hide();
            }
        });

        final TableColumn<PsrRegister, Integer> psrRegisterIdColumn = new TableColumn<>("ID");
        psrRegisterIdColumn.setResizable(false);
        psrRegisterIdColumn.setMinWidth(50);
        psrRegisterIdColumn.setMaxWidth(50);
        psrRegisterIdColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        final TableColumn<PsrRegister, String> psrRegisterNameColumn = new TableColumn<>("Наименование проекта");
        psrRegisterNameColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<PsrRegister, String> psrRegisterStatusColumn = new TableColumn<>("Статус проекта");
        psrRegisterStatusColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        final TableColumn<PsrRegister, EmployeeEntity> psrRegisterAuthorColumn = new TableColumn<>("Руководитель проекта");
        psrRegisterAuthorColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        psrRegisterAuthorColumn.setCellFactory(column ->
            new TableCell<PsrRegister, EmployeeEntity>() {
                @Override
                protected void updateItem(EmployeeEntity item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText("");
                    if(!empty && item != null)
                        this.setText(item.getInitials());
                    else
                        this.setText("");
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
                    else
                        this.setText("");
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
                    if(!empty || item != null && !item.isEmpty()) {
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
                    } else {
                        this.setGraphic(new Pane());
                    }
                }
            }
        );

        //TODO: доделать!
        final TableColumn<PsrRegister, List<PsrAttachment>> psrRegisterAttachmentColumn = new TableColumn<>("Вложения");
        psrRegisterAttachmentColumn.setStyle("-fx-alignment: CENTER;");
        psrRegisterAttachmentColumn.setCellValueFactory(new PropertyValueFactory<>("attachments"));
        psrRegisterAttachmentColumn.setCellFactory(column ->
            new TableCell<PsrRegister, List<PsrAttachment>>(){
                @Override
                protected void updateItem(List<PsrAttachment> item, boolean empty) {
                    if(!empty) {
                        if(item != null && item.size() > 0) {
                            this.setText("Есть (" + item.size() + ")");
                        } else {
                            this.setText("Пусто");
                        }
                    } else {
                        this.setText("");
                    }
                }
            }
        );

        this.table.getColumns().addAll(psrRegisterIdColumn, psrRegisterStatusColumn, psrRegisterAuthorColumn,
                psrRegisterNameColumn, psrRegisterDatesColumn, psrRegisterCommentColumn);
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
        Tools.runOnThreadFX(() -> {
            this.tableData.addAll(psrRegisters);

            this.table.getSortOrder().add(this.table.getColumns().get(0));
        });
    }

    @Override
    public void addPsrProject(PsrRegister psrRegister) {
        Tools.runOnThreadFX(() -> {
            this.tableData.add(psrRegister);
        });
    }

    @Override
    public void clear() {
        Tools.runOnThreadFX(this.tableData::clear);
    }
}
