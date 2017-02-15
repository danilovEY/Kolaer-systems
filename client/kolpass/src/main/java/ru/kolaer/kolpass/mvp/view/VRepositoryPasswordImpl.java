package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryPasswordImpl implements VRepositoryPassword {
    private BorderPane mainPane;
    private Label labelName;
    private String urlImage;
    private VPasswordHistory lastPass;
    private VPasswordHistory firstPass;
    private VPasswordHistory prevPass;
    private ImageView imageView;
    private TitledPane titledPaneLast;
    private TitledPane titledPaneFirst;
    private TitledPane titledPanePrev;
    private Stage stage;
    private VBox content;
    private Button saveDataButton;
    private MenuItem editName;
    private MenuItem deleteItem;


    public VRepositoryPasswordImpl() {
        this.editName = new MenuItem("Редактировать имя");
        this.deleteItem = new MenuItem("Удалить");

        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(this.editName, deleteItem);

        this.saveDataButton = new Button("Сохранить");

        this.labelName = new Label();
        this.labelName.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        this.labelName.setAlignment(Pos.CENTER);
        this.labelName.setContextMenu(contextMenu);

        this.imageView = new ImageView();
        this.imageView.setFitHeight(200);
        this.imageView.setFitWidth(200);
        this.imageView.setOnContextMenuRequested(e -> {
            contextMenu.show(this.imageView, e.getScreenX(), e.getScreenY());
            e.consume();
        });
        this.imageView.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> contextMenu.hide());

        this.mainPane = new BorderPane();
        this.mainPane.setStyle("-fx-background-color: lightskyblue; -fx-background-radius: 20");
        this.mainPane.setPadding(new Insets(5));
        this.mainPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(20.0), new BorderWidths(3.0))));
        this.mainPane.setOnContextMenuRequested(e -> {
            contextMenu.show(this.mainPane, e.getScreenX(), e.getScreenY());
            e.consume();
        });
        this.mainPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> contextMenu.hide());
        this.mainPane.setTop(this.labelName);
        this.mainPane.setCenter(this.imageView);
        BorderPane.setMargin(this.imageView, new Insets(10));

        this.titledPaneLast = new TitledPane("Последний пароль", null);

        this.titledPaneFirst = new TitledPane("Первый пароль", null);
        this.titledPaneFirst.setExpanded(false);

        this.titledPanePrev = new TitledPane("Предыдущий пароль", null);
        this.titledPanePrev.setExpanded(false);

        BorderPane.setAlignment(this.labelName, Pos.CENTER);
        this.init();
    }

    private void init() {
        final Button openPass = new Button("Открыть");
        openPass.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        openPass.setPrefHeight(30);
        openPass.setOnAction(e -> {
            this.stage.setTitle(this.labelName.getText());

            this.content.getChildren().clear();

            Optional.ofNullable(this.lastPass).ifPresent(pass -> {
                this.titledPaneLast.setContent(pass.getContent());
                this.content.getChildren().add(this.titledPaneLast);
            });

            Optional.ofNullable(this.prevPass).ifPresent(pass -> {
                this.titledPanePrev.setContent(pass.getContent());
                this.content.getChildren().add(this.titledPanePrev);
            });

            Optional.ofNullable(this.firstPass).ifPresent(pass -> {
                this.titledPaneFirst.setContent(pass.getContent());
                this.content.getChildren().add(this.titledPaneFirst);
            });

            this.stage.sizeToScene();
            this.stage.requestFocus();
            this.stage.centerOnScreen();
            this.stage.show();
        });

        this.content = new VBox();
        this.content.setAlignment(Pos.TOP_CENTER);

        final ScrollPane scrollPane = new ScrollPane(this.content);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        BorderPane.setMargin(scrollPane, new Insets(0,0,10,0));

        final BorderPane mainStagePane = new BorderPane();
        mainStagePane.setPadding(new Insets(10));
        mainStagePane.setCenter(scrollPane);
        mainStagePane.setBottom(this.saveDataButton);
        BorderPane.setAlignment(this.saveDataButton, Pos.CENTER_RIGHT);

        this.stage = new Stage();
        this.stage.setScene(new Scene(mainStagePane));

        this.titledPaneLast.heightProperty().addListener((obs, oldHeight, newHeight) -> this.stage.sizeToScene());
        this.titledPanePrev.heightProperty().addListener((obs, oldHeight, newHeight) -> this.stage.sizeToScene());
        this.titledPaneFirst.heightProperty().addListener((obs, oldHeight, newHeight) -> this.stage.sizeToScene());

        this.mainPane.setBottom(openPass);
        BorderPane.setAlignment(openPass, Pos.CENTER);
    }

    @Override
    public void setName(String name) {
        this.labelName.setText(name);
    }

    @Override
    public String getName() {
        return this.labelName.getText();
    }

    @Override
    public void setImageUrl(String url) {
        this.urlImage = url;
        this.imageView.setImage(new Image(url));
    }

    @Override
    public String getImageUrl() {
        return this.urlImage;
    }

    @Override
    public void setLastPassword(VPasswordHistory password) {
        this.lastPass = password;
    }

    @Override
    public void setFirstPassword(VPasswordHistory password) {
        this.firstPass = password;
    }

    @Override
    public void setPrevPassword(VPasswordHistory password) {
        this.prevPass = password;
    }

    @Override
    public VPasswordHistory getLastPassword() {
        return this.lastPass;
    }

    @Override
    public VPasswordHistory getFirstPassword() {
        return this.firstPass;
    }

    @Override
    public VPasswordHistory getPrevPassword() {
        return this.prevPass;
    }

    @Override
    public void addPasswordHistory(VPasswordHistory passwordHistory) {

    }

    @Override
    public void removePasswordHistory(VPasswordHistory passwordHistory) {

    }

    @Override
    public void setOnSaveData(Function function) {
        this.saveDataButton.setOnAction(e -> {
            function.apply(e);
            this.stage.close();
        });
    }

    @Override
    public void setOnEditName(Function<String, Void> function) {
        this.editName.setOnAction(e -> {
            TextInputDialog textInputDialog = new TextInputDialog(this.getName());
            textInputDialog.setContentText("Введите новое имя");
            textInputDialog.setHeaderText("");
            textInputDialog.setTitle("Введите имя");
            textInputDialog.showAndWait().ifPresent(function::apply);
        });
    }

    @Override
    public void setOnDelete(Function function) {
        this.deleteItem.setOnAction(function::apply);
    }

    @Override
    public void setOnReturnDefault(Function function) {
        this.stage.setOnCloseRequest(function::apply);
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }
}
