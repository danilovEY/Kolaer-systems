package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.tools.Borders;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.api.system.ui.StaticUS;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.api.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 18.08.2016.
 */
@Slf4j
public class NotificationPanelExceptionHandler implements NotificationUS,
        BaseView<NotificationPanelExceptionHandler, Parent>, StaticUS,
        Thread.UncaughtExceptionHandler {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy | hh:mm:ss");
    private final int SIMPLE_MESSAGE = 0;
    private final int INFO_MESSAGE = 1;
    private final int WARN_MESSAGE = 2;
    private final int ERROR_MESSAGE = 3;
    private BorderPane mainPane;
    private VBox vBoxNotifyMessage;
    private VBox vBoxStatic;

    @Override
    public void showSimpleNotify(String title, String text) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showErrorNotify(String title, String text) {
        this.sendMessage(ERROR_MESSAGE, title, text);
    }

    @Override
    public void showWarningNotify(String title, String text) {
        this.sendMessage(WARN_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotify(String title, String text) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showErrorNotify(String title, String text, List<NotifyAction> actions) {
        this.sendMessage(ERROR_MESSAGE, title, text, actions);
    }

    @Override
    public void showWarningNotify(String title, String text, List<NotifyAction> actions) {
        this.sendMessage(WARN_MESSAGE, title, text, actions);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);
    }

    @Override
    public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
        log.info("Server error: {}", exceptionMessage.toString());

        String title = "(" + exceptionMessage.getStatus() + ") " +
                exceptionMessage.getUrl() +
                " - " +
                exceptionMessage.getCode().getMessage();

        this.sendMessage(this.vBoxNotifyMessage, ERROR_MESSAGE, title, exceptionMessage.getMessage(),
                exceptionMessage.getExceptionTimestamp(), Collections.emptyList());
    }

    private void sendMessage(int type, String title, String text) {
        this.sendMessage(type, title, text, Collections.emptyList());
    }

    private void sendMessage(VBox typePane, int type, String title, String text, List<NotifyAction> actions) {
        sendMessage(typePane, type, title, text, new Date(), actions);
    }

    private void sendMessage(VBox typePane, int type, String title, String text, Date date, List<NotifyAction> actions) {
        Tools.runOnWithOutThreadFX(() -> {
            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setBackground(Background.EMPTY);
            switch (type) {
                case SIMPLE_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case INFO_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(58, 188, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case WARN_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 0, 0.6); -fx-effect: dropshadow(gaussian , #866432, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case ERROR_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6); -fx-effect: dropshadow(gaussian , #861a3d, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                default: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                }
            }

            content.setSpacing(3);

            if(title != null) {
                Label titleLabel = new Label(title);
                titleLabel.setTextAlignment(TextAlignment.CENTER);
                titleLabel.setWrapText(true);
                titleLabel.setMinHeight(Region.USE_PREF_SIZE);
                titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

                content.getChildren().add(titleLabel);
            }

            if(text != null) {
                Label textLabel = new Label(text);
                textLabel.setTextAlignment(TextAlignment.CENTER);
                textLabel.setWrapText(true);
                textLabel.setMinHeight(Region.USE_PREF_SIZE);
                textLabel.setFont(Font.font(null, FontWeight.BOLD, 12));

                content.getChildren().add(textLabel);
            }

            String dateToString = dateFormat.format(Optional.ofNullable(date)
                    .orElse(new Date()));

            Label timeLabel = new Label(dateToString);
            timeLabel.setTextAlignment(TextAlignment.CENTER);
            timeLabel.setWrapText(true);
            timeLabel.setFont(Font.font(null, FontWeight.BOLD, 9));

            content.getChildren().add(timeLabel);

            if(actions != null) {
                for (NotifyAction action : actions) {
                    Button button = new Button(action.getText());
                    button.setOnAction(action.getConsumer()::accept);
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(action.getText());
                    button.setTooltip(tooltip);
                    content.getChildren().add(button);
                }
            }

            final Node border = Borders.wrap(content)
                    .lineBorder()
                    .thickness(5)
                    .innerPadding(0)
                    .radius(5, 5, 5, 5)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .build()
                    .build();
            typePane.getChildren().add(border);
            border.toBack();
        });
    }

    private void sendMessage(int type, String title, String text, List<NotifyAction> actions) {
        this.sendMessage(this.vBoxNotifyMessage, type, title, text, actions);
    }

    @Override
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Ошибка в потоке: {}", t.getName(), e);
        Tools.runOnWithOutThreadFX(() -> showErrorNotify("Ошибка!", e.getLocalizedMessage()));
    }

    @Override
    public void initView(Consumer<NotificationPanelExceptionHandler> viewVisit) {
        this.vBoxNotifyMessage = new VBox();
        this.vBoxNotifyMessage.setSpacing(5);
        this.vBoxNotifyMessage.setAlignment(Pos.TOP_LEFT);
        this.vBoxNotifyMessage.setPadding(new Insets(5,5,5,5));

        final ScrollPane scrollPaneUserNotify = new ScrollPane(this.vBoxNotifyMessage);
        scrollPaneUserNotify.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneUserNotify.setMinWidth(300);
        scrollPaneUserNotify.setPrefWidth(300);
        scrollPaneUserNotify.setMaxWidth(400);
        scrollPaneUserNotify.setFitToHeight(true);
        scrollPaneUserNotify.setFitToWidth(true);


        this.vBoxStatic = new VBox();
        this.vBoxStatic.setSpacing(5);
        this.vBoxStatic.setAlignment(Pos.TOP_LEFT);
        this.vBoxStatic.setPadding(new Insets(5,5,5,5));

        final ScrollPane scrollPaneStatic = new ScrollPane(this.vBoxStatic);
        scrollPaneStatic.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneStatic.setMinWidth(300);
        scrollPaneStatic.setMinHeight(100);
        scrollPaneStatic.setPrefWidth(300);
        scrollPaneStatic.setPrefHeight(100);
        scrollPaneStatic.setMaxWidth(400);
        //scrollPaneAdminNotify.setMaxHeight(400);
        scrollPaneStatic.setFitToHeight(true);
        scrollPaneStatic.setFitToWidth(true);

        final SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(scrollPaneStatic, scrollPaneUserNotify);
        splitPane.setDividerPositions(0.6);
        splitPane.setOrientation(Orientation.VERTICAL);
        this.mainPane = new BorderPane(splitPane);
        this.mainPane.setMinWidth(300);
        this.mainPane.setPrefWidth(300);
        this.mainPane.setMaxWidth(400);


        BackgroundImage myBI= new BackgroundImage(new Image(this.getClass().getResource("/notify-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.vBoxNotifyMessage.setBackground(new Background(myBI));
        this.vBoxStatic.setBackground(new Background(myBI));

        viewVisit.accept(this);
    }

    @Override
    public void addStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> {
            BorderPane content = new BorderPane();
            content.setBackground(Background.EMPTY);
            content.setStyle("-fx-background-color: rgba(255, 156, 0, 0.6); -fx-effect: dropshadow(gaussian , #866839, 4,0,0,1 ); -fx-padding: 3;");
            content.setCenter(staticView.getContent());

            Node border = Borders.wrap(content)
                    .lineBorder()
                    .thickness(5)
                    .innerPadding(0)
                    .radius(5, 5, 5, 5)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .build()
                    .build();
            this.vBoxStatic.getChildren().add(border);
            border.toBack();
        });
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> {
            vBoxStatic.getChildren().remove(staticView.getContent());
        });
    }
}
