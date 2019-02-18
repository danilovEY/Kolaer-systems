package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.tools.Utils;
import ru.kolaer.client.core.system.ui.NotificationType;
import ru.kolaer.client.core.system.ui.NotificationView;
import ru.kolaer.client.core.system.ui.NotifyAction;
import ru.kolaer.client.core.tools.Tools;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Toasts implements NotificationView {
    private final SimpleStringProperty titleProperty = new SimpleStringProperty();
    private final SimpleStringProperty textProperty = new SimpleStringProperty();
    private final SimpleObjectProperty<NotificationType> typeProperty = new SimpleObjectProperty<>(NotificationType.NOTICE);
    private Duration duration;

    private GridPane mainPane;
    private BorderPane contentPane;
    private Node content;
    private Tooltip tooltip;
    private EventHandler<WindowEvent> onCloseEvent;
    private List<NotifyAction> notifyAction = Collections.emptyList();

//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//    static Stage PS;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        PS = primaryStage;
//
//        Button notifyAction = new Button("TETE");
//        notifyAction.setMaxWidth(Double.MAX_VALUE);
//
//        NotificationView toasts = new Toasts();
//        toasts.setTitle("TITLE");
//        toasts.setText("TEXT");
//        toasts.setDuration(Duration.seconds(5));
//        toasts.setContent(notifyAction);
//        toasts.setOnClose(windowEvent -> System.out.println("Close"));
////        toasts.setNotifyAction(Collections.singletonList(new NotifyAction("TEST", actionEvent -> System.out.println("HELLO WORLD!"))));
//        toasts.initView(BaseView::empty);
//
//
//        Button Success = new Button("Success");
//        Success.setOnAction(e -> {
//            toasts.setType(NotificationType.INFO);
//            if(toasts.isShow()) {
//                toasts.hide();
//            }
//            toasts.show();
//        });
//        Button Notice = new Button("Notice");
//        Notice.setOnAction(e -> {
//            toasts.setType(NotificationType.NOTICE);
//            if(toasts.isShow()) {
//                toasts.hide();
//            }
//            toasts.show();
//        });
//        Button Error = new Button("Error");
//        Error.setOnAction(e -> {
//            toasts.setType(NotificationType.ERROR);
//            if(toasts.isShow()) {
//                toasts.hide();
//            }
//            toasts.show();
//        });
//
//        primaryStage.setScene(new Scene(new VBox(Success, Notice, Error)));
//
//        PS.getScene().getStylesheets().add("css/ToastStyle.css");
//
//        primaryStage.show();
//
//    }

    @Override
    public void setText(String text) {
        Tools.runOnWithOutThreadFX(() -> textProperty.set(text));
    }

    @Override
    public void setTitle(String title) {
        Tools.runOnWithOutThreadFX(() -> titleProperty.set(title));
    }

    @Override
    public void setType(NotificationType type) {
        Tools.runOnWithOutThreadFX(() -> typeProperty.set(type));
    }

    @Override
    public void setPosition(Pos position) {

    }

    @Override
    public void setNotifyAction(List<NotifyAction> notifyAction) {
        this.notifyAction = notifyAction;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void show() {
        if(isViewInit()) {
            Tools.runOnWithOutThreadFX(() -> {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double y = screenBounds.getMinY() + screenBounds.getHeight();
                double x = screenBounds.getMinX() + screenBounds.getWidth() - 200;

                tooltip.show(Utils.getWindow(null), x, y);
            });
        }
    }

    @Override
    public void hide() {
        if(isViewInit()) {
            Tools.runOnWithOutThreadFX(tooltip::hide);
        }
    }

    @Override
    public boolean isShow() {
        return isViewInit() && tooltip.isShowing();
    }

    @Override
    public void setOnClose(EventHandler<WindowEvent> actionEventConsumer) {
        this.onCloseEvent = actionEventConsumer;
        if(isViewInit()) {
            tooltip.setOnHidden(actionEventConsumer);
        }
    }

    @Override
    public void initView(Consumer<NotificationView> viewVisit) {
        tooltip = new Tooltip();
        tooltip.setId("msg-tip");
        tooltip.setOnHidden(onCloseEvent);

        Label closeLabel = new Label("X");
        closeLabel.setId("Close");
        closeLabel.setOnMouseClicked(value -> tooltip.hide());

        Label textLabel = new Label();
        textLabel.setAlignment(Pos.TOP_CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setWrapText(true);
        textLabel.setId("Content");
        textLabel.textProperty().bind(textProperty);

        double maxWidth = 800;
        textLabel.setMaxWidth(maxWidth);

        Label titleLabel = new Label();
        titleLabel.setAlignment(Pos.TOP_RIGHT);
        titleLabel.setTextAlignment(TextAlignment.RIGHT);
        titleLabel.textProperty().bind(titleProperty);

        double width = 0;
        if (textLabel.getText() != null) {
            width = 150 + textLabel.getText().length() / 2.5;
        }
        if (width > maxWidth) {
            width = maxWidth;
        }
        double height = textLabel.getPrefHeight();

        contentPane = new BorderPane();
        Optional.ofNullable(content).ifPresent(contentPane::setCenter);

        mainPane = new GridPane();
        mainPane.setPrefWidth(300);
        mainPane.setPrefHeight(100);
        mainPane.getColumnConstraints().setAll(new ColumnConstraints(30, 30, 30, Priority.NEVER, HPos.LEFT, true),
                new ColumnConstraints(width, width, maxWidth, Priority.ALWAYS, HPos.RIGHT, true));

        mainPane.getRowConstraints().setAll(new RowConstraints(30, 30, 30),
                new RowConstraints(height, height, 750, Priority.ALWAYS, VPos.CENTER, true),
                new RowConstraints(height, height, 750, Priority.ALWAYS, VPos.CENTER, true));
        mainPane.setId("msgtipbox");

        mainPane.setVgap(10);
        mainPane.setHgap(10);
        mainPane.add(closeLabel, 0, 0);
        mainPane.add(titleLabel, 1, 0);
        mainPane.add(textLabel, 0, 1, 2, 1);
        mainPane.add(contentPane, 0, 2, 2, 1);

        if(!notifyAction.isEmpty()) {
            VBox vBox = new VBox();
            vBox.setFillWidth(true);

            notifyAction.stream().map(action -> {
                Button button = new Button(action.getText());
                button.setOnAction(action.getConsumer()::accept);
                button.setMaxWidth(Double.MAX_VALUE);
                return button;
            }).forEach(vBox.getChildren()::add);

            contentPane.setCenter(vBox);
        }

        GridPane.setVgrow(textLabel, Priority.ALWAYS);
        GridPane.setHgrow(textLabel, Priority.ALWAYS);

        tooltip.setGraphic(mainPane);

        SimpleBooleanProperty hoveProperty = new SimpleBooleanProperty(false);
        mainPane.setOnMouseEntered(v -> hoveProperty.set(true));
        mainPane.setOnMouseExited(v -> hoveProperty.set(false));

//        PauseTransition wait = new PauseTransition(duration);
//        wait.setOnFinished((e) -> {
//            if (hoveProperty.get()) {
//                wait.play();
//            } else {
//                tooltip.hide();
//            }
//        });
//        wait.play();

//        hoveProperty.addListener((observable, oldValue, newValue) -> wait.playFromStart());

        ChangeListener<NotificationType> changeStyle = (observable, oldValue, newValue) -> {
            if (newValue == NotificationType.NOTICE) {
                mainPane.setStyle("-fx-background-color: rgba(255,255,255,0.8);");
                titleLabel.setStyle("-fx-text-fill: #222");
                textLabel.setStyle("-fx-text-fill: #222");
            } else if (newValue == NotificationType.ERROR) {
                mainPane.setStyle("-fx-background-color: rgba(255, 25, 36, 0.8);");
                titleLabel.setStyle("-fx-text-fill: #fff");
                textLabel.setStyle("-fx-text-fill: #fff");
            } else if (newValue == NotificationType.INFO) {
                mainPane.setStyle("-fx-background-color: rgba(74,143,255,0.8);");
                titleLabel.setStyle("-fx-text-fill: #222");
                textLabel.setStyle("-fx-text-fill: #222");
            } else if (newValue == NotificationType.WARNING) {
                mainPane.setStyle("-fx-background-color: rgba(255,239,0,0.8);");
                titleLabel.setStyle("-fx-text-fill: #222");
                textLabel.setStyle("-fx-text-fill: #222");
            }
        };
        typeProperty.addListener(changeStyle);
        changeStyle.changed(null, typeProperty.get(), typeProperty.get());

        viewVisit.accept(this);
    }

    @Override
    public void setContent(Node content) {
        this.content = content;
        if(isViewInit()) {
            contentPane.setCenter(content);
        }
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}